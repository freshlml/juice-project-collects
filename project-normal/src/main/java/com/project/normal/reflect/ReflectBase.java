package com.project.normal.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class ReflectBase implements ReflectInterface, ReflectInterface2 {

    private String say;
    private String what;

    public ReflectBase(String say) {
        this.say = say;
    }

    @Override
    public void say() {
        System.out.println(this.say);
    }

    public static void base() {}

    public static void main(String argv[]) {
        Method[] ms = ReflectBase.class.getMethods();
        System.out.println(ms);
        try {
            Method m = ReflectBase.class.getDeclaredMethod("say", null);
            System.out.println(m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Constructor<?>[] cs = ReflectInterface.class.getConstructors();
        System.out.println(cs);
    }
}
