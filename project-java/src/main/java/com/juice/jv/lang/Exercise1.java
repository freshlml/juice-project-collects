package com.juice.jv.lang;

public class Exercise1 {

    public static void main(String[] argv) {

        print('A', 50);
    }

    /**
     * print:
     *    A      A       A                A             A
     *          A A     A A              A A           A A
     *                 AAAAA            AAAAA         AAAAA           ......
     *                                 A     A       A     A
     *                                              AAAAAAAAA
     * @param c      a Unicode character
     * @param lines  the lines of print, from 1 to Integer.MAX_VALUE
     * @throws IllegalArgumentException If the specified lines is less and equals than 0
     */
    static void print(char c, int lines) {
        if(lines <= 0) throw new IllegalArgumentException("the argument lines is less and equal than 0");
        StringBuilder sb = null;
        try {
            sb = new StringBuilder(lines);
        } catch (OutOfMemoryError e) { // the allocated char array is too large, so that the length is greater than Jvm heap size or the maximum array size defined by Jvm
            throw e;
        }

        System.out.printf("%" + lines + "c\n", c);  //line 1
        for(int i=2; i<=lines; i++) {
            if(i % 2 == 0) { //偶数行
                System.out.printf("%" + (lines - i + 1) + "c" + "%" + (2*i - 2) + "c\n", c, c);
            } else {
                sb.append(c).append(c);

                System.out.printf("%" + (lines - 1) + "s" + "%c" + "%" + (i - 1) + "s\n", sb, c, sb);
            }
        }


    }





}
