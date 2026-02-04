package com.fresh.juice.spring.core.env;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.Arrays;

public class EnvironmentTest {

    public static void main(String argv[]) {
        profiles_test();

        property_test();


    }

    public static void profiles_test() {
        ConfigurableEnvironment environment = new StandardEnvironment();

        Arrays.stream(environment.getActiveProfiles()).forEach(p -> {
            System.out.print(p + ",");
        });
        System.out.println("\n---active profiles---");


        Arrays.stream(environment.getDefaultProfiles()).forEach(p -> {
            System.out.print(p + ",");
        });
        System.out.println("\n---default profiles---");

        //spring boot配置文件中指定spring.profiles.active=dev,test
        //environment.getActiveProfiles()将返回dev,test
        //亦可手动调用environment#setActiveProfiles/#addActiveProfile

    }

    public static void property_test() {

        ConfigurableEnvironment environment = new StandardEnvironment();

        MutablePropertySources currentProperties = environment.getPropertySources();
        //System.out.println(currentProperties);

        //命令行参数，@PropertySource注解
        //则要通过environment.getPropertySources()#add×××手动添加

    }


}
