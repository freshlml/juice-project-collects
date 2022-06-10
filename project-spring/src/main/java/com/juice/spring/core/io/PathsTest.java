package com.juice.spring.core.io;

import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public class PathsTest {

    public static void main(String argv[]) throws Exception {
        path_test();
        System.out.println("-----------------------");

        url_path_test();
        System.out.println("-----------------------");

        test_clean_path();

    }

    public static void path_test() {
        System.out.println("user.dir: " + System.getProperty("user.dir"));

        /**
         * 相对路径
         * 相对于当前工作空间所在目录: System.getProperty("user.dir")
         * user.dir的值:
         *      C:\Users\DELL\Desktop>java -jar project-spring-1.0.0.jar
         *      user.dir: C:\Users\DELL\Desktop
         *
         *      idea中运行: user.dir = D:\ideaProject3  (当前project目录非module目录)
         */
        File f = new File("project-spring-1.0.0.jar");
        System.out.println(f.exists());
        System.out.println(f.getPath());
        Path path = f.toPath();
        System.out.println(path.getFileName());
        System.out.println(path.toString());
        System.out.println(path.isAbsolute());
        System.out.println(path.toAbsolutePath());
        System.out.println("-----------相对路径----------");

        /**
         * win机上绝对路径
         * File f = new File("D:/ideaProject3/juice-project-collects/pom.xml");
         * File ff = new File("/D:/ideaProject3/juice-project-collects/pom.xml");
         */
        /**
         * /: linux机上绝对路径, 根目录
         * /: win机上相对路径，相对于当前工作空间(user.dir)所在盘符
         */
        File af = new File("/Users/DELL/Desktop/project-spring-1.0.0.jar");
        System.out.println(af.exists());
        System.out.println("-----------绝对路径----------");
        //直接根据路径通常不太妙

        /**
         * 使用类加载器加载资源
         * @see PathsLoaderTest
         */
    }

    public static void url_path_test() throws Exception {
        /**
         *URL路径:
         *protocol://host:port/path?query
         * 如: http://localhost:7001/user?id=1&name=q
         *
         *文件URL:
         *  file:/path?query   host,port为空, path是文件的绝对路径
         *windows
         * file:/D:/ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
         * jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar
         * jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class
         *
         *linux
         * file://ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
         *
         */
        /**
         *URI路径
         * 一般与URL路径没区别
         */
        URL url = new URL("http://localhost:7001/user?id=1&name=q");
        System.out.println(url);   // http://localhost:7001/user?id=1&name=q
        System.out.println(url.toURI());  // http://localhost:7001/user?id=1&name=q

        url = new URL("http", "localhost", 7001, "user?id=1&name=q");
        System.out.println(url);  // http://localhost:7001user?id=1&name=q
        System.out.println(url.toURI()); // http://localhost:7001user?id=1&name=q

        URI uri = URI.create("http://localhost:7001/user?id=1&name=q");
        System.out.println(uri);         //  http://localhost:7001/user?id=1&name=q
        System.out.println(uri.toURL()); //  http://localhost:7001/user?id=1&name=q
        System.out.println(uri.getHost()); //   localhost
        //note: getPath调用有前置/
        System.out.println(uri.getPath()); //   /user
        System.out.println(uri.getQuery()); //  id=1&name=q
        System.out.println(uri.getSchemeSpecificPart());  // //localhost:7001/user?id=1&name=q
        System.out.println("--------------1-----------");

        //使用绝对路径构造，文件URL
        url = new URL("file:/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java");
        System.out.println(url);   //  file:/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java
        System.out.println(url.toURI()); //  file:/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java
        System.out.println(url.getHost()); // 空
        //note: getPath调用有前置/
        System.out.println(url.getPath()); //  /D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java
        System.out.println("--------------2-----------");

        //通过classloader加载资源，返回URL(文件URL)
        URL uu = PathsTest.class.getClassLoader().getResource("com/juice/spring/App.class");
        System.out.println(uu); //  file:/D:/ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
        System.out.println(uu.toURI());  //  file:/D:/ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
        System.out.println(uu.getProtocol()); // file
        System.out.println(uu.getHost());     // 空
        //note: getPath调用有前置/
        System.out.println(uu.getPath()); //  /D:/ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
        System.out.println(uu.toURI().getSchemeSpecificPart()); // /D:/ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
        System.out.println("--------------3-----------");

        //构造File,从File拿到绝对路径，构造URL
        //构造File,Path.toURI调用
        File f = new File("juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java");
        System.out.println(f.getPath()); // juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java
        Path path = f.toPath();
        System.out.println(path.getFileName()); // App.java
        System.out.println(path.isAbsolute());  // false
        System.out.println(path.toAbsolutePath());  //D:\ideaProject3\juice-project-collects\project-spring\src\main\java\com\juice\spring\App.java

        //note: 从Path得到的URI路径多了host前面的//
        URI pu = path.toUri();
        System.out.println(pu);  //  file:///D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java
        System.out.println(pu.getHost());  // null
        System.out.println(pu.getPath());  // /D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java

    }


    public static void test_clean_path() {

        System.out.println(StringUtils.cleanPath("file:///D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java"));
        System.out.println(StringUtils.cleanPath("jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar"));
        System.out.println(StringUtils.cleanPath("jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class"));
        System.out.println(StringUtils.cleanPath("file:/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java"));

        System.out.println(StringUtils.cleanPath("/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java"));
        System.out.println(StringUtils.cleanPath("D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java"));
        System.out.println(StringUtils.cleanPath("juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java"));

        System.out.println(StringUtils.cleanPath("./juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java"));
        System.out.println(StringUtils.cleanPath("../juice-project-collects/../project-spring/src/main/java/com/juice/spring/App.java"));

    }


}
