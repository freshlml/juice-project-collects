package com.project.normal.test.spring.context.autowireAnnoTest;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Set;
import java.util.stream.Stream;

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
