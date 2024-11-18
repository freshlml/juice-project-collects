package com.juice.alg.part2.chapter7;

import java.util.Arrays;
import com.juice.alg.part2.chapter7.Chapter7_Practice1.RangeOrderedException;

public class Chapter7_Practice2 {
    /*int[] a = new int[]{3, 2, 1, 4};
        System.out.println(chapter2_7.findFirstOrNull(a));
        System.out.println("---------------");

        a = new int[]{3987, 242342, 1978, 44324232, 489, 500, 110, 343, 100, 45, 56, 23, 1, 4, 3, 78};
        chapter2_7.partition_pos(a, 0, a.length, a.length/2);
        System.out.println(Arrays.toString(a));
        System.out.println("-------------");*/

        /*int[] a = new int[]{100, 45, 56, 23, 1, 4, 3, 78, 3987, 242342, 1978, 44324232, 489, 500, 110, 343};
        System.out.println("q = " + chapter_7.my_hoare_partition(a, 0, a.length));
        System.out.println(Arrays.toString(a));
        System.out.println("------my_hoare_partition-------");*/


    /*
     * 如何求q？
     * 1.给定数组a，每次q可以有多个，一个，或者没有(情况居多)
     *   一个有序的数组==>所有位置都是q; 所有位置都是q==>则一个数组有序
     *   通过find q的方法，则q不存在时怎么办？ findFirstOrNull？？
     *
     * 2.给定数组a，第j位置是第j小，则该位置是q
     *   通过若干次交换使第j位置是第j小，运行效率？？
     */
    //对于给定的数组a，找到第一个q下标(可能不存在)
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
    //hoare_partition的翻版
    public int my_hoare_partition(int a[], int begin, int end) {
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


    public static void main(String[] argv) {

        int[] a = new int[]{13, 19, 9, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //a = new int[]{13, 1, 2, 3};
        //a = new int[]{1, 2, 3, 13};
        System.out.println(hoare_partition(a, 0, a.length));
        System.out.println(Arrays.toString(a));
        System.out.println("-------------7-1-------------");

        a = new int[]{13, 19, 9, 5, 12, 8, 7, 4, 12, 2, 6, 12};
        Chapter7_Practice2 chapter_7_Practice_2 = new Chapter7_Practice2();
        try {
            int[] qt = chapter_7_Practice_2.range_partition(a, 0, a.length);
            System.out.println("[q=" + qt[0] + ", t=" + qt[1] + ")");
            System.out.println(Arrays.toString(a));
        } catch (RangeOrderedException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("------------7-2-partition------------");

        a = new int[]{13, 19, 9, 5, 12, 8, 7, 4, 12, 2, 6, 12};
        chapter_7_Practice_2.range_quick_sort(a);
        System.out.println(Arrays.toString(a));
        System.out.println("------------7-2-sort------------");


    }
}
