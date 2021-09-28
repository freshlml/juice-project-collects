package com.project.normal.test.spring.bean.factory;

public class FlSimpleAliasRegistry implements FlAliasRegistry {
    @Override
    public void registerAlias(String name, String alias) {
        System.out.println("registerAlias");
    }

    public void resolveAlias(String name) {
        System.out.println("resolveAlias");
    }
}
