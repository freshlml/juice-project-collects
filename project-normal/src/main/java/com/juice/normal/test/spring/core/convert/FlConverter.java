package com.juice.normal.test.spring.core.convert;

public interface FlConverter<S,T> {
    T convert(S source);
}
