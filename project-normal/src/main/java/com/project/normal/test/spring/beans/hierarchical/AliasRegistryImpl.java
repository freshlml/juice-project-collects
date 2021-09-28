package com.project.normal.test.spring.beans.hierarchical;

public class AliasRegistryImpl implements AliasRegistryInter {
    @Override
    public void registerAlias(String name, String alias) {
        System.out.println("registerAlias");
    }

    public void resolveAlias(String name) {
        System.out.println("resolveAlias");
    }
}
