package com.fresh.juice.alg.part2.chapter7;


import com.fresh.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;
import com.fresh.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;

public class Chapter7_Practice1 {

    //练习7.1-2
    //有序序列(包括元素都相等的情况) -->  Chapter7_1#partition(a, 0, a.length) 返回 a.length - 1
    //逆序序列  -->  Chapter7_1#partition(a, 0, a.length) 返回 0
    public static int partitionExt(int[] a, int begin, int end) {
    //检测序列是否已经有序，如果是，就抛出一个 RangeOrderedException 异常
        int i = begin - 1;
        int j = begin;
        int e = a[end - 1];

        int prev = begin - 1;
        boolean ordered = true;

        while(j < end) {
            if(a[j] <= e) {
                i++;
                int ex = a[i];
                a[i] = a[j];
                a[j] = ex;
            } else {
                ordered = false;
            }

            if(ordered && prev >= begin) {
                if(a[prev] > a[j]) ordered = false;
            }
            prev = j;

            j++;
        }
        if(ordered) throw new RangeOrderedException(begin, end, i);
        //if(i == end-1) { System.out.println("end-1处是最大值"); }
        //if(i == begin) { System.out.println("end-1处是最小值"); }
        return i;
    }

    public static void main(String[] argv) {
        int[] a = new int[] {1, 2, 3, 3, 5};

        try {
            int q = partitionExt(a, 0, a.length);
            System.out.println("q = " + q);
        } catch (RangeOrderedException ro) {
            System.out.println(ro.getMessage() + " q = " + ro.q);
        }
        IntArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
    }

    static class RangeOrderedException extends RuntimeException {
        final int begin;
        final int end;
        final int q;

        public RangeOrderedException(int begin, int end, int q) {
            super("Range[" + begin + ", " + end + ") itself ordered!!!");
            this.begin = begin;
            this.end = end;
            this.q = q;
        }
    }

    //练习7.2-5: 假设每次 partition 产生 (1-α):α 的划分，其中，0 < α <= 1/2 ( 1/α >= 1/(1-α) )
    //  T(n) = T((1-α) * n) + T(α*n) + Θ(n),      递归树，见 Chapter7#chapter7_2()
    //
    //  递归树的最小深度: n/(1/α)^x = 1,     x = log(1/α)[n]
    //  递归树的最大深度: n/(1/(1-α))^y = 1, y = log(1/(1-α))[n]
    //  则最坏情况运行时间 = n*log(1/(1-α))[n] <= C*n*lgn,        其中 C >= -(1/lg(1-α)) >= 1，越不平衡的划分有更大的系数
    //  partition 按比例划分的运行时间: O(n*lgn)
    //
    //练习7.2-6
    //  画图解法
    //  或  1-α-a = α+a ==> 2*a = 1-2α


    //练习 7.4-2
    //  最好情况下，partition 每次划分都将数组切分成两个相等长度（或者最多相差 1 个元素）的子数组.
    //  T(n) = 2*T(n/2) + Θ(n)
    //  解递归式得: T(n) = Θ(n*lgn)

    //练习7.4-6
    //  [1 * C(1, αn) * C(1, (1-α)n)] / C(3, n)
    
}
