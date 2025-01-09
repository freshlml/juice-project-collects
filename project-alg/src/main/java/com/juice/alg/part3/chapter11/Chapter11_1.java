package com.juice.alg.part3.chapter11;

import java.util.NoSuchElementException;
import java.util.Objects;

public class Chapter11_1 {

    /**
     *直接寻址表
     *  关键字的全域 U 为 [m, n)。其中 m < n ∈ Z。创建一个长度为 n-m 的数组, 该数组的下标和全域 U 中每一个关键字一一对应。
     *  现有一个序列, 其元素的关键字取自全域 U。序列中元素即可直接寻址（一个简单的下标运算函数）找到对应的数组下标。
     *
     *  若全域 U 很大, 则该数组需要占用更多的空间。
     *  如果一个序列中元素只取全域 U 中很小一部分的值, 则数组中实际存储的关键字数目比关键字总数小得多, 数组的多数空间是浪费的。
     *
     */
    private static class DirectAddressTable<V> {
        private final Node<V>[] table;
        private final int from, to;
        private int size;

        /**
         * 构造一个直接寻址表
         * @param from 全域 U 的起始数值, inclusive
         * @param to 全域 U 的终止数值, exclusive
         */
        @SuppressWarnings("unchecked")
        DirectAddressTable(int from, int to) {
            if(from >= to) throw new IllegalArgumentException("from = " + from + " must less than to = " + to);
            //assert to-from <= the allowed max array size
            this.from = from;
            this.to = to;
            this.table = new Node[to - from];
        }

        /**
         * 写入指定元素，如果发生碰撞，直接链接起来
         * @param key 关键字。[from, to)
         * @param v 值
         * @throws ArrayIndexOutOfBoundsException 如果关键字 key 不属于全域 U
         */
        public void insert(int key, V v) {
            Node<V> added = new Node<>(key, v);
            int idx = mapping(key);
            Node<V> e = table[idx];
            table[idx] = added;
            added.next = e;
            size++;
        }

        /**
         *
         * @param key 关键字。[from, to)
         * @param v value
         * @return 关键字对应的值
         * @throws ArrayIndexOutOfBoundsException 如果关键字 key 不属于全域 U
         * @throws NoSuchElementException 如果直接寻址表中不包含关键字 key
         */
        public V search(int key, V v) {
            Node<V> e = element(key);
            while(e != null && !Objects.equals(e.v, v)) {
                e = e.next;
            }
            if(e == null) throw new NoSuchElementException("直接寻址表中不包含关键字 key = " + key);
            return e.v;
        }

        //ArrayIndexOutOfBoundsException 如果关键字 key 不属于全域 U
        private Node<V> element(int key) {
            return table[mapping(key)];
        }

        /**
         *
         * @param key 关键字。[from, to)
         * @return 关键字对应的值
         * @throws ArrayIndexOutOfBoundsException 如果关键字 key 不属于全域 U
         * @throws NoSuchElementException 如果直接寻址表中不包含关键字 key
         */
        public V delete(int key, V v) {
            int idx = mapping(key);
            Node<V> e = table[idx], pe = null;
            while(e != null && !Objects.equals(e.v, v)) {
                pe = e;
                e = e.next;
            }
            if(e == null) throw new NoSuchElementException("直接寻址表中不包含关键字 key = " + key);
            if(pe == null) {
                table[idx] = e.next;
            } else {
                pe.next = e.next;
            }
            size--;
            return e.v;
        }


        //练习11.1-1
        //  最坏情况下: T(n) = Θ(n)
        public int maxKey() {
            Node<V> max = findMax(from, to);
            if(max == null) throw new NoSuchElementException("没有最大值");

            return max.key;
        }

        private Node<V> findMax(int p, int q) {
            int n = q - p;
            if(n <= 0) return null;

            int mid = p + n/2;
            Node<V> current = table[mid];
            Node<V> right = findMax(mid + 1, q);

            if(right != null) {
                return right;
            } else if(current != null) {
                return current;
            } else {
                return findMax(p, mid);
            }
        }

        //将关键字映射到数组下标
        private int mapping(int key) {
            return key - this.from;
        }

        static class Node<V> {
            int key;   //关键字∈全域 U. 也可以是其他类型, 但需要额外的一层转换, 使之属于全域 U.
            V v;
            Node<V> next;

            public Node(int key, V v) {
                this.key = key;
                this.v = v;
            }
        }
    }

    //练习11.1-2
    /*
    下标  0  1  2  3  4  5  6  7  8  9  10  11
    存值  0  1  1  0  1  1  1  0  0  1   0   0
         ↑  ↑  ↑     ↑     ↑        ↑
    元素  0  1  2     3     4        5

    起始下标计算，如 5（101）
        5 需要三个位置存储，同样 4 需要 3 个位置存储，3 需要两个位置存储，以此类推
        则 5 的起始下标为 1 + 1 + 2 + 2 + 3 = 9
     */

    /**
     *关于 "散列表"、"哈希映射表"、"映射表" 这几种说法
     *  "散列表" 一般就是指 "哈希映射表", 是采用 hash 散列函数完成映射的 "映射表"
     *
     *映射表: 将序列中元素通过 "映射函数" 映射到 "桶" 之中。一个映射表有以下几个方面需要关注:
     *  1. 映射函数
     *  2. 桶
     *  3. 碰撞: 多个元素映射到同一个桶
     */
    @SuppressWarnings("unused")
    static void mapping() {}
}
