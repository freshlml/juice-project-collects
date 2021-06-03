package com.project.normal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum ScanTypeEnum {
    SYSTEM("SYSTEM1", "系统");

    private String value;

    private String text;

    ScanTypeEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @JsonCreator
    public static ScanTypeEnum getByValue(String value) {
        return Arrays.stream(ScanTypeEnum.values()).filter(scan -> scan.getValue().equals(value)).findFirst().orElse(null);
    }

}
