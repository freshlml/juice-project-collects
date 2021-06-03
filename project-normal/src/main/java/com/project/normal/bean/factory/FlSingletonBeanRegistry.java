package com.project.normal.bean.factory;

public interface FlSingletonBeanRegistry {
    void registerSingleton(String name, Object bean);
    Object getSingleton(String name);
    boolean containsSingleton(String name);
    String[] getSingletonNames();
}
