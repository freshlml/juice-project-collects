package com.project.normal.reflect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PosAnnotation {
    PoAnnotation[] value();
}
