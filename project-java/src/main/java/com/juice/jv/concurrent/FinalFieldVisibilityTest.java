package com.juice.jv.concurrent;


public class FinalFieldVisibilityTest {

    //17.5 final Field Semantics (Java Language Specification for Java SE 8):
    // An object is considered to be "completely initialized" when its constructor finishes.
    // A thread that can only see a reference to an object after that object has been "completely initialized"
    //   is guaranteed to see the correctly initialized values for that object's final fields.
    // 注意: 如果在构造器中将 this 传递给其他线程，则其他线程中对 this 指向对象的 final fields 仍然可能存在可见性问题。

    static class MyClass {
        int x;
        final int y;
        final Model model;

        public MyClass() {
            x = 1;  //异步刷新本地缓存
            y = 2;
            model = new Model(3);
            //同步刷新 final field y 本地缓存。同步刷新 final field model and model.z (非 final) 本地缓存
        }

        //https://javamex.com/tutorials/synchronization_final.shtml
        //todo: 查看编译后的字节码，看看添加了什么特别的指令
        //todo: final parameter, final local variable
        public void read() {
            //one thread
            MyClass myClass = new MyClass();

            //another thread
            int x = myClass.x;         //could see 0
            int y = myClass.y;         //guaranteed to 2
            int z = myClass.model.z;   //guaranteed to 3, even the field z of Model is not final

            //thread 3
            myClass.model.z = 4;      //同步刷新本地缓存
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

    //final practice
    //  1). --> immutable
    //  2). --> effectively immutable
    //  3). --> mutable unmodifiable wrap
    //  4). --> thread-safe type
}




