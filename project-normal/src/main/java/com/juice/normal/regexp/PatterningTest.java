package com.juice.normal.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatterningTest {

    public static void main(String argv[]) {

        classifier_test();
        System.out.println("------------------------------------------------");

        Pattern p = Pattern.compile("\\?|\\*|\\{(([^/{}])+?)\\}");
        Matcher m = p.matcher("{username:\\w}");
        while(m.find()) {
            System.out.println("matcher string: " + m.group());
            System.out.println("matcher position: " + "start=" + m.start() + ";end=" + m.end());
            System.out.println("matcher group1: " + m.group(1));
            System.out.println("matcher group2: " + m.group(2));
            System.out.println("###############");
        }

        /*AntPathMatcher pathMatcher = new AntPathMatcher();
        Map<String, String> r = pathMatcher.extractUriTemplateVariables("{username}-{password}", "what?-123");
        System.out.println(r);*/



    }

    public static void classifier_test() {
        //贪婪
        Pattern pattern = Pattern.compile("\\{([^/]+)\\}");
        String str = "{1abc}pol{2abc}\r\nlop{3abc}";
        str = "{1abc}pol{2abc\r\nlop{3abc";
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()) {
            System.out.println("matcher string: " + matcher.group());
            System.out.println("matcher position: " + "start=" + matcher.start() + ";end=" + matcher.end());
            System.out.println("matcher group1: " + matcher.group(1));
            System.out.println("$$$$$$$$$$$$$$");
        }
        System.out.println("--------------------------------------------------");

        //非贪婪
        String patternStr = "\\{([^/]+?)\\}";
        patternStr = "\\{([^/]+?)";
        Pattern pattern2 = Pattern.compile(patternStr);
        String str2 = "{1abc}pol{2abc}\r\nlop{3abc}";
        Matcher matcher2 = pattern2.matcher(str2);
        while(matcher2.find()) {
            System.out.println("matcher string: " + matcher2.group());
            System.out.println("matcher position: " + "start=" + matcher2.start() + ";end=" + matcher2.end());
            System.out.println("matcher group1: " + matcher2.group(1));
            System.out.println("###############");
        }


    }

}
