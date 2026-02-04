package com.fresh.juice.jv.util.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatterningTest {

    public static void main(String argv[]) {

        matcher_test();
        System.out.println("-------------------1----------------------------");

        classifier_test();
        System.out.println("-------------------2-----------------------------");

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

    static void matcher_test() {
        //
        Pattern p = Pattern.compile("^.+?$", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = p.matcher("ab\r\nc");
        boolean r = m.matches();
        //true, matches 方法的语义是 match entire region, 与 regex 中 '^', '$' 和 Pattern.MULTILINE 矛盾
        System.out.println(r);
        /*
        matches: ab
        c
         */
        System.out.println("matches: " + m.group());

        m.reset();
        m.region(2, 5);
        boolean rl = m.lookingAt();
        System.out.println(rl);  //true
        /* lookingAt 方法的语义是 starting at the beginning of the region, 与 regex 中 '^' 和 Pattern.MULTILINE 矛盾
        lookingAt:
        c
         */
        System.out.println("lookingAt: " + m.group());

        m.reset();
        while(m.find()) {
            /*
            region: start=0, end=5
            find: ab
            region: start=0, end=5
            find: c
             */
            System.out.println("region: start=" + m.regionStart() + ", end=" + m.regionEnd());
            System.out.println("find: " + m.group());
        }

    }

    static void classifier_test() {
        //贪婪
        Pattern pattern = Pattern.compile("\\{([^/]+)\\}");
        String str = "{1abc}pol{2abc}\r\nlop{3abc}";
        str = "{1abc}pol{2abc\r\nlop{3abc";
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()) {
            System.out.println("matcher string: " + matcher.group());
            System.out.println("matcher position: " + "start=" + matcher.start() + ";end=" + matcher.end());
            System.out.println("matcher group1: " + matcher.group(1));
            System.out.println("###############");
        }
        System.out.println("------------------------1.1--------------------------");

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
