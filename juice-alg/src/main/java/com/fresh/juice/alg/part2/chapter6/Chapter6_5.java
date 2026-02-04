package com.fresh.juice.alg.part2.chapter6;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chapter6_5 {

    //(最大堆)最大优先队列
    static class MaxPriorityQueue {
        private final List<Node<Integer>> queue;

        public MaxPriorityQueue() {
            queue = new ArrayList<>();
        }

        public void insert(Node<Integer> node) {
            //assert node != null
            queue.add(node);
            heapify(this.queue.size()-1);
        }

        private void heapify(int i) {

            int ci = i;
            int pi = (ci+1)/2 - 1;
            Node<Integer> i_node = this.queue.get(i);
            while (pi >= 0 && this.queue.get(pi).lt(i_node)) {
                this.queue.set(ci, this.queue.get(pi));

                ci = pi;
                pi = (ci+1)/2 - 1;
            }
            if(ci != i)
                this.queue.set(ci, i_node);
        }

        public Node<Integer> max() {
            if (this.queue.size() == 0) return null;
            return this.queue.get(0);
        }

        public Node<Integer> next() {
            if (this.queue.size() == 0) return null;
            if(this.queue.size() == 1) return this.queue.remove(0);

            Node<Integer> ret = this.queue.get(0);
            this.queue.set(0, this.queue.remove(this.queue.size() - 1));
            max_heapify(0, this.queue.size());
            return ret;
        }

        private void max_heapify(int i, int heap_size) {
            //assert heap_size <= this.queue.size() : IndexOutOfRangeError
            int li = 2*i + 1;
            int ri = 2*i + 2;

            int max = i;
            if(li < heap_size && /*li < this.queue.size() &&*/ this.queue.get(max).lt(this.queue.get(li))) {
                max = li;
            }
            if(ri < heap_size && /*ri < this.queue.size() &&*/ this.queue.get(max).lt(this.queue.get(ri))) {
                max = ri;
            }

            if(max != i) {
                Node<Integer> ex = this.queue.get(max);
                this.queue.set(max, this.queue.get(i));
                this.queue.set(i, ex);
                max_heapify(max, heap_size);
            }
        }

        public void increase(Node<Integer> node, int added) {

            for(int i=0; i<this.queue.size(); i++) {
                Node<Integer> e;

                if((e=this.queue.get(i)).equals(node)) {
                    int old_weight = e.weight;
                    int new_weight = old_weight + added;
                    e.weight = new_weight;

                    if(old_weight > new_weight) {
                        max_heapify(i, this.queue.size());
                    }
                    if(old_weight < new_weight) {
                        heapify(i);
                    }

                    break;
                }
            }

        }

        @Override
        public String toString() {
            return "MaxPriorityQueue{" +
                    " queue=[" + queue.stream().map(node -> node.weight +"").reduce((w1, w2) -> w1 + ", " + w2).orElse("") +
                    "] }";
        }
    }

    static class Node<T> {
        int weight;
        final T value;
        Node(int weight, T value) {
            this.weight = weight;
            this.value = value;
        }

        static <T> Node<T> build(int weight, T value) {
            return new Node<>(weight, value);
        }

        public boolean gt(Node<?> other) {
            return other == null || this.weight > other.weight;
        }

        public boolean lt(Node<?> other) {
            return other != null && this.weight < other.weight;
        }

        public int increaseWeight(int added) {
            int oldWeight = this.weight;
            this.weight += added;
            return oldWeight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            /*boolean flag = weight == node.weight;
            flag &= Objects.equals(value, node.value);
            return flag;*/
            return Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            /*int hashCode = Integer.hashCode(oldWeight);
            hashCode = 29*hashCode + Objects.hashCode(value);
            return hashCode;*/
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "weight=" + weight +
                    ", value=" + value +
                    '}';
        }
    }

    public static void main(String[] argv) {
        MaxPriorityQueue maxPriorityQueue = new MaxPriorityQueue();

        maxPriorityQueue.insert(Node.build(16, 16));

        Node<Integer> node14 = Node.build(14, 14);
        maxPriorityQueue.insert(node14);

        maxPriorityQueue.insert(Node.build(10, 10));
        maxPriorityQueue.insert(Node.build(1, 1));
        maxPriorityQueue.insert(Node.build(4, 4));
        maxPriorityQueue.insert(Node.build(8, 8));
        maxPriorityQueue.insert(Node.build(2, 2));
        maxPriorityQueue.insert(Node.build(7, 7));

        Node<Integer> node9 = Node.build(9, 9);
        maxPriorityQueue.insert(node9);

        maxPriorityQueue.insert(Node.build(3, 3));

        System.out.println(maxPriorityQueue);
        System.out.println(maxPriorityQueue.max());
        System.out.println("#########################################################################");

        System.out.println(maxPriorityQueue);
        System.out.println(maxPriorityQueue.next());
        System.out.println(maxPriorityQueue);
        System.out.println("#########################################################################");

        System.out.println(maxPriorityQueue);
        maxPriorityQueue.increase(node9, 6);
        System.out.println("increase node9: 6");
        System.out.println(maxPriorityQueue);
        System.out.println("#########################################################################");

        System.out.println(maxPriorityQueue);
        maxPriorityQueue.increase(node14, -12);
        System.out.println("increase node14: -12");
        System.out.println(maxPriorityQueue);

    }
}
