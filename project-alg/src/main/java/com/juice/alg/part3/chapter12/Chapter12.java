package com.juice.alg.part3.chapter12;

import com.juice.alg.part3.chapter10.Chapter10_1.FixedArrayDeque;

import java.util.*;

@SuppressWarnings("unused")
public class Chapter12 {

    /**
     *二叉搜索树(BST, Binary search tree)
     *  对任意一个节点 p，其左子树的 key <= p.key；其右子树的 key >= p.key
     *
     * @param <K> key type
     * @param <V> value type
     */
    public static class BSTree<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Tree {
        /*
         *二叉搜索树
         *  - cannot contain duplicate keys
         *  - sorted by a `Comparator` provided at map creation time，or according to `Comparable` natural ordering of its keys
         *  - when this map uses natural ordering，not permit null key；or depending on whether the comparator permits
         *  - allow null value, allow duplicate value
         */
        private Node<K, V> root;
        private int size;

        private Comparator<? super K> comparator;

        /**
         * Creates an empty tree sorted.
         */
        public BSTree() {}

        /**
         * Construct a tree and add all of the entry of the specified map into this tree.
         *
         * @param map mappings to be stored in this tree
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         *                              or if the specified map is null
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        public BSTree(Map<? extends K, ? extends V> map) {
            this.putAll(map);
        }

        /**
         * Creates an empty tree sorted according to the specified comparator.
         *
         * @param comparator the specified comparator
         * @throws NullPointerException if the specified comparator is null
         */
        public BSTree(Comparator<? super K> comparator) {
            if(comparator == null)
                throw new NullPointerException("the specified comparator is null");
            this.comparator = comparator;
        }

        /**
         * Construct a tree and add all of the entry of the specified map into this tree.
         *
         * @param sortedMap mappings to be stored in this tree
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         *                              or if the specified map is null
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        public BSTree(SortedMap<K, ? extends V> sortedMap) {
            this.comparator = sortedMap.comparator();
            this.putAll(sortedMap);
        }

        /**
         * Returns the number of key-value mappings in this tree.
         *
         * @return the size of this tree
         */
        @Override
        public int size() {
            return this.size;
        }

        /**
         * Returns true if this tree contains no key-value mappings.
         *
         * @return true if this tree is empty
         */
        @Override
        public boolean isEmpty() {
            return this.size == 0;
        }

        /**
         * Returns true if this tree contains a mapping for the specified key.
         *
         * @param key the specified key
         * @return true if this tree contains a mapping for the specified key
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public boolean containsKey(Object key) {
            return entry(key) != null;
        }

        private Node<K, V> entry(Object key) {
            Node<K, V> t = this.root;

            //note that: when using natural ordering and the specified key is null and this tree is empty, do not throw NullPointerException
            while(t != null) {
                int r = compare(key, t.key);

                if(r == 0) return t;
                else if(r < 0) {
                    t = t.left;
                } else {
                    t = t.right;
                }
            }

            return null;
        }

        @SuppressWarnings("unchecked")
        private int compare(Object k1, Object k2) {
            return comparator != null ? comparator.compare((K) k1, (K) k2)
                    : ((Comparable<K>) k1).compareTo((K) k2);
        }

        /**
         * Returns true if this tree maps one or more keys to the specified value.
         *
         * This tree allow null value, so check equality by `value==null ? e==null : value.equals(e)`.
         *
         * If the type of the specified value is incompatible with this tree, return `false` rather than throw `ClassCastException`.
         *
         * @param value the specified value
         * @return true if this map maps one or more keys to the specified value
         */
        @Override
        public boolean containsValue(Object value) {
            return fullMapView.containsValue(value);
        }

        /**
         * Return the value to which the specified key is mapped,
         * or return null if this tree contains no mapping for this key (, or the specified key mapping to a null value)
         *
         * @param key the specified key
         * @return the mapped value or null if there was no mapping or the specified key mapping to a null value
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public V get(Object key) {
            Node<K, V> node = this.entry(key);
            return node == null
                    ? null         //no mapping
                    : node.value;  //may null
        }

        /**
         * Associates the specified value with the specified key in this tree.
         *
         * If the tree previously contained a mapping for the key, the old value is replaced by the specified value.
         *
         * @param key the specified key
         * @param value the specified value
         * @return the previous value or null if there was no mapping or the specified key mapping to a null value
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public V put(K key, V value) {
            return putVal(key, value, false);
        }

        private V putVal(K key, V value, boolean onlyIfAbsent) {
            Node<K, V> pt = null, t = this.root;
            int r = 0;

            if(this.comparator != null) {
                while(t != null) {
                    pt = t;

                    r = this.comparator.compare(key, t.key);
                    if(r == 0) {
                        break;
                    } else if(r < 0) {
                        t = t.left;
                    } else {
                        t = t.right;
                    }
                }
            } else if(key == null) {
                throw new NullPointerException("key is not allowed null for natural ordering");
            } else {
                @SuppressWarnings("unchecked")
                Comparable<K> comp = (Comparable<K>) key;
                while(t != null) {
                    pt = t;

                    r = comp.compareTo(t.key);
                    if(r == 0) {
                        break;
                    } else if(r < 0) {
                        t = t.left;
                    } else {
                        t = t.right;
                    }
                }
            }

            if(pt == null) {
                if(this.comparator != null) {
                    this.comparator.compare(key, key);  //check whether permit null key
                }
                this.root = new Node<>(key, value, null);
            } else if(t == null) {
                Node<K, V> node = new Node<>(key, value, pt);
                if(r < 0) {
                    pt.left = node;
                } else {
                    pt.right = node;
                }
            } else {
                V oldV = t.value;
                if(!onlyIfAbsent || oldV == null)
                    t.value = value;
                return oldV; //may null
            }

            this.size++;
            return null;
        }

        /**
         * Removes the mapping for a key from this map if it is present.
         *
         * @param key the key
         * @return the previous value (may null) or null there was no mapping for the key
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public V remove(Object key) {
            Node<K, V> node = this.entry(key);
            if(node != null) {
                V oldV = node.value;
                removeNode(node);
                return oldV;  //may null
            }
            return null;  //no mapping
        }

        private void removeNode(Node<K, V> t) {
            //assert t != null

            if (t.left != null && t.right != null) {
                Node<K, V> s = firstKey_L_T_R(t.right);  //not null

                //不改变二叉树的结构，仅将要删除节点的 key, value 替换为后继节点的 key, value 后转而删除后继节点
                t.key = s.key;
                t.value = s.value;
                t = s;
            }

            transplant(t.parent, t, t.right != null ? t.right : t.left);

            t.parent = t.left = t.right = null;
            this.size--;
        }

        void transplant(Node<K, V> pt, Node<K, V> t, Node<K, V> next) {
            if (next != null)
                next.parent = pt;

            if (pt == null) {
                this.root = next;
            } else if (pt.left == t) {
                pt.left = next;
            } else {
                pt.right = next;
            }
        }

        private void removeNode1(Node<K, V> t) {
            //assert t != null

            if(t.left != null && t.right != null) {
                Node<K, V> s = firstKey_L_T_R(t.right);  //not null
                if(s.parent != t) {
                    transplant(s.parent, s, s.right);
                    s.right = t.right;            //t.right != null
                    s.right.parent = s;
                }
                transplant(t.parent, t, s);
                s.left = t.left;                  //t.left != null
                t.left.parent = s;

            } else {
                transplant(t.parent, t, t.right != null ? t.right : t.left);
            }

            t.parent = t.left = t.right = null;
            this.size--;
        }

        private void exchange(Node<K, V> one, Node<K, V> two) {
            if(one.parent == two) { //exchange
                Node<K, V> e = one;
                one = two;
                two = e;
            }

            Node<K, V> p = one.parent, l = one.left, r = one.right, tp, tl, tr;
            boolean lw = p != null && p.left == one;

            one.parent = tp = two.parent;
            if(tp == null) {
                this.root = one;
            } else {
                if(tp.left == two) tp.left = one;
                else               tp.right = one;
            }

            one.left = tl = two.left;
            if(tl != null) tl.parent = one;

            one.right = tr = two.right;
            if(tr != null) tr.parent = one;


            two.parent = p;
            if(p == null) {
                this.root = two;
            } else {
                if(lw) p.left = two;
                else   p.right = two;
            }

            if(two == l) {
                two.left = one;
            } else {
                two.left = l;
            }
            if(two.left != null) two.left.parent = two;

            if(two == r) {
                two.right = one;
            } else {
                two.right = r;
            }
            if(two.right != null) two.right.parent = two;
        }

        private void removeNode2(Node<K, V> t) {
            //assert t != null

            if (t.left != null && t.right != null) {
                Node<K, V> s = firstKey_L_T_R(t.right);  //not null

                //交换两个节点的位置
                this.exchange(t, s);
            }

            transplant(t.parent, t, t.right != null ? t.right : t.left);

            t.parent = t.left = t.right = null;
            this.size--;
        }

        /**
         * Copies all of the mappings from the specified map to this tree.
         *
         * The behavior of this operation is undefined if the specified map is modified while the operation is in progress.
         *
         * @param map mappings to be stored in this tree
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         *                              or if the specified map is null
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            super.putAll(map);
        }

        /**
         * Removes all of the mappings from this tree.
         */
        @Override
        public void clear() {
            this.root = null;
            this.size = 0;
            this.comparator = null;
            //this.fullMapView = new AscendingMapView();  //do not need reset
        }

