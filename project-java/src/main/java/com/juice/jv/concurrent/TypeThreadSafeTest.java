package com.juice.jv.concurrent;

public class TypeThreadSafeTest {
    /*
    //在多线程共享数据的方法: static field
    // 1. primitive type: 共享值
    // 2. reference type: 共享引用值和对象

    //本地变量，共享变量，共享对象，本地对象


    //A type is thread-safe: 该类型(包括实例对象)在多线程中运行没有线程安全问题
    //A thread-safe type，可用于多线程之中
    //A not thread-safe type，不应该用于多线程之中
    //A not thread-safe type，其 object 只读并且已刷新到主存，该 object 可用于多线程之中
    //static field, static method 是(天然)共享的，它们应该 thread-safe


    //thread-safe type，其构造器和方法参数可以是任意类型。(线程)本地变量，读写值(包括引用值)是线程安全的
    // 1. 编写 thread-safe type 时
    //    1). 参数类型可接受 thread-safe: 保证对传入参数的对象的使用是 thread-safe 的
    //    2). 参数类型可接受 not thread-safe
    //        a). 当成本地对象 (对一个非线程安全的参数作线程安全处理不确定性太大)
    //        b). 保证对传入参数的对象的使用是 thread-safe 的，存在无法保证的情况
    //
    // 2. 使用 thread-safe type 时
    //    1). 传入参数的对象的类型 not thread-safe，并且对象同时在其他线程操作时，方法的线程安全性可能遭到破坏。这时，如果要保证方法执行是线程安全的:
    //        a). 查看方法内部代码在操作该参数时是否线程安全
    //        b). 否则，修改传入参数的值
    //    2). 传入参数的对象的类型 not thread-safe 本地对象，在该对象内部使用了 not thread-safe 共享对象，导致方法的线程安全性可能遭到破坏
    //        a). 查看方法内部代码，看线程是否安全
    //        b). 否则，修改传入的参数

    //thread-safe type，其返回值可以是任意类型
    // 1. 编写 thread-safe type 时，某些情况下(尽可能的?)对返回类型 unmodifiable wrap，使之变成线程安全的 (例如: com.juice.jv.concurrent.CopyOnWriteTemp#getReadOnly)
    // 2. 使用 thread-safe type 时，要注意判断返回值类型是否线程安全，存在非线程安全的共享对象


    //判断 type 是否 thread-safe:
    // 1. field(包括其他Type.field, 其他Type.method中使用的field) 存取值(包括引用值)
    // 2. not thread-safe 共享对象
    //    1). thread-safe。参数，返回值
    //    2). not thread-safe 非共享对象。参数，返回值；内部找
    //    3). not thread-safe 共享对象。整体在多线程中操作是否线程安全
    */

    /**
     * 多线程共享数据的方法: static field
     *    1. primitive type: 共享数值
     *    2. reference type: 共享引用值和对象
     *
     * 本地变量，共享变量，共享对象，本地对象
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
     *       将该"not thread-safe 共享对象"封装在 scope，在 scope 中跟踪该对象作线程安全控制，控制对 scope 之外的返回值满足"return-type-safe"原则
     *
     *    6. thread-safe 共享对象
     *       1). 直接暴露给 scope 之外
     *       2). ConcurrentHashMap 泛型实例化的类型 thread-safe & return-type-safe
     *       3). 如果存在方法返回值不满足"return-type-safe"原则
     *           将该"thread-safe 共享对象"封装在 scope，在 scope 中跟踪该对象返回值作线程安全控制，控制对 scope 之外的返回值满足"return-type-safe"原则
     *
     * 共享对象的返回值原则
     *    1. 共享对象本身的方法返回值满足"return-type-safe"原则: 在当前线程中使用(方法调用，返回值等级联的、任意的使用)不存在线程安全问题
     *    2. 或者，共享对象对其所属的 scope 之外的返回值满足"return-type-safe"原则
     *    3. 或者，这一原则可能被打破(实现代价太大或者代码编写错误等等)，则要求"任何代码"得到并使用该返回值时不发生线程安全问题，如名义上只读
     *
     * 如果满足上述 1、2 条，则不会出现不安全的共享对象的泛滥。即，从源头上保证了共享对象的使用是线程安全
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
     * A type is thread-safe: type body 中，所有共享变量，共享对象的使用线程安全
     *
     * A thread-safe type:
     *    1. 构造器调用，方法调用是线程安全(排除参数的影响)
     *    2. 如果传参一个 not return-type-safe 的共享对象(或者包含共享数据)，则该参数的使用不一定是线程安全的
     *    3. 方法返回值:
     *       1). 一般的，满足"return-type-safe"原则
     *       2). 存在一些情况无法保证此原则，如返回值类型是泛型
     *
     *
     * 分析 type 是否 thread-safe 常见的考虑点:
     *    1. 类型的 static field 是共享变量。共享对象的 instance field 也是共享变量
     *    2. field 的限定符: final, volatile。要充分明确这些关键字的意义和作用
     *    3. 共享变量的读写(数值、引用值)是否是线程安全的。特别的，数值运算是否线程安全
     *
     *    4. field指向对象可能是线程安全的，也可能不是线程安全的
     *       1). 线程安全的对象，调用方法，其执行是线程安全的。可能需要进入方法内部看看传递的参数的使用是否线程安全。返回值是否满足"return-type-safe"原则
     *       2). 非线程安全的对象，对其的任何使用，都需要满足线程安全性
     *
     *    5. Type.field 获取"共享对象"。调用方法，其执行是线程安全的。可能需要进入方法内部看看传递的参数的使用是否线程安全。返回值是否满足"return-type-safe"原则
     *
     *    6. Type.method。进入方法内部，追踪"共享对象"(包括参数传递的)，验证其使用是否线程安全。Type.method 返回值是否满足"return-type-safe"原则
     *
     *    7. 局部变量(包括参数)，变量的读写(数值、引用值)是线程安全的，数值运算是线程安全的
     *       局部变量指向对象。进入构造器、方法内部，追踪"共享对象"(包括参数传递的)，验证其使用是否线程安全。构造器、方法返回值是否满足"return-type-safe"原则
     *
     */




}
