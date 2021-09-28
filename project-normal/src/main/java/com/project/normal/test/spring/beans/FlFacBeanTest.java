package com.project.normal.test.spring.beans;

import org.springframework.beans.factory.FactoryBean;

public class FlFacBeanTest implements FactoryBean<FlFacBeanTest.Obj> {

    private final Obj obj;
    public FlFacBeanTest() {
        obj = new Obj();
    }
    @Override
    public Obj getObject() throws Exception {
        return obj;
    }

    @Override
    public Class<?> getObjectType() {
        return Obj.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public static class Obj {
        private String name;
    }
}
