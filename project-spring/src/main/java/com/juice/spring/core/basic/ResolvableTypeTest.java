package com.juice.spring.core.basic;

import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ResolvableTypeTest {

    public static void main(String argv[]) throws Exception {

        //forClassTest();
        //forClassTest2();
        forFieldTest();

    }

    public static void forClassTest() {
        ResolvableType oneType = ResolvableType.forClass(One.class);
        One one = new One();
        ResolvableType oneType2 = ResolvableType.forClass(one.getClass());

        System.out.println(oneType.resolve());
        System.out.println(oneType2.resolve());

        ResolvableType baseType = oneType2.as(Base.class);
        ResolvableType interType = oneType2.as(Inter.class);

        System.out.println(baseType.resolve());
        System.out.println(interType.resolve());
        System.out.println(oneType2.getSuperType());
        System.out.println("isTrue: " + baseType.equals(oneType2.getSuperType()));
        Arrays.stream(oneType2.getInterfaces()).forEach(System.out::print);

    }

    public static void forClassTest2() {
        //Two类声明TypeVariable(泛型参数)
        ResolvableType twoType = ResolvableType.forClass(Two.class);
        //twoType的generics域: [ResolvableType{type=TypeVariable}, ResolvableType{type=TypeVariable, resolved=Class<List>}]
        ResolvableType[] gene = twoType.getGenerics();

        //Two实例具象化了泛型参数
        Two<String, ArrayList<String>> two = new Two<>();
        ResolvableType twoType2 = ResolvableType.forClassWithGenerics(two.getClass(), String.class, ArrayList.class);
        //twoType2的generics域: [ResolvableType{type=Class<String>, resolved=Class<String>}, ResolvableType{type=Class<ArrayList>, resolved=Class<ArrayList>}]
        ResolvableType[] gene2 = twoType2.getGenerics();

        System.out.println(1);
    }


    public static void forFieldTest() throws Exception {

        Field oneField = Three.class.getDeclaredField("one");
        ResolvableType oneType = ResolvableType.forField(oneField);
        System.out.println(oneType.resolve());

        Field twoField = Three.class.getDeclaredField("two");
        ResolvableType twoType = ResolvableType.forField(twoField, Three.class);
        //[ResolvableType{type=TypeVariable}, ResolvableType{type=TypeVariable, resolved=Class<List>}]
        ResolvableType[] gene = twoType.getGenerics();

        //Field的declareClass具象化
        Three<Integer, LinkedList<String>> three = new Three<>();
        Field twoField2 = three.getClass().getDeclaredField("two");
        ResolvableType twoType2 = ResolvableType.forField(twoField2, three.getClass());
        //[ResolvableType{type=TypeVariable}, ResolvableType{type=TypeVariable, resolved=Class<List>}]
        ResolvableType[] gene2 = twoType2.getGenerics();

        //Three的子类型将Three的泛型参数具象化
        ResolvableType twoType3 = ResolvableType.forField(twoField, ThreeImpl.class);
        //[ResolvableType{type=Class<Integer>}, ResolvableType{type=ParameterizedType, resolved=ParameterizedType}]
        ResolvableType[] gene3 = twoType3.getGenerics();

        System.out.println(2);
    }


    private static class Base {}
    private interface Inter{}
    private static class One extends Base implements Inter {}

    private static class Two<T, E extends List<String>> {}

    private static class Three<T, E extends List<String>> extends Two<T, E> {
        private One one;
        private Two<T, E> two;
    }

    private static class ThreeImpl extends Three<Integer, ArrayList<String>> {}


}
