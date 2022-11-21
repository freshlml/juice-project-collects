package com.juice.alg.chapter3_11;

import java.util.NoSuchElementException;

public class Chapter3_11_2 {

    /**
     *散列表
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
     *  hash冲突: hash(key1) == hash(key2)，通常链指针法
     *
     * 3. 分析
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
        private static final int DEFAULT_CAPACITY = 8;
        private static final int MAX_CAPACITY = Integer.MAX_VALUE;
        private float loadFactor = 0.75f;
        private Node[] table;

        public HashTable() {
            table = new Node[DEFAULT_CAPACITY];
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
                if(node != null) {
                    int i2 = hash(node.key, newSize);
                    newTable[i2] = node;
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


}
