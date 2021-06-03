package com.project.normal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum ScanTypeEnum2 {
    SYSTEM("SYSTEM", "系统");

    private String value;

    private String text;

    ScanTypeEnum2(String value, String text) {
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
    public static ScanTypeEnum2 getByValue(String value) {
        return Arrays.stream(ScanTypeEnum2.values()).filter(scan -> scan.getValue().equals(value)).findFirst().orElse(null);
    }

}
