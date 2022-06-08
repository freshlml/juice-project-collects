package com.juice.normal.test.spring.context.autowireAnnoTest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = "theadPool", value = "com/project/normal/test/spring/context/autowireAnnoTest/theadPool.properties")
public class Config {


    @Bean
    public TestBeanForBasic testBeanForBasic() {
        return new TestBeanForBasic();
    }
    @Bean
    public TestBeanForType testBeanForType() {
        return new TestBeanForType();
    }

    @Bean
    public TestBean2 testBean2() {
        return new TestBean2();
    }
    @Bean
    public TestBean3 testBean3() {
        return new TestBean3();
    }

    @Bean
    public TestBean4 testBean4() {
        return new TestBean4();
    }

    @Bean
    public TestBean4 testBean4ForOther() {
        return new TestBean4();
    }


    @Bean
    public TestBean5 testBean5One() {
        return new TestBean5();
    }

    @Bean
    public TestBean5 testBean5Two() {
        return new TestBean5();
    }

    @Bean
    public TestBeanGeneric1<String> testBeanGeneric1ForString() {
        return new TestBeanGeneric1<>();
    }

    @Bean
    public TestBeanGeneric1<Integer> testBeanGeneric1ForInteger() {
        return new TestBeanGeneric1<>();
    }

    @Bean
    public TestBeanGeneric1<String> testBeanGenericString() {
        return new TestBeanGeneric1<>();
    }

    @Qualifier("bean6One")
    @Bean
    public TestBean6 testBean6_1() {
        return new TestBean6();
    }

    @Qualifier("bean6Two")
    @Bean
    public TestBean6 testBean6_2() {
        return new TestBean6();
    }


}
