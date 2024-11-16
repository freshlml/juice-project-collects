package com.juice.jv.concurrent;


public class FinalFieldVisibilityTest {

    //17.5 final Field Semantics (Java Language Specification for Java SE 8):
    // 1. An object is considered to be "completely initialized" when its constructor finishes.
    // 2. A thread that can only see a reference to an object after that object has been "completely initialized"
    //    is guaranteed to see the correctly initialized values for that object's final fields.
    // 注意: 如果在构造器中将 this 传递给其他线程，则其他线程中对 this 指向对象的 final fields 仍然可能存在可见性问题。

    //对指令重排序的限制:
    //  1. 在构造函数内对一个 final 域的写入，与随后把这个构造函数的引用赋值给一个引用变量，两个操作不能重排序
    //     x.finalField = v; StoreStore; sharedRef = x;
    //  2. 初次读一个包含 final 域对象的引用，和随后初次读这个 final 域，这两个操作不能重排序
    //     x = sharedRef; ... ; LoadLoad; i = x.finalField;
    //
    //插入内存屏障指令的位置: 一般在构造函数结束之前，也可能在构造函数之中，总之要保证，其他线程看到该构造函数的引用之前插入内存屏障指令

    static class MyClass {
        int x;
        final int y;
        final Model model;

        public MyClass() {
            x = 1;
            y = 2;  //final-write
            model = new Model(3);  //final-write
            //在适当位置(在构造函数的引用值赋值给变量之前)插入 StoreStore
            //使得 final field y、final field model 连同其非 final 的 model.z 的本地缓存刷新并禁止指令重排序
        }
        private static MyClass myClass;
        //thread1
        public void write() {
            myClass = new MyClass();
        }
        //thread2
        public void read() {
            if(myClass != null) {
                //在 myClass 与 myClass.field 之间插入 LoadLoad，禁止指令重排序
                int x = myClass.x;         //could see 0
                int y = myClass.y;         //guaranteed to 2
                int z = myClass.model.z;   //guaranteed to 3, even the field z of Model is not final
            }
        }
        public void can_not_guarantee() {  // final field 不保证后续修改的 visibility
            //thread 3
            myClass.model.z = 4;

            //thread 4
            int zz = myClass.model.z; //not guaranteed to 4
        }
    }
    static class Model {
        int z;
        Model(int z) {
            this.z = z;
        }
    }

}




