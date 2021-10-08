package com.project.normal.test.spring.core.env;

import java.util.ArrayList;
import java.util.List;

public class SimpleLogicExpTest {

    public static void main(String argv[]) {

        FlExpression exp = null;
        //exp = FlExpression.of("p1 & p2 & p3");
        exp = FlExpression.of("p1 & (p2| ( p3&p4) )");
        //exp = FlExpression.of("p1 & !p2");
        //exp = FlExpression.of("p1 & !(p2|p3)");
        //exp = FlExpression.of("p1 & (!p2|p3)");
        //exp = FlExpression.of("!p1");
        //exp = FlExpression.of("p1 & p2 | p3");
        System.out.println(exp.matches(SimpleLogicExpTest::expMatchers));


    }

    public static boolean expMatchers(String exp) {
        List<String> activeExps = new ArrayList<>();
        activeExps.add("p1");
        //activeExps.add("p2");
        activeExps.add("p3");
        activeExps.add("p4");
        return activeExps.contains(exp);
    }
}
