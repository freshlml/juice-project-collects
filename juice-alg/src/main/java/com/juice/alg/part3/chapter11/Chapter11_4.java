package com.juice.alg.part3.chapter11;

import java.util.Objects;

public class Chapter11_4 {

    /**
     *一、开放寻址法
     *  当遇到冲突时，在散列表中"探查"，直到找到一个空槽来放置待插入的元素
     *
     *  对任意关键字 key，使用开放寻址法探查 m 次，可得其探查序列 <probe(key, 0), probe(key, 1), ..., probe(key, m-1)>，probe(key, i) ∈ [0, m), i = 0, 1, ..., m-1
     *
     *  长度为 m 的散列表，共 m! 种探查序列（假设探查序列不存在重复值），即 <0, 1, ..., m-1> 的全排列
     *
     *1、线性探查
     *  probe(key, i) = (h(key) + i) mod m. i = 0, 1, ..., m-1
     *
     *  对任意关键字 key，每一个槽位都可以被探查到，即探查序列不存在重复值
     *
     *  在探查过程中，当遇到空槽或者探查回到起点(探查了 m 次)时，即结束探查
     *
     *  共 m 种探查序列。不同 key 的探查序列重合度很高
     *
     *2、二次探查
     *  probe(key, i) = (h(key) + c1*i + c2*i^2) mod m. i = 0, 1, ..., m-1
     *
     *  对关键字 key，可能存在槽位无法被探查到，即探查序列出现重复值（通过合理选择 c1, c2, m 可以让探查序列不出现重复值）
     *
     *  在探查过程中，当遇到空槽或者最多探查 m 次时，即结束探查（注意: 1. 二次探查，不一定会回到起点; 2. 因为可能出现重复值，所以第 m + 1 和以后的探查还可能探查出非空的槽）
     *
     *  共 m 种探查序列。不同 key 的探查序列重合度降低了（相对于线性探查）
     *
     *3、双重散列探查
     *  probe(key, i) = (h(key) + i*h'(key)) mod m. i = 0, 1, ..., m-1
     *
     *  对关键字 key，为了能探查所有槽位，即探查序列不出现重复值，h'(key) 的值要与 m 互素
     *    - 有一种简便的方法使这个条件成立：m 取 2 的幂，并且 h'(key) 总产生奇数
     *    - 另一种方法是：m 为素数，并且 h'(key) 总返回小于 m 的整数
     *
     *  共 m^2 种探查序列。探查序列重合度进一步降低。不同的 key，h(key)、h'(key) 的值可能很不一样。使得不同 key 的探查序列是很多样的。它看起来非常接近均匀散列？
     *
     *
     *二、开放寻址法分析
     *  随机变量 X: 一次不成功查找（即要查找的 key 不存在于散列表）的探查次数     //循环执行次数等于探查次数。循环有两个出口: 成功查找、不成功查找(null)
     *  X   1  2  3  4 ... m
     *  P
     *
     *  事件 Ai 为第 i 次探查时，探查到的是一个已被占用的槽，P(Ai) = n/m
     *
     *  因为要查找的 key 不存在于散列表中，故而 X >= i 等价于 A1 ∩ A2 ... Ai-1
     *
     *  P(X >= i) = P(A1 ∩ A2 ... Ai-1)                                                // 01_高中数学-2_概率/概率/练习题C.2-5
     *            = P(A1) * P(A2|A1) * P(A3|A1∩A2) * ... * P(Ai-1|A1∩A2∩...∩Ai-2)
     *            = n/m * (n-1)/(m-1) * (n-2)/(m-2) * ... * (n-i+2)/(m-i+2)
     *           <= (n/m)^(i-1)
     *            = α^(i-1)                     //α = n/m
     *
     *  EX = Σ(i=1~m) i*P(X=i)                  // 01_高中数学-2_概率/随机变量/数学期望
     *     = Σ(i=2~m) P(X>=i)
     *    <= Σ(i=2~m) α^(i-1)
     *     = Σ(i=1~m-1) α^i
     *     = α + α^2 + ... + α^(m-1)
     *     = α(1-α^(m-1)) / (1-α)
     *     < 1/(1-α)
     *
     *  故，一次不成功查找的平均探查次数不多于 1/(1-α)
     *
     *  求一次成功查找的平均探查次数:
     *    如果 key 是第 i+1 个被插入的关键字，则对 key 的一次查找中，探查的次数不多于 1/(1-i/m)      // 注: 成功查找的探查次数小于等于不成功查找的探查次数
     *    现，对散列表中所有 n 个关键字求平均，得到一次成功查找的平均探查次数:                       //todo why?
     *       1/n * Σ(i=0~n-1) 1/(1-i/m) = 1/n * Σ(i=0~n-1) m/(m-i) = 1/α * ln(1/(1-α))
     *
     */
    @SuppressWarnings("unused")
    static class LinearProbingTable<K, V> {

