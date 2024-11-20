package com.juice.alg.part2.chapter8;


import com.juice.alg.part1.chapter4.Chapter4_2.IntMatrixTraversal;
import com.juice.alg.part1.chapter4.Chapter4_2.IntMatrixTraversal.TraversalMode;
import com.juice.alg.part1.chapter4.Chapter4_2.MatrixPrinter;

public class Chapter8_3 {

    /**
     *第一：基数排序: 段式排序。最高有效位；最低有效位
     *  1.最高有效位。如"年-月-日"，先按"年"排序，年相等的按"月"排序，月相等的按"日"排序
     *  2.最低有效位。如"年-月-日"，先按"日"排序，再按"月"排序，再按"年"排序
     *
     *第二：n 个 d 位数的基数排序
     *  Ⅰ: 最高有效位
     *      ↓              ↓              ↓              ↓
     *     ▏329          ▎329          ▏329           328
     *     ▏355          ▎355          ▏328           329
     *     ▏657   --->   ▎328   --->   ▎355   --->
     *     ▏328          ▌657          ▌657           657
     *     ▏837          ▌657          ▌657           657
     *     ▏657          ▉837
     *
     *  Ⅱ: 最低有效位
     *        ↓            ↓            ↓            ↓
     *     ▏329          ▏355          ▏328           328
     *     ▏355          ▏657          ▏329           329
     *     ▏657   --->   ▏837   --->   ▏837   --->    355
     *     ▏328          ▏657          ▏355           657
     *     ▏837          ▏328          ▏657           657
     *     ▏657          ▏329          ▏657           837
     *
     *     要求: 每一位的比较都必须是稳定的，否则可能出错，如 29，28，第一次对 9，8 排序得到 28，29，第二次排序对 2，2 排序，如果不稳定，则将可以得到 29，28 这样的错误结果
     *
     *   Ⅲ: n 个 d 位数的存储方式
     *
     *      1).使用二维数组存储。按高位到低位。不足 d 位的使用 0 填充（造成空间浪费）。如果要表示正负数，则可约定最高位为符号位
     *         0 0 0 0 0 3 2 9   --> 329
     *         1 1 1 1 1 3 2 8   --> 11111328
     *         0 0 0 0 0 0 0 1   --> 1
     *         0 0 0 0 0 0 1 5   --> 15
     *
     *      2).使用二维数组存储。按低位到高位。不足 d 位的无需使用 0 填充。如果要表示正负数，则可约定最高位为符号位
     *         9 2 3             --> 329
     *         8 2 3 1 1 1 1 1   --> 11111328
     *         1                 --> 1
     *         5 1               --> 15
     *
     *      3).使用一维数组存储，节省了存储空间，只是寻址更加复杂
     *         329, 11111328, 15, -15, -15, -25
     *
     *   Ⅳ: 使用数组表示的 d 位数，每一个位的取值范围均为 [0, k)。则每一轮可使用计数排序。这样基数排序的运行时间为: Θ(d*(n+k))
     */
    public static void radix_sort(int[][] a, int d) {  //取 Ⅲ. 2) 存储方案
        if(a == null || a.length == 0 || a.length == 1) return;
        //assert d <= a[0].length;
        assert d > 0;

        for(int j=0; j < d; j++) {
            radix_counting_sort(a, j, 0, 10);
        }

    }
    static void radix_counting_sort(int[][] a, int j, int min, int max) { //a[0~a.length][j]数组中元素的取值范围为 [min, max)
        //assert min < max

        int[] c = new int[max - min];  //初始值为 0.

        for(int i=0; i<a.length; i++) {
            int idx = indexPos(min, a[i], j);
            c[idx] = c[idx] + 1;
        }

        for(int i=1; i<c.length; i++) {
            c[i] = c[i] + c[i-1];
        }

        int[][] b = new int[a.length][];
        for(int i=a.length-1; i>=0; i--) {   //from a.length-1 to 0，保证了计数排序是稳定的
            int idx = indexPos(min, a[i], j);
            b[ c[idx] - 1 ] = a[i];
            c[idx] = c[idx] - 1;
        }

        //a = b;
        System.arraycopy(b, 0, a, 0, a.length);
    }
    static int indexPos(int min, int[] ai, int j) {
        try {
            return ai[j] - min;
        } catch (IndexOutOfBoundsException e) {
            return min;
        }
    }

