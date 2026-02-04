package com.fresh.juice.spring.core.type;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class ForTestAliasForCheck {

    //@AliasFor使用规则测试

    public static void main(String argv[]) throws Exception {
        pair();
        meta();
        meta_present();
    }

    //explicit alias pair
    public static void pair() throws Exception {
        Method m = ForTestAliasForCheck.class.getDeclaredMethod("m1");


        //1.attribute and AliasFor指定的attribute must declare default value and
        //  must declare the same default value
        //2.attribute and AliasFor指定的attribute must declare the same return type
        //3.#annotation should not be used
        //4.不能定义指向自己的AliasFor
        //5.explicit alias pair必须pair

        AnnotationUtils.synthesizeAnnotation(m.getAnnotation(PairAnnotation.class), m);

    }
    @PairAnnotation
    void m1() {}
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    @interface PairAnnotation {
        @AliasFor(value = "locations")
        String[] value() default {};
        @AliasFor("value")
        String[] locations() default {};
    }

    //Explicit alias for meta-annotation
    public static void meta() throws Exception {
        Method m = ForTestAliasForCheck.class.getDeclaredMethod("m2");

        //1.#annotation must reference an hierarchy meta-annotation
        //2.#attribute must reference the attribute in #annotation
        //3.attribute and AliasFor指定的attribute must declare the same return type
        //4.attribute and AliasFor指定的attribute must declare a default value and
        //  must declare the same default value

        AnnotationUtils.synthesizeAnnotation(m.getAnnotation(ThreeAnnotation.class), m);


        //1.#annotation must reference an hierarchy meta-annotation
        //2.#attribute must reference the attribute in #annotation
        //3.attribute and AliasFor指定的attribute must declare the same return type
        AnnotationUtils.synthesizeAnnotation(m.getAnnotation(TwoAnnotation.class), m);

    }

    @ThreeAnnotation
    @TwoAnnotation
    void m2() {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Documented
    @interface MetaAnnotation {
        @AliasFor("locations")
        String[] value() default {};
        @AliasFor("value")
        String[] locations() default {};
        String t();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE,ElementType.METHOD})
    @Documented
    @MetaAnnotation(t = "meta-t")
    @interface TwoAnnotation {  //直接的
        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] value() default {};
        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] onePaths() default {};
        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] twoPaths() default {};

        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "t")
        String two_t() default "";
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    @TwoAnnotation
    @interface ThreeAnnotation {
        @AliasFor(annotation = TwoAnnotation.class,
                attribute = "twoPaths")
        String[] p1() default {};
        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] p2() default {};

        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "t")
        String three_t() default "";
    }


    /**
     *  meta-annotation: 元注解，即注解上的注解，元注解仍然可以是directly present
     *                    or indirectly present。**declared**方法通常并不会返回
     *                    更多的注解，因为注解类不会继承任何注解。
     *  meta-present: hierarchy meta-annotation
     *                 当meta-annotation是directly present时,其本身在继承路径中
     *                 当meta-annotation是indirectly present时,容器注解在继承路径中
     */
    public static void meta_present() throws Exception {

        Method m = ForTestAliasForCheck.class.getDeclaredMethod("m3");

        AnnotationUtils.synthesizeAnnotation(m.getAnnotation(FourAnnotation.class), m);

    }
    @FourAnnotation
    void m3() {}
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    @ComponentScan("1")
    @ComponentScan("2")
    @interface FourAnnotation {
        @AliasFor(annotation = ComponentScan.class, attribute = "value")
        String[] value() default {};

        @AliasFor(annotation = ComponentScan.class, attribute = "value")
        String[] path() default {};
    }


}
