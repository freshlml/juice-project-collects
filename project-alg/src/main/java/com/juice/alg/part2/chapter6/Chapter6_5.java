package com.juice.alg.part2.chapter6;

import java.util.ArrayList;
import java.util.List;

public class Chapter6_5 {



    public static void main(String argv[]) {
        MaxPriorityQueue maxPriorityQueue = new MaxPriorityQueue();

        maxPriorityQueue.insert(Node.build(16, 16));
        Node node14 = Node.build(14, 14);
        maxPriorityQueue.insert(node14);
        maxPriorityQueue.insert(Node.build(10, 10));
        maxPriorityQueue.insert(Node.build(1, 1));
        maxPriorityQueue.insert(Node.build(4, 4));
        maxPriorityQueue.insert(Node.build(8, 8));
        maxPriorityQueue.insert(Node.build(2, 2));
        maxPriorityQueue.insert(Node.build(7, 7));
        Node node9 = Node.build(9, 9);
        maxPriorityQueue.insert(node9);
        maxPriorityQueue.insert(Node.build(3, 3));
        System.out.println(maxPriorityQueue);
        System.out.println(maxPriorityQueue.max());
        System.out.println(maxPriorityQueue);
        System.out.println(maxPriorityQueue.next());
        System.out.println(maxPriorityQueue);
        maxPriorityQueue.increase(node9, 6);
        maxPriorityQueue.increase(node14, -12);
        System.out.println(maxPriorityQueue);

    }


    //(最大堆)最大优先队列
    static class MaxPriorityQueue {
        private List<Node> queue;

        public MaxPriorityQueue() {
            queue = new ArrayList<>();
        }

        public void insert(Node n) {
            //assert n != null
            queue.add(n);
            heapify(this.queue.size()-1);
        }

        private void heapify(int i) {

            int ci = i;
            int pi = (ci+1)/2 - 1;
            Node ci_node = this.queue.get(ci);
            while (pi >= 0 && this.queue.get(pi).lt(ci_node)) {
                this.queue.set(ci, this.queue.get(pi));

                ci = pi;
                pi = (ci+1)/2 - 1;
            }
            if(ci != i)
                this.queue.set(ci, ci_node);
        }

        public Node max() {
            if (this.queue.size() == 0) return null;
            return this.queue.get(0);
        }

        public Node next() {
            if (this.queue.size() == 0) return null;
            if(this.queue.size() == 1) this.queue.remove(0);
            Node ret = this.queue.get(0);
            this.queue.set(0, this.queue.remove(this.queue.size()-1));
            max_heapify(0, this.queue.size());
            return ret;
        }

        private void max_heapify(int i, int heap_size) { //heap_size必须<=this.queue.size(),否则IndexOutOfRangeError

            int li = 2*(i+1)-1;
            int ri = 2*(i+1);

            int max = i;
            if(!isOutRange(li, heap_size) && /*li < this.queue.size() &&*/ this.queue.get(i).lt(this.queue.get(li))) {
                max = li;
            }
            if(!isOutRange(ri, heap_size) && /*ri < this.queue.size() &&*/ this.queue.get(max).lt(this.queue.get(ri))) {
                max = ri;
            }

            if(max != i) {
                Node ex = this.queue.get(max);
                this.queue.set(max, this.queue.get(i));
                this.queue.set(i, ex);
                max_heapify(max, heap_size);
            }
        }
        private boolean isLeaf(int i) {
            return i>=this.queue.size()/2;
        }
        private boolean isOutRange(int i, int heap_size) {
            return i>=heap_size;
        }

        public void increase(Node node, int added) {

            for(int i=0 ;i<this.queue.size(); i++) {
                if(this.queue.get(i).equals(node)) {
                    int old_key = this.queue.get(i).key;
                    int new_key = old_key + added;
                    this.queue.get(i).key = new_key;
                    if(old_key > new_key) {
                        max_heapify(i, this.queue.size());
                    }
                    if(old_key < new_key) {
                        heapify(i);
                    }
                    break;
                }
            }

        }

        @Override
        public String toString() {
            return "MaxPriorityQueue{" +
                    " queue=[" + queue.stream().map(node -> node.key+"").reduce((key1, key2) -> key1 + ", " + key2).orElse("") +
                    "] }";
        }
    }



    static class Node {
        int key;
        int value;
        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }

        static Node build(int key, int value) {
            return new Node(key, value);
        }

        public boolean gt(Node other) {
            return other == null || key > other.key;
        }

        public boolean lt(Node other) {
            return other != null && key < other.key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            boolean flag = key == node.key;
            flag &= value == node.value;
            return flag;
        }

        @Override
        public int hashCode() {
            int hashCode = Integer.hashCode(key);
            hashCode = 29*hashCode + Integer.hashCode(value);
            return hashCode;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

}
