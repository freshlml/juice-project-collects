package com.project.normal.bean.factory;

public class FlBeanFactory extends FlDefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public void registerAlias(String name, String alias) {
        System.out.println("不同来源的registerAlias");
    }

}
