package com.juice.normal.enums;

import java.util.HashMap;
import java.util.Map;

public class EnumEat {

    public static void main(String argv[]) {

        Class<? extends ScanTypeEnum> clazz = ScanTypeEnum.SYSTEM.getClass();
        System.out.println(clazz);

        Enum<ScanTypeEnum> father = ScanTypeEnum.SYSTEM;

        //Enum<?> 是使用enum关键字定义的枚举的基类型
        Map<Enum<?>, String> e = new HashMap<>();
        e.put(ScanTypeEnum.SYSTEM, "123");
        e.put(ScanTypeEnum2.SYSTEM, "321");

        //Enum<ScanTypeEnum> 是使用enum关键字定义的ScanTypeEnum枚举的基类型
        Map<Enum<ScanTypeEnum>, String> e2 = new HashMap<>();
        e2.put(ScanTypeEnum.SYSTEM, "123");
        //e2.put(ScanTypeEnum2.SYSTEM, "321");


    }
}