        /**
         * Return the value to which the specified key is mapped(may null), or defaultValue if this map contains no mapping for this key.
         *
         * @param key the specified key
         * @param defaultValue default value
         * @return the mapped value(may null) or null if there was no mapping for the specified key
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public V getOrDefault(Object key, V defaultValue) {
            Node<K, V> node = this.entry(key);
            return node == null
                    ? defaultValue   //no mapping
                    : node.value;    //may null
        }

        /**
         * If the specified key is not already associated with a value (or is mapped to null),
         * associates it with the given value and returns null, else returns the current value.
         *
         * @param key the specified key
         * @param value the specified value
         * @return the previous value or null if there was no mapping or the specified key mapping to a null value
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public V putIfAbsent(K key, V value) {
            return putVal(key, value, true);
        }

        /**
         * Removes the entry for the specified key only if it is currently mapped to the specified value.
         *
         * @param key the key
         * @param value the value
         * @return true if it has mapping for the specified key and value is equal
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public boolean remove(Object key, Object value) {
            Node<K, V> node = this.entry(key);
            if(node != null && Objects.equals(value, node.value)) {
                removeNode(node);
                return true;
            }
            return false;  //no mapping or value is not equal
        }

        /**
         * Replaces the entry for the specified key only if it is currently mapped to the specified value.
         *
         * @param key the key
         * @param oldValue the old value
         * @param newValue the new value
         * @return true if it has mapping for the specified key and value is equal
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            Node<K, V> node = this.entry(key);
            if(node != null && Objects.equals(oldValue, node.value)) {
                node.value = newValue;
                return true;
            }
            return false;  //no mapping or value is not equal
        }

        /**
         * Replaces the entry for the specified key only if it is currently mapped to some value.
         *
         * @param key the key
         * @param value the value
         * @return the previous value(may null if this map permits null value) or null if there was no mapping
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public V replace(K key, V value) {
            Node<K, V> node = this.entry(key);
            if(node != null) {
                V oldV = node.value;
                node.value = value;
                return oldV;  //may null
            }
            return null;  //no mapping
        }

        /**
         * 返回 this tree 中 keys 的 set view，which iterator 迭代 keys in "按 key 排序过的 key 序列"
         *
         * the set is backed by the tree，tree 中的修改反映在 set，反之亦然
         *
         * 如果一个迭代器在穿越 set 期间，this tree 被修改了(排除迭代器的 remove() 方法产生的修改)，迭代的结果是未定义的
         *
         * the set 支持元素删除：Iterator#remove, Set#remove, Set#removeAll, Set#tetainAll, Set#clear 方法，不支持元素添加：Set#add, Set#addAll方法
         *
         * @return a set view of the keys contained in this tree
         */
        @Override
        public Set<K> keySet() {
            return fullMapView.keySet();
        }

        private class KeyIterator implements Iterator<K> {
            private final Iterator<Entry<K, V>> it;

            KeyIterator(Iterator<Entry<K, V>> it) {
                this.it = it;
            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public K next() {
                return it.next().getKey();
            }

            @Override
            public void remove() {
                it.remove();
            }
        }

        /**
         * 返回 this tree 中 values 的 collection view，which iterator 迭代values in "按 key 排序过的 key 序列" 顺序
         *
         * the collection is backed by the tree，tree 中的修改反映在 collection，反之亦然
         *
         * 如果一个迭代器在穿越 collection 期间，this tree 被修改了(排除迭代器的 remove() 方法产生的修改)，迭代的结果是未定义的
         *
         * the collection 支持元素删除：Iterator#remove, Collection#remove, Collection#removeAll, Collection#tetainAll, Collection#clear方法，
         * 不支持元素添加：Collection#add, Collection#addAll
         *
         * @return a collection view of the values contained in this tree
         */
        @Override
        public Collection<V> values() {
            return fullMapView.values();
        }

        /**
         * 返回 this tree 中 mappings 的 set view，which iterator 迭代 mappings in "按 key 排序过的 key 序列" 顺序
         *
         * the set is backed by the tree，tree 中的修改反映在 set，反之亦然
         *
         * 如果一个迭代器在穿越 set 期间，this tree 被修改了(排除迭代器的 remove() 方法产生的修改)，迭代的结果是未定义的
         *
         * the set 支持元素删除：Iterator#remove, Set#remove, Set#removeAll, Set#tetainAll, Set#clear方法，不支持元素添加：Set#add, Set#addAll方法
         *
         * @return a set view of the mappings contained in this tree
         */
        @Override
        public Set<Entry<K, V>> entrySet() {
            return fullMapView.entrySet();
        }

        private abstract class EntryIterator implements Iterator<Entry<K, V>> {
            Node<K, V> current, fence, lastRet;

            EntryIterator(Node<K, V> current, Node<K, V> fence) {
                this.current = current;
                this.fence = fence;
            }

            @Override
            public boolean hasNext() {
                return current != fence;
            }

            @Override
            public Entry<K, V> next() {
                if(current == fence)
                    throw new NoSuchElementException("has no more elements");

                lastRet = current;
                current = stepOne(current);
                return lastRet;
            }

            abstract Node<K, V> stepOne(Node<K, V> node);

            void removeAscending() {
                if(lastRet == null)
                    throw new IllegalStateException("can not remove before the next method called or can not call remove method again");

                if(lastRet.left != null && lastRet.right != null)  //for removeNode 补丁
                    current = lastRet;
                BSTree.this.removeNode(lastRet);
                lastRet = null;
            }

            void removeDescending() {
                if(lastRet == null)
                    throw new IllegalStateException("can not remove before the next method called or can not call remove method again");

                BSTree.this.removeNode(lastRet);
                lastRet = null;
            }

        }

        private class AscendingEntryIterator extends EntryIterator {
            AscendingEntryIterator(Node<K, V> first) {
                this(first, null);
            }

            AscendingEntryIterator(Node<K, V> first, Node<K, V> fence) {
                super(first, fence);
            }

            @Override
            public void remove() {
                removeAscending();
            }

            @Override
            Node<K, V> stepOne(Node<K, V> node) {
                return BSTree.this.next(node);
            }
        }

        private class DescendingEntryIterator extends EntryIterator {
            DescendingEntryIterator(Node<K, V> first) {
                this(first, null);
            }

            DescendingEntryIterator(Node<K, V> first, Node<K, V> fence) {
                super(first, fence);
            }

