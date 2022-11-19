package com.juice.alg.chapter3_10;

public class Chapter3_10_5 {

    //思考题10-2
    /*a: O(n)
    static class Node {
        int item;
        Node prev;
        Node next;
        public Node(Node prev, int item, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }
    void union(heap) {
        Node t = this.head;
        Node heap_t = heap.head;

        while(t != null && heap_t != null) {
            if(t.item == heap_t.item) {
                t = t.next;
                heap_t = heap_t.next;
            } else if(t.item < heap_t.item) {
                t = t.next;
            } else {
                Node prev = t.prev;

                Node newNode = new Node(prev, heap_t.item, t);
                if(prev != null) {
                    prev.next = newNode;
                } else {
                    this.head = newNode;
                }
                t.prev = newNode;

                heap_t = heap_t_next;
            }
        }
        if(heap_t != null) {
            while(heap_t != null) {
                this.addLast(heap_t.item);
                heap_t = heap_t.next;
            }
        }

    }
     */
    /*c: O(n + C*lgN)
    union(left, right) {

        Node newNode = new Node(null, min(left.head.key, right.head.key), null)

        if(left.head.key < right.head.key) {
            left.head.key = left.tail.key
            unlink left.tail
            heapify
        } else if(a.head.key > b.head.key) {
            b.head.key = b.tail.key
            unlink b.tail
            heapify
        }

        调整left, right的元素个数，使得最终left.length == right.length or left.length = right.length + 1

        newNode.next = left;
        left.prev = newNode;

        layer = 2;
        while(layer < total layer) { //total layer = lgN
            left_first = left;
            left_end = current layer的最后一个元素
            next_left = left_end.next;

            right_first = right;
            right_end = current layer的最后一个元素
            next_right = right_end.next;

            left_end.next = right_first;
            right_first.prev = left_end;

            right_end.next = next_left;
            next_left.prev = right_end;

            left = next_left;
            right = next_right;
        }

        for last layer:
            move some from right to left if necessary


    }
     */



}
