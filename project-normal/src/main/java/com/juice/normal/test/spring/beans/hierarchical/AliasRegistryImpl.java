package com.juice.normal.test.spring.beans.hierarchical;

public class AliasRegistryImpl implements AliasRegistryInter {
    @Override
    public void registerAlias(String name, String alias) {
        System.out.println("registerAlias");
    }

    //此标记有两条线,下箭头和上箭头，表示，子类的接口的实现
    public void resolveAlias(String name) {
        System.out.println("resolveAlias");
    }
}
