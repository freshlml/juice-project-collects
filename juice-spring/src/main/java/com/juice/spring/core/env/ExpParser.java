package com.juice.spring.core.env;

import java.util.StringTokenizer;

public class ExpParser {

    /**
     * @see com.fresh.core.exp.SimpleLogicExp
     */

    public static void main(String argv[]) {
        tokenizer();

    }

    public static void tokenizer() {
        StringTokenizer tokens = new StringTokenizer("(p1 &p2 & (p2-1 | p2-2)) | ! p3 | (p4 ) | !(p5 & p6) | (!p7 & p8) (|) p9 | (!)p10", "()&|!", true);
        while(tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if(!token.isEmpty()) {
                System.out.println(token);
            }
        }
    }


}
