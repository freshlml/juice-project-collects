package com.juice.spring.core.type;


import java.lang.annotation.*;
import java.lang.reflect.Method;

public class ForTestMetaAnnotation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Documented
    @interface SuperAnnotation {
        String t();
        String s();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE,ElementType.METHOD})
    @Documented
    @SuperAnnotation(t = "t", s = "s")            //自定义注解作为元注解
    @interface TempAnnotation {
        String value();
        String t();
    }

    @TempAnnotation(value = "temp", t="override T")
    void m() {}

    /**
     *  meta-annotation: 元注解，即注解上的注解。注解作为AnnotatedElement声明注解，
     *                    和Class等AnnotatedElement在声明注解语法方面没有区别，即
     *                    元注解任可以声明成directly present or indirectly present
     *  hierarchy meta-annotation: 注解上定义注解，构成一个注解的继承结构
     *               当meta-annotation是directly present时,其本身在继承结构中
     *               当meta-annotation是indirectly present时,容器注解在继承结构中
     *
     *  无限递归: meta-annotation造成解析时的无限递归?,visited机制
     *
     */
    public static void main(String argv[]) throws Exception {
        //TempAnnotation的is-a性质: TempAnnotation is a SuperAnnotation

        Method m = ForTestMetaAnnotation.class.getDeclaredMethod("m");
        TempAnnotation tempAnnotation = m.getAnnotation(TempAnnotation.class);

        System.out.println(tempAnnotation.value());  //temp
        System.out.println(tempAnnotation.t());      //override T

        SuperAnnotation superAnnotation = tempAnnotation.annotationType().getAnnotation(SuperAnnotation.class);
        System.out.println(superAnnotation.t());   //t
        System.out.println(superAnnotation.s());   //s

        //1.有别于类的继承
        //sub = new Sub();
        //sub.继承的基类方法
        //而通过meta-annotation自定义的注解的继承，无法做到：
        //  tempAnnotation.SuperAnnotation中属性
        //因此, no,todo
                //TempAnnotation的属性解析规则: (类似于python的属性搜索规则)
                //先查找TempAnnotation属性；如果找不到，在查找SuperAnnotation属性。即TempAnnotation属性覆盖SuperAnnotation中同名属性

        //2.hierarchy meta-annotation
        //  tempAnnotation.TempAnnotation中属性
        //  tempAnnotation as SuperAnnotation superAnnotation
        //  superAnnotation.SuperAnnotation中属性(符合覆盖定义的属性会被TempAnnotation中的属性覆盖)

    }

}
