package com.juice.alg.part2.chapter6;

import com.juice.alg.part1.chapter2.Chapter2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chapter6_Practice1 {
    //练习6.1-4: 位于叶子节点

    //练习6.5-9: k个有序链表合并成一个有序链表，时间复杂度O(n*lgk)
    /*
        [k 个链表]
        [k/2 个链表] [k/2 个链表]                               1    1 * n
        [k/4 个链表] [k/4 个链表] [k/4 个链表] [k/4 个链表]        2    2 * n/2
        ...
        [1 个链表] [1 个链表] ...                               x     2^(x-1) * n/2^(x-1)

        k/2^x = 1, k = 2^x, x = lgk

        1 * n + 2 * n/2 + ... + 2^(x-1) * n/2^(x-1) = n * lgk
     */
    public void merge_list(int[] a) {
        if(a == null || a.length == 0) return;

        List<Integer> k_list = build_k_list();
        check_k_list(k_list, a.length);
        if(k_list == null || k_list.size() == 0) return;

        merge_list(a, k_list);
    }
    public void merge_list(int[] a, List<Integer> k_list) {
        if (k_list.size() == 0) return;

        List<Integer> n_k_list = new ArrayList<>();
        //[0, k_list(0)) merge [k_list(0), k_list(1)); [k_list(1), k_list(2)) merge [k_list(2), k_list(3)); [k_list(3), n)
        //             [0, k_list(0)) merge [k_list(0), k_list(1)); [k_list(1), n);
        //             [0, k_list(0)) merge [k_list(0), n);
        for(int i=0, start=0; i<k_list.size(); ) {

            if (i + 1 < k_list.size()) {
                Chapter2.merge(a, start, k_list.get(i), k_list.get(i + 1));
                start = k_list.get(i+1);
                n_k_list.add(start);
                i = i+2;
            }
            else {
                Chapter2.merge(a, start, k_list.get(i), a.length);
                break;
            }
        }

        merge_list(a, n_k_list);

    }
    public void check_k_list(List<Integer> k_list, int n) {
        if(k_list == null || k_list.size() == 0) return;
        int size = k_list.size();

        if(k_list.get(0) < 0)
            k_list.set(0, 0);

        if(k_list.get(size-1) >= n)
            k_list.set(size-1, n-1);

        for(int i=size-1; i>0; i--) {
            if(k_list.get(i-1) > k_list.get(i))
                throw new RuntimeException("k_list异常");
        }

    }
    public List<Integer> build_k_list() {
        List<Integer> k_list = new ArrayList<>();
        k_list.add(3);
        k_list.add(6);
        k_list.add(11);
        k_list.add(20);
        return k_list;
    }

    public static void main(String[] argv) {
        Chapter6_Practice1 chapter6_Practice1 = new Chapter6_Practice1();

        int[] a = new int[]{1,2,3, 5,7,9, 2,3,4,5,6, 1,1,2,2,3,3,4,5,7, -5,-2,-1};
        chapter6_Practice1.merge_list(a);
        System.out.println(Arrays.toString(a));

    }

}
