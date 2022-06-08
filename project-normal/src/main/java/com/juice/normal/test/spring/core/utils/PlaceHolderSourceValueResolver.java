package com.juice.normal.test.spring.core.utils;

/**
 * PlaceHolder属性值获取器
 */
@FunctionalInterface
public interface PlaceHolderSourceValueResolver {
    String sourceValue(String key);
}
