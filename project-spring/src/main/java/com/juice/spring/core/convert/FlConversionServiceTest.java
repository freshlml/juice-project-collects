package com.juice.spring.core.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.ArrayList;
import java.util.List;

public class FlConversionServiceTest {

    public static void main(String argv[]) {

        ConversionService con = DefaultConversionService.getSharedInstance();
        String source = "123,abc, jkl";
        List<ConvertTest> cl = new ArrayList<>();
        TypeDescriptor targetType = TypeDescriptor.valueOf(cl.getClass());
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        Object result = con.convert(source, sourceType, targetType);
        System.out.println(result);





    }


}
