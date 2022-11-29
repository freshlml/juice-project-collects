package com.juice.jv.util.ognl_todo;

import ognl.DefaultClassResolver;

public class FlClassResolver extends DefaultClassResolver {
    @Override
    protected Class toClassForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
}
