package com.fresh.juice.jv.lang.numerical;

public class Exercise4 {

    public static void main(String[] argv) {
        examine(16_777_215.9999f);   //16777216
        examine(16_777_215.0001f);   //16777215
        //1111 0000 0000 0000 0000 0000 |0011.000000...
        examine(251_658_243.0001f);   //整数部分精度丢失: 251658240 == 1111 0000 0000 0000 0000 0000 |0000

        System.out.println(calPolynome(10));
        System.out.println(calPolynome(100));
    }

    //浮点数存储值分析
    static void examine(float f) {
        int fs = Float.floatToIntBits(f);

        int E = fs & 0x7f_80_00_00;   //获取阶码
        int M = fs & 0x00_7f_ff_ff;   //有效数位
        int S = fs & 0x80_00_00_00;   //符号位

        if(E == 0x7f_80_00_00) {      //无穷或 NaN
            if(M != 0) {
                System.out.println("NaN");                // result = 0?
            } else if(S == 0) {
                System.out.println("POSITIVE INFINITY");  // result =  (1 << 24)?
            } else {
                System.out.println("NEGATIVE INFINITY");  // result = -(1 << 24)?
            }
        } else if(E == 0) {  //非规格化数
            System.out.println(0);
        } else {  //规格化数
            //计算指数值
            int raw_e = (E - 0x3f_80_00_00) & 0x7f_80_00_00;
            int e = raw_e << 1 >> 24;

            if(e > 24) {
                System.out.println("整数部分精度丢失: " + (int) f);
            } else {
                System.out.println((int) f);
            }
        }
    }

    /**
     * 多项式求值：4 * (1 - 1/3 + 1/5 - 1/7 ...)
     *
     * @param n  累计项数, from 1 to Integer.MAX_VALUE
     * @return   多项式的值
     * @throws IllegalArgumentException  如果项数 n 为 0 或者负数
     */
    static double calPolynome(int n) {
        if(n <= 0) throw new IllegalArgumentException("项数 n 为 0 或者负数");

        double result = 0;
        int sign = 1;
        for(int i = 0; i < n; i++) {
            result += sign * ( 1.0 / (2.0 * i + 1.0) );
            sign *= -1;
        }
        result *= 4.0;

        return result;
    }

}
