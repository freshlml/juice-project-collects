package com.project.normal.test.spring.env;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.util.HashMap;
import java.util.Map;

public class PropertySourceResolver {

    public static void main(String argv[]) {
        MutablePropertySources propertySources = new MutablePropertySources();
        Map<String, Object> map = new HashMap<>();
        map.put("user.name", "root");
        map.put("user.password", "123456");
        map.put("user.url", "java.lang.${user.name}");
        MapPropertySource mapPropertySource = new MapPropertySource("test-using", map);
        propertySources.addFirst(mapPropertySource);
        PropertySourcesPropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        System.out.println(propertyResolver.getProperty("user.name"));
        System.out.println(propertyResolver.getProperty("user.password"));
        System.out.println(propertyResolver.getProperty("user.url"));
    }

}
