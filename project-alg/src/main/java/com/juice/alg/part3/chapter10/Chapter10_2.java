package com.juice.alg.part3.chapter10;

import java.util.NoSuchElementException;

public class Chapter10_2 {

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


}
