package com.juice.alg.chapter1_2;

import java.util.Arrays;

public class Chapter1_2_1 {

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
     * "循环不不变式"的证明:
     * 初始化: c[a.length+1] all赋值为0; i=0
     * 保持: c[k+1,k,...,0]为a[k,...,0]与b[k,...,0]的和===>c[k+2,k+1,...,0]为a[k+1,...,0]与b[k+1,...,0]
     * 终止: i=a.length终止
     */


    //练习2.3-5，二分查找法,Ω=log2(n)
    public static boolean binary_search(int a[], int key) {
        if(a == null) return false;
        if(a.length == 0) return false;

        return binary_search(a, 0, a.length, key);
    }
    public static boolean binary_search(int a[], int begin, int end, int key) {

        int n = end - begin;
        if(n==0) return false;
        if(n==1) return a[begin]==key ? true : false;

        boolean ret = false;
        if(a[begin + n/2] > key) {
            ret = binary_search(a, begin, begin + n / 2, key);
        } else if(a[begin + n/2] < key) {
            ret = binary_search(a, begin + n/2 + 1, end, key);
        } else {
            ret = true;
        }

        return ret;
    }

    //练习2.3-6
    //1.递增序列找到第一个(逆序来看)<=的
    public static int binary_search_af(int a[], int key) {
        if(a == null) return -1;
        if(a.length == 0) return -1;

        return binary_search_af(a, 0, a.length, key);
    }
    public static int binary_search_af(int a[], int begin, int end, int key) {

        int n = end - begin;
        if(n == 0) return -1;
        if(n == 1) return a[begin]<=key ? begin : -1;

        int pos = -1;
        if(a[begin + n/2] > key) {
            pos = binary_search_af(a, begin, begin + n / 2, key);
        } else if(a[begin + n/2] <= key) {
            pos = binary_search_af(a, begin + n/2 + 1, end, key);
            if(pos == -1) pos = begin + n/2;
        }

        return pos;
    }
    //2.并不能做到Ω=n*log2(n),实际还是Ω=n^2
    public static int binary_search_af_mv(int a[], int begin, int end, int key) {

        int n = end - begin;
        if(n == 0) return -1;
        if(n == 1) {
            if(a[begin]<=key) return begin;
            else {
                a[begin+1] = a[begin];
                return -1;
            }
        }

        int pos = -1;
        if(a[begin + n/2] > key) {
            for(int k=end; k>(begin + n/2); k--) {
                a[k] = a[k-1];
            }
            pos = binary_search_af_mv(a, begin, begin + n / 2, key);
        } else if(a[begin + n/2] <= key) {
            pos = binary_search_af_mv(a, begin + n/2 + 1, end, key);
            if(pos == -1) pos = begin + n/2;
        }

        return pos;
    }
    public static void insertion_sort_binary(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int j = 1; j<a.length; j++) {
            int key = a[j];
            int pos = binary_search_af_mv(a, 0, j, key);
            a[pos+1] = key;
        }

    }


    //练习2.3-7
    //1.先归并排序，再二分查找
    //2.n个元素，任取两个元素: C(2,n)=n*(n-1)/2
    //3.todo


    public static void main(String argv[]) {

        int[] a = {1,2,2,3,4,4,5,6};

        int pos = binary_search_af(a, 3);
        System.out.println(pos);
        System.out.println("#####");

        int[] b = {3,6,1,2,9,2,4,7,1};
        insertion_sort_binary(b);
        Arrays.stream(b).forEach(System.out::println);
    }

}