    public static void main(String[] argv) {
        int[][] a = new int[][] { {1}, {9, 2, 3}, {5, 5, 3}, {7, 5, 6}, {8, 2, 3}, {7, 3, 8}, {7, 5, 6}, {0, 1}, {1, 0} };
        IntMatrixTraversal.of(a, TraversalMode.COLUMN_REVERSE).forEach(MatrixPrinter.of()::print);
        System.out.println("----------");
        radix_sort(a, 3);
        IntMatrixTraversal.of(a, TraversalMode.COLUMN_REVERSE).forEach(MatrixPrinter.of()::print);

        System.out.println("##########");

        int[][] a2 = new int[][]{ {3,2,9}, {3,2,8}, {3,5,5}, {6,5,7}, {6,5,7}, {8,3,7} };
        radix_sort_r(a2, 2);


    }

    /**
     *给定一个 d 位数和正整数 r <= d. 令 p = ⌈d/r⌉
     *
     *使用 p 作为比较次数，每次比较 r 位，则运行时间为:
     *  f(r) = p * (n + k^r) = d/r * (n + k^r)
     *
     *  1. r <= d < log k (n) 时，f(r) < d/r * 2n。则取 r = d 时，得到最优解: f(r) < 2*n
     *  2. log k (n) <= r <= d 时，当 r = log k (n) 时得到最优解 f(r) = d*2n/log k (n)
     */
    public static void radix_sort_r(int[][] a, int r) {  //取 Ⅲ. 1) 存储方案
        if(a == null || a.length == 0 || a.length == 1) return;

        int n = a.length;
        int d = a[0].length; //safe
        //assert 0 < r <= d
        int p = d%r > 0 ? d/r+1 : d/r;

        for(int t=0; t<p; t++) {
            int end = Double.valueOf(Math.pow(10, Math.min(d-t*r, r))).intValue();
            radix_counting_sort_r(a, t, r, 0, end);
        }
    }
    static void radix_counting_sort_r(int[][] a, int t, int r, int begin, int end) {
        //assert begin < end

        int[] c = new int[end - begin];  //初始值为0

        for(int i=0; i<a.length; i++) {
            int idx = indexPos_r(a[i], t, r);
            c[idx] = c[idx] + 1;
        }

        for(int i=1; i<c.length; i++) {
            c[i] = c[i] + c[i-1];
        }

        int[][] b = new int[a.length][];
        for(int i=a.length-1; i>=0; i--) {
            int idx = indexPos_r(a[i], t, r);
            b[ c[idx] - 1 ] = a[i];
            c[idx] = c[idx] - 1;
        }

        System.arraycopy(b, 0, a, 0, a.length);
    }
    static int indexPos_r(int[] ai, int t, int r) {
        int re = 0;
        for(int pos=(ai.length-1) - t*r, k=0; pos>=0 && k<r; pos--, k++) {
            re += ai[pos] * Double.valueOf(Math.pow(10, k)).intValue();
        }
        return re;
    }


    //练习8.3-2: 插入排序、归并排序、堆排序是稳定的。快速排序不稳定
    //  1. 对快速排序: 元素交换操作时，遍历序列判断使用当前值或者替代之交换。额外时间开销: Θ(n)。额外空间开销: Θ(1)
    //  2. 给每一个值记录原始下标，排完序后，对连续的相等区间，根据原始下标排序

    //练习8.3-4: 3*[n + d(n)]
    //  n 个 [0, n^3) 区间的整数。d = log10(n^3) = 3*log10(n)，k = 10。
    //  有 Θ(d*(n+k)) = Θ(3*log10(n)*(n+10)) = Θ(n * log10(n))
    //
    //  根据 radix_sort_r(...):
    //  当 r = log10(n), f(r) = d*2n/log10(n) = Θ(n)

}