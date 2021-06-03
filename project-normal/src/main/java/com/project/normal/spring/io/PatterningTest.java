package com.project.normal.spring.io;

public class PatterningTest {

    public static void main(String argv[]) {

        /*Pattern pattern = Pattern.compile("\\{([^/]+?)\\}");
        Matcher matcher = pattern.matcher("{123gdfs}fasdfs{1fasd}\r\nfasdfs{opo}");
        while(matcher.find()) {
            System.out.println("matcher string: " + matcher.group());
            System.out.println("matcher position: " + "start=" + matcher.start() + ";end=" + matcher.end());
            System.out.println("matcher group1: " + matcher.group(1));
            System.out.println("###############");
        }*/

        //? * {{非/}} {非/{}} {\\{} {\\}}
        /*Pattern p = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");
        Matcher m = p.matcher("{username:\\w}");
        while(m.find()) {
            System.out.println("matcher string: " + m.group());
            System.out.println("matcher position: " + "start=" + m.start() + ";end=" + m.end());
            //System.out.println("matcher group1: " + m.group(1));
            System.out.println("###############");
        }*/

        /*AntPathMatcher pathMatcher = new AntPathMatcher();
        Map<String, String> r = pathMatcher.extractUriTemplateVariables("{username}-{password}", "what?-123");
        System.out.println(r);*/



    }

}
