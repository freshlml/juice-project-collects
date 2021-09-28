package com.project.normal.test.spring.beans;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;

import java.util.Arrays;


public class FlBeansTest {

    public static void main(String argv[]) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);

        beanDefinitionReader.register(FlFacBeanTest.class);
        beanDefinitionReader.register(FlBeanTest.class);

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

        //System.out.println(beanFactory.isFactoryBean("flFacBeanTest"));
        //System.out.println(beanFactory.isFactoryBean("&flFacBeanTest"));

        //beanFactory.registerAlias("flBeanTest", "flBeanTest_al");
        //Arrays.stream(beanFactory.getAliases("flBeanTest_al")).forEach(System.out::print);

        beanFactory.registerAlias("flFacBeanTest", "flFacBeanTest_al");
        Arrays.stream(beanFactory.getAliases("flFacBeanTest")).forEach(System.out::println);
        Arrays.stream(beanFactory.getAliases("flFacBeanTest_al")).forEach(System.out::println);
        Arrays.stream(beanFactory.getAliases("&flFacBeanTest")).forEach(System.out::println);
        Arrays.stream(beanFactory.getAliases("&flFacBeanTest_al")).forEach(System.out::println);

        if(false || true && true) {
            System.out.println(1);
        }


        System.out.println("hello");
    }


}
