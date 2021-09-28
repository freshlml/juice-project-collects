package com.project.normal.test.spring.bean.factory;

public interface BeanFactory extends FlSingletonBeanRegistry{

    void registerAlias(String name, String alias);
    void resolveAlias(String name);


}
