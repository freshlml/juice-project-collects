package com.project.normal.spring.convert;

public interface FlConverter<S,T> {
    T convert(S source);
}
