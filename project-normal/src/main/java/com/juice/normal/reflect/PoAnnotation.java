package com.juice.normal.reflect;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(PosAnnotation.class)
public @interface PoAnnotation {
    String name();
}
