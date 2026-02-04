package com.fresh.juice.jv.lang.numerical;

public class Exercise2 {

    public static void main(String[] argv) {

        print(Integer.MAX_VALUE);

        /*for(int i=Integer.MAX_VALUE; i>=46340; i--) {  //46340^2 < Integer.MAX_VALUE, 46341^2 > Integer.MAX_VALUE
            System.out.println("i = " + i + ", i*i = " + i*i + ", width: " + intSize(i*i));
        }*/
    }

    /**
     * print:
     *  a      a^2      a^3
     *  2147   4609609  1306895931
     *  214    45796    9800344
     *  21     441      9261
     *  2      4        8
     *
     * @param value  the value of print, from 1 to Integer.MAX_VALUE
     * @throws IllegalArgumentException If the specified lines is less and equals than 0
     */
    static void print(int value) {
        if(value <= 0) throw new IllegalArgumentException("the argument sum is less and equal than 0");

        int valueMaxWidth = intSize(value);
        int sqrValueMaxWidth;
        if(value > Integer.MAX_VALUE/value) { //sum*sum overflow
            sqrValueMaxWidth = 11;  //estimate width
        } else {
            sqrValueMaxWidth = intSize(value*value);
        }

        System.out.printf("%s" + "%" + (valueMaxWidth - 1 + 3 + 3) + "s" + "%" + (sqrValueMaxWidth - 3 + 3 + 3) + "s\n", "a", "a^2", "a^3"); //print title

        for(int i=value; i>=1; i=i/2) {
            int iw = intSize(i);
            int sqr = i*i;             //may overflow
            int sqrw = intSize(sqr);
            int cub = sqr*i;           //may overflow
            int cubw = intSize(cub);
            System.out.printf("%d" + "%" + (sqrw + (valueMaxWidth + 3) - iw) + "d" + "%" + (cubw + (sqrValueMaxWidth + 3) - sqrw) + "d\n", i, sqr, cub);
        }


    }


    final static int [] sizeTable = { 9, 99, 999, 9_999, 99_999, 999_999, 9_999_999, 99_999_999, 999_999_999, Integer.MAX_VALUE };

    static int intSize(int x) {
        if(x == Integer.MIN_VALUE) return 11;
        int supplement = 0;
        if(x < 0) {
            x = (-x);
            supplement = 1;
        }
        for (int i=0; ; i++)
            if (x <= sizeTable[i])
                return i + 1 + supplement;
    }

}
