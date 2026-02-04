package com.fresh.juice.jv.lang.numerical;

public class Exercise4 {

    public static void main(String[] argv) {

        //浮点数存储值分析:
        //   一个整数部分数值∈[-2^54, 2^54]的小数存储为 double，将该 double 转化成的整数，要么是原小数的整数部分，还要么是原小数的整数部分+1
        double result = 0;
        double d = 1123.956789;
        long dl = Double.doubleToLongBits(d);
        long jm = dl & 0x7f_f0_00_00_00_00_00_00L;  //获取阶码
        if(jm == 0x7f_f0_00_00_00_00_00_00L) {  //无穷或者 NaN
            if(Double.isNaN(d)) result = 0;
            else if(d == Double.POSITIVE_INFINITY) result = (1L << 54);
            else result = -(1L << 54);
        } else if(jm == 0) {  //非规格化小数
            result = 0;
        } else {
            //计算指数值
            long zs = (jm - 0x3f_f0_00_00_00_00_00_00L) & 0x7f_f0_00_00_00_00_00_00L;
            System.out.println(zs << 1 >> 54);
            if((zs << 1 >> 53) > 54) {
                System.out.println("舍入造成的整数部分值与原小数整数部分值差距太大");
                //result = (long) d;
            } else {
                result = (double) (long) d;
            }
        }
        System.out.println(result);

        //System.out.println(calPolynome(Integer.MAX_VALUE));
    }

    /**
     * 多项式求值：4*(1 - 1/3 + 1/5 - 1/7 ...)
     *
     * @param n  累计项数, from 1 to Integer.MAX_VALUE
     * @return   多项式的值
     * @throws IllegalArgumentException  如果项数 n 为 0 或者负数
     */
    static double calPolynome(int n) {
        if(n <= 0) throw new IllegalArgumentException("项数 n 为 0 或者负数");

        double result = 0;
        int sign = 1;
        for(int i=0; i<n; i++) {
            result += sign*(1.0/(2.0*(i+1)-1.0));
            sign *= -1;
        }
        result *= 4.0;

        return result;
    }

}