            @Override
            public void remove() {
                removeDescending();
            }

            @Override
            Node<K, V> stepOne(Node<K, V> node) {
                return BSTree.this.prev(node);
            }
        }


        //// SortedMap

        /**
         * Returns the comparator used to order the keys, or null if this map uses the Comparable natural ordering of its keys
         *
         * @return the comparator, or null
         */
        @Override
        public Comparator<? super K> comparator() {
            return this.comparator;
        }

        /**
         * Returns the first (lowest) key currently in this tree.
         *
         * @return the first (lowest) key currently in this tree
         * @throws NoSuchElementException if this tree is empty
         */
        @Override
        public K firstKey() {
            Node<K, V> node = firstKey_L_T_R(this.root);
            if(node == null)
                throw new NoSuchElementException("this tree is empty");
            return node.getKey();
        }

        /**
         * Returns the last (highest) key currently in this tree.
         *
         * @return the last (highest) key currently in this tree
         * @throws NoSuchElementException if this tree is empty
         */
        @Override
        public K lastKey() {
            Node<K, V> node = firstKey_R_T_L(this.root);
            if(node == null)
                throw new NoSuchElementException("this tree is empty");
            return node.getKey();
        }

        /**
         * Returns a view of the portion(部分) of this map whose keys range [fromKey，toKey).
         *
         * The returned map is backed by this map, so changes in the returned map are reflected in this map, and vice-versa.
         *
         * The returned map supports all optional map operations that this map supports.
         *
         * The returned map will throw an `IllegalArgumentException` on an attempt to insert a key outside its range
         *
         * @param fromKey the from key
         * @param toKey the to key
         * @return a view of the portion of this map
         * @throws ClassCastException         if fromKey and toKey cannot be compared to one another using this map's comparator (or, no comparator, using natural ordering)
         *                                    Implementations may, but are not required to, throw this exception
         * @throws NullPointerException       if fromKey or toKey is null and this map does not permit null keys
         * @throws IllegalArgumentException   if fromKey is greater than toKey;
         *                                 or if this map itself has a restricted range, and fromKey or toKey lies outside the bounds of the range
         */
        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }

        /**
         * Returns a view of the portion(部分) of this map whose keys < toKey.
         *
         * The returned map is backed by this map, so changes in the returned map are reflected in this map, and vice-versa.
         *
         * The returned map supports all optional map operations that this map supports.
         *
         * The returned map will throw an `IllegalArgumentException` on an attempt to insert a key outside its range.
         *
         * @param toKey the to key
         * @return a view of the portion of this map
         * @throws ClassCastException         if the toKey is not compatible with this map's comparator (or, no comparator, using natural ordering)
         *                                    Implementations may, but are not required to, throw this exception
         * @throws NullPointerException       if fromKey or toKey is null and this map does not permit null keys
         * @throws IllegalArgumentException   if this map itself has a restricted range, and fromKey or toKey lies outside the bounds of the range
         */
        @Override
        public SortedMap<K, V> headMap(K toKey) {
            return headMap(toKey, false);
        }

        /**
         * Returns a view of the portion(部分) of this map whose keys >= fromKey.
         *
         * The returned map is backed by this map, so changes in the returned map are reflected in this map, and vice-versa.
         *
         * The returned map supports all optional map operations that this map supports.
         *
         * The returned map will throw an `IllegalArgumentException` on an attempt to insert a key outside its range.
         *
         * @param fromKey the from key
         * @return a view of the portion of this map
         * @throws ClassCastException         if the from is not compatible with this map's comparator (or, no comparator, using natural ordering)
         *                                    Implementations may, but are not required to, throw this exception
         * @throws NullPointerException       if fromKey or toKey is null and this map does not permit null keys
         * @throws IllegalArgumentException   if this map itself has a restricted range, and fromKey or toKey lies outside the bounds of the range
         */
        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }


        //// NavigableMap

        /**
         * Returns a key-value mapping associated with the least key in this tree, or null if the tree is empty.
         *
         * @return an entry with the least key, or {@code null} if this tree is empty
         */
        @Override
        public Entry<K, V> firstEntry() {
            return firstKey_L_T_R(this.root);
        }

        /**
         * Returns a key-value mapping associated with the greatest key in this tree, or null if the tree is empty.
         *
         * @return an entry with the greatest key, or {@code null} if this tree is empty
         */
        @Override
        public Entry<K, V> lastEntry() {
            return firstKey_R_T_L(this.root);
        }

        /**
         * Removes and Returns a key-value mapping associated with the least key in this tree, or null if the tree is empty.
         *
         * @return the removed least entry, or {@code null} if this tree is empty
         */
        @Override
        public Entry<K, V> pollFirstEntry() {
            Node<K, V> node = firstKey_L_T_R(this.root);
            if(node != null) {
                removeNode(node);
                return node;
            }
            return null;  //this tree is empty
        }

        /**
         * Removes and Returns a key-value mapping associated with the greatest key in this tree, or null if the tree is empty.
         *
         * @return the removed greatest entry, or {@code null} if this tree is empty
         */
        @Override
        public Entry<K, V> pollLastEntry() {
            Node<K, V> node = firstKey_R_T_L(this.root);
            if(node != null) {
                removeNode(node);
                return node;
            }
            return null;  //this tree is empty
        }

        /**
         * Returns the greatest key-value mapping with the key strictly less than the given key, or null if there is no such entry.
         *
         * @param key the key, may not contained in this tree
         * @return the greatest key-value mapping with the key strictly less than the given key, or null if there is no such entry
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public Entry<K, V> lowerEntry(K key) {
            Node<K, V> t = this.root;

            while(t != null) {
                int r = compare(key, t.key);

                if(r <= 0) {
                    if(t.left == null)
                        return prev(t);  //may null
                    else
                        t = t.left;
                } else {
                    if(t.right == null)
                        return t;
                    else
                        t = t.right;
                }
            }

            return null;
        }

        /**
         * Returns the greatest key strictly less than the given key, or null if there is no such key (or the returned key is null).
         *
         * @param key the key, may not contained in this tree
         * @return the greatest key strictly less than the given key, or null if there is no such key
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public K lowerKey(K key) {
            Entry<K, V> node = lowerEntry(key);
            return node == null
                    ? null                //no such entry, thus no such key
                    : node.getKey();      //may null if this tree permit null key
        }

        /**
         * Returns the greatest key-value mapping with the key less than or equal to the given key, or null if there is no such entry.
         *
         * @param key the key, may not contained in this tree
         * @return the greatest key-value mapping with the key less than or equal to the given key, or null if there is no such entry
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public Entry<K, V> floorEntry(K key) {
            Node<K, V> t = this.root;

            while(t != null) {
                int r = compare(key, t.key);

                if(r < 0) {
                    if(t.left == null)
                        return prev(t);  //may null
                    else
                        t = t.left;
                } else {
                    if(t.right == null)
                        return t;
                    else
                        t = t.right;
                }
            }

            return null;
        }

        /**
         * Returns the the key less than or equal to the given key, or null if there is no such key (or the returned key is null).
         *
         * @param key the key, may not contained in this tree
         * @return the the key less than or equal to the given key, or null if there is no such key
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public K floorKey(K key) {
            Entry<K, V> node = floorEntry(key);
            return node == null
                    ? null                //no such entry, thus no such key
                    : node.getKey();      //may null if this tree permit null key
        }

        /**
         * Returns the least key-value mapping with the key strictly greater than the given key, or null if there is no such entry.
         *
         * @param key the key, may not contained in this tree
         * @return the least key-value mapping with the key strictly greater than the given key, or null if there is no such entry
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public Entry<K, V> higherEntry(K key) {
            Node<K, V> t = this.root;

            while(t != null) {
                int r = compare(key, t.key);

                if(r >= 0) {
                    if(t.right == null)
                        return next(t);  //may null
                    else
                        t = t.right;
                } else {
                    if(t.left == null)
                        return t;
                    else
                        t = t.left;
                }
            }

            return null;
        }

        /**
         * Returns the least key strictly greater than the given key, or null if there is no such key (or the returned key is null).
         *
         * @param key the key, may not contained in this tree
         * @return the least key strictly greater than the given key, or null if there is no such key
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public K higherKey(K key) {
            Entry<K, V> node = higherEntry(key);
            return node == null
                    ? null                //no such entry, thus no such key
                    : node.getKey();      //may null if this tree permit null key
        }

        /**
         * Returns the least key-value mapping with the key greater than or equal to the given key, or null if there is no such entry.
         *
         * @param key the key, may not contained in this tree
         * @return the least key-value mapping with the key greater than the given key, or null if there is no such entry
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public Entry<K, V> ceilingEntry(K key) {
            Node<K, V> t = this.root;

            while(t != null) {
                int r = compare(key, t.key);

                if(r > 0) {
                    if(t.right == null)
                        return next(t);  //may null
                    else
                        t = t.right;
                } else {
                    if(t.left == null)
                        return t;
                    else
                        t = t.left;
                }
            }

            return null;
        }

        /**
         * Returns the least key greater than or equal to the given key, or null if there is no such key (or the returned key is null).
         *
         * @param key the key, may not contained in this tree
         * @return the least key greater than the given key, or null if there is no such key
         * @throws NullPointerException if the specified key is null and this tree uses natural ordering or its comparator does not permit null keys
         * @throws ClassCastException if the type of the specified key is incompatible and can not be compared with this tree
         */
        @Override
        public K ceilingKey(K key) {
            Entry<K, V> node = ceilingEntry(key);
            return node == null
                    ? null                //no such entry, thus no such key
                    : node.getKey();      //may null if this tree permit null key
        }

