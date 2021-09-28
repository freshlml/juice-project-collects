package com.project.normal.test.reflect;

public interface AllInter1 extends AllInter {
    void inter1();  //abstract方法: 子接口可以重写
    default void interDefault1() {} //非abstract，default方法: 子接口可以重写

    static void interStatic1() {} //静态方法不能被子接口重写，子接口可以定义相同签名的静态方法

    @Override
    void interDefault();
}
