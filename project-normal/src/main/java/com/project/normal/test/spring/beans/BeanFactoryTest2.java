package com.project.normal.test.spring.beans;


import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.Bean;

public class BeanFactoryTest2 {


    public static void main(String argv[]) {
        //构造BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //使用BeanDefinitionReader加载BeanDefinition并注册到BeanFactory
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);
        beanDefinitionReader.register(Bean1.class);
        beanDefinitionReader.register(Bean2.class);
        //GetBeanDefinition
        BeanDefinition bd = beanFactory.getBeanDefinition("TBeanFactoryTest.Bean1");
        System.out.println(bd);
        ((AbstractBeanDefinition) bd).setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        //在BeanFactory中发现bean instance: getBean
        Bean1 tempInstance = beanFactory.getBean(Bean1.class); //初次获取singleton bean,会createBean,并且缓存
        System.out.println(tempInstance);
        Bean1 tempInstanceAgain = beanFactory.getBean(Bean1.class); //同一个singleton bean
        System.out.println(tempInstanceAgain);

    }


    static class Bean1 implements BeanNameAware, InitializingBean {
        private Bean2 beans;

        public void setBeans(Bean2 beans) {
            this.beans = beans;
        }
        private static String beanName;
        @Override
        public void setBeanName(String name) {
            this.beanName = name;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("#Bean1 initial##");
        }
    }
    static class Bean2 {

    }

    @Bean("bean1")
    public Bean1 bean1() {
        return new Bean1();
    }

}
