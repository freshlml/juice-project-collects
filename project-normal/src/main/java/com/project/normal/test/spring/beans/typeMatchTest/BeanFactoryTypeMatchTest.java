package com.project.normal.test.spring.beans.typeMatchTest;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.core.ResolvableType;


public class BeanFactoryTypeMatchTest {

    public static void main(String argv[]) {

        testIsTypeMatch();

        System.out.println("hello");
    }


    public static void testIsTypeMatch() {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);

        //register方法注册带泛型参数的bean
        beanDefinitionReader.register(TypeMatchGenericBean.class);

        //注册的BeanDefinition保存了Class<?> beanClass，通过Class仅能得到TypeVariable{T}
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("testGenericBean");
        //merged BeanDefinition,RootBeanDefinition的targetType==null
        BeanDefinition mergedBeanDefinition = beanFactory.getMergedBeanDefinition("testGenericBean");

        //false: TestGenericBean with generic[String.class]
        System.out.println(beanFactory.isTypeMatch("testGenericBean", ResolvableType.forClassWithGenerics(TypeMatchGenericBean.class, String.class)));
        //true
        System.out.println(beanFactory.isTypeMatch("testGenericBean", ResolvableType.forClass(TypeMatchGenericBean.class)));


        beanDefinitionReader.register(TypeMatchGenericBeanA.class); //A extends TestGenericBean<String>
        beanDefinitionReader.register(TypeMatchGenericBeanB.class); //B extends TestGenericBean<Integer>

        ResolvableType superType = ResolvableType.forClass(TypeMatchGenericBean.class);
        ResolvableType superWithStrGenerics = ResolvableType.forClassWithGenerics(TypeMatchGenericBean.class, String.class);
        ResolvableType superWithIntGenerics = ResolvableType.forClassWithGenerics(TypeMatchGenericBean.class, Integer.class);
        System.out.println(beanFactory.isTypeMatch("testGenericBeanA", superType));  //true
        System.out.println(beanFactory.isTypeMatch("testGenericBeanA", superWithStrGenerics)); //true
        System.out.println(beanFactory.isTypeMatch("testGenericBeanA", superWithIntGenerics)); //false


    }




}
