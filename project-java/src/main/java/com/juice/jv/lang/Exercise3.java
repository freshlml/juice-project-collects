package com.juice.jv.lang;

public class Exercise3 {

    public static void main(String[] argv) {

        int a1 = Integer.MIN_VALUE+1;
        int d = Integer.MAX_VALUE;
        int n = 3;

        int sum = sumArithmeticSeries(a1, calAn(a1, d, n), n);
        System.out.println(sum);
    }

    /**
     * 求等差级数的 n 项和。等差级数的每一项数值大小均使用 int 类型表示。
     * 数学中的计算公式: n*(a1 + an)÷2，结果是一个整数，即能够整除。
     *   1. n 为偶数，则 n 能够整除 2
     *   2. n 为奇数，则 a1 + an = 2*a1 + (n-1)*d 能够整除 2
     * 因为 n*(a1 + an) 能够整除 2，因此， n*(a1 + an)÷2 ==> (n*(a1 + an))/2
     *
     * 检测等差级数的 d 是否为整数，如果 (an-a1) 能够整除 (n-1)，则是整数，否则，IllegalArgumentException
     * 
     * @param a1  首项，可能为负值
     * @param an  第 n 项，可能为负值
     * @param n   项数，大于0
     * @return 等差级数的n项和
     * @throws IllegalArgumentException  如果项数 n 为 0 或者负数，或者
     *                                   如果等差级数的 d 不是一个整数
     * @throws ArithmeticException       如果 n 项和超过 int 的表示范围
     */
    static int sumArithmeticSeries(int a1, int an, int n) {
        if(n <= 0) throw new IllegalArgumentException("项数 n 为 0 或者负数");
        if(n == 1) return a1;

        long dlt = ((long) an - (long) a1);  //no overflow
        if(dlt % (n-1) != 0) throw new IllegalArgumentException("等差级数的 d 不是一个整数");

        int a1n = Math.addExact(a1, an); //如果 a1+an overflow, 则 sum 必定 overflow

        int sum = 0;
        if(n % 2 == 0) { //n为偶数，(n*(a1 + an))/2 ==> (n/2) * (a1 + an)
            sum = Math.multiplyExact(n/2, a1n);
        } else { //n为奇数，(n*(a1 + an))/2 ==> n * ((a1 + an)/2)
            sum = Math.multiplyExact(n, a1n/2);
        }

        return sum;
    }

    /**
     * 求等差级数的第 n 项。
     * 计算公式: a1 + (n-1)*d.
     *
     * @param a1  首项，可能为负值
     * @param d   项差，可能为负值
     * @param n   项数，大于0
     * @return    等差级数的第 n 项
     * @throws IllegalArgumentException  如果项数 n 为 0 或者负数
     * @throws ArithmeticException       如果第 n 项超过 int 的表示范围
     */
    static int calAn(int a1, int d, int n) {
        if(n <= 0) throw new IllegalArgumentException("项数 n 为 0 或者负数");

        int dlt = n-1;
        long an = (long) a1 + (long) dlt * d;  //no overflow
        if ((an & 0xffff_ffff_0000_0000L) == 0) {
            return (int) an;
        }

        throw new ArithmeticException("第 n 项超过 int 的表示范围");
    }

}
