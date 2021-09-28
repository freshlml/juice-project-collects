package com.project.normal.test.reflect;

import java.util.List;

public class Book2 {

    public Book2(String var) {}
    public Book2(int[] vars) {}
    public <T> Book2(T var) {}
    public <T extends Number> Book2(T var) {}
    public <T> Book2(T[] array) {}
    public <T> Book2(List<T>[] listArray) {}
    public <T> Book2(Book<T> ts) {}
    public Book2(List<? extends Number> bk) {}

    public <T> T say() {
        return (T) "abc";
    }
    public <T> T say(T t) {
        return t;
    }
    public <T> T[] see() {
        //T[] t = new T[1];
        return null;
    }
    public <T> List<T>[] see(List<T>[] param) {
        //ArrayList<T>[] a = new ArrayList<T>[1];
        return param;
    }
    public <T> Book<T> eat() {
        return (Book<T>) new Book<String>();
    }
    public <T> Book<T> eat(Book<T> book) {
        return book;
    }
    public Book<? extends Number> getEat() {
        return new Book<Integer>();
    }


}
