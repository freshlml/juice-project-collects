package com.project.normal.test.spring.env;

import java.util.function.Supplier;

public abstract class AssertUtils {

    /**
     * when not true throw expression
     * @param expression
     * @param type
     * @param messageSupplier
     */
    public static void isTrue(boolean expression, ExpressionType type, Supplier<String> messageSupplier) {
        if (!expression) {
            throwsExp(type, messageSupplier);
        }
    }

    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return messageSupplier!=null ?messageSupplier.get() :"";
    }

    private static void throwsExp(ExpressionType type, Supplier<String> messageSupplier) {
        if(type == null) throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        switch (type) {
            case None:
                throw new IllegalArgumentException(nullSafeGet(messageSupplier));
            case IllegalArgumentException:
                throw new RuntimeException(nullSafeGet(messageSupplier));
            default:
                throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }
    public enum ExpressionType {
        None, IllegalArgumentException
    }
}
