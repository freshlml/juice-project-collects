package com.fresh.juice.spring.core.type;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Set;

public class ForTestAnnotationUtils {

    public static void main(String argv[]) {
        testGetDeclaredRepeatableAnnotations();
        System.out.println("############################");
        attrs();

    }


    private static void testGetDeclaredRepeatableAnnotations() {

        //存在无限递归?

        Set<Annotation1> results = AnnotationUtils.getDeclaredRepeatableAnnotations(A.class, Annotation1.class, Annotation2Container.class);
        System.out.println(results);

    }
    @Annotation1("A_1")
    @Annotation2("A_2_1")
    @Annotation2("A_2_2")
    @Annotation3("A_3")
    private static class A{}

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Annotation1 {
        String value() default "1";
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Repeatable(Annotation2Container.class)
    @interface Annotation2 {
        String value() default "2";
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Annotation2Container {
        Annotation2[] value();
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Annotation4("多层")
    @Annotation1("other A_1")
    @Annotation2("other A_2_1")
    @Annotation2("other A_2_2")
    @interface Annotation3 {
        String value() default "3";
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Annotation1("多层 A_1")
    @interface Annotation4 {
        String value() default "4";
    }


    public static void attrs() {

        Annotation5 rawAnnotation = Attr.class.getAnnotation(Annotation5.class);

        Annotation5 synthesizeAnnotation = AnnotationUtils.findAnnotation(Attr.class, Annotation5.class);

        System.out.println(Arrays.toString(rawAnnotation.locations()));
        System.out.println(Arrays.toString(synthesizeAnnotation.locations()));

        AnnotationAttributes attrs = AnnotationUtils.getAnnotationAttributes(null, synthesizeAnnotation,
                                                       false, true);

        System.out.println(attrs);


    }

    @Annotation5(value = "a5", paths = {"1", "2"}, clazz = ForTestAliasForCheck.class, a6 = @Annotation6)
    private static class Attr{}

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Annotation1
    @interface Annotation5 {
        String value() default "5";

        @AliasFor("paths")
        String[] locations() default {};
        @AliasFor("locations")
        String[] paths() default {};

        Class<ForTestAliasForCheck> clazz();

        Annotation6 a6();
    }
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Annotation6 {
        String value() default "${one}";
    }

}
