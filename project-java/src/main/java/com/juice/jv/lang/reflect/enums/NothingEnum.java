package com.juice.jv.lang.reflect.enums;

public enum NothingEnum {
    ONE("one", "one");

    NothingEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }
    private String value;
    private String text;

}
