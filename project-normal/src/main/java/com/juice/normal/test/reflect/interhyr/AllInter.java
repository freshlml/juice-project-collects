package com.juice.normal.test.reflect.interhyr;

public interface AllInter {
    void inter();
    default void interDefault() {}
    static void interStatic() {}
}