        /**
         * 返回 this map 中 keys 的 NavigableSet view，which iterator 迭代 keys in "按 key 排序过的 key 序列".
         *
         * The set is backed by the map, so changes to the map are reflected in the set, and 反之亦然.
         *
         * 如果一个迭代器在穿越 set 期间，this map 被修改了(排除迭代器的 remove() 方法产生的修改)，迭代的结果是未定义的.
         *
         * the set 支持元素删除：Iterator#remove, Set#remove, Set#removeAll, Set#tetainAll, Set#clear 方法，不支持元素添加：Set#add, Set#addAll 方法.
         *
         * @return a navigable set view of the keys in this map
         */
        @Override
        public NavigableSet<K> navigableKeySet() {
            return fullMapView.navigableKeySet();
        }

        /**
         * 返回 this map 中 keys 的 reverse order NavigableSet view，which iterator 迭代 keys in "按 key 排序过的 key 序列"的反序.
         *
         * The set is backed by the map, so changes to the map are reflected in the set, and 反之亦然.
         *
         * 如果一个迭代器在穿越 set 期间，this map 被修改了(排除迭代器的 remove() 方法产生的修改)，迭代的结果是未定义的.
         *
         * the set 支持元素删除：Iterator#remove, Set#remove, Set#removeAll, Set#tetainAll, Set#clear 方法，不支持元素添加：Set#add, Set#addAll 方法.
         *
         * @return a reverse order navigable set view of the keys in this map
         */
        @Override
        public NavigableSet<K> descendingKeySet() {
            return fullMapView.descendingKeySet();
        }

        NavigableMap<K, V> fullMapView = new AscendingMapView();
        /**
         * Returns a reverse order view of the mappings contained in this map.
         *
         * The descending map is backed by this map, so changes to the map are reflected in the descending map, and 反之亦然.
         *
         * If either map is modified while an iteration over a collection view of either map is in progress (except through the iterator's own remove), the results of the iteration are undefined.
         *
         * The returned map has an ordering equivalent to `Collections.reverseOrder(comparator())`.
         *
         * The expression `m.descendingMap().descendingMap()` returns a view essentially equivalent to m.
         *
         * @return a reverse order view of this map
         */
        @Override
        public NavigableMap<K, V> descendingMap() {
            return fullMapView.descendingMap();
        }

        private class SetView extends AbstractSet<K> implements NavigableSet<K> {
            MapView mapView;

            SetView(MapView mapView) {
                this.mapView = mapView;
            }

            @Override
            public int size() {
                return mapView.size();
            }

            @Override
            public boolean isEmpty() {
                return mapView.isEmpty();
            }

            @Override
            public boolean contains(Object k) {
                return mapView.containsKey(k);
            }

            @Override
            public boolean remove(Object k) {
                Node<K, V> node = BSTree.this.entry(k);
                if(node != null) {
                    BSTree.this.removeNode(node);
                    return true;
                }
                return false;
            }

            @Override
            public void clear() {
                mapView.clear();
            }

            @Override
            public Iterator<K> iterator() {
                return new KeyIterator(mapView.createEntryIterator());
            }

            @Override
            public Iterator<K> descendingIterator() {
                return descendingSet().iterator();
            }

            @Override
            public NavigableSet<K> descendingSet() {
                return mapView.descendingKeySet();
            }

            @Override
            public Spliterator<K> spliterator() {
                return null;  //todo
            }

            @Override
            public Comparator<? super K> comparator() {
                return mapView.comparator();
            }

            @Override
            public K first() {
                return mapView.firstKey();
            }

            @Override
            public K last() {
                return mapView.lastKey();
            }

            @Override
            public SortedSet<K> subSet(K fromElement, K toElement) {
                return subSet(fromElement, true, toElement, false);
            }

            @Override
            public SortedSet<K> headSet(K toElement) {
                return headSet(toElement, false);
            }

            @Override
            public SortedSet<K> tailSet(K fromElement) {
                return tailSet(fromElement, true);
            }

            @Override
            public K pollFirst() {
                Entry<K, V> node = mapView.pollFirstEntry();
                return node == null
                        ? null
                        : node.getKey();
            }

            @Override
            public K pollLast() {
                Entry<K, V> node = mapView.pollLastEntry();
                return node == null
                        ? null
                        : node.getKey();
            }

            @Override
            public K lower(K k) {
                return mapView.lowerKey(k);
            }

            @Override
            public K floor(K k) {
                return mapView.floorKey(k);
            }

            @Override
            public K ceiling(K k) {
                return mapView.ceilingKey(k);
            }

            @Override
            public K higher(K k) {
                return mapView.higherKey(k);
            }

            @Override
            public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
                return new SetView((MapView) mapView.subMap(fromElement, fromInclusive, toElement, toInclusive));
            }

            @Override
            public NavigableSet<K> headSet(K toElement, boolean inclusive) {
                return new SetView((MapView) mapView.headMap(toElement, inclusive));
            }

