package com.juice.alg.part3.chapter11;

import java.util.*;

public class Chapter11_2 {

    /**
     *散列表(hash table)
     *  1. 使用数组表示桶（table）
     *     0    1    2    3    4    5  ...  m-1  m
     *    | |  | |  | |  | |  | |  | |     |  |
     *
     *  2. capacity 与 load factor
     *     n: 当前元素个数
     *     m: 桶大小，等于 table.length
     *     load factor = n/m
     *
     *  3. hash 函数（散列函数）
     *     使用 hash 散列函数，将元素映射到桶下标（0 ~ m-1）。{@link Chapter11_3}
     *
     *  4. hash 冲突（碰撞）
     *     链指针法、开放寻址法等
     */
    static class HashTable<K, V> extends AbstractMap<K, V>
                                 implements Map<K, V>
    {
        /*
         *散列表
         *  - cannot contain duplicate keys
         *  - non-ordered guarantee
         *  - permits null key and null value
         */

        static final int DEFAULT_CAPACITY = 16;
        static final int MAXIMUM_CAPACITY = 1 << 30;
        private final float loadFactor = 0.75f;

        private Node<K, V>[] table;
        private int size;

        /**
         * Constructor a hash table with the capacity `DEFAULT_CAPACITY`.
         */
        public HashTable() {
            this(DEFAULT_CAPACITY);
        }

        /**
         * Constructor a hash table with the specified capacity.
         *
         * @param capacity the specified capacity
         * @throws IllegalArgumentException if the specified capacity is negative or is larger than `MAXIMUM_CAPACITY`
         */
        @SuppressWarnings("unchecked")
        public HashTable(int capacity) {
            if(capacity < 0 || capacity > MAXIMUM_CAPACITY)
                throw new IllegalArgumentException("the capacity must not be negative and must less or equal than " + MAXIMUM_CAPACITY);

            table = new Node[capacity];
        }

        /**
         * Construct a hash table and add all of the entry of the specified map into this map.
         *
         * @param map the specified collection
         * @throws NullPointerException     if the specified map is null
         */
        @SuppressWarnings("unused")
        public HashTable(Map<? extends K, ? extends V> map) {
            putAll(map);
        }

        /**
         * Returns the number of key-value mappings in this map.
         *
         * @return the number of key-value mappings in this map
         */
        @Override
        public int size() {
            return size;
        }

        /**
         * Returns true if this map contains no key-value mappings.
         *
         * @return true if this map contains no key-value mappings
         */
        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * Returns true if this map contains a mapping for the specified key.
         *
         * This map allow null key, so check equality by `key==null ? e==null : key.equals(e)`.
         *
         * If the specified key is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * @param key key whose presence in this map is to be tested
         * @return true if this map contains a mapping for the specified key
         */
        @Override
        public boolean containsKey(Object key) {
            return entry(key) != null;
        }

        private Node<K, V> entry(Object key) {
            int idx = mapping(hash(key), table.length);
            Node<K, V> e = table[idx];
            while(e != null && !Objects.equals(key, e.key)) {
                e = e.next;
            }
            return e;
        }

        /**
         * Returns true if this map maps one or more keys to the specified value.
         *
         * This map allow null value, so check equality by `value==null ? e==null : value.equals(e)`.
         *
         * If the specified value is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * @param value value whose presence in this map is to be tested
         * @return true if this map maps one or more keys to the specified value
         */
        @Override
        public boolean containsValue(Object value) {
            return super.containsValue(value);
        }

        /**
         * Return the value to which the specified key is mapped, or
         * return null if this map contains no mapping for the specified key, or
         * the specified key mapping to a `null` value).
         *
         * This map allow null key, so check equality by `key==null ? e==null : key.equals(e)`.
         *
         * This map allow null value, so return null can not indicate that there is no mapping for the specified key.
         *
         * If the specified key is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * @param key the key whose associated value is to be returned
         * @return the value(may null) to which the specified key is mapped, or null
         */
        @Override
        public V get(Object key) {
            Node<K, V> node = entry(key);
            return node != null ? node.value : null;
        }

        /**
         * Associates the specified value with the specified key in this map.
         *
         * If the map previously contained a mapping for the key, the old value is replaced by the specified value.
         *
         * Return the previous value or null if there was no mapping for key or the specified key mapping to a null value.
         *
         * This map allow null key, so check equality by `key==null ? e==null : key.equals(e)`.
         *
         * If the specified key and value is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * @param key key with which the specified value is to be associated
         * @param value value to be associated with the specified key
         * @return the previous value associated with key(may null), or null
         */
        public V put(K key, V value) {
            return putVal(key, value, false);
        }

        private V putVal(K key, V value, boolean onlyIfAbsent) {
            int hash = hash(key);
            Node<K, V> added = new Node<>(key, value, hash);

            int idx = mapping(hash, table.length);
            Node<K, V> e = table[idx], pe = null;
            while(e != null && !Objects.equals(key, e.key)) {
                pe = e;
                e = e.next;
            }
            if(e == null) {
                if(pe == null) {
                    table[idx] = added;
                } else {
                    pe.next = added;
                }
            } else {
                V oldV = e.value;
                if(!onlyIfAbsent || oldV == null)
                    e.value = value;
                return oldV;  //may null
            }

            if(++size > this.table.length * loadFactor) {
                resize();
            }

            return null;
        }

        /**
         * Removes the mapping for a key from this map if it is present.
         *
         * Return the previous value or null if there was no mapping for key or the specified key mapping to a null value.
         *
         * This map allow null key, so check equality by `key==null ? e==null : key.equals(e)`.
         *
         * If the specified key is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * @param key key whose mapping is to be removed from the map
         * @return the previous value(may null) associated with key, or null if there was no mapping for key
         */
        @Override
        public V remove(Object key) {
            Node<K, V> removed = removeNode(key, null, false);
            return removed != null
                    ? removed.value //may null
                    : null;
        }

        private Node<K, V> removeNode(Object key, Object value, boolean matchValue) {
            int idx = mapping(hash(key), table.length);
            Node<K, V> e = table[idx], pe = null;
            while(e != null && !Objects.equals(key, e.key)) {
                pe = e;
                e = e.next;
            }
            if(e != null && (!matchValue || (Objects.equals(value, e.value)))) {
                if (pe == null) {
                    table[idx] = e.next;
                } else {
                    pe.next = e.next;
                }
                size--;
                return e;
            }
            return null;
        }

        /**
         * Copies all of the mappings from the specified map to this map.
         *
         * The behavior of this operation is undefined if the specified map is modified while the operation is in progress.
         *
         * @param map mappings to be stored in this map
         * @throws NullPointerException    if the specified map is null
         */
        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            super.putAll(map);
        }

