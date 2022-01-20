package com.project.alg.chapter1_2;

import java.util.Arrays;

public class Chapter1_2 {

    public static void main(String argv[]) {

        int a[] = {7, 3, 2, 4, 5, 2};
        insertion_sort(a);
        Arrays.stream(a).forEach(System.out::println);

        int b[] = {7, 3, 2, 4, 5, 2};
        select_sort(b);
        Arrays.stream(b).forEach(System.out::println);
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
     * 循环不变时式的数学归纳法证明
     *
     * 初始化: 初始化令j=1，因为A[0]是有序的
     * 保持: A[0,1...,k]有序，循环一次后，A[0,1,...,k,k+1]有序
     * 终止: j=n时终止，此时A{0,1,...,n-1}有序
     *
     */


    //练习2.1-4
    public static int[] binary_add(int[] a, int[] b) {

        int c[] = new int[a.length+1];
        for(int i=0; i<a.length; i++) {
            int sum = a[i] + b[i] + c[i];
            switch (sum) {
                case 3:
                    c[i+1] = 1;
                    c[i] = 1;
                    break;
                case 1:
                    c[i+1] = 0;
                    c[i] = 1;
                    break;
                case 2:
                    c[i+1] = 1;
                    c[i] = 0;
                    break;
                case 0:
                    c[i+1] = 0;
                    c[i] = 0;
                    break;
                default :
                    c[i+1] = 0;
                    c[i] = -1;
            }
        }
        return c;
    }
    /**
     * 证明:
     * 初始化: c[a.length+1] all赋值为0; i=0
     * 保持: c[k+1,k,...,0]为a[k,...,0]与b[k,...,0]的和===>c[k+2,k+1,...,0]为a[k+1,...,0]与b[k+1,...,0]
     * 终止: i=a.length终止
     */


    //练习2.2-2
    public static void select_sort(int a[]) {
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




}
