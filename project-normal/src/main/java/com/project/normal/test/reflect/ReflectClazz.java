package com.project.normal.test.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@PoAnnotation
@PoAnnotation
public class ReflectClazz extends ReflectBase {

    public ReflectClazz(String say) {
        super(say);
    }

    @Override
    public void say(String say) {
        System.out.println(say);
    }

    @Override
    public void say() {
        super.say();
    }

    public static void main(String argv[]) {
        Method[] ms = ReflectClazz.class.getMethods();
        System.out.println(ms);

        Constructor<?>[] cs = ReflectClazz.class.getConstructors();
        System.out.println(cs);
    }

}
