package com.juice.alg.part3.chapter10;

import java.util.NoSuchElementException;

public class Chapter10_3 {

    //固定大小数组表示的链表
    static class FixedCapacityArrayList {
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


        public int removeFirst() {
            if(this.head == -1) throw new NoSuchElementException();

            int first = this.head;
            int v = this.element[first];
            int next = this.next[first];

            if(next != -1) {
                this.prev[next] = -1;
            } else {
                this.tail = -1;
            }
            this.head = next;

            freed(first);

            this.size--;
            return v;
        }


        public int removeLast() {
            if(this.head == -1) throw new NoSuchElementException();

            int last = this.tail;
            int v = this.element[last];
            int prev = this.prev[last];

            if(prev != -1) {
                this.next[prev] = -1;
            } else {
                this.head = -1;
            }
            this.tail = prev;

            freed(last);

            this.size--;
            return v;
        }


        public int getFirst() {
            if(this.head == -1) throw new NoSuchElementException();

            return this.element[this.head];
        }


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


        public int remove(int index) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            int n = node(index);
            int v = this.element[n];

            int prev = this.prev[n];
            int next = this.next[n];

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

            freed(n);

            this.size--;
            return v;
        }


        public int set(int index, int e) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            int n = node(index);
            int v = this.element[n];
            this.element[n] = e;

            return v;
        }


        public int get(int index) {
            if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();

            int n = node(index);
            return this.element[n];
        }


        public int size() {
            return this.size;
        }


        public boolean isEmpty() {
            return this.size == 0;
        }


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


        //练习10.3-4
        /*
        private void compact_freed(int n) {
            int size = this.size();
            int al = n;

            if(n != size-1) {
                int prev = this.prev[size-1];
                int next = this.next[size-1];

                this.element[n] = this.element[size-1];
                this.prev[n] = this.prev[size-1];
                this.next[n] = this.next[size-1];

                if(prev != -1) {
                    this.next[prev] = n;
                } else {
                    this.head = n;
                }
                if(next != -1) {
                    this.prev[next] = n;
                } else {
                    this.tail = n;
                }

                al = size-1;
            }

            next[al] = free;
            prev[al] = -2;
            free = al;
        }
         */
        //思考题10-3
        /*
        public boolean compact_contains(int e) {
            int size = this.size();
            int i = this.head;
            while(i != -1 && this.element[i] < e) {
                int j = RANDOM(0, size-1);

                if this.element[i] < this.element[j] && this.element[j] <= e:
                    i = j;
                    if this.element[i] == e:
                        return true;

                i = this.next[i];
            }
            if(this.element[i] > e): return false;
            else return true;

        }
         */

    }

}
