package com.project.normal.test.reflect;

public interface AllInter {
    void inter();
    default void interDefault() {}
    static void interStatic() {}
}
