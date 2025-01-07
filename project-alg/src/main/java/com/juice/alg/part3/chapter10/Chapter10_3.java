package com.juice.alg.part3.chapter10;

import java.util.Arrays;
import java.util.Objects;

public class Chapter10_3 {

    public static void main(String[] argv) {
        FixedArrayLinkedList<Integer> list = new FixedArrayLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        list.remove(1);

        System.out.println(list);
    }

    /**
     *Fixed capacity double linked sequence(list)
     *  - special ordered as an double linked sequence
     *  - allow duplicate elements
     *  - permit null elements
     *
     * @param <E> element type
     */
    public static class FixedArrayLinkedList<E> {
        private static final int DEFAULT_CAPACITY = 16;

        private final E[] elements;
        private final int[] next;
        private final int[] prev;

        private int head = -1;
        private int tail = -1;
        private int size = 0;

        private int free;

        /**
         * Construct a double linked sequence with the size `DEFAULT_CAPACITY`.
         */
        public FixedArrayLinkedList() { this(DEFAULT_CAPACITY); }

        /**
         * Construct a double linked sequence with the specified capacity.
         *
         * @param capacity the capacity, at the range of (0, allowed max array size]
         * @throws IllegalArgumentException if the specified capacity if out of the range of (0, allowed max array size]
         */
        public FixedArrayLinkedList(int capacity) {
            //assert 0 < capacity <= the allowed max array size
            @SuppressWarnings("unchecked")
            E[] elms = (E[]) new Object[capacity];
            this.elements = elms;
            prev = new int[capacity];
            next = new int[capacity];

            for(int i=0; i < next.length - 1; i++) {
                next[i] = i + 1;
            }
            next[next.length - 1] = -1;
            free = 0;
            Arrays.fill(prev, -1);
        }

        /**
         * Returns the number of elements in this list.
         *
         * @return the number of elements in this list
         */
        public int size() {
            return size;
        }

        /**
         * Returns true if this list contains no elements.
         *
         * @return true if this list contains no elements
         */
        @SuppressWarnings("unused")
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * Returns true if this list contains the specified element.
         *
         * This collection allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         *
         * f the specified element is incompatible with this list, return `false` rather than throw `ClassCastException`.
         *
         * @param o element whose presence in this list is to be tested
         * @return true if this list contains the specified element
         */
        @SuppressWarnings("unused")
        public boolean contains(Object o) {
            int p = this.head;
            while(p != -1 && !Objects.equals(o, elements[p])) {
                p = next[p];
            }
            return p != -1;
        }

        /**
         * Removes all of the elements from this list.
         * The list will be empty after this method returns.
         */
        @SuppressWarnings("unused")
        public void clear() {
            Arrays.fill(elements, null);
            for(int i=0; i < next.length - 1; i++) {
                next[i] = i + 1;
            }
            next[next.length - 1] = -1;
            free = 0;
            Arrays.fill(prev, -1);
            head = tail = -1;
            size = 0;
        }

        private int allocate() {
            if(free == -1) throw new IllegalStateException("there is no more space for allocate");

            int r = free;
            free = next[free];
            return r;
        }

        private void freed(int idx) {
            //assert idx != -1;
            next[idx] = free;
            prev[idx] = -1;
            free = idx;
        }

        //练习10.3-4
        @SuppressWarnings("unused")
        private void compact_freed(int idx) {
            //assert idx != -1
            int last = size;

            if(idx != last) {
                moveTo(last, idx);
                freed(last);
            } else {
                freed(idx);
            }
        }

        private void moveTo(int from, int to) {
            elements[to] = elements[from];
            prev[to] = prev[from];
            next[to] = next[from];

            elements[from] = null;
            prev[from] = next[from] = -1;

            if(prev[to] != -1) {
                next[prev[to]] = to;
            } else {
                head = to;
            }
            if(next[to] != -1) {
                prev[next[to]] = to;
            } else {
                tail = to;
            }
        }

        /**
         * Appends the specified element to the end of this list, Returns true if this list changed as a result of the call.
         *
         * @param e element to be appended to this list
         * @return true if this collection changed as a result of the call
         * @throws IllegalStateException         if the element cannot be added at this time due to capacity restrictions
         */
        public boolean add(E e) {
            insertAt(-1, e);
            return true;
        }

