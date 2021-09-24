package com.project.normal.spring.beans;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;


public class FlBeansTest {

    public static void main(String argv[]) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);

        beanDefinitionReader.register(FlFacBeanTest.class);
        //String[] ns = beanFactory.getBeanNamesForType(FlFacBeanTest.class);

        //Object facBeanOf = beanFactory.getBean("flFacBeanTest");
        //facBeanOf = beanFactory.getBean("&flFacBeanTest");

        //FlFacBeanTest oneOrTwo = beanFactory.getBean(FlFacBeanTest.class);
        //FlFacBeanTest.Obj oneOrTwo2 = beanFactory.getBean(FlFacBeanTest.Obj.class);

        //System.out.println(beanFactory.containsBean("flFacBeanTest"));
        //System.out.println(beanFactory.containsBean("&flFacBeanTest"));
        //System.out.println(beanFactory.getType("flFacBeanTest"));
        //System.out.println(beanFactory.getType("&flFacBeanTest"));

        //System.out.println(beanFactory.isTypeMatch("flFacBeanTest", FlFacBeanTest.class));
        //System.out.println(beanFactory.isTypeMatch("&flFacBeanTest", FlFacBeanTest.class));

        System.out.println(beanFactory.isFactoryBean("flFacBeanTest"));
        System.out.println(beanFactory.isFactoryBean("&flFacBeanTest"));


        System.out.println("hello");
    }


}
