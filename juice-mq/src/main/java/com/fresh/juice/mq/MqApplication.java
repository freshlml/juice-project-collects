package com.fresh.juice.mq;

public class MqApplication {

    public static void main(String[] argv) {
        try {
            for (int i = 0; i < 9; i++) {
                try {
                    System.out.println(i);
                    if ((i % 3) == 0) throw new RuntimeException(i + "");
                } catch (Exception e) {
                    System.out.println("catch exp");
                }
            }
        } finally {
            System.out.println("finally");
        }

    }
}
