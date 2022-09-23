package com.juice.alg.chapter2_8;

import java.util.Arrays;

public class Chapter2_8_2 {

    /**
     *计数排序
     * 1.概念: n个元素的输入序列A中的每一个元素值都在C[begin, end)区间。当end-begin = k = O(n)时，计数排序的运行时间为O(n)
     * 2.实现说明
     *   1). 输入序列A中的元素通过"下标映射"到C
     *   2). 对于任意输入元素x，计算<=x的元素个数，利用这一信息，就可以将x放到输出数组的位置上
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
    public int[] counting_sort(int a[], int begin, int end) {
        if(a == null) return null;
        if(a.length == 0 || a.length == 1) return a;

        //assert begin < end

        int[] c = new int[end - begin];  //初始值为0

        for(int i=0; i<a.length; i++) {
            int idx = indexPos(begin, a[i]);
            c[idx] = c[idx] + 1;
        }

        for(int i=1; i<c.length; i++) {
            c[i] = c[i] + c[i-1];
        }

        int[] b = new int[a.length];
        for(int i=a.length-1; i>=0; i--) { //from a.length-1 to 0，保持相同元素的排序稳定性
            int idx = indexPos(begin, a[i]);
            b[ c[idx] - 1 ] = a[i];
            c[idx] = c[idx] - 1;   //for 相等元素
        }

        return b;
    }
    public int indexPos(int begin, int i) {
        if(begin == 0) return i;
        else if(begin > 0) return i - begin;
        else return i + begin;
    }

    //练习8.2-4： Count[a, b] = Count[0, b] - Count[0, a] + Count(a)


    public static void main(String argv[]) {

        Chapter2_8_2 chapter2_8_2 = new Chapter2_8_2();
        int[] a = new int[]{6, 5, 5, 2, 1, 3, 6, 7};
        int[] b = chapter2_8_2.counting_sort(a, 0, 8);
        Arrays.stream(b).forEach(System.out::println);

    }

}
