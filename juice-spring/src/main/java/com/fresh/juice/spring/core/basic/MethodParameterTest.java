package com.fresh.juice.spring.core.basic;

import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MethodParameterTest {

    public static void main(String argv[]) throws Exception {
        MethodParameterTest testObj = new MethodParameterTest();
        Method[] mds = testObj.getClass().getDeclaredMethods();
        Method method = null;
        for(int i=0; i<mds.length; i++) {
            if(mds[i].getName() == "test") {
                method = mds[i];
                break;
            }
        }
        if(method != null) {

            MethodParameter methodParameter = new MethodParameter(method, 9, 1);

            //参数类型: Class<?>
            Class<?> parameterType = methodParameter.getParameterType();
            System.out.println(parameterType);
            //参数类型: Type
            Type genericParameterType = methodParameter.getGenericParameterType();
            System.out.println(genericParameterType);

            //note: 当前嵌套等级依赖于上一层
            //每一层的nestingLevel都需要维护一个index对，若只有一个值可以没有index对
            //  但如果有多个值而没有index对，将默认取最后一个值
            methodParameter.increaseNestingLevel();

            //如果嵌套等级>参数的嵌套，表示是参数的最后一层嵌套
            //如果是参数是GenericArrayType，TypeVariable并且设置了嵌套等级>=1
            //  getNestedParameterType调用将可能得到错误的结果，可使用getParameterType or getGenericParameterType 替代
            //如果参数的嵌套中存在GenericArrayType，TypeVariable
            //  getNestedParameterType调用将可能得到错误的结果，可使用getNestedGenericParameterType 替代
            Class<?> nestedParameterType = methodParameter.getNestedParameterType();
            System.out.println(nestedParameterType);

            Type nestedGenericParameterType = methodParameter.getNestedGenericParameterType();
            System.out.println(nestedGenericParameterType);

            System.out.println("Hello");
        }

    }

    <T> void test (T t,
                   Temp temp,
                   int i,
                   T[] ts,
                   List<T>[] list,
                   List<Integer>[] listInt,
                   List<Bean<T>>[] listBean,
                   Map<List<Integer>, Map<Long, Temp>> map,
                   List<T> listT,
                   List<T[]> listTArray
    ) {
    }


    private static class Temp {}
    private static class Bean<T> {}


}
