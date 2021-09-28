package com.project.normal.test.reflect;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeTest<T> {

    @PoMAnnotation
    private List<T> list;
    private Map<String, T> map;
    private Set<String> set;

    public static void main(String argv[]) throws Exception {

        Field listField = TypeTest.class.getDeclaredField("list");
        Type listFieldGenericType = listField.getGenericType(); //is ParameterizedType
        System.out.println(listFieldGenericType);
        ParameterizedType listFieldGenericTypeParam = (ParameterizedType) listFieldGenericType;
        Type[] actualType = listFieldGenericTypeParam.getActualTypeArguments();
        Type rawType = listFieldGenericTypeParam.getRawType();

        TypeVariable typeVariable = (TypeVariable) actualType[0];


        Field mapField = TypeTest.class.getDeclaredField("map");
        Type mapFieldGenericType = mapField.getGenericType(); //is ParameterizedType
        System.out.println(mapFieldGenericType);
        ParameterizedType mapFieldGenericTypeParam = (ParameterizedType) mapFieldGenericType;
        Type[] mapActualType = mapFieldGenericTypeParam.getActualTypeArguments();
        Type mapRawType = mapFieldGenericTypeParam.getRawType();



        Field setField = TypeTest.class.getDeclaredField("set");
        Type setFieldGenericType = setField.getGenericType(); //is ParameterizedType
        System.out.println(setFieldGenericType);
        ParameterizedType setFieldGenericTypeParam = (ParameterizedType) setFieldGenericType;
        Type[] setActualType = setFieldGenericTypeParam.getActualTypeArguments();
        Type setRawType = setFieldGenericTypeParam.getRawType();

        AnnotatedType annotatedType = setField.getAnnotatedType();
        AnnotatedParameterizedType at = (AnnotatedParameterizedType) annotatedType;
        Type t1 = at.getType();
        AnnotatedType[] t2 = at.getAnnotatedActualTypeArguments();
        Type tt2 = t2[0].getType();
        System.out.println(1);


    }




}
