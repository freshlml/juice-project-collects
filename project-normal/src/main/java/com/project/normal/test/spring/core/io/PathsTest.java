package com.project.normal.test.spring.core.io;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

public class PathsTest {


    public static void main(String argv[]) {

        System.out.println("user.dir: " + System.getProperty("user.dir"));

        /**
         * 相对路径
         * 相对于当前工作空间所在目录: System.getProperty("user.dir")
         * C:\Users\DELL\Desktop>java -jar NormalProject.jar
         * user.dir: C:\Users\DELL\Desktop
         */
        File f = new File("NormalProject.jar");
        System.out.println(f.exists());
        System.out.println(f.getPath());
        Path path = f.toPath();
        System.out.println(path.getFileName());
        System.out.println(path.toString());
        System.out.println(path.isAbsolute());
        System.out.println(path.toAbsolutePath());
        System.out.println(path.toUri());

        /**
         * win机上绝对路径
         * File f = new File("D:/ideaProject/NormalProject/pom.xml");
         */

        /**
         * /: linux机上绝对路径, 根目录
         * /: win机上相对路径，相对于当前工作空间所在盘符
         */
        File af = new File("/Users/DELL/Desktop/NormalProject.jar");
        System.out.println(af.exists());


        /**
         * 使用类加载器加载资源
         * @see LoaderTest
         */
        URL classesPath = PathsTest.class.getClassLoader().getResource("com/freshjuice/fl/resources/Resources.class");
        System.out.println(classesPath);
        URL classpathPath = PathsTest.class.getClassLoader().getResource("java/lang/String.class");
        System.out.println(classpathPath);
        URL jarRes = PathsTest.class.getClassLoader().getResource("javafx-mx.jar"); //貌似classpath上的文件不能被定位
        System.out.println(jarRes);



    }


}
