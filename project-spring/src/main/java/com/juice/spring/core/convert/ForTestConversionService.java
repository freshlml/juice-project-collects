package com.juice.spring.core.convert;

import com.fresh.common.utils.AssertUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.ArrayList;
import java.util.List;

public class ForTestConversionService {

    public static void main(String argv[]) {

        String2Collection();


    }
    //todo 需要用到哪个或者自定义需先充分测试


    private static void String2Collection() {

        DefaultConversionService conversionService = new DefaultConversionService();

        //StringToCollectionConverter注册的ConvertiblePair(String.class, Collection.class)
        String source = "1,2,3";
        //注意点1.通常source不一定非常规范，则通常需要对source的修剪或者对处理不受期待的结果或者处理convert异常？
        source = "1,2,3,aa";  //aa -> 数字
        source = "1,2,3,";    //empty string
        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(List.class); //List without element type and List is interface
        targetType = TypeDescriptor.valueOf(ArrayList.class); //ArrayList without element type
        targetType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Integer.class)); //List with element type
        //注意点2.通常不应该让sourceType和targetType为空
        //assert sourceType != null && targetType != null
        //注意点3.如果source==null，通常也应该在外面处理
        if(source == null) {Object target = null; return;}
        if(conversionService.canConvert(sourceType, targetType)) {

            Object target = conversionService.convert(source, sourceType, targetType);

            System.out.println(target);
        }

    }


}
