package com.project.normal.test.spring.context;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;

public class ContextTypeMatchTest {

    public static void main(String argv[]) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        //context.register(TestBeanConfig.class);
        context.scan("com.project.normal.test.spring.context");

        context.refresh();

        BeanDefinition beanDefinition = context.getBeanDefinition("testContextGenericBean1");
        //ResolvableType factoryMethodReturnType域保存了@Bean方法的返回值类型
        BeanDefinition mergedBeanDefinition = context.getBeanFactory().getMergedBeanDefinition("testContextGenericBean1");

        ResolvableType noGeneric = ResolvableType.forClass(ContextTypeMatchGenericBean.class);
        ResolvableType superWithStrGenerics = ResolvableType.forClassWithGenerics(ContextTypeMatchGenericBean.class, String.class);
        ResolvableType superWithIntGenerics = ResolvableType.forClassWithGenerics(ContextTypeMatchGenericBean.class, Integer.class);

        System.out.println(context.isTypeMatch("testContextGenericBean1", noGeneric));            //true
        System.out.println(context.isTypeMatch("testContextGenericBean1", superWithStrGenerics)); //true
        System.out.println(context.isTypeMatch("testContextGenericBean1", superWithIntGenerics)); //false

        System.out.println("hello");
    }

}
