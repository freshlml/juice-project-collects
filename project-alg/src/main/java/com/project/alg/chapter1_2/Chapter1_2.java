package com.project.alg.chapter1_2;

import java.util.Arrays;

public class Chapter1_2 {

    public static void main(String argv[]) {

        int a[] = {7, 3, 2, 4, 5, 2};
        insertion_sort(a);
        Arrays.stream(a).forEach(System.out::println);
        System.out.println("###############");

        int b[] = {7, 3, 2, 4, 5, 2};
        selection_sort(b);
        Arrays.stream(b).forEach(System.out::println);
        System.out.println("###############");

        int c[] = {7, 3, 2, 4, 5, 2};
        merge_sort(c);
        Arrays.stream(c).forEach(System.out::println);
        System.out.println("###############");
    }


    public static void insertion_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int j = 1; j<a.length; j++) {
            int key = a[j];
            int i = j-1;
            while(i >= 0 && a[i] > key) {  //if >=: 相等的值的相对位置发生逆转
                a[i+1] = a[i];
                i--;
            }
            a[i+1] = key;
        }

    }
    /**
     *"循环不变时式"的数学归纳法证明
     *
     * 初始化: 初始化令j=1，因为A[0]是有序的
     * 保持: A[0,1...,k]有序，循环一次后，A[0,1,...,k,k+1]有序
     * 终止: j=n时终止，此时A{0,1,...,n-1}有序
     *
     *insertion_sort运行时间分析(假设每条语句运行的时间是一个常数C)
     * T(n) = C*n + C*(n-1) + C*ΣPj,j=1 to n-1 + C*(ΣPj - 1)
     * 其中ΣPj'max = 1 + 2 + 3 + ... + (n-1) = n*(n-1)/2
     *    ΣPj'min = 1 + 1 + 1 + ... + 1 = n-1
     * T(n)'max = C*n + C*(n-1) + C*[n*(n-1)/2] + C*[n*(n-1)/2 - 1]
     *时间复杂度Ω=n^2
     *
     */


    public static void selection_sort(int a[]) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int i=0; i<a.length-1; i++) {
            int j = i+1;
            int p = i;
            while(j < a.length) {
                if (a[j] < a[p]) {
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
     *                        [n]                                          2^0份
     *           [n/2]                      [n/2]                   第1层   2^1份   2^0次merge
     *      [n/4]      [n/4]          [n/4]         [n/4]           第2层   2^2份   2^1次merge
     *  [n/8]  [n/8] [n/8]  [n/8]  [n/8]  [n/8] [n/8]  [n/8]        第3层   2^3份   2^2次merge
     *  ...
     *  [1],[1],...[1]                                              第x层   2^x份   2^(x-1)次merge
     *
     *  2^x = n ==>  x = log2N
     *
     *T(n) = C*(2^0 + 2^1 +...+ 2^x) + [ 2^(x-1) * 2 + 2^(x-2) * 2^2 +...+ 2^0 * 2^x ]
     *     = C*n + x*2^x
     *     = C*n + n*log2N
     *时间复杂度Ω=n*log2N
     */
    public static void merge_sort(int a[]) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        merge_sort(a, 0, a.length);
    }
    public static void merge_sort(int a[], int begin, int end) {

        int n = end - begin;
        if(n == 1) return;

        merge_sort(a, begin, begin + n/2);
        merge_sort(a, begin + n/2, end);

        merge(a, begin, begin + n/2, end);

    }
    public static void merge(int a[], int p, int q, int r) {

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

}
