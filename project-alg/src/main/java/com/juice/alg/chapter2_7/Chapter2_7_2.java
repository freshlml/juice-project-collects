package com.juice.alg.chapter2_7;

import java.util.Arrays;

public class Chapter2_7_2 {

    public static void main(String argv[]) {

        int[] a = new int[]{13, 19, 9, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //a = new int[]{13, 1, 2, 3};
        //a = new int[]{1, 2, 3, 13};
        System.out.println(hoare_partition(a, 0, a.length));
        System.out.println(Arrays.toString(a));
        System.out.println("-------------7-1-------------");

        a = new int[]{13, 19, 9, 5, 12, 8, 7, 4, 12, 2, 6, 12};
        Chapter2_7_2 chapter2_7_2 = new Chapter2_7_2();
        try {
            int[] qt = chapter2_7_2.range_partition(a, 0, a.length);
            System.out.println("[q=" + qt[0] + ", t=" + qt[1] + ")");
            System.out.println(Arrays.toString(a));
        } catch (RangeOrderedException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("------------7-2-partition------------");

        a = new int[]{13, 19, 9, 5, 12, 8, 7, 4, 12, 2, 6, 12};
        chapter2_7_2.range_quick_sort(a);
        System.out.println(Arrays.toString(a));
        System.out.println("------------7-2-sort------------");


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


    //思考题7-2
    //a: n^2
    //b: 序列中和主元相等的值无需参与后续递归
    int[] range_partition(int a[], int begin, int end) {
        int i = begin-1;
        int l = i;
        int j = begin;
        int k = a[end-1];  //主元

        int prev = begin-1;
        boolean ordered = true;

        while(j < end) {

            if(a[j] == k) {
                l++;
                int ex = a[l];
                a[l] = a[j];
                a[j] = ex;
            } else if(a[j] < k) {
                int ex = a[j];
                l++;
                a[j] = a[l];
                i++;
                a[l] = a[i];
                a[i] = ex;
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
        return new int[]{i+1, l+1};
    }
    //c:
    void range_quick_sort(int a[]) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        range_quick_sort(a, 0, a.length);
    }
    void range_quick_sort(int a[], int begin, int end) {
        if((end - begin) <= 1) return;

        int[] q;
        try {
            q = range_partition(a, begin, end);
        } catch (RangeOrderedException ro) {
            //System.out.println(ro.getMessage());
            return;
        }
        range_quick_sort(a, begin, q[0]);
        range_quick_sort(a, q[1], end);
    }
    //d: range_partition返回的下标范围不参与后续的递归
    // 序列中i与j(i<j)比较一次的概率 P(Xij=1) = 2/(j-i+1), ?todo


    //思考题7-4: c: partition返回的值满足划分比1-α:α
    //             导致更大的系数，但最坏情况下递归深度减小，todo


    //思考题7-5: 均匀随机的选择主元，每一个位置被选择的概率为1/n
    //a: P(i) = C(1, 1) * C(i-1, 1) * C(n-i, 1) / C(n, 3)
    //b: 增加了大约1.5倍


    //思考题7-6: 区间的模糊排序，todo

}
