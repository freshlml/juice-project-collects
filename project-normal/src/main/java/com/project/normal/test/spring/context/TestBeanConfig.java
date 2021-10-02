package com.project.normal.test.spring.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeanConfig {

    @Bean
    public TestContextBean testContextBean() {
        TestContextBean testContextBean = new TestContextBean();
        return testContextBean;
    }

    @Bean
    public TestContextBean2 testContextBean2() {
        return new TestContextBean2();
    }

}
