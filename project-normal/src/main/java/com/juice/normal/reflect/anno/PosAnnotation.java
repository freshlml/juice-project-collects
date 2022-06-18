package com.juice.normal.reflect.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PosAnnotation {
    PoAnnotation[] value();
}
