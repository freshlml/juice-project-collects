package com.juice.spring.core.env;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyResolverTest {

    /**
     * placeholder解析
     * @see com.fresh.core.placeholder.SimplePlaceHolderResolver
     */

    public static void main(String argv[]) {
        propertySourceResolver();

    }

    public static void propertySourceResolver() {
        MutablePropertySources propertySources = new MutablePropertySources();

        Map<String, Object> map = new HashMap<>();
        map.put("user.name", "lml");
        map.put("user.password", "123456");
        map.put("user.url", "java.lang.${user.name}");
        map.put("user.foots", "1,2,3,4,qwer");
        MapPropertySource mapPropertySource = new MapPropertySource("test-using", map);
        propertySources.addFirst(mapPropertySource);

        ResourcePropertySource resourcePropertySource = null;
        try {
            resourcePropertySource = new ResourcePropertySource("classpath:db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        propertySources.addFirst(resourcePropertySource);  //更高优先级

        PropertySourcesPropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        System.out.println(propertyResolver.getProperty("user.name", String.class));
        System.out.println(propertyResolver.getProperty("user.password"));
        System.out.println(propertyResolver.getProperty("user.url"));  //placeholder解析
        System.out.println(propertyResolver.getProperty("user.foots", List.class)); //类型转换

    }



}
