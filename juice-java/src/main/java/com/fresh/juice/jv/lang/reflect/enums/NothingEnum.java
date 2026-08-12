package com.fresh.juice.jv.lang.reflect.enums;

public enum NothingEnum {
    ONE("one", "one");

    NothingEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }
    private final String value;
    private final String text;

}
