package com.project.normal.test.spring.beans;

import org.springframework.beans.factory.FactoryBean;

public class TestFacBean implements FactoryBean<TestFacBean.Obj> {

    private final Obj obj;
    public TestFacBean() {
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
