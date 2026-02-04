package com.juice.alg.part2.chapter6;

import com.juice.alg.part1.chapter2.Chapter2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class Chapter6_Practice1 {
    //练习6.1-4: 位于叶子节点

    //练习6.5-3
    static class MinPriorityQueue<T> extends AbstractPriorityQueue<T> {

        public MinPriorityQueue() {
            super(false);
        }

        @Override
        public Chapter6_5.Node<T> min() {
            return peek();
        }

        @Override
        public Chapter6_5.Node<T> max() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Chapter6_5.Node<T> next() {
            return poll();
        }
    }

    abstract static class AbstractPriorityQueue<T> {
        private final List<Chapter6_5.Node<T>> queue;
        private final BiFunction<Chapter6_5.Node<?>, Chapter6_5.Node<?>, Boolean> func;
        private final boolean max;

        //max=true: 最大优先队列
        AbstractPriorityQueue(boolean max) {
            this.queue = new ArrayList<>();
            this.max = max;
            this.func = max ? Chapter6_5.Node::lt : Chapter6_5.Node::gt;
        }

        public void insert(Chapter6_5.Node<T> node) {
            //assert node != null
            queue.add(node);
            heapify(this.queue.size()-1);
        }

        private void heapify(int i) {
            int ci = i;
            int pi = (i+1)/2 - 1;
            Chapter6_5.Node<T> i_node = this.queue.get(i);

            while(pi >= 0 && func.apply(this.queue.get(pi), i_node)) {
                this.queue.set(ci, this.queue.get(pi));

                ci = pi;
                pi = (ci+1)/2 - 1;
            }
            if(ci != i)
                this.queue.set(ci, i_node);
        }

        protected Chapter6_5.Node<T> peek() {
            if (this.queue.size() == 0) return null;
            return this.queue.get(0);
        }

        public abstract Chapter6_5.Node<T> min();
        public abstract Chapter6_5.Node<T> max();

        protected Chapter6_5.Node<T> poll() {
            if (this.queue.size() == 0) return null;
            if(this.queue.size() == 1) return this.queue.remove(0);

            Chapter6_5.Node<T> ret = this.queue.get(0);
            this.queue.set(0, this.queue.remove(this.queue.size() - 1));
            heapify(0, this.queue.size());
            return ret;
        }

        public abstract Chapter6_5.Node<T> next();

        private void heapify(int i, int heap_size) {
            //assert heap_size <= this.queue.size() : IndexOutOfRangeError
            int li = 2*i + 1;
            int ri = 2*i + 2;

            int head = i;
            if(li < heap_size && /*li < this.queue.size() &&*/ func.apply(this.queue.get(head), this.queue.get(li))) {
                head = li;
            }
            if(ri < heap_size && /*ri < this.queue.size() &&*/ func.apply(this.queue.get(head), this.queue.get(ri))) {
                head = ri;
            }

            if(head != i) {
                Chapter6_5.Node<T> ex = this.queue.get(head);
                this.queue.set(head, this.queue.get(i));
                this.queue.set(i, ex);
                heapify(head, heap_size);
            }
        }

        public void increase(Chapter6_5.Node<T> node, int added) {
            int queueSize = this.queue.size();

            for(int i=0; i<queueSize; i++) {
                Chapter6_5.Node<T> e;

                if((e = this.queue.get(i)).equals(node)) {

                    int old_weight = e.increaseWeight(added);
                    int new_weight = e.weight;

                    //从集合角度理解 if-else-if
                    if(max && old_weight < new_weight) {
                        heapify(i);
                    } else if(max && old_weight > new_weight) {
                        heapify(i, queueSize);
                    } else if(old_weight < new_weight) {
                        heapify(i, queueSize);
                    } else if(old_weight > new_weight) {
                        heapify(i);
                    }

                    /*if(max) {
                        if (old_weight > new_weight) {
                            heapify(i, this.queue.size());
                        }
                        if (old_weight < new_weight) {
                            heapify(i);
                        }
                    } else {
                        if (old_weight > new_weight) {
                            heapify(i);
                        }
                        if (old_weight < new_weight) {
                            heapify(i, this.queue.size());
                        }
                    }*/

                    break;
                }
            }
        }

        @Override
        public String toString() {
            return "PriorityQueue{" +
                    " queue=[" + queue.stream().map(node -> node.weight +"").reduce((w1, w2) -> w1 + ", " + w2).orElse("") +
                    "] }";
        }
    }

    //练习6.5-9: k个有序链表合并成一个有序链表，时间复杂度O(n*lgk)
    /*
    方法一:
        [k 个链表]
        [k/2 个链表] [k/2 个链表]                               1    1 * n
        [k/4 个链表] [k/4 个链表] [k/4 个链表] [k/4 个链表]        2    2 * n/2
        ...
        [1 个链表] [1 个链表] ...                               x     2^(x-1) * n/2^(x-1)

        k/2^x = 1, k = 2^x, x = lgk

        1 * n + 2 * n/2 + ... + 2^(x-1) * n/2^(x-1) = n * lgk
     方法二:
        使用最小堆完成 k 路归并
     */
    public void merge_list(int[] a) {  //方法一
        if(a == null || a.length == 0) return;

        List<Integer> k_list = build_k_list();
        check_k_list(k_list, a.length);
        if(k_list == null || k_list.size() == 0) return;

        merge_list(a, k_list);
    }
    private void merge_list(int[] a, List<Integer> k_list) {
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

    //方法二
    public void merge_list(int[][] a) {
        if(a == null || a.length == 0) return;

        StringBuilder sb = new StringBuilder();
        MinPriorityQueue<V> minPriorityQueue = new MinPriorityQueue<>();
        for(int i=0; i<a.length; i++) {
            if(a[i].length > 0) {
                Chapter6_5.Node<V> node = Chapter6_5.Node.build(a[i][0], new V(i, 0));
                minPriorityQueue.insert(node);
            }
        }

        Chapter6_5.Node<V> min;
        while((min = minPriorityQueue.next()) != null) {
            sb.append(min.weight).append(" ");
            int i, j;
            if((j = min.value.j + 1) < a[i = min.value.i].length) {
                minPriorityQueue.insert(Chapter6_5.Node.build(a[i][j], new V(i, j)));
            }
        }

        System.out.println(sb);
    }
    static class V {
        int i;
        int j;

        V(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            V v = (V) o;
            return i == v.i && j == v.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }

        @Override
        public String toString() {
            return "V{" +
                    "i=" + i +
                    ", j=" + j +
                    '}';
        }
    }

    public static void main(String[] argv) {
        MinPriorityQueue<Integer> minPriorityQueue = new MinPriorityQueue<>();

        minPriorityQueue.insert(Chapter6_5.Node.build(16, 16));
        minPriorityQueue.insert(Chapter6_5.Node.build(14, 14));
        minPriorityQueue.insert(Chapter6_5.Node.build(10, 10));
        minPriorityQueue.insert(Chapter6_5.Node.build(1, 1));
        minPriorityQueue.insert(Chapter6_5.Node.build(4, 4));
        minPriorityQueue.insert(Chapter6_5.Node.build(8, 8));

        Chapter6_5.Node<Integer> node2 = Chapter6_5.Node.build(2, 2);
        minPriorityQueue.insert(node2);

        minPriorityQueue.insert(Chapter6_5.Node.build(7, 7));
        minPriorityQueue.insert(Chapter6_5.Node.build(9, 9));
        minPriorityQueue.insert(Chapter6_5.Node.build(3, 3));

        System.out.println(minPriorityQueue);
        System.out.println(minPriorityQueue.min());
        System.out.println("#########################################################################");

        System.out.println(minPriorityQueue);
        System.out.println(minPriorityQueue.next());
        System.out.println(minPriorityQueue);
        System.out.println("#########################################################################");

        System.out.println(minPriorityQueue);
        minPriorityQueue.increase(node2, 100);
        System.out.println("increase node2: 100");
        System.out.println(minPriorityQueue);
        System.out.println("#########################################################################");


        Chapter6_Practice1 chapter6_Practice1 = new Chapter6_Practice1();

        int[] a = new int[]{1,2,3, 5,7,9, 2,3,4,5,6, 1,1,2,2,3,3,4,5,7, -5,-2,-1};
        chapter6_Practice1.merge_list(a);
        System.out.println(Arrays.toString(a));
        System.out.println("#########################################################################");

        chapter6_Practice1.merge_list(new int[][] {{1,2,3}, {5,7,9}, {2,3,4,5,6}, {}, {1,1,2,2,3,3,4,5,7}, {-5,-2,-1}});

    }



}
