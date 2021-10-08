package com.project.normal.test.spring.core.io;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public class ResourceTest {

    public static void main(String argv[]) throws Exception {

        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource("jar:file:/E:/apache-maven-3.6.1/localRepository/com/freshjuice/fl/ArchIsomer/0.0.1-SNAPSHOT/ArchIsomer-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes/com/freshjuice/fl/base/mapper/Temp.Mapper.xml");
        System.out.println(resource);
        System.out.println(resource.exists());
        //对于jar文件内部的资源使用DefaultResourceLoader加载，返回UrlResource，直接读？？
        InputStream is = resource.getInputStream();
        byte[] bys = new byte[512];
        is.read(bys);
        System.out.println(new String(bys));


//        String location = "/temp/config/**/*.Mapper.xml";
//        int prefixEnd = location.indexOf(':') + 1;
//        int rootDirEnd = location.length();
//        while (rootDirEnd > prefixEnd && new AntPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd))) {
//            rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
//        }
//        if (rootDirEnd == 0) {
//            rootDirEnd = prefixEnd;
//        }
//        System.out.println(location.substring(0, rootDirEnd));


    }

}
