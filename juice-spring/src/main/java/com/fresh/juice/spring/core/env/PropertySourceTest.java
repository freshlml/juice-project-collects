package com.fresh.juice.spring.core.env;

import org.springframework.core.env.EnumerablePropertySource;

public class PropertySourceTest {

    public static void main(String argv[]) {
        printEnv();
        printJvmProperty();

        propertySourceTest();
    }

    public static void printEnv() {
        System.getenv().forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
        System.out.println("------系统环境变量------");
    }

    public static void printJvmProperty() {
        System.getProperties().forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
        System.out.println("------JVM属性------");
    }

    public static void propertySourceTest() {

        PropertyHandler propertyHandler = new ForTestPropertyHandler();
        CommonPropertySource commonPropertySource = new CommonPropertySource("forTest", propertyHandler);

        System.out.println(commonPropertySource.getProperty("..."));

    }

    static class CommonPropertySource extends EnumerablePropertySource<PropertyHandler> {

        public CommonPropertySource(String name, PropertyHandler source) {
            super(name, source);
        }

        @Override
        public String[] getPropertyNames() {
            return source.getPropertyNames();
        }

        @Override
        public Object getProperty(String name) {
            return source.getProperty(name);
        }

        @Override
        public boolean containsProperty(String name) {
            return source.containsProperty(name);
        }
    }

    interface PropertyHandler {
        Object getProperty(String name);
        String[] getPropertyNames();
        boolean containsProperty(String name);
    }
    static class ForTestPropertyHandler implements PropertyHandler {

        @Override
        public Object getProperty(String name) {
            return "1";
        }

        @Override
        public String[] getPropertyNames() {
            return new String[]{"1"};
        }

        @Override
        public boolean containsProperty(String name) {
            return true;
        }
    }

}