        /*
         *线性探查散列表
         *  - cannot contain duplicate keys
         *  - non-ordered guarantee
         *  - permit null key and null value
         */
        static final int DEFAULT_CAPACITY = 16;
        static final int MAXIMUM_CAPACITY = 1 << 30;

        private Node<K, V>[] table;
        private int size;

        public LinearProbingTable() {
            this(DEFAULT_CAPACITY);
        }

        @SuppressWarnings("unchecked")
        public LinearProbingTable(int capacity) {
            if(capacity < 0 || capacity > MAXIMUM_CAPACITY)
                throw new IllegalArgumentException("the capacity must not be negative and must less or equal than " + MAXIMUM_CAPACITY);

            table = new Node[capacity];
        }

        /**
         * Return the value to which the specified key is mapped, or
         * return null if this map contains no mapping for the specified key, (or the specified key mapping to a `null` value).
         *
         * @param key the key whose associated value is to be returned
         * @return the value to which the specified key is mapped, or null if there was no mapping for key or the specified key mapping to a null value
         */
        public V get(K key) {
            Node<K, V> node = entry(key);
            return node != null
                    ? node.value //may null
                    : null;
        }

        private Node<K, V> entry(K key) {
            int m = table.length;
            int from = mapping(hash(key), m);
            for(int i=0; i<m; i++) {
                int idx = probe(from, m, i);
                Node<K, V> e = table[idx];

                if (e == null) {
                    break;
                } else if (e.status != Node.DELETED && Objects.equals(key, e.key)) {
                    return e;
                }
            }
            return null;
        }

        /**
         * Associates the specified value with the specified key in this map.
         *
         * If the map previously contained a mapping for the key, the old value is replaced by the specified value.
         *
         * Return the previous value or null if there was no mapping for key or the specified key mapping to a null value.
         *
         * @param key key with which the specified value is to be associated
         * @param value value to be associated with the specified key
         * @return the previous value associated with key, or null if there was no mapping for key or the specified key mapping to a null value
         */
        public V put(K key, V value) {
            int hash = hash(key);
            Node<K, V> added = new Node<>(hash, key, value);

            int m = table.length;
            int from = mapping(hash, m);
            int candidate = -1;

            for(int i=0; i<m; i++) {
                int idx = probe(from, m, i);
                Node<K, V> e = table[idx];

                if (e == null) {
                    if(candidate == -1) candidate = idx;
                    break;
                } else if(e.status == Node.DELETED) {
                    if(candidate == -1) candidate = idx;
                } else if(Objects.equals(key, e.key)) {
                    V oldV = e.value;
                    e.value = value;
                    return oldV;  //may null
                }
            }

            table[candidate] = added;
            if(++size == m) {
                resize();
            }

            return null;
        }

