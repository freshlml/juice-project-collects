package com.project.normal.test.spring.bean.test;

import com.project.normal.test.spring.bean.factory.FlBeanFactory;

public class Main {

    public static void main(String argv[]) {

        FlBeanFactory flBeanFactory = new FlBeanFactory();
        flBeanFactory.registerAlias("", "");
        flBeanFactory.resolveAlias("");


    }

}
