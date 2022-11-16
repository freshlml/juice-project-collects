package com.juice.alg.chapter3_10;

import java.util.NoSuchElementException;

public class Chapter3_10_2 {

    //double linked list
    interface List {
        void addFirst(int e);
        void addLast(int e);
        int removeFirst();
        int removeLast();
        int getFirst();
        int getLast();

        void add(int index, int e);
        int remove(int index);
        int set(int index, int e);
        int get(int index);

        int size();
        boolean isEmpty();
        boolean contains(int e);

    }
    static class DoubleLinkedList implements List {
        private Node head;
        private Node tail;
        private int size = 0;

        public DoubleLinkedList() {}

        @Override
        public void addFirst(int e) {
            Node n = new Node(null, e, this.head);

            if(this.head != null) {
                this.head.prev = n;
            } else {
                this.tail = n;
            }
            this.head = n;

            this.size++;
        }

        @Override
        public void addLast(int e) {
            Node n = new Node(this.tail, e, null);

            if(this.tail != null) {
                this.tail.next = n;
            } else {
                this.head = n;
            }
            this.tail = n;

            this.size++;
        }

        @Override
        public int removeFirst() {
            if(this.head == null) throw new NoSuchElementException(); //empty

            int v = this.head.item;
            Node next = this.head.next;
            this.head.next = null;
            if(next != null) {
                next.prev = null;
            } else {
                this.tail = null;
            }
            this.head = next;

            this.size--;
            return v;
        }

        @Override
        public int removeLast() {
            if(this.tail == null) throw new NoSuchElementException(); //empty

            int v = this.tail.item;
            Node prev = this.tail.prev;
            this.tail.prev = null;
            if(prev != null) {
                prev.next = null;
            } else {
                this.head = null;
            }
            this.tail = prev;

            this.size--;
            return v;
        }

        @Override
        public int getFirst() {
            if(this.head == null) throw new NoSuchElementException(); //empty

            return this.head.item;
        }

        @Override
        public int getLast() {
            if(this.tail == null) throw new NoSuchElementException(); //empty

            return this.tail.item;
        }

        @Override
        public void add(int index, int e) {
            if(index < 0 || index > this.size) throw new IndexOutOfBoundsException();

            if(index == this.size) {
                addLast(e);
            } else {
                Node n = node(index);

                Node newNode = new Node(n.prev, e, n);

                Node prev = n.prev;
                n.prev = newNode;
                if(prev != null) {
                    prev.next = newNode;
                } else {
                    this.head = newNode;
                }

                this.size++;
            }

        }

        @Override
        public int remove(int index) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            Node n = node(index);
            int v = n.item;

            Node prev = n.prev;
            Node next = n.next;
            n.prev = null;
            n.next = null;
            if(prev != null) {
                prev.next = next;
            } else {
                this.head = next;
            }
            if(next != null) {
                next.prev = prev;
            } else {
                this.tail = prev;
            }

            return v;
        }

        @Override
        public int set(int index, int e) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            Node n = node(index);
            int v = n.item;
            n.item = e;