        /**
         * Removes the mapping for a key from this map if it is present.
         *
         * Return the previous value or null if there was no mapping for key or the specified key mapping to a null value.
         *
         * @param key key whose mapping is to be removed from the map
         * @return the previous value associated with key, or null if there was no mapping for key or the specified key mapping to a null value
         */
        public V remove(K key) {
            /*
             *一. 开放寻址法，删除某个 entry 时，表中其他 entry 需要重新寻址: 其他 entry 当前位置所属探查序列位置的前置探查序列位置存在空槽时，其他 entry 需要改变位置
             *    线性探查，从删除的 entry 的位置开始，到下一个空槽之间的所有 entry 依次检测
             *    其他探查，通常需要检测表中所有 entry
             *
             *二. 删除优化: 将删除的 entry 置 DELETED 标记，并不将 entry 从槽中移除，即当前槽不为空而是由一个死结点占据
             */
            Node<K, V> removing = entry(key);
            V r = null;
            if(removing != null) {
                r = removing.value;  //may null
                removing.status = Node.DELETED;
                removing.value = null;
                size--;
            }
            return r;
        }

        private static int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        }

        private static int mapping(int hash, int m) {
            return hash % m;
        }

        private static int probe(int from, int m, int i) {
            return (from + i) % m;
        }

        private void resize() {
            int oldSize = this.table.length; //this.table.length != 0
            int newSize = oldSize * 2;       //increment by double

            if(newSize < 0 ||                       //if overflow
               newSize > MAXIMUM_CAPACITY) {
                newSize = MAXIMUM_CAPACITY;
            }

            if(oldSize == newSize) throw new Error("this table has been reached to its allowed max capacity");

            @SuppressWarnings("unchecked")
            Node<K, V>[] newTable = new Node[newSize];

            for(int j = 0; j < this.table.length; j++) {
                Node<K, V> node = this.table[j];

                if(node != null && node.status != Node.DELETED) {
                    int from = mapping(node.hash, newSize);

                    for (int i = 0; i < newSize; i++) {
                        int idx = probe(from, newSize, i);

                        if (newTable[idx] == null) {
                            newTable[idx] = node;
                            break;
                        }
                    }
                }
            }
            this.table = newTable;
        }

        static class Node<K, V> {
            static byte DELETED = -1;
            final int hash;
            final K key;
            V value;
            byte status = 0;

            public Node(int hash, K key, V value) {
                this.hash = hash;
                this.key = key;
                this.value = value;
            }
        }
    }

    //练习11.4-4
    /*
    1、公式化简
       probe(key, i) = (h(key) + i*h'(key)) mod m. i = 0, 1, ..., m-1

       probe(key, i+1) = ( h(key) + (i+1)*h'(key) ) mod m
                       = [ probe(key, i) + h'(key) mod m ] mod m

       即 next = [ prev + h'(key) mod m ] mod m
    2、证明: 如果 h'(key) 与 m 互素，则探查序列不出现重复值

       证明: 对任意 i∈[0, m-1], 有 probe(key, i); 对任意 k∈(0, m-1], 有 probe(key, i+k), 只需证明 probe(key, i) != probe(key, i+k)

       probe(key, i) = ( h(key) + i*h'(key) ) mod m = X, 0 <= X <= m-1

       probe(key, i+k) = ( h(key) + (i+k)*h'(key) ) mod m = [ X + k*h'(key) mod m ] mod m

       即，只需 k*h'(key) mod m != 0 即可。若 h'(key) 与 m 互质，则可使此条件满足
     3、一些推导
       probe(key, 0) = h(key) mod m

       probe(key, m) = ( h(key) + m*h'(key) ) mod m
                     = ( h(key) mod m + m*h'(key) mod m ) mod m
                     = ( probe(key, 0) + m*h'(key) mod m ) mod m
                     = probe(key, 0)

       即，probe(key, m) == probe(key, 0), probe(key, m+1) == probe(key, 1), ...
    */

    //练习11.4-5 todo
    
}
