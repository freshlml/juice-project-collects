package com.juice.jv.concurrent;

@SuppressWarnings("unused")
public class SynchronizedTest {
    //原子性: 不可打断，互斥

    //从内存屏障角度来理解 synchronized 如何限制指令重排序和保证可见性:
    /*
    MonitorEnter
    load-load barrier, load-store barrier

    ...

    store-store barrier, load-store barrier
    MonitorExit
    store-load barrier
    */

}