            return v;
        }

        @Override
        public int get(int index) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            Node n = node(index);
            return n.item;
        }

        private Node node(int index) {
            Node n = this.head;
            for(int i=0; i<index; i++) {
                n = n.next;
            }
            return n;
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.head == null;
        }

        @Override
        public boolean contains(int e) {
            Node n = this.head;
            while(n != null) {
                if(n.item == e) return true;
                n = n.next;
            }

            return false;
        }


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

    }


    //固定大小数组表示的链表
    static class FixedCapacityArrayList implements List {
        private final int[] element;
        private final int[] next;
        private final int[] prev;
        private static final int DEFAULT_CAPACITY = 16;
        private int head = -1;
        private int tail = -1;
        private int free = 0;
        private int size;

        public FixedCapacityArrayList() { this(DEFAULT_CAPACITY); }

        public FixedCapacityArrayList(int capacity) {
            //assert 0 < capacity <= Integer.MAX_VALUE

            element = new int[capacity];
            prev = new int[capacity];
            next = new int[capacity];
            next[next.length - 1] = -1;
            for(int i=next.length-2; i>=0; i--) {
                next[i] = i+1;
            }
            for(int i=0; i<prev.length; i++) {
                prev[i] = -2;
            }
        }

        private int allocate() {
            if(free == -1) throw new IndexOutOfBoundsException();

            int v = free;
            free = next[free];
            return v;
        }

        private void freed(int n) {
            next[n] = free;
            prev[n] = -2;
            free = n;
        }

        @Override
        public void addFirst(int e) {
            int newNode = allocate();

            this.element[newNode] = e;
            this.next[newNode] = this.head;
            this.prev[newNode] = -1;

            if(this.head != -1) {
                this.prev[this.head] = newNode;
            } else {
                this.tail = newNode;
            }
            this.head = newNode;

            this.size++;
        }

        @Override
        public void addLast(int e) {
            int newNode = allocate();

            this.element[newNode] = e;
            this.next[newNode] = -1;
            this.prev[newNode] = this.tail;

            if(tail != -1) {
                this.next[this.tail] = newNode;
            } else {
                this.head = newNode;
            }
            this.tail = newNode;

            this.size++;
        }

        @Override
        public int removeFirst() {
            if(this.head == -1) throw new NoSuchElementException();

            int first = this.head;
            int v = this.element[first];
            int next = this.next[first];

            freed(first);
            if(next != -1) {
                this.prev[next] = -1;
            } else {
                this.tail = -1;
            }
            this.head = next;

            this.size--;
            return v;
        }

        @Override
        public int removeLast() {
            if(this.head == -1) throw new NoSuchElementException();

            int last = this.tail;
            int v = this.element[last];
            int prev = this.prev[last];

            freed(last);
            if(prev != -1) {
                this.next[prev] = -1;
            } else {
                this.head = -1;
            }
            this.tail = prev;

            this.size--;
            return v;
        }

        @Override
        public int getFirst() {
            if(this.head == -1) throw new NoSuchElementException();

            return this.element[this.head];
        }

        @Override
        public int getLast() {
            if(this.tail == -1) throw new NoSuchElementException();

            return this.element[this.tail];
        }

        private int node(int index) {
            int n = this.head;
            for(int i=0; i<index; i++) {
                n = this.next[n];
            }
            return n;
        }

        @Override
        public void add(int index, int e) {
            if(index < 0 || index > this.size) throw new IndexOutOfBoundsException();

            if(index == this.size) {
                addLast(e);
            } else {
                int n = node(index);

                int newNode = allocate();
                this.element[newNode] = e;
                this.next[newNode] = n;
                this.prev[newNode] = this.prev[n];

                int prev = this.prev[n];
                this.prev[n] = newNode;
                if(prev != -1) {
                    this.next[prev] = newNode;
                } else {
                    this.head = newNode;
                }

                this.size++;
            }

        }

        @Override
        public int remove(int index) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            int n = node(index);
            int v = this.element[n];

            int prev = this.prev[n];
            int next = this.next[n];
            freed(n);
            if(prev != -1) {
                this.next[prev] = next;
            } else {
                this.head = next;
            }
            if(next != -1) {
                this.prev[next] = prev;
            } else {
                this.tail = prev;
            }

            this.size--;
            return v;
        }

        @Override
        public int set(int index, int e) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            int n = node(index);
            int v = this.element[n];
            this.element[n] = e;

            return v;
        }

        @Override
        public int get(int index) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            int n = node(index);
            return this.element[n];
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.size == 0;
        }

        @Override
        public boolean contains(int e) {
            int n = this.head;
            while(n != -1) {
                if(element[n] == e) return true;
                n = next[n];
            }

            return false;
        }

        //练习10.3-5
        public void compactify() {

            for(int i=this.size, j=this.free, free_prev = -1; i<this.element.length && j != -1; ) {
                if(j >= this.size) {
                    free_prev = j;
                    j = this.next[j];
                    continue;
                }

                if(isFree(i)) {
                    i++;
                    continue;
                } else {
                    int j_next = this.next[j];
                    int i_next = this.next[i];
                    int i_prev = this.prev[i];

                    this.element[j] = this.element[i];
                    this.next[j] = this.next[i];
                    this.prev[j] = this.prev[i];
                    if(i_prev != -1) {
                        this.next[i_prev] = j;
                    } else {
                        this.head = j;
                    }
                    if(i_next != -1) {
                        this.prev[i_next] = j;
                    } else {
                        this.tail = j;
                    }

                    this.next[i] = j_next;
                    this.prev[i] = -2;
                    if(free_prev != -1) {
                        this.next[free_prev] = i;
                    } else {
                        this.free = i;
                    }

                    free_prev = i;
                    j = j_next;
                    i++;
                }

            }

        }

        private boolean isFree(int n) {
            return this.prev[n] == -2;
        }

    }

}
