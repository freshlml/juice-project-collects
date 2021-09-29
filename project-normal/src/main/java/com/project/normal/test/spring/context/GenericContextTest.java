package com.project.normal.test.spring.context;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestGenericContext {

    public static void main(String argv[]) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register();
        context.scan();

        context.refresh();




    }

}
