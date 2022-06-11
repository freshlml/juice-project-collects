package com.juice.spring.core.io;

import java.net.URL;
import java.util.Enumeration;

public class PathsLoaderTest {

    public static void main(String argv[]) throws Exception {

        loader();
        test();

    }

    public static void loader() {
        /**
         * 使用Classloader加载资源
         */
        //bootstrap-classloader从sun.boot.class.path指定的jar包中加载资源, note: bootstrap-classloader不支持加载目录资源
        //1. JDK安装目录\jre\lib\resources.jar、rt.jar、sunrsasign.jar、jsse.jar、jce.jar、charsets.jar、jfr.jar
        //2. JDK安装目录\jre\classes
        System.out.println("sun.boot.class.path: " + System.getProperty("sun.boot.class.path"));
        //Ext-classloader从java.ext.dirs指定的目录中加载资源，note: ext-classloader指定的是目录，支持加载目录中的jar包中的资源
        //1. JDK安装目录\jre\lib\ext
        //2. ~\Sun\Java\lib\ext，eg:windows上C:\WINDOWS\Sun\Java\lib\ext
        System.out.println("java.ext.dirs: " + System.getProperty("java.ext.dirs"));
        //1. App-Classloader从java.class.path指定的jar包或者目录中加载资源
        //  java.class.path的值:
        //     如果通过C:\Users\DELL\Desktop>java -jar project-spring.jar
        //              则java.class.path = project-spring.jar
        //     如果是idea中运行,idea会把当前应用打包输出的路径写入java.class.path,如D:\ideaProject3\juice-project-collects\project-spring\target\classes
        //              其行为和java -jar保持一致
        //2. MANIFEST.MF的Class-Path指定的jar包
        //idea中运行额外增加: JDK安装目录\jre\lib\charsets.jar、deploy.jar、javaws.jar、jce.jar、jfr.jar、jfxswt.jar、jsse.jar、management-agent.jar、plugin.jar、resources.jar、rt.jar
        //idea中运行额外增加: JDK安装目录\jre\lib\ext\access-bridge-64.jar、cldrdata.jar、dnsns.jar、jaccess.jar、jfxrt.jar、localedata.jar、nashorn.jar、sunec.jar、sunjce_provider.jar、sunmscapi.jar、sunpkcs11.jar、zipfs.jar
        //idea中运行，会将当前应用的pom.xml中依赖的jar包在本地仓库中的路径写入java.class.path
        //java -jar运行，应用的pom.xml中依赖的jar包在本地仓库的路径不会写入了java.class.path (@link 项目打包方式)
        System.out.println("java.class.path: " + System.getProperty("java.class.path"));
        System.out.println("---------------------");

        /**项目打包方式 */
        //项目打成jar包，依赖jar包的内容合并到项目jar包


        //项目打成jar
        //依赖的jar包放到某个目录(可以是项目jar包内部目录或者磁盘的任意目录),需要在项目jar包中的MANIFEST.MF文件中通过Class-Path: ./lib/commons-loggings.jar 指定依赖的jar包位置
        //App-Classloader能够从Class-Path加载资源


        //maven项目打包(通过maven-jar-plugin插件打包)
        //项目打成jar包，依赖的jar存在于本地仓库，项目jar包未关联上依赖的jar包
        //  如果是在idea中运行，依赖jar包的本地仓库路径写入java.class.path
        //  如果通过java -jar运行，依赖jar包的本地仓库路径不会写入java.class.path


        //Spring Boot中打包(通过spring-boot-maven-plugin插件打包)
        //项目打成jar包，顶层package(如com)不在位于jar包的顶层，而是放到了BOOT-INF/classes目录下，MANIFEST.MF中指定Spring-Boot-Classes: BOOT-INF/classes/
        //依赖的jar包放到了BOOT-INF/lib目录下，MANIFEST.MF中指定Spring-Boot-Lib: BOOT-INF/lib/
        //自定义ClassLoader，继承App-Classloader

        //因此如果一个非Spring Boot项目依赖Spring Boot项目
        //无法加载Spring Boot项目中的类
        //加载资源要加上BOOT-INF/classes/前缀,eg: BOOT-INF/classes/com/...
        //但是import就不行了，因为import 以package开始
    }


    public static void test() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL res = classLoader.getResource("com/juice/spring/core/io/article.xml");
        System.out.println(res); // file:/D:/ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/core/io/article.xml
        System.out.println("-----------1----------");

        URL sr = classLoader.getResource("java/lang/String.class");
        //classLoader.getResource("java/lang"); //null
        System.out.println(sr);  // jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class
        System.out.println("-----------2----------");

        URL rtRe = classLoader.getResource("rt.jar");
        //sun.boot.class.path写入的路径为: jdk安装目录\jre\lib\rt.jar，可加载的是rt.jar包中的资源
        System.out.println(rtRe);  // null
        System.out.println("-----------3----------");

        //指定目录中的jar包中的资源, 只有ext-classloader支持
        URL rtInRe = classLoader.getResource("a");
        System.out.println(rtInRe);  // jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/ext/a.jar!/a
        System.out.println("-----------4----------");


        //不支持通配符
        Enumeration<URL> ress = classLoader.getResources("com/juice/spring/core/io/**");
        while(ress.hasMoreElements()) {
            System.out.println(ress.nextElement());
        }

        //getResources获取所有匹配的资源
        Enumeration<URL> jarInsRes = classLoader.getResources("com/fresh");
        while(jarInsRes.hasMoreElements()) {
            System.out.println(jarInsRes.nextElement());
        }

    }
}
