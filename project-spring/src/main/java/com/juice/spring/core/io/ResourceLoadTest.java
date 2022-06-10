package com.juice.spring.core.io;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public class ResourceLoadTest {

    public static void main(String argv[]) throws Exception {

        defaultResourceLoaderTest();
        System.out.println("------------1----------");

        fileSystemResourceLoaderTest();

    }


    public static void defaultResourceLoaderTest() throws Exception {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();

        Resource oneRes = defaultResourceLoader.getResource("/com/juice/spring/core/io/article.xml");
        System.out.println(oneRes.getURL());

        Resource classPathRes = defaultResourceLoader.getResource("classpath:com/juice/spring/core/io/article.xml");
        System.out.println(classPathRes.getURL());

        Resource urlRes = defaultResourceLoader.getResource("file:/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/core/io/article.xml");
        System.out.println(urlRes.getURL());

        Resource resource = defaultResourceLoader.getResource("jar:file:/E:/apache-maven-3.6.1/localRepository/com/freshjuice/fl/ArchIsomer/0.0.1-SNAPSHOT/ArchIsomer-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes/com/freshjuice/fl/base/mapper/Temp.Mapper.xml");
        System.out.println(resource.getURL());

        InputStream is = urlRes.getInputStream();
        byte[] bys = new byte[512];
        is.read(bys);
        System.out.println(new String(bys));

        Resource defaultRes = defaultResourceLoader.getResource("com/juice/spring/core/io/article.xml");
        System.out.println(defaultRes.getURL());

    }

    public static void fileSystemResourceLoaderTest() throws Exception {
        FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();

        Resource oneRes = fileSystemResourceLoader.getResource("/juice-project-collects/project-spring/src/main/java/com/juice/spring/core/io/article.xml");

        Resource resource = fileSystemResourceLoader.getResource("/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/core/io/article.xml");

        InputStream is = resource.getInputStream();
        byte[] bys = new byte[512];
        is.read(bys);
        System.out.println(new String(bys));

    }

}
