package com.juice.spring.core.convert;

import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class FlTypeTest {

    public static void main(String argv[]) {

        List<ConvertTest> cl = new ArrayList<>();
        ResolvableType tp = ResolvableType.forClass(cl.getClass());
        ResolvableType cll = tp.asCollection().getGeneric(0);
        System.out.println(cll.resolve());

        ParameterizedType p = (ParameterizedType)cl.getClass().getGenericSuperclass();
        System.out.println(p.getActualTypeArguments()[0]);


    }

}
