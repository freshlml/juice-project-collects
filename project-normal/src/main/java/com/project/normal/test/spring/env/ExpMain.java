package com.project.normal.test.spring.env;

import java.util.StringTokenizer;

public class ExpMain {

    public static void main(String argv[]) {

        /*System.out.println("###########expression##########");
        Predicate<String> predicate = ExpMain::expMatchers;
        FlExpression exp = FlExpression.of("p1 | p2 | (p3 & p4)");
        boolean result = exp.matches(predicate);
        System.out.println(result);*/

        AssertUtils.isTrue(false, null, null);


        System.out.println("########StringTokenizer########");
        StringTokenizer tokens = new StringTokenizer("(p1 &p2 & (p2-1 | p2-2)) | ! p3 | (p4 ) | !(p5 & p6) | (!p7 & p8) (|) p9 | (!)p10", "()&|!", true);
        while(tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if(!token.isEmpty()) {
                System.out.println(token);
            }
        }
    }

   /* public static boolean expMatchers(String exp) {
        List<String> activeExps = new ArrayList<>();
        //activeExps.add("p1");
        //activeExps.add("p2");
        activeExps.add("p3");
        activeExps.add("p4");
        return activeExps.stream().anyMatch(active -> active.equals(exp));
    }*/

}
