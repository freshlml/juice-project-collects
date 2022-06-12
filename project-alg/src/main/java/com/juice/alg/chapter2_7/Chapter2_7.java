package com.juice.alg.chapter2_7;

import java.util.Arrays;

public class Chapter2_7 {
    /**
     *快速排序
     * 基本思想(分治): 给定数组a[p,...,r)，如果下标q满足 a[p,...,q)  <= a[q] <= a[q+1,..,r), 则下标q位置有序
     *              递归处理子数组a[p,...,q),a[p+1,..,r)
     * 如何求q？
     * 1.给定数组a，每次q可以有多个，一个，或者没有(情况居多)
     *   一个有序的数组==>所有位置都是q; 所有位置都是q==>则一个数组有序
     *   通过find q的方法，则q不存在时怎么办？ findFirstOrNull？？
     *
     * 2.给定数组a，第j位置是第j小，则该位置是q
     *   通过若干次交换使第j位置是第j小，运行效率？？
     *
     * 3.给定数组a，需要在仅遍历一次的情况下，求出q
     *
     */
    //对于给定的数组a，找到第一个p下标(可能不存在)
    public int findFirstOrNull(int a[]) {
        if (a == null || a.length == 0) return -1;

        int max = 0;
        int k = 0;
        int i = k + 1;
        while(i < a.length) {

            while(i < a.length && a[k] <= a[i]) {
                if(a[max] <= a[i]) {
                    max = i;
                }
                i++;
            }
            if(i == a.length) break;

            i++;
            while(i < a.length && a[i] < a[max]) {
                i++;
            }
            if(i == a.length) { k=-1; break; }

            k = i;
            max = i;
            i++;
        }
        return k;
    }
    //使第q位置成为第q小
    //q二分时，最坏情况n/2*n,too low
    public void partition_pos(int a[], int begin, int end, int q) {
        boolean out = false;
        int times = 1;
        while(!out) {
            out = true;
            boolean t = false;

            int pos = q;
            for(int i=q-1; i>=begin; i--) {
                if(a[i] > a[pos]) pos = i;
            }
            if(pos != q) {
                int ex = a[pos];
                a[pos] = a[q];
                a[q] = ex;
                t = true;
            }

            if(times == 1 || t) {
                pos = q;
                for(int j=q+1; j<end; j++) {
                    if(a[j] < a[pos]) pos = j;
                }
                if(pos != q) {
                    int ex = a[pos];
                    a[pos] = a[q];
                    a[q] = ex;
                    out = false;
                }
            }

            times++;
        }
        System.out.println(q + ",运行" + (times-1) + "次");
    }
    //只遍历一遍，求出q
    public int partition(int a[], int begin, int end) {
        int i = begin;
        int j = end - 1;
        int q = j;

        while(i < j) {
            while (i <= j - 1 && a[i] <= a[j]) {
                i++;
            }
            if (i == j) break;

            int ex = a[i];
            a[i] = a[j];
            a[j] = ex;

            q = i;
            j--;
            while (j >= i + 1 && a[j] >= a[i]) {
                j--;
            }
            if (j == i) break;

            ex = a[i];
            a[i] = a[j];
            a[j] = ex;

            q = j;
            i++;
        }
        return q;
    }
    //只遍历一遍，求出q
    //全相等/有序 -->   i+1=end-1
    //i+1=end-1 --/-> 全相等/有序; "i+1=end-1 <--> end-1处是最大值"
    //逆序       -->   i+1=begin
    //i+1=begin --/-> 逆序;       "i+1=begin <--> end-1处是最小值"
    public int partition_book(int a[], int begin, int end) {
        int i = begin-1;
        int j = begin;
        int k = a[end-1];

        int prev = begin-1;
        boolean ordered = true;

        while(j < end-1) {
            if(a[j] <= k) {  //逆序: >=
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
        if(ordered) throw new RangeOrderedException(begin, end, i+1);
        //if(i+1 == end-1) { System.out.println("end-1处是最大值"); }
        //if(i+1 == begin) { System.out.println("end-1处是最小值"); }
        a[end-1] = a[i+1];
        a[i+1] = k;
        return i+1;
    }
    //快速排序
    public void quick_sort(int a[]) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        quick_sort(a, 0, a.length);
    }
    public void quick_sort(int a[], int begin, int end) {

        if((end - begin) <= 1) return;

        int q;
        try {
            q = partition_book(a, begin, end);
        } catch (RangeOrderedException ro) {
            //System.out.println(ro.getMessage());
            return;
        }
        quick_sort(a, begin, q);
        quick_sort(a, q + 1, end);

    }


    public static void main(String argv[]) {
        Chapter2_7 chapter2_7 = new Chapter2_7();

        /*int[] a = new int[]{3, 2, 1, 4};
        System.out.println(chapter2_7.findFirstOrNull(a));
        System.out.println("---------------");

        a = new int[]{3987, 242342, 1978, 44324232, 489, 500, 110, 343, 100, 45, 56, 23, 1, 4, 3, 78};
        chapter2_7.partition_pos(a, 0, a.length, a.length/2);
        System.out.println(Arrays.toString(a));
        System.out.println("-------------");*/

        int[] a = new int[]{100, 45, 56, 23, 1, 4, 3, 78, 3987, 242342, 1978, 44324232, 489, 500, 110, 343};
        System.out.println("q = " + chapter2_7.partition(a, 0, a.length));
        System.out.println(Arrays.toString(a));
        System.out.println("------partition-------");

        a = new int[]{100, 45, 56, 23, 1, 4, 3, 78, 3987, 242342, 1978, 44324232, 489, 500, 110, 343};
        System.out.println("q = " + chapter2_7.partition_book(a, 0, a.length));
        System.out.println(Arrays.toString(a));
        System.out.println("------partition_book1-------");

        a = new int[]{1, 2, 3, 3, 5};
        try {
            int q = chapter2_7.partition_book(a, 0, a.length);
            System.out.println("q = " + q);
        } catch (RangeOrderedException ro) {
            System.out.println(ro.getMessage());
            System.out.println("q = " + ro.getQ());
        }
        System.out.println(Arrays.toString(a));
        System.out.println("------partition_book2-------");

        a = new int[]{1, 1, 3, 4, 5, 6, 888, 123, 123, 999};
        chapter2_7.quick_sort(a);
        System.out.println(Arrays.toString(a));
        System.out.println("------quick_sort-------");

    }

}
