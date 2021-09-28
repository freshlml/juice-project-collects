package com.project.normal.test.spring.core;

import org.springframework.core.annotation.SynthesizingMethodParameter;
import java.lang.reflect.Method;

public class SynthesizingMethodParameterTest {

    public static void main(String argv[]) throws Exception {

        SynthesizingMethodParameterTest testObj = new SynthesizingMethodParameterTest();
        Method method = testObj.getClass().getMethod("test", new Class<?>[]{});

        SynthesizeTest an = method.getAnnotation(SynthesizeTest.class);
        
        SynthesizingMethodParameter methodParameter = SynthesizingMethodParameter.forExecutable(method, -1);

        SynthesizeTest anno = methodParameter.getMethodAnnotation(SynthesizeTest.class);

        String alis = anno.nameAli();

        System.out.println("hello");
    }


    @SynthesizeTest(valueAli = "123")
    public Object test() {return null;}


}
