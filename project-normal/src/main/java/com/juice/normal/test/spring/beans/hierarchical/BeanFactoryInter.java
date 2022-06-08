package com.juice.normal.test.spring.beans.hierarchical;

public interface BeanFactoryInter extends SingletonBeanRegistryInter {

    void registerAlias(String name, String alias);
    void resolveAlias(String name);


}
