package com.project.normal.test.spring.context;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
public class TestContextBean {

    @Autowired
    private TestContextBean2 testContextBean2;

}
