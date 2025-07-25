package com.juice.jv.concurrent;

@SuppressWarnings("unused")
public class SynchronizedTest {
    //原子性: 不可打断，互斥

    //从内存屏障角度来理解 synchronized 如何限制指令重排序和保证可见性:
    /*
    MonitorEnter                                          //清空线程本地缓存
    load-load barrier, load-store barrier

    ...

    store-store barrier, load-store barrier               //store-store barrier 刷新缓存
    MonitorExit
    store-load barrier                                    //store-load barrier 刷新缓存
    */

}
