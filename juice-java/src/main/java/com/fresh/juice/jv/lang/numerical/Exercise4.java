package com.fresh.juice.jv.lang.numerical;

public class Exercise4 {

    public static void main(String[] argv) {
        examine(Float.NaN);                        //NaN
        examine(Float.POSITIVE_INFINITY);          //POSITIVE INFINITY
        examine(Float.NEGATIVE_INFINITY);          //NEGATIVE INFINITY

        //0.1111_1110_0111_1011_1110_111
        float f = -0x0.fe_7b_eeP-126f;
        examine(f);                                //-0.11111110011110111110111 * 2^-126

        //1.1111_0001_0001_0001_1111_111
        float f2 = 0x1.f1_11_feP5f;               //1.11110001000100011111111 * 2^5
        examine(f2);

        System.out.println("--------------------------------------");
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
            //print S 0.M * 2^-126
            StringBuilder sb = new StringBuilder();
            if(S != 0) sb.append("-");
            sb.append("0.");
            sb.append(Integer.toBinaryString(M));
            sb.append(" * 2^-126");
            System.out.println(sb);
        } else {  //规格化数
            //计算指数值
            int raw_e = (E - 0x3f_80_00_00) & 0x7f_80_00_00;
            int e = raw_e << 1 >> 24;
            //print S 1.M * 2 ^ e
            StringBuilder sb = new StringBuilder();
            if(S != 0) sb.append("-");
            sb.append("1.");
            sb.append(Integer.toBinaryString(M));
            sb.append(" * 2^").append(e);
            System.out.println(sb);
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
