package com.juice.jv.concurrent;

public class SynchronizedTest {
    //原子性

    //可见性
    //MonitorEnter    //清空缓存
    //...
    //MonitorExit     //刷新缓存

    //对指令重排序的限制
    //  1. synchronized 之中的变量读写不能重排序其范围外面
    //  2. synchronized 之前的写不能重排序到 MonitorExit 之后
    //  3. synchronized 之后的读不能重排序到 MonitorEnter 之前

}
