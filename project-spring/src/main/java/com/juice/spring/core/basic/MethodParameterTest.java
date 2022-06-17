package com.juice.spring.core.basic;

import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public class MethodParameterTest {

    public static void main(String argv[]) throws Exception {
        MethodParameterTest testObj = new MethodParameterTest();
        Method method = testObj.getClass().getMethod("test", new Class<?>[]{Map.class});

        MethodParameter methodParameter = MethodParameter.forExecutable(method, 0);

        Class<?> parameterType = methodParameter.getParameterType();
        Type genericParameterType = methodParameter.getGenericParameterType();

        methodParameter.increaseNestingLevel(); //or nested()
        methodParameter.setTypeIndexForCurrentLevel(1); //0-第一个;1-第二个

        Class<?> nestedParameterType = methodParameter.getNestedParameterType();
        Type nestedGenericParameterType = methodParameter.getNestedGenericParameterType();


        System.out.println("Hello");
    }

    public TempForMethodParameterTest test(Map<String, TempForMethodParameterTest> paramMap) {
        return null;
    }

    private static class TempForMethodParameterTest {

    }


}
