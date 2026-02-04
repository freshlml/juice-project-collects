package com.juice.spring.core.io;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;

import java.net.URL;

public class ResourceTest {

    public static void main(String argv[]) throws Exception {

        //通过ClassLoader加载资源，classpath路径具有通用性
        ClassPathResource classPathResource = new ClassPathResource("com/juice/spring/io/article.xml");

        if(classPathResource.isFile()) {
            FileSystemResource fileSystemResource = new FileSystemResource(classPathResource.getFile());
            System.out.println(fileSystemResource);
        }

        URL fileUrl = classPathResource.getURL();
        System.out.println(fileUrl);


        UrlResource urlResource = new UrlResource(fileUrl);
        System.out.println(urlResource);

        FileUrlResource fileUrlResource = new FileUrlResource(fileUrl);
        System.out.println(fileUrlResource);

    }



}
