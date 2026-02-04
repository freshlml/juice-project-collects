package com.fresh.juice.spring.context.autowireAnnoTest;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutowiredAnnotationTest {

    /**
     * 测试AutowiredAnnotationBeanPostProcessor
     */
    public static void main(String argv[]) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.project.normal.test.spring.context.autowireAnnoTest");
        context.refresh();

        TestBeanForBasic beanBasic = context.getBean(TestBeanForBasic.class);
        TestBeanForType beanType = context.getBean(TestBeanForType.class);




        System.out.println("hello");
    }

}
