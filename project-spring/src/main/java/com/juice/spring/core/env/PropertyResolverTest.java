package com.juice.spring.core.env;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyResolverTest {

    /**
     * placeholder解析
     * @see com.fresh.common.placeholder.SimplePlaceHolderResolver
     */

    public static void main(String argv[]) {
        propertySourceResolver();

    }

    public static void propertySourceResolver() {
        Map<String, Object> map = new HashMap<>();
        map.put("user.name", "root");
        map.put("user.password", "123456");
        map.put("user.url", "java.lang.${user.name}");
        map.put("user.foots", "1,2,3,4,5");
        MapPropertySource mapPropertySource = new MapPropertySource("test-using", map);

        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(mapPropertySource);

        PropertySourcesPropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        System.out.println(propertyResolver.getProperty("user.name", String.class));
        System.out.println(propertyResolver.getProperty("user.password"));
        System.out.println(propertyResolver.getProperty("user.url"));  //placeholder解析
        System.out.println(propertyResolver.getProperty("user.foots", List.class)); //类型转换

    }



}
