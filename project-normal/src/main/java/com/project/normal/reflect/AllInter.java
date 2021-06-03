package com.project.normal.reflect;

public interface AllInter {
    void inter();
    default void interDefault() {}
    static void interStatic() {}
}
