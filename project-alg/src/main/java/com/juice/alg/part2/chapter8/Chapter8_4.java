package com.juice.alg.part2.chapter8;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;

public class Chapter8_4 {

    /**
     *桶排序思想
     * 桶:  0   1   2   3   4   5 ... k-1   k
     *     | | | | | | | | | | | |   |  |  | |
     * n个数的序列 "映射" 桶下标
     * 1.桶排序的关键是如何确定k的大小
     * 2.确定映射关系，映射需要满足：大的数映射的桶下标不小于小的数映射的桶下标，hash散列就不适合
     * 3.相同桶下标中数需要排序，如可以是链表结构在插入时排序
     *
     *
     *计数排序中应用桶思想
     * 1.k的大小是确定的
     * 2.桶下标映射 {@link Chapter8_2#indexPos(int, int)}，满足
     * 3.√
     *
     *
     *A[n],A中任意一个数A[j]∈[0,1)，应用桶排序
     * 1.将区间均等分成n份，得到n个区间: [0, 1/n), [1/n, 2/n), ... ,[n-1/n, 1)，k=n       ;@see 区间均等划分的过程
     * 2.桶下标映射: A[j]*n
     * 3.√
     * 最快情况运行时间：所有数落在一个桶中，退化成插入排序
     * 平均情况运行时间：
     *      1.假设A[n]满足[0，1)上的均匀分布，则对A[n]中任意一个数A[j]落在i号桶的概率=1/n
     *      2.随机变量ni: i号桶中元素数量
     *        Σi(0,n-1)[ni^2]; 求期望E( Σi(0,n-1)[ni^2] ) = 2 - 1/n
     *      3.平均情况运行时间O(n)
     * 期望运行时间：真实情况是A[n]不一定满足[0，1)上的均匀分布
     *      1.只要保证ni^2与n成线性关系，桶排序仍然线性？
     *
     *
     *区间均等划分的过程，[0, 1)区间
     * [0, t), [t, 2t), [2t, 3t), ... , [(m-1)t, mt)，共m个区间
     * mt=1，t=1/m ==> [0, 1/m), [1/m, 2/m), [2/m, 3/m), ... , [(m-1)/m, m/m) ; 桶下标映射：t*m
     *
     * 令m=n，共n个区间
     *      [0, 1/n), [1/n, 2/n), [2/n, 3/n), ... , [(n-1)/n, n/n)
     *      桶下标应映射 A[j] * n
     *
     * 或者令m=根号n，共根号n个区间
     *      [0, 1/根号n), [1/根号n, 2/根号n), [2/根号n, 3/根号n), ... , [(根号n-1)/根号n, 根号n/根号n)
     *      桶下标应映射 A[j] * 根号n
     */

    public float[] bucket_sort(float[] a) {
        if(a == null) return null;
        if(a.length == 0 || a.length == 1) return a;

        int n = a.length;
        Node[] bucket = new Node[n];
        for(int j=0; j<n; j++) {
            int idx = (int) a[j] * n;
            Node nn = new Node(a[j], null);
            Node p;
            if((p = bucket[idx]) == null) {
                bucket[idx] = nn;
            } else if(p.getKey() > nn.getKey()) {
                bucket[idx] = nn;
                nn.setNext(p);
            } else {
                while(p.getNext() != null && p.getNext().getKey() <= nn.getKey()) { //error: 浮点数的大小比较
                    p = p.getNext();
                }
                Node ct = p.getNext();
                p.setNext(nn);
                nn.setNext(ct);
            }
        }

        float[] b = new float[a.length];
        for(int k=0, j=0; k<bucket.length; k++) {
            Node p;
            for(p = bucket[k]; p != null; ) {
                b[j++] = p.getKey();
                p = p.getNext();
            }
        }

        return b;

    }

    //练习8.4-3: E[X*Y] = E[X]*E[Y];X,Y独立
    //          E^2[X] = E[X^2] - 方差

    //练习8.4-4
    /*
    区间划分                                     根号m * r1 = 1 ==> r1 = 1/根号m             下标映射
    [0, r1)                      1号区间         [0, 1/根号m)                               (d * 根号m)^2 --> 0
    [r1, 根号2 * r1)              2号区间         [1/根号m, 根号2/根号m)                       (d * 根号m)^2 --> 1
    [根号2 * r1, 根号3 * r1)       3号区间         [根号2/根号m, 根号3/根号m)                    (d * 根号m)^2 --> 2
    [根号3 * r1, 根号4 * r1)       4号区间         [根号3/根号m, 根号4/根号m)                    (d * 根号m)^2 --> 3
    ...
    [根号m-1 * r1, 根号m * r1)     m号区间         [根号m-1/根号m, 根号m/根号m)                  (d * 根号m)^2 --> m-1

    可以令 m=n, 共n个区间
        [0, 1/根号n), [1/根号n, 根号2/根号n), [根号2/根号n, 根号3/根号n), ... , [根号n-1/根号n, 根号n/根号n) ; 下标映射 (d * 根号n)^2

    或者令 m=n^2, 共n^2个区间
        [0, 1/n), [1/n, 根号2/n), [根号2/n, 根号3/n), ... , [根号n-1/n, 根号n/n) ; 下标映射 (d * n)^2

     */

    //练习8.4-5 todo

    public int[] bucket_sort_for_counting(int[] a, int begin, int end) {
        if(a == null) return null;
        if(a.length == 0 || a.length == 1) return a;

        //assert begin < end

        Counting_Node[] bucket = new Counting_Node[end - begin];

        for(int i=0; i<a.length; i++) {
            int idx = counting_index(begin, a[i]);

            Counting_Node p;
            Counting_Node nn = new Counting_Node();
            nn.setKey(a[i]).setTail(nn);
            if((p = bucket[idx]) == null) {
                bucket[idx] = nn;
            } else {
                p.getTail().setNext(nn);
                p.setTail(nn);
            }
        }

        int[] b = new int[a.length];
        for(int k=0, j=0; k<bucket.length; k++) {
            Counting_Node p;
            for(p = bucket[k]; p != null; ) {
                b[j++] = p.getKey();
                p = p.getNext();
            }
        }

        return b;
    }
    public int counting_index(int begin, int ai) {
        if(begin == 0) return ai;
        else if(begin > 0) return ai - begin;
        else return ai + begin;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class Counting_Node {
        private int key;
        private Counting_Node next;
        private Counting_Node tail;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class Node {
        private float key;
        private Node next;
    }


    public static void main(String argv[]) {
        Chapter8_4 chapter2_8_4 = new Chapter8_4();

        int[] a = new int[]{6, 5, 5, 2, 1, 3, 6, 7};
        int[] b = chapter2_8_4.bucket_sort_for_counting(a, 0, 10);
        Arrays.stream(b).forEach(System.out::println);


    }

}
