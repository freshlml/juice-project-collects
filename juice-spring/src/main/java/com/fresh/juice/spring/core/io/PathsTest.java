package com.fresh.juice.spring.core.io;

import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
         *相对路径
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
        System.out.println(path.toUri());
        //相对路径的问题: 相对路径在不同环境下不通用，取决于当前运行的目录
        System.out.println("-----------相对路径----------");

        /**
         *win机上绝对路径
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
        //绝对路径的问题: 系统兼容性

        /**
         *classpath路径具有通用性。使用类加载器加载资源
         * @see PathsLoaderTest
         */
    }

    public static void url_path_test() throws Exception {
        /**
         *URL路径:
         *protocol://host:port/path?query
         * eg: http://localhost:7001/user?id=1&name=q
         *
         *文件URL:
         *  file:/path      host,port为空, path是文件的绝对路径
         *windows
         *  file:/D:/ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
         *linux
         *  file://ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
         *
         *jar:file格式:
         * jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class
         *
         *特殊:
         * 指向jar包/zip压缩档的文件URL
         *  file:/E:/apache-maven-3.6.1/localRepository/com/fresh/fresh-common/1.0.0/fresh-common-1.0.0.jar
         *  file:/E:/apache-maven-3.6.1/.../a.zip
         * 特殊的jar:file
         *  jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/
         *  jar:file:/E:/apache-maven-3.6.1/localRepository/com/fresh/fresh-common/1.0.0/fresh-common-1.0.0.jar!/com/fresh/common/a.jar
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

        //文件URL
        url = new URL("file:/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java");
        System.out.println(url);   //  file:/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java
        System.out.println(url.toURI()); //  file:/D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java
        System.out.println(url.getProtocol()); // file
        System.out.println(url.getHost()); // 空
        //note: getPath调用有前置/
        System.out.println(url.getPath()); //  /D:/ideaProject3/juice-project-collects/project-spring/src/main/java/com/juice/spring/App.java
        System.out.println("--------------2-----------");

        //通过classloader加载资源,返回URL
        URL uu = PathsTest.class.getClassLoader().getResource("com/juice/spring/App.class");
        System.out.println(uu); //  file:/D:/ideaProject3/juice-project-collects/project-spring/target/classes/com/juice/spring/App.class
        URL uu2 = PathsTest.class.getClassLoader().getResource("java/lang/String.class");
        System.out.println(uu2); // jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class
        System.out.println("--------------3-----------");

        //jar:file格式
        //PathsTest.class.getClassLoader().getResource("java/lang/String.class");
        url = new URL("jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class");
        System.out.println(url);   // jar:file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class
        System.out.println(url.getProtocol()); // jar
        System.out.println(url.getHost()); // 空
        System.out.println(url.getPath()); // file:/C:/Program%20Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class
        System.out.println(url.toURI().getScheme()); // jar
        System.out.println(url.toURI().getSchemeSpecificPart()); //  file:/C:/Program Files/Java/jdk1.8.0_202/jre/lib/rt.jar!/java/lang/String.class

        InputStream is = url.openStream();
        byte[] bys = new byte[512];
        is.read(bys);
        System.out.println(new String(bys));
        System.out.println("--------------4-----------");

        //特殊的jar:file
        url = new URL("jar:file:/E:/apache-maven-3.6.1/localRepository/com/fresh/fresh-common/1.0.0/fresh-common-1.0.0.jar!/");
        System.out.println(url);   // jar:file:/E:/apache-maven-3.6.1/localRepository/com/fresh/fresh-common/1.0.0/fresh-common-1.0.0.jar
        System.out.println(url.getProtocol()); // jar
        System.out.println(url.getHost()); //  空
        System.out.println(url.getPath()); //  file:/E:/apache-maven-3.6.1/localRepository/com/fresh/fresh-common/1.0.0/fresh-common-1.0.0.jar!/
        //InputStream iss = url.openStream();  //java.io.IOException: no entry name specified
        //指向jar包的文件URL
        url = new URL("file:/E:/apache-maven-3.6.1/localRepository/com/fresh/fresh-common/1.0.0/fresh-common-1.0.0.jar");
        InputStream iss = url.openStream(); //jar是一个归档文件，读出来的内容是jar包的归档信息和归档内容
        iss.close();
        /*//使用File读取jar包内容
        //1.得到jar包的绝对路径
        String abPath = "E:/apache-maven-3.6.1/localRepository/com/fresh/fresh-common/1.0.0/fresh-common-1.0.0.jar";
        //2.创建File
        File jarFile = new File(abPath);
        System.out.println(jarFile.isDirectory()); //jar包是一个归档文件而不是目录
        //3.读取jar包内容
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(jarFile), "gbk"));
        String line = br.readLine();
        int times = 1;
        while(line != null && times < 50) {
            System.out.println(line);
            line = br.readLine();
            times++;
        }
        br.close();
        //读出来的内容是jar包的归档信息和归档内容*/

        //jar包内容读取方法: JarFile类
        //1.得到jar包的绝对路径
        String abPath = "E:/apache-maven-3.6.1/localRepository/com/fresh/fresh-common/1.0.0/fresh-common-1.0.0.jar";
        //2.创建JarFile
        JarFile jarFile = new JarFile(abPath);
        //3.得到所有的条目
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry et = entries.nextElement();
            System.out.println(et.getName());
        }

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
