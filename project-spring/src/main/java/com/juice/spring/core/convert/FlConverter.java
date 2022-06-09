package com.juice.spring.core.convert;

public interface FlConverter<S,T> {
    T convert(S source);
}
