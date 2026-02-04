package com.juice.alg.part3.chapter10;

public class Chapter10_Practice {

    //思考题10-2
    //a: insert: Θ(n). minimum: Θ(1). extract-min: Θ(1). union: Θ(n).
    /*
    class Node { int item; Node prev; Node next; }

    void union(heap) {               //合并两个有序链表
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
    //b: insert: Θ(n*lgn). minimum: Θ(1). extract-min: Θ(n*lgn). union: O(n*lgn)？.
    /*    0    1    2    3    4    5    6    7    8    9
     *   [ ]  [ ]  [ ]  [ ]  [ ]  [ ]  [ ]  [ ]  [ ]  [ ]
     *
     *extract-min:
     *            0                        1
     *       1         2              2           3
     *    3     4   5     6        4     5      6    7
     *  7   8  9                 8   9
     *
     * 当移除下标 0 处节点后，原奇数下标节点的父节点保持不变，原偶数下标节点的父节点发生变化
     */
    //c:
    /*
    union(left, right) { // O(n*lgn)？

        Node newNode = new Node(null, min(left.key, right.key), null)

        if(left.key <= right.key) {
            left.extract-min();
        } else {
            right.extract-min();
        }

        newNode.next = left;
        left.prev = newNode;

        while(true) {
            left_first = left;
            left_end = current layer 的最后一个元素
            next_left = left_end.next;

            right_first = right;
            right_end = current layer 的最后一个元素
            next_right = right_end.next;

            left_end.next = right_first;
            right_first.prev = left_end;

            if next_left == null:
                tag = left'tail
                break;
            else:
                right_end.next = next_left;
                next_left.prev = right_end;
                if next_right == null:
                    tag = right'tail
                    break;

            left = next_left;
            right = next_right;
        }

        todo 处理 newNode 链的最后一个节点到 tag 处节点
    }
    */


    //思考题10-3, todo
    /*
    public boolean compact_contains(int e) {
        int size = this.size();
        int i = this.head;
        while(i != -1 && this.elements[i] < e) {
            int j = RANDOM(0, size-1);

            if this.elements[i] < this.elements[j] && this.elements[j] <= e:
                i = j;
                if this.elements[i] == e:
                    return true;

            i = this.next[i];
        }
        if(this.elements[i] > e): return false;
        else return true;
    }
    */

}
