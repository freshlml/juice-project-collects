package com.juice.alg.part2.chapter7;


import com.juice.alg.part1.chapter2.Chapter2_Practice.PositionArrayPrinter;
import com.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;
import com.juice.alg.part1.chapter4.Chapter4_1.SubArrayNode;
import com.juice.alg.part1.chapter4.Chapter4_1.SubArrayNodeArrayPrinter;
import com.juice.alg.part2.chapter7.Chapter7_Practice1.RangeOrderedException;

public class Chapter7_Practice2 {

    //思考题7-1
    static int hoare_partition(int[] a, int p, int r) { //规模为 n 的数组，运行时间为 Θ(n)
        int x = a[p];
        int i = p - 1;
        int j = r;

        while (true) {

            do {
                j--;
            } while(a[j] >= x); // 下标 j 缺少越界判断，即 j >= p

            do {
                i++;
            } while(a[i] <= x); // 下标 i 缺少越界判断，即 i < r

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
    //hoare_partition 的翻版
    static int hoare_partition_ext(int[] a, int begin, int end) { //规模为 n 的数组，运行时间为 Θ(n)
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
    //找出第一个位置 q, 使得 ∀i < q, a[i] <= a[q]
    //                   ∀j > q, a[q] <= a[j]
    //  给定数组 a，q 可以有多个、一个、或者没有(情况居多)
    static int findFirstOrNull(int[] a) {
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
            if(i == a.length) { k = -1; break; }

            k = i;
            max = i;
            i++;
        }
        return k;
    }
    //使第 q 位置成为第 q 小
    //q = (end - begin)/2 时，最坏情况: (n/2)*n, too low
    static int partition_pos(int[] a, int begin, int end, int q) {
        boolean out = false;
        boolean t;
        int count = 0;
        while(!out) {
            out = true;
            t = false;

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

            if(count == 0 || t) {
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

            count++;
        }
        return count;
    }

    //思考题7-2
    //b:
    static int[] range_partition(int[] a, int begin, int end) {
        int i = begin - 1;
        int l = i;
        int j = begin;
        int k = a[end - 1];

        int prev = begin - 1;
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
        return new int[] {i+1, l+1};
    }
    //c:
    static void range_quick_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        range_quick_sort(a, 0, a.length);
    }
    static void range_quick_sort(int[] a, int begin, int end) {
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
    //d: range_partition 返回的下标范围不参与后续的递归
    //  P’(Y<i,j> = 1) = 2/(j-i+1 + k1 + k2), 其中 k1 表示除 i, j 外与 a[i] 相等的元素的个数，0<=k1<=n-2.
    //                                        k2 表示除 i, j 外与 a[j] 相等的元素的个数，0<=k2<=n-2.
    //  P’(Y<i,j> = 1) <= P(Y<i,j> = 1), 因此，运行时间只少不多


    public static void main(String[] argv) {
        int[] a = new int[] {13, 19, 9, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //a = new int[] {13, 1, 2, 3};
        //a = new int[] {1, 2, 3, 13};
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(0)::print);
        int q1 = hoare_partition(a, 0, a.length);
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(q1)::print);
        System.out.println("------hoare_partition--------------------------------------------------------");

        a = new int[] {100, 45, 56, 23, 1, 4, 3, 78, 3987, 242342, 1978, 44324232, 489, 500, 110, 343};
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(a.length - 1)::print);
        int q2 = hoare_partition_ext(a, 0, a.length);
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(q2)::print);
        System.out.println("------hoare_partition_ext----------------------------------------------------");

        a = new int[] {3, 2, 1, 4};
        int q3 = findFirstOrNull(a);
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(q3)::print);
        System.out.println("---------------------------------------------------------------------------");

        a = new int[] {3987, 242342, 1978, 44324232, 489, 500, 110, 343, 100, 45, 56, 23, 1, 4, 3, 78};
        int count = partition_pos(a, 0, a.length, a.length/2);
        System.out.println("使第 " + a.length/2 + " 位置成为第 " + a.length/2 + " 小");
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(a.length/2)::print);
        System.out.println("数组长度: " + a.length + ", 运行次数: " + count);
        System.out.println("-------------------------------------------------------------------------");


        a = new int[] {13, 19, 9, 5, 12, 8, 7, 4, 12, 2, 6, 12};
        try {
            IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(a.length - 1)::print);
            int[] qt = range_partition(a, 0, a.length);
            SubArrayNode<Integer> subArrayNode = new SubArrayNode<>(a[qt[0]], qt[0], qt[1], () -> "duplicate");
            IntArrayTraversal.of(a).forEach(SubArrayNodeArrayPrinter.<Integer, Integer> of(subArrayNode)::print);
        } catch (RangeOrderedException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("------range_partition---------------------------------------------------");

        IntArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
        range_quick_sort(a);
        IntArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
        System.out.println("------range_quick_sort--------------------------------------------------");

    }

    //思考题7-4: c: 使 partition 返回的值满足划分比 1-α:α
    //             导致更大的系数，但最坏情况下递归深度减小，todo


    //思考题7-5: 均匀随机的选择主元，每一个位置被选择的概率为1/n
    //a: P(i) = C(1, 1) * C(1, i-1) * C(1, n-i) / C(n, 3)
    //b: 增加了大约 1.5 倍

    //思考题7-6: 区间的模糊排序，todo
}
