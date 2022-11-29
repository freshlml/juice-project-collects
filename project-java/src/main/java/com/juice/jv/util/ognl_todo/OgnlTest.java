package com.juice.jv.util.ognl_todo;

import ognl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OgnlTest {
    public static void main(String argv[]) throws Exception {
        //注册root对象为HashMap,指定属性访问器
        OgnlRuntime.setPropertyAccessor(HashMap.class, new PropertyAccessor() {
            @Override
            public Object getProperty(Map context, Object root, Object property) throws OgnlException {
                Map map = (Map) root;
                Object result = map.get(property);
                return result;
            }
            @Override
            public void setProperty(Map context, Object root, Object property, Object value) throws OgnlException {
                Map<Object, Object> map = (Map<Object, Object>) root;
                map.put(property, value);
            }
            @Override
            public String getSourceAccessor(OgnlContext ognlContext, Object o, Object o1) {
                return null;
            }
            @Override
            public String getSourceSetter(OgnlContext ognlContext, Object o, Object o1) {
                return null;
            }
        });

        Map<String, Object> contextMap = new HashMap<>();

        Map<String, Object> scriptMap = new HashMap<>();
        List<Long> scriptList = new ArrayList<>();
        scriptList.add(1L);
        scriptMap.put("list", scriptList);
        contextMap.put("_parameter1", scriptMap);

        T t = new T("123");
        contextMap.put("_parameter2", t);

        contextMap.put("_parameter3", "str");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "name");
        contextMap.put("_parameter4", paramMap);

        //创建OgnlContext,设置一个root对象
        Map context = Ognl.createDefaultContext(contextMap, new FlMemberAccess(), new FlClassResolver(), null);

        //创建表达式
        Object expressionResult = Ognl.parseExpression("_parameter1.list != null and _parameter1.list.size>0");
        System.out.println(expressionResult);

        //表达式在OgnlContext的root对象上解析
        Object result = Ognl.getValue(expressionResult, context, contextMap);
        System.out.println(result);
    }

    static class T{
        T(String str){this.str = str;}
        String str;
        public String getI(){return str;}
    }
}
