package com.project.normal.test.spring.core.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Enumeration;

public class LoaderTest {

    public static void main(String argv[]) throws Exception {
        /**
         * 使用Classloader加载资源
         */
        //bootstrap-classloader从sun.boot.class.path路径中加载资源
        //JDK安装目录\jre\lib\resources.jar、rt.jar、sunrsasign.jar、jsse.jar、jce.jar、charsets.jar、jfr.jar
        //JDK安装目录\jre\classes
        System.out.println("sun.boot.class.path: " + System.getProperty("sun.boot.class.path"));
        //Ext-classloader从java.ext.dirs路径中加载资源
        //JDK安装目录\jre\lib\ext
        //~\Sun\Java\lib\ext，eg:windows上C:\WINDOWS\Sun\Java\lib\ext
        System.out.println("java.ext.dirs: " + System.getProperty("java.ext.dirs"));
        //App-Classloader从java.class.path路径中，MANIFEST.MF的Class-Path 加载资源
        //idea中运行额外: JDK安装目录\jre\lib\charsets.jar、deploy.jar、javaws.jar、jce.jar、jfr.jar、jfxswt.jar、jsse.jar、management-agent.jar、plugin.jar、resources.jar、rt.jar
        //idea中运行额外: JDK安装目录\jre\lib\ext\access-bridge-64.jar、cldrdata.jar、dnsns.jar、jaccess.jar、jfxrt.jar、localedata.jar、nashorn.jar、sunec.jar、sunjce_provider.jar、sunmscapi.jar、sunpkcs11.jar、zipfs.jar
        //当前工作空间所在目录: System.getProperty("user.dir")
        //    idea中运行为当前应用打包输出的路径,如D:\ideaProject\NormalProject\target\classes
        //    如果通过C:\Users\DELL\Desktop>java -jar NormalProject.jar 运行，user.dir = C:\Users\DELL\Desktop
        //idea中运行，则会将依赖jar包的本地仓库路径写入java.class.path
        //java -jar运行，依赖jar包的本地仓库路径不会写入了java.class.path
        System.out.println("java.class.path: " + System.getProperty("java.class.path"));

        System.out.println("---------------------");

        //classpath:com/freshjuice/fl/base/mapper/Temp.Mapper.xml
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> rs = classLoader.getResources("com");
        while(rs.hasMoreElements()) {
            System.out.println(rs.nextElement());
        }
        ObjectMapper om = new ObjectMapper();
        System.out.println("依赖的jar: " + om);


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
        //项目打成jar包，项目clazz文件本并不位于jar包的顶层目录而是在BOOT-INF/classes/目录，MANIFEST.MF中指定Spring-Boot-Classes: BOOT-INF/classes/
        //依赖的jar包放到项目jar包里面的BOOT-INF\lib目录下面，MANIFEST.MF中指定Spring-Boot-Lib: BOOT-INF/lib/
        //自定义ClassLoader，继承App-Classloader

        //因此如果一个非Spring Boot项目依赖Spring Boot项目
        //无法加载Spring Boot项目中的类
        //加载资源要加上BOOT-INF/classes/前缀,eg: BOOT-INF/classes/com/freshjuice/fl/base/mapper/Temp.Mapper.xml
    }

}
