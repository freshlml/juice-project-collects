package com.juice.alg.part2.chapter7;


import com.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;
import com.juice.alg.part1.chapter2.Chapter2_Practice.PositionArrayPrinter;

import java.util.Arrays;


public class Chapter7_1 {
    /**
     *快速排序
     *  基本思想(分治): 给定数组 a[p, ..., r)，如果下标 q 满足: a[p, ..., q) <= a[q] <= a[q+1,..,r), 则下标 q 位置有序
     *               递归处理子数组 a[p, ..., q), a[q+1, ..., r)
     *  给定数组求 q 的方法: (仅遍历一遍)
     *    partition(...)
     */
    public static int partition(int[] a, int p, int r) {  //规模为 n 的数组，运行时间为 Θ(n)
        //assert a!= null;
        //assert p ∈ [0, a.length); assert r ∈ [0, a.length); assert p < r;
        int pos = r - 1;
        int e = a[pos];
        int j = p - 1, k = p;

        while(k < pos) {
            if(a[k] <= e) {
                j++;

                int ex = a[j];
                a[j] = a[k];
                a[k] = ex;
            }
            k++;
        }

        if(++j < pos) {
            int ex = a[j];
            a[j] = e;
            a[pos] = ex;
        }
        return j;
    }
    //快速排序
    public static void quick_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        quick_sort(a, 0, a.length);
    }
    public static void quick_sort(int[] a, int begin, int end) {
        //assert a != null
        //assert begin ∈ [0, a.length); assert end ∈ [0, a.length);
        if((end - begin) <= 1) return;

        int q = partition(a, begin, end);

        quick_sort(a, begin, q);
        quick_sort(a, q + 1, end);
    }

    public static void main(String[] argv) {

        int[] a = new int[] {100, 45, 56, 23, 1, 4, 3, 78, 3987, 242342, 1978, 44324232, 489, 500, 110, 343};
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(a.length - 1)::print);
        int q = partition(a, 0, a.length);
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(q)::print);
        System.out.println("------partition---------------------------------------------------------------");

        IntArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
        System.out.println();
        quick_sort(a);
        IntArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
        System.out.println("------quick_sort--------------------------------------------------------------");


        Node[] nodes = new Node[]{
                new Node(94, 1),
                new Node(5, 1),
                new Node(0, 1),
                new Node(3, 1),
                new Node(1, 1),
                new Node(5, 2),
                new Node(84, 1),
                new Node(3, 2)
        };
        partition_node(nodes, 0, nodes.length);
        System.out.println(Arrays.toString(nodes));  //Node{5,2} 到 Node{5,1}前面了，这样将导致 quick_sort 不稳定
        System.out.println("-------partition不是稳定的------------------------------------------------------");
        //注意: 被选作主元的Node{3,2}，相对于Node{3,1}是稳定的

        quick_sort_node(nodes);
        //[Node{0,1}, Node{1,1}, Node{3,1}, Node{3,2}, Node{5,2}, Node{5,1}, Node{84,1}, Node{94,1}]
        //System.out.println(Arrays.toString(nodes));  //Node{5,2} 到 Node{5,1}前面了，quick_sort不稳定

    }

    //partition 不是稳定的
    private static class Node {
        int value;
        int tag;

        Node(int value, int tag) {
            this.value = value;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return "Node{" + value + "," + tag + '}';
        }
    }
    public static int partition_node(Node[] a, int begin, int end) {
        int i = begin - 1;
        int j = begin;
        int k = a[end - 1].value;

        while(j < end) {
            if(a[j].value <= k) {
                i++;
                Node ex = a[i];
                a[i] = a[j];
                a[j] = ex;
            }
            j++;
        }
        return i;
    }
    public static void quick_sort_node(Node[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        quick_sort_node(a, 0, a.length);
    }
    public static void quick_sort_node(Node[] a, int begin, int end) {
        if((end - begin) <= 1) return;

        int q = partition_node(a, begin, end);

        quick_sort_node(a, begin, q);
        quick_sort_node(a, q + 1, end);
    }

}
