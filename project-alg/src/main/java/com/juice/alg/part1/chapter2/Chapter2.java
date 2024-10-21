package com.juice.alg.part1.chapter2;

import java.util.Arrays;

public class Chapter2 {

    public static void main(String[] argv) {

        int[] a = {7, 3, 2, 4, 5, 2};
        insertion_sort(a);
        Arrays.stream(a).forEach(ArrayPrinter.of(a.length)::print);
        System.out.println("#############################################");

        int[] b = {7, 3, 2, 4, 5, 2};
        selection_sort(b);
        Arrays.stream(b).forEach(ArrayPrinter.of(a.length)::print);
        System.out.println("#############################################");

        int[] c = {7, 3, 2, 4, 5, 2};
        merge_sort(c);
        Arrays.stream(c).forEach(ArrayPrinter.of(a.length)::print);
        System.out.println("#############################################");
    }


    public static void insertion_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int i=1; i < a.length; i++) {
            int ka = a[i];
            int j = i - 1;
            while(j >= 0 && a[j] > ka) {  //如果使用 a[j] >= ka: 相等的值的原相对位置发生逆转
                a[j+1] = a[j];
                j--;
            }
            a[j+1] = ka;
        }

    }
    /**
     *"循环不变时式"的数学归纳法证明
     *
     * 初始化: 初始化令i=1，因为A[0]是有序的
     * 保持: A[0,1...,k]有序，循环一次后，A[0,1,...,k,k+1]有序
     * 终止: i=n时终止，此时A{0,1,...,n-1}有序
     *
     *insertion_sort运行时间分析( 假设每条语句运行的时间是一个常数 C )
     * T(n) = C1*n + C2*(n-1) + C3*ΣPi + C4*(ΣPi - 1)   , i=1 to n-1
     * 其中ΣPi'max = 1 + 2 + 3 + ... + (n-1) = n*(n-1)/2
     *    ΣPi'min = 1 + 1 + 1 + ... + 1 = n-1
     * T(n)'max = C1*n + C2*(n-1) + C3*[n*(n-1)/2] + C4*[n*(n-1)/2 - 1]
     *          = C1*n + C2*n - C2 + (C3+C4)/2*n^2 - (C3+C4)/2*n - C4
     *          ≈ C1*n + C2*n + (C3+C4)/2*n^2 - (C3+C4)/2*n
     *          ≈ (C3+C4)/2*n^2 ,C3=while循环执行时间; C4=while中所有语句执行一次的时间之和; C1=for; C2=for中除while之外的语句
     *Θ = n^2
     *
     */


    public static void selection_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int i=0; i < a.length-1; i++) {
            int j = i+1;
            int p = i;
            while(j < a.length) {
                if(a[j] < a[p]) {
                    p = j;
                }
                j++;
            }
            int t = a[i];
            a[i] = a[p];
            a[p] = t;
        }

    }


    /**
     *merge_sort运行时间分析
     *                        [n]
     *           [n/2]                      [n/2]                   第1次分解, n/2^1   2^0次merge
     *      [n/4]      [n/4]          [n/4]         [n/4]           第2次分解, n/2^2   2^1次merge
     *  [n/8]  [n/8] [n/8]  [n/8]  [n/8]  [n/8] [n/8]  [n/8]        第3次分解, n/2^3   2^2次merge
     *  ...
     *  [1],[1],...[1]                                              第x次分解, n/2^x   2^(x-1)次merge
     *
     *  n/2^x = 1 ==> 2^x = n ==>  x = log2(n)
     *
     *T(n) = C1*(2^0 + 2^1 +...+ 2^x) + [ 2^(x-1) * [C2+C3*2^1+C4*(2^1-1))] + 2^(x-2) * [C2+C3*2^2+C4*(2^2-1)] +...+ 2^0 * [C2+C3*2^x+C4*(2^x-1)] ]
     *     = 2*C1*n - C1 + C2*n - C2 + (C3+C4)*x*2^x - C4*n
     *     ≈ 2*C1*n + C2*n + (C3+C4)*n*log2(n) - C4*n
     *     ≈ (C3+C4)*n*log2(n) ,C3=merge,for比较; C4=merge,for中语句一次执行的时间之和; C1=merge_sort中外围的语句之和; C2=merge中for之外的语句之和
     *O=n*log2(n)
     */
    public static void merge_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        merge_sort(a, 0, a.length);
    }
    public static void merge_sort(int[] a, int begin, int end) {

        int n = end - begin;
        if(n == 1) return;

        merge_sort(a, begin, begin + n/2);
        merge_sort(a, begin + n/2, end);

        merge(a, begin, begin + n/2, end);

    }
    public static void merge(int[] a, int p, int q, int r) {

        /*for(int i=p, j=q; i<j && j<r; ) {

            if(a[i] >= a[j]) {
                int t = a[j];
                for(int l=j; l>i; l--) {
                    a[l] = a[l-1];
                }
                a[i] = t;
                j++;
            }
            i++;
        }*/

        int[] left = new int[q-p+1];
        left[left.length-1] = Integer.MAX_VALUE;
        int[] right = new int[r-q+1];
        right[right.length-1] = Integer.MAX_VALUE;
        System.arraycopy(a, p, left, 0, left.length-1);
        System.arraycopy(a, q, right, 0, right.length-1);

        for(int i=0, j=0, l=p; l<r; l++) {
            if(left[i] > right[j]) {
                a[l] = right[j];
                j++;
            } else {
                a[l] = left[i];
                i++;
            }
        }
    }


    static class ArrayPrinter {
        private final int length;
        private int count = 1;

        ArrayPrinter(int length) {
            //assert length > 0
            this.length = length;
        }

        <T> void print(T t) {
            System.out.print(t);
            if(count < length) {
                System.out.print(", ");
            } else {
                System.out.println();
            }
            count++;
        }

        static ArrayPrinter of(int length) {
            return new ArrayPrinter(length);
        }
    }
}
