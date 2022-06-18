package com.juice.alg.chapter2_7;

import java.util.Arrays;

public class Chapter2_7_2 {

    public static void main(String argv[]) {

        int[] a = new int[]{13, 19, 9, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //a = new int[]{13, 1, 2, 3};
        //a = new int[]{1, 2, 3, 13};
        System.out.println(hoare_partition(a, 0, a.length));
        System.out.println(Arrays.toString(a));
    }

    //思考题7-1
    //b. 存在越界问题
    public static int hoare_partition(int a[], int p, int r) {
        int x = a[p];
        int i = p-1;
        int j = r;

        while (true) {

            do {
                j--;
            } while(a[j] >= x); // j<p 越界

            do {
                i++;
            } while(a[i] <= x); // i>=r 越界

            if(i < j) {
                int ex = a[i];
                a[i] = a[j];
                a[j] = ex;
            } else {
                break;
            }
        }

        a[p] = a[j];
        a[j] = x;
        return j;
    }




}
