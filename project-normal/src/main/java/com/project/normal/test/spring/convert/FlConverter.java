package com.project.normal.test.spring.convert;

public interface FlConverter<S,T> {
    T convert(S source);
}
