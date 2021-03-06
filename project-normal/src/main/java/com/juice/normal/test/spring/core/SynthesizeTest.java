package com.juice.normal.test.spring.core;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SynthesizeTest {
    @AliasFor("nameAli")
    String valueAli() default "";
    @AliasFor("valueAli")
    String nameAli() default "";
}
