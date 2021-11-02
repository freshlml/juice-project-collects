package com.project.normal.test.reflect.interhyr;

public interface AllInter2 extends AllInter1, AllInterE {
    void inter2();
    default void interDefault2() {}
    static void interStatic2() {}

    @Override
    void inter1();
    @Override
    default void interDefault1() {}

    static void interStatic1() {}
}
