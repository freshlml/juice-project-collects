package com.project.normal.test.spring.beans.hierarchical;

public interface SingletonBeanRegistryInter {
    void registerSingleton(String name, Object bean);
    Object getSingleton(String name);
    boolean containsSingleton(String name);
    String[] getSingletonNames();
}
