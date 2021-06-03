package com.project.normal.bean.factory;

public class FlDefaultSingletonBeanRegistry extends FlSimpleAliasRegistry implements FlSingletonBeanRegistry {
    @Override
    public void registerSingleton(String name, Object bean) {
        System.out.println("registerSingleton");
    }

    @Override
    public Object getSingleton(String name) {
        System.out.println("getSingleton");
        return null;
    }

    @Override
    public boolean containsSingleton(String name) {
        System.out.println("containsSingleton");
        return false;
    }

    @Override
    public String[] getSingletonNames() {
        System.out.println("getSingletonNames");
        return new String[0];
    }
}
