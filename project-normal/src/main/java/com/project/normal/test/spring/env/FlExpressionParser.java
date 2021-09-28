package com.project.normal.test.spring.env;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public abstract class FlExpressionParser {

    public static FlExpression parseExpression(String expression) {
        StringTokenizer tokenizer = new StringTokenizer(expression, "()&|!", true);
        FlExpression exp = tokenParser(tokenizer, expression, Context.NONE);
        return exp;
    }

    private static FlExpression tokenParser(StringTokenizer tokenizer, String expression, Context context) {
        List<FlExpression> list = new ArrayList<>();
        ParserState state = null;
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if(token.isEmpty()) continue;
            switch (token) {
                case "(":
                    FlExpression subExp = tokenParser(tokenizer, expression, Context.SUB);
                    if(context == Context.NOT) return subExp;
                    list.add(subExp);
                    break;
                case ")":
                    FlExpression merged = merge(list, state, expression);
                    //return merged;
                    if(context == Context.SUB) return merged;
                    list.clear();
                    list.add(merged);
                    state = null;
                    break;
                case "&":
                    AssertUtils.isTrue(state == null || state == ParserState.AND, AssertUtils.ExpressionType.IllegalArgumentException, () -> "invalid expression["+expression+"]: when the expression or sub-expression is using the |, here con not using &");
                    state = ParserState.AND;
                    break;
                case "|":
                    AssertUtils.isTrue(state == null || state == ParserState.OR, AssertUtils.ExpressionType.IllegalArgumentException, () -> "invalid expression["+expression+"]: when the expression or sub-expression is using the &, here con not using |");
                    state = ParserState.OR;
                    break;
                case "!":
                    FlExpression subExpNot = tokenParser(tokenizer, expression, Context.NOT);
                    FlExpression notSubExp = notPredicate -> !subExpNot.matches(notPredicate);
                    list.add(notSubExp);
                    break;
                default:
                    FlExpression exp = predicate -> predicate.test(token);
                    if(context == Context.NOT) return exp;
                    list.add(exp);
            }
        }
        return merge(list, state, expression);
    }

    private static FlExpression merge(List<FlExpression> list, ParserState state, String expression) {
        AssertUtils.isTrue(!list.isEmpty(), AssertUtils.ExpressionType.IllegalArgumentException, () -> "invalid expression["+expression+"]");
        if(list.size() == 1) return list.get(0);
        FlExpression exp = null;
        if(list.size() > 1) {
            FlExpression[] exps = list.toArray(new FlExpression[0]);
            if(state == ParserState.AND) {
                exp = predicate -> {
                    return Arrays.stream(exps).allMatch(flExp -> flExp.matches(predicate));
                };
            } else if(state == ParserState.OR) {
                exp = predicate -> {
                    return Arrays.stream(exps).anyMatch(flExp -> flExp.matches(predicate));
                };
            }
        }
        return exp;
    }


    private enum ParserState {
        AND, OR
    }
    private enum Context {
        NONE, NOT, SUB
    }
}
