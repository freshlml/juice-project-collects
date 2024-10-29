package com.juice.jv.concurrent;

public class ThreadSafeTest {

    /*
     * 多线程共享数据的方法: static field
     *    1. primitive type: 共享数值
     *    2. reference type: 共享引用值和对象
     *
     * 共享变量(shared variable)，共享对象(shared object)
     *
     *
     * 共享对象
     *    1. immutable
     *       thread-safe & return-type-safe
     *    2. effectively immutable
     *       一旦数据刷新到主存，thread-safe & return-type-safe
     *    3. unmodifiable
     *       thread-safe
     *
     *    4. 名义上 readonly (not thread-safe and mutable) 共享对象     private/package static final field --> char[]  //人为约定只读
     *       将该共享对象封装在 scope，在 scope 中跟踪该对象只作读操作，控制不对 scope 之外返回该对象
     *
     *    5. not thread-safe 共享对象
     *       原则上，一个"not thread-safe 共享对象"不直接暴露给scope之外。在 scope 中对该对象作线程安全控制。控制对 scope 之外的返回值满足"return-type-safe"原则
     *
     *    6. thread-safe 共享对象
     *       1). 直接暴露给 scope 之外
     *       2). ConcurrentHashMap 泛型实例化的类型 thread-safe & return-type-safe
     *       3). 如果存在方法返回值不满足"return-type-safe"原则
     *           将该"thread-safe 共享对象"封装在 scope，在 scope 中对该对象返回值作线程安全控制，控制对 scope 之外的返回值满足"return-type-safe"原则
     *
     * 共享对象的返回值原则
     *    1. 共享对象本身的方法返回值满足"return-type-safe"原则: 在当前线程中使用(方法调用，返回值等级联的、任意的使用)不存在线程安全问题
     *    2. 或者，共享对象对其所属的 scope 之外的返回值满足"return-type-safe"原则
     *    3. 或者，这一原则可能被打破(实现代价太大或者代码编写错误等等)，则要求"任何代码"得到并使用该返回值时不发生线程安全问题，如名义上只读
     *
     * 如果满足上述 1、2 条，则不会出现不安全的共享对象的泛滥。即，从源头上保证了共享对象的使用是线程安全的
     * 如果上述 1、2 条被打破:
     *    1. Type A 编码时，获得一个共享对象(static field)
     *       1). 判断该共享对象是否满足 return-type-safe 原则
     *       2). 或者，仅判断共享对象的当前方法调用的返回值是否满足 return-type-safe 原则，并不断追踪
     *       3). 或者，想象共享对象的代码逻辑，推断该共享对象是否满足 return-type-safe 原则
     *
     *    2. 当判断一个共享对象是否满足 return-type-safe 原则后，就可以寻机使用之，以使得共享对象的使用是线程安全的
     *
     *    3. 如果 Type A 编码时，获得一个共享对象后没有关注其是否满足 return-type-safe 原则且该共享对象实际上不满足该原则。则
     *       1). 该共享对象和其方法调用返回值在 Type A 中借由参数、返回值被任意传递
     *
     *    4. Type B 使用 Type A，由于不知道 Type A 是否正确使用了共享对象，则
     *       1). 需要进入 Type A 内部，追踪共享对象，而这个追踪网络可能很复杂
     *
     *
     *A class(object) is thread-safe:
     *  - instance field 当成共享数据在多线程中没有线程安全问题(即，正确处理了可能存在的 data race，保证了可见性，摒除了可能存在的指令重排序的影响等)
     *  - 该 class(object) 在多线程中使用没有线程安全问题
     *
     */
}
