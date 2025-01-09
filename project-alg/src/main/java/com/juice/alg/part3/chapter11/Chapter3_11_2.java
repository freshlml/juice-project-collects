package com.juice.alg.part3.chapter11;

import java.util.NoSuchElementException;

public class Chapter3_11_2 {

    /**
     *散列表(hash table)
     *  将元素通过 "映射函数" 映射到 "桶" 之中。
     *
     * table(桶):  0   1   2   3   4   5 ... m-1
     *            | | | | | | | | | | | |   |  |
     *
     * 1. capacity 与 load factor
     *  n: 元素个数
     *  m: 桶大小，等于table.length
     *  load factor = n/m
     *
     * 2. hash函数
     *  确定性: hash(key)始终返回相同的结果
     *  相等性: hash(key1) == hash(key2) where key1 == key2 (optional)
     *  设计指标: 4.分析 / 平均情况运行时间 / 假设: hash函数需要满足对n个元素中每一个元素等可能(或近似等可能)的选择m个槽中的任意一个槽
     *          例子1, 如果n个元素独立均匀的分布于[0, 1)中，那么hash函数可以是: key * m
     *          例子2, {@link HashTable#hash(int, int)}
     *          例子3, 如果知道关键字key满足全域U上的某分布，则通过hash函数调控使得key等可能的选择槽位
     *
     * 3. hash冲突
     *  hash(key1) == hash(key2)，通常链指针法
     *
     * 4. 分析
     *  最坏情况运行时间:
     *      O(n) 当n个元素都散列到同一个槽
     *
     *  平均情况运行时间:
     *      假设: hash函数对n个元素中每一个元素等可能的选择m个槽中的任意一个槽，且与其他元素被散列到什么位置无关(独立)
     *           记为 P(i) = 1/m, i=1, 2, ..., n
     *
     *      随机变量n(j): 表示table中下标为j的链表元素个数, j=0, 1, ..., m-1
     *      n(j)   0   1   2    ...    n
     *      P
     *
     *      P(0) = C(0, n) * (m-1)^n / m^n     = C(0, n) * (1-1/m)^n * (1/m)^0
     *      P(1) = C(1, n) * (m-1)^(n-1) / m^n = C(1, n) * (1-1/m)^(n-1) * (1/m)^1
     *      P(2) = C(2, n) * (m-1)^(n-2) / m^n = C(2, n) * (1-1/m)^(n-2) * (1/m)^2
     *      ...
     *      P(n) = C(n, n) * (m-1)^(n-n) / m^n = C(n, n) * (1-1/m)^(n-n) * (1/m)^n
     *
     *      二项分布, E[n(j)] = n/m = load factor
     *
     *      O(1 + n/m)
     *
     *
     *
     */
    interface Table {
        void put(int key, int value);
        int get(int key);
        int remove(int key);

        int size();
        boolean isEmpty();
    }

    static class HashTable implements Table {
        private int size;
        private static final int DEFAULT_CAPACITY = 12;
        private static final int MAX_CAPACITY = Integer.MAX_VALUE;
        private float loadFactor = 0.75f;
        private Node[] table;

        public HashTable() {
            this(DEFAULT_CAPACITY);
        }

        public HashTable(int initialCapacity) {
            if(initialCapacity < 0 || initialCapacity > MAX_CAPACITY)
                throw new IllegalArgumentException();

            table = new Node[initialCapacity];
        }

        @Override
        public void put(int key, int value) {
            int i = hash(key, this.size);
            Node e = this.table[i];
            Node newNode = new Node(key, value);
            if(e == null) {
                table[i] = newNode;
            } else {
                Node ne = e;
                while(ne != null && ne.key != key) {
                    ne = ne.next;
                }
                if(ne != null) {
                    ne.value = value;
                    return;
                } else {
                    newNode.next = e.next;
                    e.next = newNode;
                }
            }
            this.size++;

            if(this.size / this.table.length > loadFactor) {
                resize();
            }

        }

        private void resize() {
            int oldSize = this.table.length; //this.table.length != 0
            int newSize = oldSize * 2;

            if(newSize < 0 || newSize > MAX_CAPACITY) {
                newSize = MAX_CAPACITY;
            }

            if(newSize == oldSize) return;

            Node[] newTable = new Node[newSize];
            for(int i = 0; i < this.table.length; i++) {
                Node node = this.table[i];
                while(node != null) {
                    Node next = node.next;
                    node.next = null;

                    int i2 = hash(node.key, newSize);
                    if(newTable[i2] == null) {
                        newTable[i2] = node;
                    } else {
                        Node ne = newTable[i2];
                        while(ne != null && ne.key != node.key) {
                            ne = ne.next;
                        }
                        if(ne != null) {
                            ne.value = node.value;
                            return;
                        } else {
                            node.next = newTable[i2].next;
                            newTable[i2].next = node;
                        }
                    }
                    node = next;
                }
            }
            this.table = newTable;
        }

        @Override
        public int get(int key) {
            int i = hash(key, this.size);
            Node e = this.table[i];
            while(e != null && e.key != key) {
                e = e.next;
            }
            if(e == null) throw new NoSuchElementException();
            return e.value;
        }

        @Override
        public int remove(int key) {
            int i = hash(key, this.size);
            Node e = this.table[i];
            Node prev = null;
            while(e != null && e.key != key) {
                prev = e;
                e = e.next;
            }
            if(e == null) throw new NoSuchElementException();
            int v = e.value;
            if(prev != null) {
                prev.next = e;
            } else {
                table[i] = e.next;
            }

            this.size--;
            return v;
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.size == 0;
        }

        /*
        * Table: 0   1   2   3   4   5   6   7   8   9
        *        | | | | | | | | | | | | | | | | | | | |
        * 全域U:  {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        *         10,11,...,
        *         ...}
        * 如果 key: int (或者key: Object 的 hashCode(): int) 满足等可能取自全域U中任意一个值(或者, {0,10,...}, {1,11,...}... 这些集合的可能性之和相等)
        * 则，hash(key)将等可能的落在Table的任意一个槽位上
        *
         */
        private int hash(int key, int size) {
            return key % size;
        }

        class Node {
            int key;
            int value;
            Node next;

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
    }

    //思考题7-2
    /* n个槽的散列表，插入n个元素
     * 假设: hash函数对n个元素中每一个元素等可能的选择n个槽中的任意一个槽，且与其他元素被散列到什么位置无关(独立)
     *      记为 P(i) = 1/n, i=1, 2, ..., n
     * a:
     *   随机变量n(j): 编号为j的槽中链接元素的个数
     *   Q(n(j) = k) = C(k, n) * (1-1/n)^(n-k) * (1/n)^k
     *
     * 随机变量M: 各槽中所含关键字数的最大值
     * M   0   1    2    ...    n
     * P
     * P(0) = 0
     * P(k) = ?
     * P(n) = n/n^n
     *
     * b: P(k) <= n*Q(k)
     *
     * c: 斯特林公式，不懂，todo
     *
     * d,e, 公式太复杂，看不懂，todo
     *
     */


    //全域散列法，数论? todo
    //11.5，思考题11-4, todo

}
