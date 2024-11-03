package com.juice.jv.concurrent;

import sun.misc.Unsafe;

public class VolatileTest {
}

class VolatileTest1 {
    private static int x;
    public static void main(String[] argv) {

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("update begin...");
            x = 1;                                   //线程本地缓存异步刷到主存，不会失效化其他线程中对相同目标的本地缓存
            System.out.println("update end...");
        }).start();

        int i = 0;
        while(x == 0) {                              //线程本地缓存中的 x 值一直是 0，除非清空本地缓存
            if(i == 0) {
                System.out.println("running begin...");
                i = 1;
            }
        }
        System.out.println("running end...");        //不会执行到此
    }
}

class VolatileTest2 {
    private static int x;
    public static void main(String[] argv) {

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("update begin...");
            x = 1;                                  //线程本地缓存异步刷到主存，不会失效化其他线程中对相同目标的本地缓存
            System.out.println("update end...");
        }).start();

        int i = 0;
        while(x == 0) {                             //因为线程本地缓存被清空，去主存中取 x 的值
            if(i == 0) {
                System.out.println("running begin...");
                i = 1;
            }
            synchronized (VolatileTest.class) {     //清空本地缓存
            }
        }
        System.out.println("running end...");       //run
    }
}


class VolatileTest3 {
    private static volatile int x;
    public static void main(String[] argv) {

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("update begin...");
            x = 1;                                   //线程本地缓存同步刷到主存 (内存屏障指令)
            System.out.println("update end...");
        }).start();

        int i = 0;
        while(x == 0) {                              //失效化线程本地缓存，去主存中取 x 的值 (内存屏障指令)
            if(i == 0) {
                System.out.println("running begin...");
                i = 1;
            }
        }
        System.out.println("running end...");        //run
    }
}


//Full volatile Visibility Guarantee: https://jenkov.com/tutorials/java-concurrency/volatile.html#full-volatile-visibility-guarantee

//1.If Thread A writes to a volatile variable and Thread B subsequently reads the same volatile variable,
//  then all variables visible to Thread A before writing the volatile variable,
//  will also be visible to Thread B after it has read the volatile variable.
//2.If Thread A reads a volatile variable, then all all variables visible to Thread A
//  when reading the volatile variable will also be re-read from main memory.

//为了保证 volatile 的可见性，对指令重排序的限制如下:
// 1. 对 volatile 变量写之前的变量读写，不能重排序到对 volatile 变量写之后
// 2. 对 volatile 变量读之后的变量读写，不能重排序到对 volatile 变量读之前

class MyClass {
    private int years;
    private int months;
    private volatile int days;

    public void update(int years, int months, int days){
        this.years  = years;
        this.months = months;
        this.days   = days;    //线程本地缓存同步刷新 (内存屏障指令)
        // when a value is written to days, then all variables "visible" to the thread are also written to main memory.
        // That means, that when a value is written to days, the values of years and months are also written to main memory.
    }

    public int totalDays() {
        // When reading the value of days, the values of months and years are also read into main memory.
        // Therefore you are guaranteed to see the latest values of days, months and years with the below read sequence.
        int total = this.days;    //失效化线程本地缓存 (内存屏障指令)
        total += this.months * 30;  //may overflow
        total += this.years * 365;  //may overflow
        return total;
    }
}

//MyClass 类的问题: update方法中，years, months 依赖于 days 对线程本地缓存的同步刷新，当代码执行到 `this.months = months` 线程切换时，仍然有可见性问题。
//使用 CopyOnWrite 解决上述问题: 使"依赖方"在"被依赖方"同步刷新本地缓存之前，对其他线程不可见。

class CopyOnWriteTemp {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;
    static {
        try {
            valueOffset = unsafe.objectFieldOffset(CopyOnWriteTemp.class.getDeclaredField("temp"));
        } catch (Exception ex) { throw new Error(ex); }
    }
    private boolean compareAndSet(Temp except, Temp update) {
        return unsafe.compareAndSwapObject(this, valueOffset, except, update);
    }

    private volatile Temp temp;

    public CopyOnWriteTemp(int i) {
        this.temp = new Temp(i);
    }

    /*public synchronized void add(int added) {  //synchronized
        Temp cow = new Temp(this.temp.getI());
        cow.add(added);
        this.temp = cow;
    }
    */
    public void add(int added) {  //volatile + cas
        Temp cow = new Temp();
        do {
            cow.setI(this.temp.getI());
            cow.add(added);
        } while(!compareAndSet(this.temp, cow));
    }

    public int get() {
        return this.temp.getI();
    }

    public Temp getReadOnly() {
        return new UnmodifiableTemp(this.temp);
    }
}
class Temp {
    private int i;
    public Temp() {}
    public Temp(int i) {
        this.i = i;
    }
    public int getI() {
        return i;
    }
    public void setI(int i) {
        this.i = i;
    }
    public void add(int added) {
        this.i += added; //may overflow
    }
}

class UnmodifiableTemp extends Temp {
    public UnmodifiableTemp(Temp temp) {
        super(temp.getI());
    }
    @Override
    public void setI(int i) {
        throw new UnsupportedOperationException("不支持修改");
    }
    @Override
    public void add(int added) {
        throw new UnsupportedOperationException("不支持修改");
    }
}