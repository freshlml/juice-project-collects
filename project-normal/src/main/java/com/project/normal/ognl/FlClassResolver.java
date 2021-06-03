package com.project.normal.ognl;

import ognl.DefaultClassResolver;
import org.apache.ibatis.io.Resources;

public class FlClassResolver extends DefaultClassResolver {
    @Override
    protected Class toClassForName(String className) throws ClassNotFoundException {
        return Resources.classForName(className);
    }
}
