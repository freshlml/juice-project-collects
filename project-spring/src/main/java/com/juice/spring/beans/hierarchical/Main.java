package com.juice.spring.beans.hierarchical;


public class Main {

    public static void main(String argv[]) {

        BeanFactoryImpl flBeanFactory = new BeanFactoryImpl();
        flBeanFactory.registerAlias("", "");
        flBeanFactory.resolveAlias("");
        flBeanFactory.getSingletonNames();

    }

}
