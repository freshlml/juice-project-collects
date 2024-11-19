package com.juice.alg.part2.chapter8;


import com.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;

public class Chapter8_2 {

    /**
     *计数排序
     * 1.概念: n个元素的输入序列A中的每一个元素值都在C[begin, end)区间。当end-begin = k = O(n)时，计数排序的运行时间为O(n)
     * 2.实现说明
     *   1). 输入序列A中的元素通过"下标映射"到C
     *   2). 对于任意输入元素x，计算<=x的元素个数，利用这一信息，就可以将x放到输出数组的位置上
     *   3). 相等值具有稳定性，但非原址
     * 3.实例
     *   A[n] = {6, 5, 5, 2, 1, 3, 6, 7}
     *   C[0, 8) = 0  1  2  3  4  5  6  7  8 下标
     *             0  0  0  0  0  0  0  0  0 初始值
     *
     *
     *   for i from 0 to A.length - 1:
     *      A[i] "下标映射"为idx=A[i], C[idx] = C[idx] + 1
     *                _  _  _     _  _  _
     *   C[0, 8) = 0  1  2  3  4  5  6  7  8 下标
     *             0  1  1  1  0  2  2  1  0 times
     *
     *
     *   C[i] = C[i] + C[i-1]
     *                _  _  _     _  _  _
     *   C[0, 8) = 0  1  2  3  4  5  6  7  8 下标
     *             0  1  2  3  3  5  7  8  8 <=
     *
     *
     *   for i from A.length-1 to 0: //从尾到头是为了保持相等值的排序稳定性
     *      A[i] "下标映射"为idx=A[i], B[ C[idx] - 1 ] = A[i]
     *      C[idx] = C[idx] - 1  //for 相等元素
     *   B[n] = 1  2  3  5  5  6  6  7
     *
     */
    public static int[] counting_sort(int[] a, int min, int max) { //数组中元素的取值范围为 [min, max)
        if(a == null) return null;
        if(a.length == 0 || a.length == 1) return a;

        //assert begin < end

        int[] c = new int[max - min];  //初始值为 0

        for(int i=0; i<a.length; i++) {
            int idx = indexPos(min, a[i]);
            c[idx] = c[idx] + 1;
        }

        for(int i=1; i<c.length; i++) {
            c[i] = c[i] + c[i-1];
        }

        int[] b = new int[a.length];
        for(int i=a.length-1; i>=0; i--) { //from a.length-1 to 0，保证了计数排序是稳定的
            int idx = indexPos(min, a[i]);
            b[ c[idx] - 1 ] = a[i];
            c[idx] = c[idx] - 1;
        }

        return b;
    }
    static int indexPos(int min, int ai) {
        return ai - min;
    }

    public static void main(String[] argv) {
        int[] a = new int[] {-100, 50, 1, 79, -9, -23, 6, -23, -23, 7, -100};
        int[] b = counting_sort(a, -100, 80);

        IntArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
        IntArrayTraversal.of(b).forEach(ArrayPrinter.of()::print);
    }

    //练习8.2-4： Count[a, b] = Count[0, b] - Count[0, a] + Count(a)

}
