package com.juice.alg.part2.chapter8;


import com.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;

public class Chapter8_2 {

    /**
     *计数排序
     *  1. n 个元素的输入序列 A 中的每一个元素值都在 [min, max) 区间。令 k = max - min，计数排序的运行时间为 Θ(n+k)
     *  2. 计数排序是稳定的，非原址的
     *  3. 对每一个输入元素 x，确定小于 x的元素个数。利用这一信息，就可以直接把 x 放到它在输出数组中的位置上了
     *     关键点: 将输入序列 A 中的元素映射到 C 的下标
     *
     *  4. 实例
     *    A[n] = {-1, 5, 5, 2, 1, 3, 6, 6, 7}, A[i] ∈ [-1, 8)
     *
     *    C[0, 9):  0  1  2  3  4  5  6  7  8 下标
     *              0  0  0  0  0  0  0  0  0 初始值
     *
     *    for i from 0 to A.length - 1:
     *      idx = A[i] - (-1);
     *      C[idx] = C[idx] + 1;
     *              _  _  _  _     _  _  _
     *    C[0, 9):  0  1  2  3  4  5  6  7  8 下标
     *              1  1  1  1  0  2  2  1  0 次数
     *
     *    C[i] = C[i] + C[i-1]
     *              _  _  _  _     _  _  _
     *    C[0, 8):  0  1  2  3  4  5  6  7  8 下标
     *              1  2  3  4  4  6  8  9  9
     *
     *    for i from A.length-1 to 0:
     *      idx = A[i] - (-1);
     *      B[ C[idx] - 1 ] = A[i];
     *      C[idx] = C[idx] - 1;
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
