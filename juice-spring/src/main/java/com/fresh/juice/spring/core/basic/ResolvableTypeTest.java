package com.fresh.juice.spring.core.basic;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class ResolvableTypeTest {

    public static void main(String argv[]) throws Exception {
        forUsingTest1();
        forUsingTest2();
        forUsingTest3();
        forUsingTest4();

    }


    private static void forUsingTest1() throws Exception {
        ResolvableType r = ResolvableType.forClass(Bean1.class);

        System.out.println(r.getGenerics()[0].resolve()); //泛型参数解析

    }
    private static class Bean1<T extends Number> {}


    private static void forUsingTest2() throws Exception {
        Field listField = ResolvableTypeTest.class.getDeclaredField("list");
        ResolvableType listResolvableType = ResolvableType.forType(listField.getGenericType());
        Type q1 = listResolvableType.getType();   //ParameterizedType
        Class<?> w1 = listResolvableType.resolve(); //Class<List>

        ResolvableType clazzResolve = ResolvableType.forClassWithGenerics(Bean2.class, listResolvableType);
        Type q2 = clazzResolve.getType();  //ParameterizedType
        Class<?> w2 = clazzResolve.resolve();  //Class<Bean2>

        //Bean2的泛型参数 被解析成 listResolvableType
        ResolvableType generic = clazzResolve.getGeneric(0);

        Type q3 = generic.getType();
        Class<?> w4 = generic.resolve();
        
    }
    private List<String> list;
    private static class Bean2<T> {} //unresolvable TypeVariable


    private static void forUsingTest3() throws Exception {
        Field beanField = ResolvableTypeTest.class.getDeclaredField("bean3");
        ResolvableType beanResolvableType = ResolvableType.forType(beanField.getGenericType());
        Type q1 = beanResolvableType.getType();   //ParameterizedType
        Class<?> w1 = beanResolvableType.resolve(); //Class<Bean3>

        //Field t的泛型变量T 被解析成Integer
        ResolvableType fr = ResolvableType.forField(Bean3.class.getDeclaredField("t"), beanResolvableType);
        Type q2 = fr.getType();
        Class<?> w2 = fr.resolve();

    }
    private Bean3<Integer> bean3;
    private static class Bean3<T extends Number> {
        T t;
    }


    private static void forUsingTest4() throws Exception {

        //使用ParameterizedTypeReference，可以随意构造ResolvableType
        ResolvableType r = ResolvableType.forType(new ParameterizedTypeReference<Bean3<Integer>>() {});
        Type q1 = r.getType();

        Field beanField = ResolvableTypeTest.class.getDeclaredField("bean3");
        ResolvableType beanResolvableType = ResolvableType.forType(beanField.getGenericType());
        Type q2 = beanResolvableType.getType();

    }


}
