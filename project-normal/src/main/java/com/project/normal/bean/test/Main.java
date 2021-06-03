package com.project.normal.bean.test;

import com.project.normal.bean.factory.FlBeanFactory;

public class Main {

    public static void main(String argv[]) {

        FlBeanFactory flBeanFactory = new FlBeanFactory();
        flBeanFactory.registerAlias("", "");
        flBeanFactory.resolveAlias("");


    }

}