        /**
         * Removes all of the mappings from this map.
         */
        @Override
        public void clear() {
            Arrays.fill(table, null);
            size = 0;
        }

        /**
         * If the specified key has no mapping with a value or map to null value, associates it with the given value and return null
         * otherwise return the current value.
         *
         * This map allow null key, so check equality by `key==null ? e==null : key.equals(e)`.
         *
         * If the specified key and value is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * The default implementation makes no guarantees about synchronization or atomicity properties of this method.
         * Any implementation providing atomicity guarantees must override this method and document its concurrency properties ?
         *
         * @param key key with which the specified value is to be associated
         * @param value value to be associated with the specified key
         * @return the previous value associated with the specified key
         *         or null if there was no mapping for the key or map to null value
         */
        @Override
        public V putIfAbsent(K key, V value) {
            return putVal(key, value, true);
        }

        /**
         * Removes the entry for the specified key only if it is currently mapped to the specified value.
         *
         * If the specified key and value is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * @param key key with which the specified value is associated
         * @param value value expected to be associated with the specified key
         * @return true if the value was removed
         */
        @Override
        public boolean remove(Object key, Object value) {
            return removeNode(key, value, true) != null;
        }


        /**
         * Replaces the entry for the specified key only if it is currently mapped to the specified value.
         *
         * If the specified key and value is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * @param key key with which the specified value is associated
         * @param oldValue value expected to be associated with the specified key
         * @param newValue value to be associated with the specified key
         * @return true if the value was replaced
         */
        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            return replaceNode(key, oldValue, newValue, true) != null;
        }

        private Node<K, V> replaceNode(Object key, Object oldValue, V newValue, boolean matchValue) {
            int idx = mapping(hash(key), table.length);
            Node<K, V> e = table[idx];
            while(e != null && !Objects.equals(key, e.key)) {
                e = e.next;
            }
            if(e != null && (!matchValue || (Objects.equals(oldValue, e.value)))) {
                e.value = newValue;
                return e;
            }
            return null;
        }

        /**
         * Replaces the entry for the specified key only if it is currently mapped to some value.
         *
         * If the specified key is incompatible with this map, return `false` rather than throw `ClassCastException`.
         *
         * @param key key with which the specified value is associated
         * @param value value to be associated with the specified key
         * @return the previous value associated with the specified key, or null if there was no mapping for the key
         *         (A null return can also indicate that the map previously associated null with the key)
         */
        @Override
        public V replace(K key, V value) {
            Node<K, V> replaced = replaceNode(key, null, value, false);
            return replaced != null
                    ? replaced.value //may null
                    : null;
        }

        private static int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        }

        private static int mapping(int hash, int m) {
            return hash % m;
        }

        private void resize() {
            int oldSize = this.table.length; //this.table.length != 0
            int newSize = oldSize * 2;

            if(newSize < 0 || newSize > MAXIMUM_CAPACITY) {
                newSize = MAXIMUM_CAPACITY;
            }

            if(newSize == oldSize) return;

            @SuppressWarnings("unchecked")
            Node<K, V>[] newTable = new Node[newSize];

            for(int i = 0; i < this.table.length; i++) {
                Node<K, V> node = this.table[i];

                while(node != null) {
                    Node<K, V> next = node.next;
                    node.next = null;

                    int newIdx = mapping(hash(node.key), newSize);
                    Node<K, V> ne = newTable[newIdx];
                    newTable[newIdx] = node;
                    node.next = ne;

                    node = next;
                }
            }
            this.table = newTable;
        }

        static class Node<K, V> implements Map.Entry<K, V> {
            final int hash;
            final K key;
            V value;
            Node<K, V> next;

            Node(K key, V value, int hash) {
                this.key = key;
                this.value = value;
                this.hash = hash;
            }

            @Override
            public K getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return value;
            }

            @Override
            public V setValue(V value) {
                V old = this.value;
                this.value = value;
                return old;
            }

            @Override
            public boolean equals(Object o) {
                if (!(o instanceof Node)) return false;

                Node<?, ?> e = (Node<?, ?>) o;
                return Objects.equals(getKey(), e.getKey()) && Objects.equals(getValue(), e.getValue());
            }

            @Override
            public int hashCode() {
                return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
            }
        }

        /**
         * 返回 this map 中 keys 的 set view.
         *
         * the set is backed by the map，map 中的修改反映在 set，反之亦然.
         *
         * 如果一个迭代器在穿越 set 期间，this map 被修改了(排除迭代器的 remove() 方法产生的修改)，迭代的结果是未定义的.
         *
         * the set 支持元素删除：Iterator#remove, Set#remove, Set#removeAll, Set#retainAll, Set#clear 方法，不支持元素添加：Set#add, Set#addAll 方法
         *
         * @return  a set view of the keys contained in this map
         */
        @Override
        public Set<K> keySet() {
            return super.keySet();
        }

        /**
         * 返回 this map 中 values 的 collection view.
         *
         * the collection is backed by the map，map 中的修改反映在 collection，反之亦然.
         *
         * 如果一个迭代器在穿越 collection 期间，this map 被修改了(排除迭代器的 remove() 方法产生的修改)，迭代的结果是未定义的.
         *
         * the collection 支持元素删除：Iterator#remove, Collection#remove, Collection#removeAll, Collection#retainAll, Collection#clear 方法
         * 不支持元素添加：Collection#add, Collection#addAll 方法.
         *
         * @return a collection view of the values contained in this map
         */
        @Override
        public Collection<V> values() {
            return super.values();
        }

        Set<Map.Entry<K,V>> entrySet;

        /**
         * 返回 this map 中 mappings 的 set view.
         *
         * the set is backed by the map，map 中的修改反映在 set，反之亦然.
         *
         * 如果一个迭代器在穿越 set 期间，this map 被修改了(排除迭代器的 remove() 方法产生的修改)，迭代的结果是未定义的.
         *
         * the set 支持元素删除：Iteratoe#remove, remove, removeAll, retainAll, clear 方法，不支持元素添加：add, addAll 方法.
         *
         * @return a set view of the mappings contained in this map
         */
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            if(entrySet == null) {
                entrySet = new EntrySet();
            }
            return entrySet;
        }

        private class EntrySet extends AbstractSet<Entry<K, V>> {

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new EntryIterator();
            }

            @Override
            public Spliterator<Entry<K, V>> spliterator() {
                return null; //todo
            }

            @Override
            public int size() {
                return HashTable.this.size();
            }

            @Override
            public boolean isEmpty() {
                return HashTable.this.isEmpty();
            }

            private HashTable.Node<?, ?> node(Node<?, ?> entry) {
                int idx = HashTable.mapping(HashTable.hash(entry.key), HashTable.this.table.length);
                HashTable.Node<K, V> e = HashTable.this.table[idx];
                while(e != null && !Objects.equals(entry, e)) {
                    e = e.next;
                }
                return e;
            }

            @Override
            public boolean contains(Object entry) {
                if(!(entry instanceof HashTable.Node)) return false;

                HashTable.Node<?, ?> etr = (HashTable.Node<?, ?>) entry;
                return node(etr) != null;
            }

            @Override
            public boolean remove(Object entry) {
                if(!(entry instanceof HashTable.Node)) return false;
                HashTable.Node<?, ?> etr = (HashTable.Node<?, ?>) entry;

                return HashTable.this.removeNode(etr.key, etr.value, true) != null;
            }

            @Override
            public void clear() {
                HashTable.this.clear();
            }
        }

        private class EntryIterator implements Iterator<Entry<K, V>> {
            Node<K, V> current, last;
            int idx = 0;

            EntryIterator() {
                forward();
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Entry<K, V> next() {
                if(current == null)
                    throw new NoSuchElementException("has no more elements");

                last = current;
                forward();
                return last;
            }

            void forward() {
                while (idx < HashTable.this.table.length) {
                    if(current == null)
                        current = HashTable.this.table[idx];
                    else
                        current = current.next;

                    if(current != null) return;
                    else idx++;
                }
                current = null;
            }

            @Override
            public void remove() {
                if(last == null)
                    throw new IllegalStateException("can not remove before the next method called or can not call remove method again");

                HashTable.this.removeNode(last.key, null, false);
                last = null;
            }
        }

    }


    /*
     * 链接法散列的分析
     *    最坏情况运行时间:
     *      O(n) 当n个元素都散列到同一个槽
     *
     *    平均情况运行时间:
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
     */

}
