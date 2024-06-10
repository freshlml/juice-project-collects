package com.juice.jv.concurrent;

public class TypeThreadSafeTest {

    //在多线程共享数据的方法: static field
    // 1. primitive type: 共享值
    // 2. reference type: 共享引用值和对象


    //A type is thread-safe: 该类型(包括实例对象)在多线程中运行没有线程安全问题
    //A thread-safe type，可用于多线程之中
    //A not thread-safe type，不应该用于多线程之中
    //A not thread-safe type，其 object 只读并且已刷新到主存，该 object 可用于多线程之中


    //thread-safe type，其构造器和方法参数可以是任意类型
    // 1. 编写 thread-safe type 时
    //    1). thread-safe type 参数，保证对参数的使用 thread-safe
    //    2). not thread-safe type 参数，认为其不应该用于多线程之中(对一个非线程安全的参数作线程安全处理不确定性太大)
    //
    // 2. 使用 thread-safe type 时，传入参数的值的类型 not thread-safe，并且同时在其他线程操作时，方法的线程安全性可能遭到破坏。这时，如果要保证方法执行是线程安全的:
    //                            1). 查看方法内部代码在操作该参数时是否线程安全
    //                            2). 否则，修改传入参数的值

    //thread-safe type，其返回值可以是任意类型
    // 1. 编写 thread-safe type 时，某些情况下(尽可能的?)对返回值 unmodifiable wrap，使之变成线程安全的 (例如: com.juice.jv.concurrent.CopyOnWriteTemp#getReadOnly)
    // 2. 使用 thread-safe type 时，要注意判断返回值是否是线程安全的


}
