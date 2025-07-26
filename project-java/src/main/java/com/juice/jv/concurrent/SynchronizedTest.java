package com.juice.jv.concurrent;

@SuppressWarnings("unused")
public class SynchronizedTest {
    //原子性: 不可打断，互斥

    //在 Java 存储模型(JMM) 下，synchronized 的内存屏障(限制指令重排序和保证可见性)如下:
    /*
    MonitorEnter                                          //清空所有已失效的线程本地缓存
    load-load barrier, load-store barrier

    ...

    store-store barrier, load-store barrier               //store-store barrier 刷新线程本地缓存
    MonitorExit
    store-load barrier                                    //store-load barrier 刷新线程本地缓存
    */

}
