package com.juice.jv.concurrent;

@SuppressWarnings("unused")
public class StaticFieldInitializationVisibilityTest {

    //12.4 Initialization of Classes and Interfaces (Java Language Specification for Java SE 8):
    //  1. 使用 static field 之前，必须完成类的初始化
    //  2. 可能有多个线程尝试初始化同一个类，通过 "初始化锁" 控制只能由一个线程完成类的初始化，其他线程被阻塞
    //  3. 完成初始化的线程在释放锁时刷新本地缓存，使得访问该类的 static field 的任何线程都可以看到初始化的值，此为 static field initialization visibility

}