        private void insertAt(int p, E e) {
            int added = allocate();
            elements[added] = e;
            next[added] = p;

            if(p == -1) {
                prev[added] = tail;
                if(tail != -1) {
                    next[tail] = added;
                    tail = added;
                } else {
                    head = tail = added;
                }
            } else if(prev[p] == -1) {
                prev[p] = added;
                //prev[added] = -1;
                head = added;
            } else {
                next[prev[p]] = added;
                prev[added] = prev[p];
                prev[p] = added;
            }
            size++;
        }

        /**
         * Inserts the specified element at the specified position in this list.
         * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
         *
         * @param index index at which the specified element is to be inserted
         * @param e  the element to be inserted
         * @throws IndexOutOfBoundsException     if the index is out of range (index < 0 || index > size())
         * @throws IllegalStateException         if the element cannot be added at this time due to capacity restrictions
         */
        @SuppressWarnings("unused")
        public void add(int index, E e) {
            if(index < 0 || index > size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + "]");

            int p;
            if(index == size()) {  //speed for addLast
                p = -1;
            } else {
                p = elementAt(index);
            }
            insertAt(p, e);
        }

        private int elementAt(int index) {
            //assert index >= 0;
            int p = head;
            for(int i = 0; i < index && p != -1; i++) {
                p = next[p];
            }
            return p;
        }

        /**
         * Removes the first matched element from this list, If this list does not contains the element, it is unchanged and return false.
         *
         * This collection allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         *
         * If the specified element is incompatible with this list, return `false` rather than throw `ClassCastException`.
         *
         * @return true if this list contained the specified element
         */
        public boolean remove(Object o) {
            for(int p=head; p != -1; ) {
                if(Objects.equals(o, elements[p])) {
                    removeAt(p);
                    return true;
                }
                p = next[p];
            }
            return false;
        }

        private void removeAt(int p) {
            if(p == -1) return;

            if(next[p] != -1) {
                prev[next[p]] = prev[p];
            } else {
                tail = prev[p];
            }

            if(prev[p] != -1) {
                next[prev[p]] = next[p];
            } else {
                head = next[p];
            }
            elements[p] = null;
            prev[p] = next[p] = -1;
            freed(p);
            size--;
        }

        /**
         * Removes the element at the specified position in this list.
         *
         * Shifts any subsequent elements to the left (subtracts one from their indices).
         *
         * @param index the index of the element to be removed
         * @return the element previously at the specified position
         * @throws IndexOutOfBoundsException      if the index is out of range (index < 0 || index >= size())
         */
        public E remove(int index) {
            if(index < 0 || index >= size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + ")");

            int p = elementAt(index);
            E rv = elements[p];
            removeAt(p);
            return rv;
        }

        /**
         * Replaces the element at the specified position in this list with the specified element.
         *
         * @param index index of the element to replace
         * @param e element to be stored at the specified position
         * @return the element previously at the specified position
         * @throws IndexOutOfBoundsException      if the index is out of range (index < 0 || index >= size())
         */
        @SuppressWarnings("unused")
        public E set(int index, E e) {
            if(index < 0 || index >= size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + ")");

            int p = elementAt(index);
            E rv = elements[p];
            updateAt(p, e);
            return rv;
        }

        private void updateAt(int p, E e) {
            if(p != -1) {
                elements[p] = e;
            }
        }

        /**
         * Returns the element at the specified position in this list.
         *
         * @param index index of the element to return
         * @return  the element at the specified position in this list
         * @throws IndexOutOfBoundsException      if the index is out of range (index < 0 || index >= size())
         */
        @SuppressWarnings("unused")
        public E get(int index) {
            if(index < 0 || index >= size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + ")");

            return elements[elementAt(index)];
        }

        //练习10.3-5
        public void compactify() {
            for(int i = 0, p = head; i < size; i++) {
                if(prev[i] == -1 && head != i) {  //free bucket
                    int nt = next[i];
                    moveTo(p, i);
                    next[p] = nt;
                    if(free == i) {
                        free = p;
                    }
                    p = next[i];
                }
            }
        }

        //思考题10-3
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

}
