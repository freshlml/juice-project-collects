package com.project.normal.test.spring.beans.hierarchical;

public class BeanFactoryImpl extends SingletonBeanRegistryImpl implements BeanFactoryInter {

    @Override
    public void registerAlias(String name, String alias) {
        System.out.println("不同来源的registerAlias");
    }

}
