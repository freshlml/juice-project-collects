package com.project.normal.spring.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Enumeration;

public class LoadResourceTest {

    public static void main(String argv[]) throws Exception {
        /**
         * 使用Classloader加载资源
         */
        //bootstrap-classloader从sun.boot.class.path路径中加载资源
        //~\jre\lib\*.jar
        //~\jre\classes
        System.out.println("sun.boot.class.path: " + System.getProperty("sun.boot.class.path"));
        //Ext-classloader从java.ext.dirs路径中加载资源
        //~\jre\lib\ext
        //~\Sun\Java\lib\ext
        System.out.println("java.ext.dirs: " + System.getProperty("java.ext.dirs"));
        //App-Classloader从java.class.path路径中，MANIFEST.MF的Class-Path 加载资源
        //D:\ideaProject\NormalProject\target\classes;  当前application的目录
        // 如果通过java -jar ./Desktop/NormalProject-1.0-SNAPSHOT.jar 运行，
        // 则为 ./Desktop/NormalProject-1.0-SNAPSHOT.jar，总而言之，当前application中的类要设置进去
        //E:\apache-maven-3.6.1\localRepository\junit\junit\4.12\junit-4.12.jar; 依赖jar包的本地仓库路径
        // 如果通过java -jar ./Desktop/NormalProject-1.0-SNAPSHOT.jar 运行
        // 没有此值,从而NoClassDefFound，no resource found
        //C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.6\lib\idea_rt.jar      idea中运行时写入的路径
        System.out.println("java.class.path: " + System.getProperty("java.class.path"));


        //classpath:com/freshjuice/fl/base/mapper/Temp.Mapper.xml
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> rs = classLoader.getResources("com");
        while(rs.hasMoreElements()) {
            System.out.println(rs.nextElement());
        }
        ObjectMapper om = new ObjectMapper();
        System.out.println("依赖的jar: " + om);

        //项目打成jar包时,依赖的jar包会同时打包进来
        //在MANIFEST.MF文件中通过 Class-Path: ./lib/commons-loggings.jar 指定依赖的jar包位置
        //1.从java.class.path(此时项目jar包的路径设置进去了)搜索资源;2.类加载器能够通过Class-Path定位到依赖jar包的位置


        //maven项目打包时，默认其依赖不会打包进来
        //1.从java.class.path(此时项目jar包的路径设置进去了)搜索资源;
        //                       2.依赖的jar包
        //                           如果是在工程中运行，则会将依赖jar包的本地仓库路径写入java.class.path，
        //                           从而AppClassloader能够加载依赖jar包的资源
        //                           而如果通过java -jar运行，依赖jar包的本地仓库路径"不会写入"java.class.path
        //                           从而NoClassDefFound，no resource found


        //所以，maven打包时，将依赖包打包并存放在一个目录,同时在项目jar包的MANIFEST.MF中通过Class-Path指定依赖的jar包目录
        //此时，java.class.path中设置了当前运行jar包的路径(在当前运行jar包中加载资源)
        //     MANIFEST.MF文件中Class-Path指定依赖包的目录(在依赖包中加载资源)


        //Spring Boot中maven打包
        //将依赖包放到项目jar包里面的BOOT-INF\lib目录下面
        //在MANIFEST.MF中指定Spring-Boot-Lib: BOOT-INF/lib/ (注意没有Class-Path)
        //指定项目代码的目录, Spring-Boot-Classes: BOOT-INF/classes/
        //他是自定义Classloader

        //因此如果一个非boot项目依赖boot项目
        //加载资源要加上BOOT-INF/...前缀: BOOT-INF/classes/com/freshjuice/fl/base/mapper/Temp.Mapper.xml
    }

}
