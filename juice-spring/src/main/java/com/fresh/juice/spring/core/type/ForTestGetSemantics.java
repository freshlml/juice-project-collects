package com.fresh.juice.spring.core.type;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.*;
import java.util.Set;

public class ForTestGetSemantics {

    public static void main(String argv[]) {
        testGetMergedAnnotationAttributes();
        System.out.println("########################");

        testGetAllMergedAnnotations();
        System.out.println("########################");

        testGetMergedRepeatableAnnotations();
    }

    //test AnnotatedElementUtils#getMergedAnnotationAttributes
    private static void testGetMergedAnnotationAttributes() {

        AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(Attr.class, OneAnnotation.class);
        System.out.println(attrs);

        AnnotationAttributes metaAttrs = AnnotatedElementUtils.getMergedAnnotationAttributes(Attr.class, MetaAnnotation.class);
        System.out.println(metaAttrs);

        AnnotationAttributes metaAttr2s = AnnotatedElementUtils.getMergedAnnotationAttributes(Attr2.class, MetaAnnotation.class);
        System.out.println(metaAttr2s);

    }

    @OneAnnotation(t="T", onePaths={"1", "2"})
    private static class Attr{}

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MetaAnnotation {
        @AliasFor("locations")
        String[] value() default {};
        @AliasFor("value")
        String[] locations() default {};
        String t();
    }
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MetaAnnotation(t="meta-t")
    @interface OneAnnotation {
        //String locations() default {}; //?
        //String value; //?

        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] onePaths();

        String t();
    }


    @ThreeAnnotation(p1 = {"p"}, p2 = {"p"})
    private static class Attr2{}

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MetaAnnotation(t="meta-t")
    @interface TwoAnnotation {
        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] value() default{};
        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] onePaths() default{};
        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] twoPaths() default{};

    }
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @TwoAnnotation(onePaths = {"onePaths"})
    @interface ThreeAnnotation {
        @AliasFor(annotation = TwoAnnotation.class,
                attribute = "twoPaths")
        String[] p1() default {};
        @AliasFor(annotation = MetaAnnotation.class,
                attribute = "locations")
        String[] p2() default {};
    }


    //test AnnotatedElementUtils#testGetAllMergedAnnotations
    private static void testGetAllMergedAnnotations() {

        Set<Annotation1> attrs = AnnotatedElementUtils.getAllMergedAnnotations(Attr3.class, Annotation1.class);

        System.out.println(attrs);

    }

    @Annotation3("first-3")
    @Annotation22("first-22")
    @Annotation1("first-1")
    private static class Attr3{}

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
    @Annotation1("2-1")
    @interface Annotation2 {
        String value() default "2";
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Annotation1("22-1")
    @interface Annotation22 {
        String value() default "2";
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Annotation2("3-2")
    @Annotation1("3-1")
    @interface Annotation3 {
        String value() default "3";
    }


    //test AnnotatedElementUtils#getMergedRepeatableAnnotations
    private static void testGetMergedRepeatableAnnotations() {

        Set<Annotation4> attrs = AnnotatedElementUtils.getMergedRepeatableAnnotations(Attr4.class, Annotation4.class);

        System.out.println(attrs);

    }

    @Annotation6
    @Annotation4("first-4-1")
    @Annotation4("first-4-2")
    private static class Attr4{}

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Repeatable(Annotation4Container.class)
    @interface Annotation4 {
        String value() default "4";
    }
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Annotation4Container {
        Annotation4[] value();
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Annotation4("5-4-1")
    @Annotation4("5-4-2")
    @interface Annotation5 {
        String value() default "5";
    }
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Annotation4("6-4-1")
    @Annotation4("6-4-2")
    @Annotation5
    @interface Annotation6 {
        String value() default "6";
    }



}
