package com.juice.spring.core.convert;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.ArrayList;
import java.util.List;

public class ForTestConversionService {

    //测试ConversionService
    public static void main(String argv[]) {

        String2Collection();


    }


    private static void String2Collection() {

        DefaultConversionService conversionService = new DefaultConversionService();

        //StringToCollectionConverter注册的ConvertiblePair(String.class, Collection.class)
        String source = "1,2,3";
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(List.class); //List without element type and List is interface
        targetType = TypeDescriptor.valueOf(ArrayList.class); //ArrayList without element type
        targetType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Integer.class)); //List with element type
        if(conversionService.canConvert(sourceType, targetType)) {

            Object target = conversionService.convert(source, sourceType, targetType);

            System.out.println(target);
        }


        //todo 需要用到哪个或者自定义需先充分测试


    }


}