            @Override
            public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
                return new SetView((MapView) mapView.tailMap(fromElement, inclusive));
            }
        }

        /**
         * Returns a view of the portion(部分) of this map whose keys range from fromKey to toKey, fromInclusive, toInclusive defines the bound.
         *
         * The returned map is backed by this map, so changes in the returned map are reflected in this map, and vice-versa.
         *
         * The returned map supports all optional map operations that this map supports.
         *
         * The returned map will throw an `IllegalArgumentException` on an attempt to insert a key outside its range.
         *
         * @param fromKey fromKey
         * @param fromInclusive fromInclusive
         * @param toKey toKey
         * @param toInclusive toInclusive
         * @return a view of the portion of this map
         * @throws ClassCastException         if fromKey and toKey cannot be compared to one another using this map's comparator (or, no comparator, using natural ordering)
         *                                    Implementations may, but are not required to, throw this exception
         * @throws NullPointerException       if fromKey or toKey is null and this map does not permit null keys
         * @throws IllegalArgumentException   if fromKey is greater than toKey;
         *                                 or if this map itself has a restricted range, and fromKey or toKey lies outside the bounds of the range
         */
        @Override
        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return fullMapView.subMap(fromKey, fromInclusive, toKey, toInclusive);
        }

        /**
         * Returns a view of the portion(部分) of this map whose keys < toKey, or <= toKey when inclusive is true.
         *
         * The returned map is backed by this map, so changes in the returned map are reflected in this map, and vice-versa.
         *
         * The returned map supports all optional map operations that this map supports.
         *
         * The returned map will throw an `IllegalArgumentException` on an attempt to insert a key outside its range.
         *
         * @param toKey toKey
         * @param inclusive inclusive
         * @return a view of the portion of this map
         * @throws ClassCastException         if the toKey is not compatible with this map's comparator (or, no comparator, using natural ordering)
         *                                    Implementations may, but are not required to, throw this exception
         * @throws NullPointerException       if fromKey or toKey is null and this map does not permit null keys
         * @throws IllegalArgumentException   if this map itself has a restricted range, and fromKey or toKey lies outside the bounds of the range
         */
        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return fullMapView.headMap(toKey, inclusive);
        }

        /**
         * Returns a view of the portion(部分) of this map whose keys > fromKey, or >= fromKey when inclusive is true.
         *
         * The returned map is backed by this map, so changes in the returned map are reflected in this map, and vice-versa.
         *
         * The returned map supports all optional map operations that this map supports.
         *
         * The returned map will throw an `IllegalArgumentException` on an attempt to insert a key outside its range.
         *
         * @param fromKey fromKey
         * @param inclusive inclusive
         * @return a view of the portion of this map
         * @throws ClassCastException         if the fromKey is not compatible with this map's comparator (or, no comparator, using natural ordering)
         *                                    Implementations may, but are not required to, throw this exception
         * @throws NullPointerException       if fromKey or toKey is null and this map does not permit null keys
         * @throws IllegalArgumentException   if this map itself has a restricted range, and fromKey or toKey lies outside the bounds of the range
         */
        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return fullMapView.tailMap(fromKey, inclusive);
        }

        private abstract class MapView extends AbstractMap<K,V> implements NavigableMap<K,V> {
            K fromKey;
            boolean fromInclusive;
            boolean usingFromKey;
            K toKey;
            boolean toInclusive;
            boolean usingToKey;

            MapView() {
                this.usingFromKey = this.usingToKey = false;
            }

            MapView(K fromKey, boolean fromInclusive, boolean usingFromKey,
                    K toKey, boolean toInclusive, boolean usingToKey) {
                check(fromKey, usingFromKey, toKey, usingToKey);
                this.usingFromKey = usingFromKey;
                this.usingToKey = usingToKey;

                this.fromKey = fromKey;
                this.fromInclusive = fromInclusive;
                this.toKey = toKey;
                this.toInclusive = toInclusive;
            }

            private void check(K k1, boolean usingFromKey, K k2, boolean usingToKey) {
                if(usingFromKey && usingToKey) {
                    int r = BSTree.this.compare(k1, k2);
                    if(BSTree.this.comparator == null && k2 == null)
                        throw new NullPointerException("key is not allowed null for natural ordering");

                    if(r > 0)
                        throw new IllegalArgumentException("the fromKey is greater than toKey");
                } else if(usingFromKey) {
                    BSTree.this.compare(k1, k1);
                } else if(usingToKey) {
                    BSTree.this.compare(k2, k2);
                }
            }

            @Override
            public int size() {
                if(!usingFromKey && !usingToKey)
                    return BSTree.this.size();

                int count = 0;
                for (Entry<K, V> kvEntry : entrySet()) {
                    count++;
                }
                return count;
            }

            @Override
            public boolean isEmpty() {
                return size() == 0;
            }

            boolean inRange(Object key) {
                return inLowerBound(key) && inUpperBound(key);
            }

            boolean inLowerBound(Object key) {
                int r;
                return !usingFromKey || (r = BSTree.this.compare(key, fromKey)) > 0 || (r == 0 && fromInclusive);
            }

            boolean inUpperBound(Object key) {
                int r;
                return !usingToKey || (r = BSTree.this.compare(key, toKey)) < 0 || ( r == 0 && toInclusive);
            }

            @Override
            public boolean containsKey(Object key) {
                return inRange(key) && BSTree.this.containsKey(key);
            }

            @Override
            public boolean containsValue(Object value) {
                for(Entry<K, V> entry : entrySet()) {
                    if(Objects.equals(value, entry.getValue()))
                        return true;
                }
                return false;
            }

            @Override
            public V get(Object key) {
                return inRange(key)
                        ? BSTree.this.get(key)
                        : null;
            }

            @Override
            public V put(K key, V value) {
                if(!inRange(key))
                    throw new IllegalArgumentException("key is not in range");

                return BSTree.this.put(key, value);
            }

            @Override
            public V remove(Object key) {
                return inRange(key)
                        ? BSTree.this.remove(key)
                        : null;
            }

            @Override
            public void putAll(Map<? extends K, ? extends V> m) {
                BSTree.this.putAll(m);
            }

            @Override
            public void clear() {
                if(!usingFromKey && !usingToKey) {
                    BSTree.this.clear();
                    return;
                }
                Iterator<Entry<K, V>> it = entrySet().iterator();
                while(it.hasNext()) {
                    it.next();
                    it.remove();
                }
            }

            @Override
            public V getOrDefault(Object key, V defaultValue) {
                return inRange(key)
                        ? BSTree.this.getOrDefault(key, defaultValue)
                        : defaultValue;
            }

            @Override
            public V putIfAbsent(K key, V value) {
                if(!inRange(key))
                    throw new IllegalArgumentException("key is not in range");

                return BSTree.this.putIfAbsent(key, value);
            }

            @Override
            public boolean remove(Object key, Object value) {
                return inRange(key) && BSTree.this.remove(key, value);
            }

            @Override
            public boolean replace(K key, V oldValue, V newValue) {
                return inRange(key) && BSTree.this.replace(key, oldValue, newValue);
            }

            @Override
            public V replace(K key, V value) {
                return inRange(key)
                        ? BSTree.this.replace(key, value)
                        : null;
            }

            Set<K> keySet;
            @Override
            public Set<K> keySet() {
                if(keySet == null) {
                    keySet = new AbstractSet<K>() {
                        @Override
                        public Iterator<K> iterator() {
                            return new KeyIterator(MapView.this.entrySet().iterator());
                        }

                        @Override
                        public Spliterator<K> spliterator() {
                            return null;  //todo
                        }

                        @Override
                        public int size() {
                            return MapView.this.size();
                        }

                        @Override
                        public boolean isEmpty() {
                            return MapView.this.isEmpty();
                        }

                        @Override
                        public boolean contains(Object k) {
                            return MapView.this.containsKey(k);
                        }

                        @Override
                        public boolean remove(Object k) {
                            if(!inRange(k)) return false;
                            Node<K, V> node = BSTree.this.entry(k);
                            if(node != null) {
                                BSTree.this.removeNode(node);
                                return true;
                            }
                            return false;
                        }

                        @Override
                        public void clear() {
                            MapView.this.clear();
                        }
                    };
                }
                return keySet;
            }

            @Override
            public Collection<V> values() {
                return super.values();
            }

            abstract Iterator<Entry<K, V>> createEntryIterator();

            Set<Entry<K, V>> entrySet;
            @Override
            public Set<Entry<K, V>> entrySet() {
                if(entrySet == null) {
                    entrySet = new AbstractSet<Entry<K, V>>() {

                        @Override
                        public Iterator<Entry<K, V>> iterator() {
                            return MapView.this.createEntryIterator();
                        }

                        @Override
                        public Spliterator<Entry<K, V>> spliterator() {
                            return null;  //todo
                        }

                        @Override
                        public int size() {
                            return MapView.this.size();
                        }

                        @Override
                        public boolean isEmpty() {
                            return MapView.this.isEmpty();
                        }

                        @Override
                        public boolean contains(Object entry) {
                            if(!(entry instanceof BSTree.Node)) return false;
                            BSTree.Node<?, ?> node = (BSTree.Node<?, ?>) entry;
                            if(!inRange(node.key)) return false;

                            BSTree.Node<?, ?> e = BSTree.this.entry(node.key);
                            return e != null && Objects.equals(e.value, node.value);
                        }

                        @Override
                        public boolean remove(Object entry) {
                            if(!(entry instanceof BSTree.Node)) return false;
                            BSTree.Node<?, ?> node = (BSTree.Node<?, ?>) entry;
                            if(!inRange(node.key)) return false;

                            Node<K, V> e = BSTree.this.entry(node.key);
                            if(e != null && Objects.equals(node.value, e.value)) {
                                BSTree.this.removeNode(e);
                                return true;
                            }
                            return false;
                        }

                        @Override
                        public void clear() {
                            MapView.this.clear();
                        }
                    };
                }
                return entrySet;
            }

            @Override
            public Entry<K, V> pollFirstEntry() {
                Entry<K, V> entry = firstEntry();
                if(entry != null) {
                    BSTree.this.removeNode((Node<K, V>) entry);
                    return entry;
                }
                return null;
            }

            @Override
            public Entry<K, V> pollLastEntry() {
                Entry<K, V> entry = lastEntry();
                if(entry != null) {
                    BSTree.this.removeNode((Node<K, V>) entry);
                    return entry;
                }
                return null;
            }

            @Override
            public SortedMap<K, V> subMap(K fromKey, K toKey) {
                return subMap(fromKey, true, toKey, false);
            }

            @Override
            public SortedMap<K, V> headMap(K toKey) {
                return headMap(toKey, false);
            }

            @Override
            public SortedMap<K, V> tailMap(K fromKey) {
                return tailMap(fromKey, true);
            }

            @Override
            public K lowerKey(K key) {
                Entry<K, V> e = lowerEntry(key);
                return e == null
                        ? null
                        : e.getKey();
            }

            @Override
            public K floorKey(K key) {
                Entry<K, V> e = floorEntry(key);
                return e == null
                        ? null
                        : e.getKey();
            }

            @Override
            public K ceilingKey(K key) {
                Entry<K, V> e = ceilingEntry(key);
                return e == null
                        ? null
                        : e.getKey();
            }

            @Override
            public K higherKey(K key) {
                Entry<K, V> e = higherEntry(key);
                return e == null
                        ? null
                        : e.getKey();
            }

            NavigableMap<K, V> descendingMap;

            void setDescendingMap(NavigableMap<K, V> descendingMap) {
                this.descendingMap = descendingMap;
            }

            NavigableSet<K> navigableKeySet;

            @Override
            public NavigableSet<K> navigableKeySet() {
                if(navigableKeySet == null)
                    navigableKeySet = new SetView(this);

                return navigableKeySet;
            }

            @Override
            public NavigableSet<K> descendingKeySet() {
                return this.descendingMap().navigableKeySet();
            }
        }

        private class DescendingMapView extends MapView {

            DescendingMapView() {}

            DescendingMapView(K fromKey, boolean fromInclusive, boolean usingFromKey,
                              K toKey, boolean toInclusive, boolean usingToKey) {
                super(fromKey, fromInclusive, usingFromKey, toKey, toInclusive, usingToKey);
            }

            @Override
            Iterator<Entry<K, V>> createEntryIterator() {
                return new DescendingEntryIterator((Node<K, V>) first(), (Node<K, V>) fence());
            }

            Entry<K, V> first() {
                return !usingToKey ? BSTree.this.lastEntry()
                        : toInclusive ? BSTree.this.floorEntry(toKey)
                        : BSTree.this.lowerEntry(toKey);
            }

            Entry<K, V> fence() {
                return !usingFromKey ? null
                        : fromInclusive ? BSTree.this.lowerEntry(fromKey)
                        : BSTree.this.floorEntry(fromKey);
            }

            @Override
            public Comparator<? super K> comparator() {
                return Collections.reverseOrder(BSTree.this.comparator());
            }

            @Override
            public K firstKey() {
                return !usingToKey ? BSTree.this.lastKey()
                        : toInclusive ? ceilingKey(toKey)
                        : higherKey(toKey);
            }

            @Override
            public K lastKey() {
                return !usingFromKey ? BSTree.this.firstKey()
                        : fromInclusive ? floorKey(fromKey)
                        : lowerKey(fromKey);
            }

            @Override
            public Entry<K, V> firstEntry() {
                return !usingToKey ? BSTree.this.lastEntry()
                        : toInclusive ? ceilingEntry(toKey)
                        : higherEntry(toKey);
            }

            @Override
            public Entry<K, V> lastEntry() {
                return !usingFromKey ? BSTree.this.firstEntry()
                        : fromInclusive ? floorEntry(fromKey)
                        : lowerEntry(fromKey);
            }

            @Override
            public Entry<K, V> lowerEntry(K key) {
                Entry<K, V> e = BSTree.this.higherEntry(key);
                return e == null || !inRange(e.getKey())
                        ? null
                        : e;
            }

            @Override
            public Entry<K, V> floorEntry(K key) {
                Entry<K, V> e = BSTree.this.ceilingEntry(key);
                return e == null || !inRange(e.getKey())
                        ? null
                        : e;
            }

            @Override
            public Entry<K, V> ceilingEntry(K key) {
                Entry<K, V> e = BSTree.this.floorEntry(key);
                return e == null || !inRange(e.getKey())
                        ? null
                        : e;
            }

            @Override
            public Entry<K, V> higherEntry(K key) {
                Entry<K, V> e = BSTree.this.lowerEntry(key);
                return e == null || !inRange(e.getKey())
                        ? null
                        : e;
            }

            @Override
            public NavigableMap<K, V> descendingMap() {
                if(descendingMap == null) {
                    descendingMap = new AscendingMapView(fromKey, fromInclusive, usingFromKey,
                            toKey, toInclusive, usingToKey);
                    ((AscendingMapView) descendingMap).setDescendingMap(this);
                }
                return descendingMap;
            }

            @Override
            public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
                if(!inRange(fromKey) || !inRange(toKey))
                    throw new IllegalArgumentException("key is not in range");

                return new DescendingMapView(fromKey, fromInclusive, true, toKey, toInclusive, true);
            }

            @Override
            public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
                if(!inRange(toKey))
                    throw new IllegalArgumentException("key is not in range");

                return new DescendingMapView(null, false, false, toKey, inclusive, true);
            }

            @Override
            public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
                if(!inRange(fromKey))
                    throw new IllegalArgumentException("key is not in range");

                return new DescendingMapView(fromKey, inclusive, true, null, false, false);
            }
        }

        private class AscendingMapView extends MapView {

            AscendingMapView() {}

            AscendingMapView(K fromKey, boolean fromInclusive, boolean usingFromKey,
                             K toKey, boolean toInclusive, boolean usingToKey) {
                super(fromKey, fromInclusive, usingFromKey, toKey, toInclusive, usingToKey);
            }

            @Override
            Iterator<Entry<K, V>> createEntryIterator() {
                return new AscendingEntryIterator((Node<K, V>) first(), (Node<K, V>) fence());
            }

            Entry<K, V> first() {
                return !usingFromKey ? BSTree.this.firstEntry()
                        : fromInclusive ? BSTree.this.ceilingEntry(fromKey)
                        : BSTree.this.higherEntry(toKey);
            }

            Entry<K, V> fence() {
                return !usingToKey ? null
                        : toInclusive ? BSTree.this.higherEntry(toKey)
                        : BSTree.this.ceilingEntry(toKey);
            }

            @Override
            public Comparator<? super K> comparator() {
                return BSTree.this.comparator();
            }

            @Override
            public K firstKey() {
                return !usingFromKey ? BSTree.this.firstKey()
                        : fromInclusive ? ceilingKey(fromKey)
                        : higherKey(fromKey);
            }

            @Override
            public K lastKey() {
                return !usingToKey ? BSTree.this.lastKey()
                        : toInclusive ? floorKey(toKey)
                        : lowerKey(toKey);
            }

            @Override
            public Entry<K, V> firstEntry() {
                return !usingFromKey ? BSTree.this.firstEntry()
                        : fromInclusive ? ceilingEntry(fromKey)
                        : higherEntry(fromKey);
            }

            @Override
            public Entry<K, V> lastEntry() {
                return !usingToKey ? BSTree.this.lastEntry()
                        : toInclusive ? floorEntry(toKey)
                        : lowerEntry(toKey);
            }

            @Override
            public Entry<K, V> lowerEntry(K key) {
                Entry<K, V> e = BSTree.this.lowerEntry(key);
                return e == null || !inRange(e.getKey())
                        ? null
                        : e;
            }

            @Override
            public Entry<K, V> floorEntry(K key) {
                Entry<K, V> e = BSTree.this.floorEntry(key);
                return e == null || !inRange(e.getKey())
                        ? null
                        : e;
            }

            @Override
            public Entry<K, V> ceilingEntry(K key) {
                Entry<K, V> e = BSTree.this.ceilingEntry(key);
                return e == null || !inRange(e.getKey())
                        ? null
                        : e;
            }

            @Override
            public Entry<K, V> higherEntry(K key) {
                Entry<K, V> e = BSTree.this.higherEntry(key);
                return e == null || !inRange(e.getKey())
                        ? null
                        : e;
            }

            @Override
            public NavigableMap<K, V> descendingMap() {
                if(descendingMap == null) {
                    descendingMap = new DescendingMapView(fromKey, fromInclusive, usingFromKey,
                            toKey, toInclusive, usingToKey);
                    ((DescendingMapView) descendingMap).setDescendingMap(this);
                }
                return descendingMap;
            }

            @Override
            public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
                if(!inRange(fromKey) || !inRange(toKey))
                    throw new IllegalArgumentException("key is not in range");

                return new AscendingMapView(fromKey, fromInclusive, true, toKey, toInclusive, true);
            }

            @Override
            public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
                if(!inRange(toKey))
                    throw new IllegalArgumentException("key is not in range");

                return new AscendingMapView(null, false, false, toKey, inclusive, true);
            }

            @Override
            public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
                if(!inRange(fromKey))
                    throw new IllegalArgumentException("key is not in range");

                return new AscendingMapView(fromKey, inclusive, true, null, false, false);
            }
        }

        static class Node<K, V> implements Entry<K, V> {
            /*final*/ K key;
            V value;

            Node<K, V> parent;
            Node<K, V> left, right;

            Node(K key, V value, Node<K, V> parent) {
                this.key = key;
                this.value = value;
                this.parent = parent;
            }

            @Override
            public K getKey() {
                return this.key;
            }

            @Override
            public V getValue() {
                return this.value;
            }

            @Override
            public V setValue(V value) {
                V oldV = this.value;
                this.value = value;
                return oldV;
            }
        }


        //// Tree

        @Override
        public void L_T_R() {
            L_T_R0(this.root);
            System.out.println("L_T_R0");
            L_T_R1(this.root);
            System.out.println("L_T_R1");
            L_T_R2(this.root);
            System.out.println("L_T_R2\n");
        }

        static <K, V> void L_T_R0(Node<K, V> node) {  //递归
            if(node == null) return;

            L_T_R0(node.left);
            System.out.print(node.key + " ");
            L_T_R0(node.right);
        }

        static <K, V> Node<K, V> firstKey_L_T_R(Node<K, V> node) {
            if(node == null) return null;

            while(node.left != null) {
                node = node.left;
            }
            return node;
        }

        static <K, V> void L_T_R1(Node<K, V> node) {  //回溯
            if(node == null) return;
            Node<K, V> finish = node.parent;

            Node<K, V> t = firstKey_L_T_R(node);
            while(t != finish) {
                System.out.print(t.key + " ");

                if(t.right != null) {
                    t = firstKey_L_T_R(t.right);
                } else {
                    Node<K, V> pt = t.parent;

                    while(pt != finish && pt.right == t) {
                        t = pt;
                        pt = t.parent;
                    }

                    t = pt;
                }
            }
        }

        static <K, V> void pushToStack_L_T_R(FixedArrayDeque<Node<K, V>> stack, Node<K, V> node) {
            while(node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        static <K, V> void L_T_R2(Node<K, V> node) {  //stack
            if(node == null) return;

            FixedArrayDeque<Node<K, V>> stack = new FixedArrayDeque<>();
            pushToStack_L_T_R(stack, node);

            while(!stack.isEmpty()) {
                Node<K, V> t = stack.pop();
                System.out.print(t.key + " ");

                pushToStack_L_T_R(stack, t.right);
            }
        }

        @Override
        public void T_L_R() {
            T_L_R0(this.root);
            System.out.println("T_L_R0");
            T_L_R1(this.root);
            System.out.println("T_L_R1");
            T_L_R2(this.root);
            System.out.println("T_L_R2\n");
        }

        static <K, V> void T_L_R0(Node<K, V> node) {  //递归
            if(node == null) return;

            System.out.print(node.key + " ");
            T_L_R0(node.left);
            T_L_R0(node.right);
        }

        static <K, V> void T_L_R1(Node<K, V> node) {  //回溯
            if(node == null) return;
            Node<K, V> finish = node.parent;

            Node<K, V> t = node;
            while(t != finish) {
                System.out.print(t.key + " ");

                if(t.left != null) {
                    t = t.left;
                } else {
                    Node<K, V> pt = t;

                    while(pt != finish && (pt.right == null || pt.right == t)) {
                        t = pt;
                        pt = pt.parent;
                    }

                    if(pt != finish)
                        t = pt.right;
                    else t = pt;  //pt == finish
                }
            }
        }

        static <K, V> void T_L_R2(Node<K, V> node) {  //stack
            if(node == null) return;

            FixedArrayDeque<Node<K, V>> stack = new FixedArrayDeque<>(4);
            stack.push(node);

            while(!stack.isEmpty()) {
                Node<K, V> t = stack.pop();
                System.out.print(t.key + " ");

                if(t.right != null)
                    stack.push(t.right);
                if(t.left != null)
                    stack.push(t.left);
            }
        }

        @Override
        public void L_R_T() {
            L_R_T0(this.root);
            System.out.println("L_R_T0");
            L_R_T1(this.root);
            System.out.println("L_R_T1");
            L_R_T2(this.root);
            System.out.println("L_R_T2\n");
        }

        static <K, V> void L_R_T0(Node<K, V> node) {  //递归
            if(node == null) return;

            L_R_T0(node.left);
            L_R_T0(node.right);
            System.out.print(node.key + " ");
        }

        static <K, V> Node<K, V> firstKey_L_R_T(Node<K, V> node) {
            if(node == null) return null;

            while(node.left != null) {
                node = node.left;
            }
            if(node.right == null) return node;
            else
                return firstKey_L_R_T(node.right);
        }

        static <K, V> void L_R_T1(Node<K, V> node) {  //回溯
            if(node == null) return;
            Node<K, V> finish = node.parent;

            Node<K, V> t = firstKey_L_R_T(node);
            while(t != finish) {
                System.out.print(t.key + " ");

                Node<K, V> pt = t.parent;
                if(pt != finish && pt.left == t && pt.right != null)
                    t = firstKey_L_R_T(pt.right);
                else t = pt;
            }
        }

        static <K, V> void pushToStack_L_R_T(FixedArrayDeque<Node<K, V>> stack, Node<K ,V> node) {
            if(node == null) return;

            stack.push(node);
            while(node.left != null) {
                node = node.left;
                stack.push(node);
            }
            if(node.right != null)
                pushToStack_L_R_T(stack, node.right);
        }

        static <K, V> void L_R_T2(Node<K, V> node) {  //stack
            if(node == null) return;
            Node<K, V> finish = node.parent;

            FixedArrayDeque<Node<K, V>> stack = new FixedArrayDeque<>();
            pushToStack_L_R_T(stack, node);

            while(!stack.isEmpty()) {
                Node<K, V> t = stack.pop();
                System.out.print(t.key + " ");

                Node<K, V> pt = t.parent;
                if(pt != finish && pt.left == t && pt.right != null)
                    pushToStack_L_R_T(stack, pt.right);
            }
        }

        static <K, V> Node<K, V> firstKey_R_T_L(Node<K, V> node) {
            if(node == null) return null;

            while(node.right != null) {
                node = node.right;
            }
            return node;
        }

        @Override
        public void BFS() {
            Node<K, V> node = this.root;
            if(node == null) return;

            FixedArrayDeque<Node<K, V>> queue = new FixedArrayDeque<>();
            queue.offer(node);

            while(!queue.isEmpty()) {
                Node<K, V> t = queue.poll();
                System.out.print(t.key + " ");

                if(t.left != null)
                    queue.offer(t.left);
                if(t.right != null)
                    queue.offer(t.right);
            }

            System.out.println("BFS");
        }

        //successor(后继): 有序序列中给定节点的后面一个节点(互异序列中，大于给定节点值的最小节点)
        Node<K, V> next(Node<K, V> t) {
            if(t != null) {
                if(t.right != null)
                    return firstKey_L_T_R(t.right);  //not null
                else {
                    Node<K, V> pt = t.parent;

                    while(pt != null && pt.right == t) {
                        t = pt;
                        pt = t.parent;
                    }

                    return pt;  //may null
                }
            }
            return null;
        }

        //predecessor(前驱): 有序序列中给定节点的前面一个节点(互异序列中，小于给定节点值的最大节点)
        Node<K, V> prev(Node<K, V> t) {
            if(t != null) {
                if(t.left != null)
                    return firstKey_R_T_L(t.left); //not null
                else {
                    Node<K, V> pt = t.parent;

                    while(pt != null && pt.left == t) {
                        t = pt;
                        pt = t.parent;
                    }

                    return pt;  //may null
                }
            }
            return null;
        }
    }

    public interface Tree {
        void L_T_R();
        void T_L_R();
        void L_R_T();
        void BFS();
    }

    public static void main(String[] argv) {
        BSTree<Integer, Integer> tree = new BSTree<>();

        tree.put(18, 18);
        tree.put(12, 12);
        tree.put(10, 10);
        tree.put(7, 7);
        tree.put(14, 14);
        tree.put(15, 15);
        tree.put(21, 21);
        tree.put(19, 19);
        tree.put(24, 24);
        tree.put(22, 22);
        tree.put(25, 25);
        tree.put(23, 23);
        tree.put(13, 13);

        tree.L_T_R();
        tree.T_L_R();
        tree.L_R_T();
        tree.BFS();
        System.out.println("--------------------------------------------遍历-------------------------------------------");

        System.out.println("least key: " + BSTree.firstKey_L_T_R(tree.root).key);
        System.out.println("greatest key: " + BSTree.firstKey_R_T_L(tree.root).key);
        System.out.println("19'prev: " + tree.prev(tree.entry(19)).key);
        System.out.println("19'next: " + tree.next(tree.entry(19)).key);

        Integer k = 19;
        System.out.println("greater than " + k + ": " + tree.higherKey(k));
        System.out.println("greater than or eq to " + k + ": " + tree.ceilingKey(k));

        System.out.println("--------------------------------------------前驱、后继--------------------------------------");

        tree.remove(7);
        tree.remove(13, 1333);
        tree.BFS();

        tree.remove(21);
        tree.BFS();

        tree.remove(19);
        tree.remove(18);
        tree.BFS();

        System.out.println("--------------------------------------------删除---------------------------------------------");

        Iterator<Map.Entry<Integer, Integer>> it = tree.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, Integer> node = it.next();
            System.out.print(node.getKey() + " ");
            if(node.getKey().equals(12) || node.getKey().equals(24))
                it.remove();
        }
        System.out.println();
        tree.BFS();

        System.out.println("--------------------------------------------entry set---------------------------------------");

        NavigableMap<Integer, Integer> fullReverseMapView = tree.descendingMap();
        System.out.println(fullReverseMapView.descendingMap().descendingMap() == fullReverseMapView);  //true

        NavigableSet<Integer> fullSetView = tree.navigableKeySet();
        System.out.println(fullSetView == fullReverseMapView.descendingKeySet());  //true

        NavigableSet<Integer> fullReverseSetView = tree.descendingKeySet();
        System.out.println(fullReverseSetView == fullReverseMapView.navigableKeySet());  //true

        NavigableMap<Integer, Integer> subMapView =
                tree.subMap(11, true, 23, false);
        for(Map.Entry<Integer, Integer> entry : subMapView.entrySet()) {
            System.out.print(entry.getKey() + " ");
        }
        System.out.println("subMapView");

        NavigableSet<Integer> subSetView = subMapView.navigableKeySet();
        for(Integer key : subSetView) {
            System.out.print(key + " ");
        }
        System.out.println("subSetView");

        NavigableSet<Integer> subReverseSetView = subSetView.descendingSet();
        System.out.println(subReverseSetView.descendingSet() == subSetView);  //true

        for(Integer key : subReverseSetView) {
            System.out.print(key + " ");
        }
        System.out.println("subReverseSetView");

        System.out.println("--------------------------------------------MapView、SetView--------------------------------");
    }


    //练习 12.1-3
    //栈: BSTree#L_T_R2(BSTree.Node), 回溯法: BSTree#L_T_R1(BSTree.Node)

    //练习 12.1-4
    // BSTree#T_L_R(), BSTree#L_R_T()


    //练习 12.2-3
    // BSTree#prev(Node<K, V>)

    //练习 12.2-7
    // BSTree#L_T_R1(BSTree.Node)
    // 运行时间: 每个节点最多被访问 3 次，n 个节点共被访问 3*n 次。故 T(n) = Θ(n)


    //练习 12.3-3
    // n * f(h). f(h)'max = Θ(n), f(h)'min = Θ(lgn)

    //练习12.3-5
    /*
    static class Node {
        int key;
        Node parent;
        Node left;
        Node right;
        Node succ; //额外 succ 指针，指向后继

        public Node(int key, Node parent) {
            this.key = key;
            this.parent = parent;
        }
    }
    L_T_R:
        if(this.root == null) return;
        Node t = firstKey_L_T_R(this.root);
        while(t != null) {
            print t.key;
            t = t.succ;
        }

    put(int e): 额外维护 succ 指针
        if(this.root == null) {
            this.root = new Node(e, null);
        } else {
            Node t = root;
            Node pt = null;
            while(t != null) {
                if(e < t.key) {
                    pt = t;
                    t = t.left;
                } else if(e > t.key) {
                    pt = t;
                    t = t.right;
                } else {           //duplicate key
                    t.key = e;
                    return;
                }
            }
            Node t = new Node(e, pt);
            if(e < pt.key) {
                pt.left = t;

                Node r = pt;
                Node pr = r.parent;
                while(pr != null && pr.left == r) {
                    r = pr;
                    pr = pr.parent;
                }

                if(pr == null)
                    t.succ = pt
                else {
                    pr.succ = t;
                    t.succ = pt;
                }
            } else {
                pt.right = t;

                Node r = pt;
                Node pr = r.parent;
                while(pr != null && pr.right == r) {
                    r = pr;
                    pr = pr.parent;
                }
                
                if(pr == null)
                    pt.succ = t
                else {
                    pt.succ = t;
                    t.succ = pr;
                }
            }
        }
        this.size++;

     remove(int e):
        只需额外维护 succ 指针，将 t 的前驱.succ = next 即可
     */

    //练习12.3-6
    /* 修改 removeNode(Node<K, V>:
         if (t.left != null && t.right != null) {
            Node<K, V> ps = firstKey_R_T_L(t.left);  //not null
            t.key = ps.key;
            t.value = ps.value;
            t = ps;
         }
     */

}
