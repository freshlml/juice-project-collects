package com.project.normal.test.spring.context;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GenericContextTest {

    public static void main(String argv[]) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        //context.register(TestBeanConfig.class);
        context.scan("com.project.normal.test.spring.context");

        context.refresh();

        Object testBeanConfig = context.getBean("testBeanConfig");
        Object testContextBean = context.getBean("testContextBean");
        Object testContextBean2 = context.getBean("testContextBean2");


        System.out.println(testBeanConfig);
        System.out.println(testContextBean);
        System.out.println(testContextBean2);

    }

}
