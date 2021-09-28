package com.project.normal.test.reflect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Book<T> {
    T t;  //TypeVariable，泛型变量类型
    T[] array;//GenericArrayType，泛型数组类型;TypeVariable
    List<T>[] listArray;//GenericArrayType，泛型数组类型;ParameterizedType,TypeVariable
    Book<T> list;//ParameterizedType，泛型类型;TypeVariable
    List<? extends Number> bb; //ParameterizedType，泛型类型;WildcardType
    List<String> strList; //ParameterizedType，泛型类型;Class<String>
    List<? extends List<T>> ll; //ParameterizedType，泛型类型;WildcardType，ParamerizedType，TypeVariable

    public Book() {
    }

    public Book(T t, T[] array, List<T>[] listArray, Book<T> list, List<? extends Number> bb, List<String> strList, List<? extends List<T>> ll) {
        this.t = t;
        this.array = array;
        this.listArray = listArray;
        this.list = list;
        this.bb = bb;
        this.strList = strList;
        this.ll = ll;
    }

    public static void main(String argv[]) throws Exception {
        List<String>[] genericArray = (ArrayList<String>[]) Array.newInstance(ArrayList.class, 2);
        new Book<String>("123", new String[1], genericArray, new Book<String>(), new ArrayList<Integer>(), new ArrayList<String>(), new ArrayList<ArrayList<String>>());

        System.out.println(1);
    }

}
