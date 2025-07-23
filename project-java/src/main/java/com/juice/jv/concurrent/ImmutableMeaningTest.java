package com.juice.jv.concurrent;

@SuppressWarnings("unused")
public class ImmutableMeaningTest {

    //An object is immutable: 该对象构造之后，其状态保持不变(一个无状态对象是不可变的特殊情况)

    //class Boolean, Byte, Short, Integer, Long, Float, Double, String is immutable.
    //class Enum Constant, Annotation Instance is immutable.
    //class Type, TypeVariable, GenericArrayType, ParameterizedType, WildcardType, Class, AnnotatedType is immutable.
    //class Field, Method, Constructor, Parameter is immutable.
    //class java.time.* is immutable.


    //this object(or class) is immutable
    static class Immutable1 {
        private final Immutable1Used immutable1Used;
        public Immutable1(Immutable1Used immutable1Used) {
            this.immutable1Used = immutable1Used;
        }
        public Immutable1Used getImmutable1Used() {
            return this.immutable1Used;
        }
        public Immutable1 add(int added) {
            return new Immutable1(this.immutable1Used.add(added));
        }
    }
    static class Immutable1Used {  //this object(or class) is immutable
        private final int x;
        public Immutable1Used(int x) {
            this.x = x;
        }
        public int getX() {
            return this.x;
        }
        public Immutable1Used add(int added) {
            return new Immutable1Used(this.x + added); //may overflow
        }
    }


    //this object(or class) is immutable
    static class Immutable2 {
        private final Immutable2Used immutable2Used;  //如果子类或者外部对该 field 可见，必须确保不修改该 field 指向对象的状态。
        public Immutable2(Immutable2Used immutable2Used) {
            //深拷贝一份，防止外部对该参数指向对象修改而导致 this object 的 immutable 性质受损。
            //也可以不拷贝，但要确保外部不会修改参数指向对象的的状态。
            Immutable2Used copied = new Immutable2Used(immutable2Used.getX());
            this.immutable2Used = copied;
        }
        //1. 深拷贝一份，杜绝外部对 this.immutable2Used 指向对象的修改, 从而导致 this object 的 immutable 性质受损。
        //2. 对 this.immutable2Used 作只读封装。 (优先)
        //3. 如果约定外部不会对 this.immutable2Used 指向对象修改，则可直接返回。
        public Immutable2Used getImmutable2Used() {
            return new Immutable2Used(this.immutable2Used.getX());
            //return the readonly of this.immutable2Used;
            //return this.immutable2Used;
        }
        public Immutable2 add(int added) {
            //can not alter the status of the object pointed by this.immutable2Used.
            Immutable2Used newImmutable2Used = new Immutable2Used(this.immutable2Used.getX());  //深拷贝一份.
            newImmutable2Used.add(added);
            return new Immutable2(newImmutable2Used);
        }
    }
    static class Immutable2Used {  //this object(or class) is mutable
        private int x;
        public Immutable2Used(int x) {
            this.x = x;
        }
        public int getX() {
            return this.x;
        }
        public Immutable2Used add(int added) {
            this.x += added;  //may overflow
            return this;
        }
    }


    //this object(or class) is effectively immutable
    static class Immutable3 {
        private Immutable3Used immutable3Used;  //如果子类或者外部对该 field 可见，必须确保不修改该 field 的引用值以及其指向的对象的状态。
        public Immutable3(Immutable3Used immutable3Used) {
            //深拷贝一份，防止外部对该参数指向对象修改而导致此 object 的 immutable 性质受损。
            //也可以不拷贝，但要确保外部不会修改参数指向对象的的状态。
            Immutable3Used copied = new Immutable3Used(immutable3Used.getX());
            this.immutable3Used = copied;
        }
        //1. 深拷贝一份，杜绝外部对 this.immutable3Used 指向对象的修改, 从而导致 this object 的 immutable 性质受损。
        //2. 对 this.immutable3Used 作只读封装。
        //3. 如果约定外部不会对 this.immutable3Used 指向对象修改，则可直接返回。
        public Immutable3Used getImmutable3Used() {
            return new Immutable3Used(this.immutable3Used.getX());
            //return the readonly of this.immutable3Used;
            //return this.immutable3Used;
        }
        public Immutable3 add(int added) {
            //can not alter the reference of this.immutable3Used, and the status of the object pointed by this.immutable3Used.
            Immutable3Used newImmutable3Used = new Immutable3Used(this.immutable3Used.getX());  //深拷贝一份.
            newImmutable3Used.add(added);
            return new Immutable3(newImmutable3Used);
        }
    }
    static class Immutable3Used {  //this object(or class) is mutable
        private int x;
        public Immutable3Used(int x) {
            this.x = x;
        }
        public int getX() {
            return this.x;
        }
        public Immutable3Used add(int added) {
            this.x += added;  //may overflow
            return this;
        }
    }


    //this object(or class) is effectively immutable
    static class Immutable4 {
        private Immutable4Used immutable4Used;  //如果子类或者外部对该 field 可见，必须确保不修改该 field 的引用值。
        public Immutable4(Immutable4Used immutable4Used) {
            this.immutable4Used = immutable4Used;
        }
        public Immutable4Used getImmutable4Used() {
            return this.immutable4Used;
        }
        public Immutable4 add(int added) {
            //can not alter the reference of this.immutable4Used.
            return new Immutable4(this.immutable4Used.add(added));
        }
    }
    static class Immutable4Used {  //this object(or class) is immutable
        private final int x;
        public Immutable4Used(int x) {
            this.x = x;
        }
        public int getX() {
            return this.x;
        }
        public Immutable4Used add(int added) {
            return new Immutable4Used(this.x + added); //may overflow
        }
    }


    //unmodified 封装
    /*static class UnmodifiedList<E> extends List<E> {  //this object(or class) is immutable
        private final List<E> list;               //private field, 外部不可见
        public UnmodifiedList(List<E> list) {     //深拷贝一份，可杜绝外部的影响。对 list 深拷贝一份代价太大，则要确保外部不会修改参数 list 指向对象的状态。
            this.list = list;
        }
        @Override
        public boolean add(E e) {
            throw UnsupportedOperationException("not supported");
        }
        ...
    }*/


    //immutable 的例子甚多，总之，要紧扣 immutable 的定义，即，对象构造之后，状态不变.

    //A class 中存在若干 field，其状态保持不变，同时存在若干 field，其状态会发生改变.

}
