package com.juice.normal.test.spring.context.typeMatchTest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TypeMatchBeanConfig {

    //@Lazy
    @Bean
    public TypeMatchGenericBean<String> testContextGenericBean1() {
        return new TypeMatchGenericBean<>();
    }

    @Bean
    public TypeMatchGenericBean<Integer> testContextGenericBean2() {
        return new TypeMatchGenericBean<>();
    }

}
