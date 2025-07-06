package com.juice.alg.part3.chapter10;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

public class Chapter10_2 {

    public static void main(String[] argv) {
        DoubleLinkedList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(1, 3);

        System.out.println(list);
    }

    /**
     *Double linked sequence(list)
     *  - special ordered as an double linked sequence
     *  - allow duplicate elements
     *  - permit null elements
     *
     * @param <E> element type
     */
    public static class DoubleLinkedList<E> extends AbstractSequentialList<E>
                                            implements List<E>
    {
        private Node<E> head;
        private Node<E> tail;
        private int size;

        /**
         * Construct a empty double linked sequence.
         */
        public DoubleLinkedList() {}

        /**
         * Construct a double linked sequence and add all of the element of the specified collection into this sequence.
         *
         * @param c the specified collection
         * @throws NullPointerException     if the specified collection is null
         */
        @SuppressWarnings("unused")
        public DoubleLinkedList(Collection<? extends E> c) {
            addAll(c);
        }

        /**
         * Returns the number of elements in this list.
         *
         * @return the number of elements in this list
         */
        @Override
        public int size() {
            return size;
        }

        /**
         * Returns true if this list contains the specified element.
         *
         * This collection allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         *
         * If the specified element is incompatible with this list, return `false` rather than throw `ClassCastException`.
         *
         * @param o element whose presence in this list is to be tested
         * @return true if this list contains the specified element
         */
        @Override
        public boolean contains(Object o) {
            return node(o) != null;
        }

        //Return the first equivalent element from first to last, or null if nothing matches.
        private Node<E> node(Object o) {
            Node<E> p = this.head;
            while(p != null && !Objects.equals(o, p.v)) {
                p = p.next;
            }
            return p;
        }

        /**
         * Returns an array containing all of the elements in this list in proper sequence (from first to last element).
         *
         * The returned array will be "safe" in that no references to it are maintained by this list.
         * (In other words, this method must allocate a new array even if this list is backed by an array).
         * The caller is thus free to modify the returned array.
         *
         * This method acts as bridge between array-based and collection-based APIs.
         *
         * @return an array containing all of the elements in this list in proper sequence
         */
        @Override
        public Object[] toArray() {
            Object[] r = new Object[size];
            fillInto(r);
            return r;
        }

        private int fillInto(Object[] r) {
            Node<E> p = this.head;
            int idx = 0;
            while(p != null) {
                r[idx++] = p.v;
                p = p.next;
            }
            return idx;
        }

        /**
         * Returns an array containing all of the elements in this list, If the list fits in(>=) the specified array, it is returned therein.
         * Otherwise, a new array is allocated with the runtime type of the specified array and the size of this list.
         *
         * If the specified array has more elements than this list, the element in the array immediately following the end of the list is set to null.
         * (This is useful in determining the length of this list only if the caller knows that this list does not contain any null elements.)
         *
         * Order: from first to last element.
         *
         * This method acts as bridge between array-based and collection-based APIs, and control over the runtime type, save allocation costs.
         *
         * @param a the array into which the elements of this collection are to be stored, if it is big enough; otherwise, a new array of the same runtime type is allocated for this purpose
         * @return  an array containing the elements of this list
         * @throws ArrayStoreException  if the runtime type of the specified array is not a supertype of the runtime type of every element in this list
         * @throws NullPointerException if the specified array is null
         */
        @Override
        public <T> T[] toArray(T[] a) {
            @SuppressWarnings("unchecked")
            T[] r = a.length < size ? (T[]) Array.newInstance(a.getClass().getComponentType(), size) : a;
            int idx = fillInto(r);
            while(idx < r.length) {
                r[idx++] = null;
            }
            return r;
        }

        /**
         * Returns true if this list contains all of the elements in the specified collection.
         *
         * If the element'type in the specified collection is incompatible with this list, return `false` rather than throw `ClassCastException`.
         *
         * @param c collection to be checked for containment in this list
         * @return true if this list contains all of the elements of the specified collection
         * @throws NullPointerException if the specified collection is null
         */
        @Override
        public boolean containsAll(Collection<?> c) {
            return super.containsAll(c);
        }

        /**
         * Appends all of the elements in the specified collection to the end of this list, in the order
         * that they are returned by the specified collection's iterator.
         *
         * The behavior of this operation is undefined if the specified collection is modified while the operation is in progress.
         * (Note that this will occur if the specified collection is this list, and it's nonempty.)
         *
         * @param c collection containing elements to be added to this list
         * @return  true if this list changed as a result of the call
         * @throws NullPointerException if the specified collection is null
         */
        @Override
        public boolean addAll(Collection<? extends E> c) {
            return super.addAll(c);
        }

        /**
         * Removes from this list all of its elements that are contained in the specified collection, Returns true if this list changed as a result of the call.
         *
         * After this call returns, this list will contain no elements in common with the specified collection.
         *
         * @param c collection containing elements to be removed from this list
         * @return  true if this list changed as a result of the call
         * @throws ClassCastException      if the types of one or more elements in this list are incompatible with the specified collection(optional)
         * @throws NullPointerException    if this list contains one or more null elements and the specified collection does not support null elements(optional),
         *                              or if the specified collection is null
         */
        @Override
        public boolean removeAll(Collection<?> c) {
            return super.removeAll(c);
        }

        /**
         * Retains the elements in this list that are contained in the specified collection.
         * Returns true if this collection changed as a result of the call.
         *
         * @param c collection containing elements to be retained in this list
         * @return  true if this list changed as a result of the call
         * @throws ClassCastException      if the types of one or more elements in this collection are incompatible with the specified collection(optional)
         * @throws NullPointerException    if this list contains one or more null elements and the specified collection does not permit null elements(optional)
         *                              or if the specified collection is null
         */
        @Override
        public boolean retainAll(Collection<?> c) {
            return super.retainAll(c);
        }

        /**
         * Removes all of the elements from this list.
         * The list will be empty after this method returns.
         */
        @Override
        public void clear() {
            head = tail = null;
            size = 0;
        }

        /**
         * Inserts all of the elements in the specified collection into this list at the specified position, return true if this list changed as a result of the call.
         *
         * Shifts the element currently at that position (if any) and any subsequent elements to the right (increases their indices).
         *
         * The new elements will appear in this list in the order that they are returned by the specified collection's iterator.
         *
         * The behavior of this operation is undefined if the specified collection is modified while the operation is in progress.
         * (Note that this will occur if the specified collection is this list, and it's nonempty.)
         *
         * @param index index at which to insert the first element from the specified collection
         * @param c  collection containing elements to be added to this list
         * @return   true if this list changed as a result of the call
         * @throws NullPointerException          if the specified collection is null
         * @throws IndexOutOfBoundsException     if the index is out of range (index < 0 || index > size())
         */
        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            return super.addAll(index, c);
        }

        /**
         * Appends the specified element to the end of this list, Returns true if this list changed as a result of the call.
         *
         * @param e element to be appended to this list
         * @return true if this collection changed as a result of the call
         */
        @Override
        public boolean add(E e) {
            insertAt(null, e);
            return true;
        }

        private void insertAt(Node<E> node, E e) {
            Node<E> added = new Node<>(e);
            added.next = node;

            if(node == null) {
                added.prev = tail;
                if(tail != null) {
                    tail.next = added;
                    tail = added;
                } else {
                    head = tail = added;
                }
            } else if(node.prev == null) {
                node.prev = added;
                head = added;
            } else {
                node.prev.next = added;
                added.prev = node.prev;
                node.prev = added;
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
         */
        @Override
        public void add(int index, E e) {
            if(index < 0 || index > size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + "]");

            Node<E> node;
            if(index == size()) {  //speed for addLast
                node = null;
            } else {
                node = elementAt(index);
            }
            insertAt(node, e);
        }

        private Node<E> elementAt(int index) {
            //assert index >= 0;
            Node<E> node = head;
            for(int i = 0; i < index && node != null; i++) {
                node = node.next;
            }
            return node;
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
        @Override
        public boolean remove(Object o) {
            for(Node<E> p = head; p != null; ) {
                if(Objects.equals(o, p.v)) {
                    removeAt(p);
                    return true;
                }
                p = p.next;
            }
            return false;
        }

        private void removeAt(Node<E> node) {
            if(node == null) return;

            if(node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }

            if(node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }
            node.v = null;
            node.prev = node.next = null;
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
        @Override
        public E remove(int index) {
            if(index < 0 || index >= size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + ")");

            Node<E> node = elementAt(index);
            E rv = node.v;
            removeAt(node);
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
        @Override
        public E set(int index, E e) {
            if(index < 0 || index >= size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + ")");

            Node<E> node = elementAt(index);
            E rv = node.v;
            updateAt(node, e);
            return rv;
        }

        private void updateAt(Node<E> node, E e) {
            if(node != null) {
                node.v = e;
            }
        }

        /**
         * Returns the element at the specified position in this list.
         *
         * @param index index of the element to return
         * @return  the element at the specified position in this list
         * @throws IndexOutOfBoundsException      if the index is out of range (index < 0 || index >= size())
         */
        @Override
        public E get(int index) {
            if(index < 0 || index >= size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + ")");

            return elementAt(index).v;
        }

        /**
         * Returns the first matched element index from this list.
         *
         * If this list does not contain the element, return -1.
         *
         * This collection allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         *
         * If the specified element is incompatible with this list, return `-1` rather than throw `ClassCastException`.
         *
         * @param o element to search for
         * @return  the index of the first occurrence of the specified element in this list, or -1 if not contains
         */
        @Override
        public int indexOf(Object o) {
            Node<E> p = head;
            int index = 0;
            while(p != null) {
                if(Objects.equals(o, p.v)) break;
                p = p.next;
                index++;
            }
            return p == null ? -1 : index;
        }

        /**
         * Returns the last matched element index from this list.
         *
         * If this list does not contain the element, return -1.
         *
         * This collection allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         *
         * If the specified element is incompatible with this list, return `-1` rather than throw `ClassCastException`.
         *
         * @param o element to search for
         * @return  the index of the last occurrence of the specified element in this list, or -1 if not contains
         */
        @Override
        public int lastIndexOf(Object o) {
            Node<E> p = tail;
            int index = size() - 1;
            while(p != null) {
                if(Objects.equals(o, p.v)) break;
                p = p.prev;
                index--;
            }
            return p == null ? -1 : index;
        }

        /**
         * Returns a list iterator over the elements in this list (in proper sequence).
         *
         * @return a list iterator over the elements in this list (in proper sequence)
         */
        @Override
        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        /**
         * Returns a list iterator over the elements in this list (in proper sequence), starting at the specified position in the list.
         *
         * @param index the specified position for starting traversal
         * @return a list iterator over the elements in this list (in proper sequence), starting at the specified position in the list
         * @throws IndexOutOfBoundsException      if the index is out of range (index < 0 || index > size())
         */
        @Override
        public ListIterator<E> listIterator(int index) {
            if(index < 0 || index > size())
                throw new IndexOutOfBoundsException("index = " + index + " is out of bounds [" + 0 + ", " + size() + ")");

            Node<E> firstToTraverse = elementAt(index); //Note that firstToTraverse is null if the index equals the size of this list.
            return new ListItr(firstToTraverse, index);
        }

        private class ListItr implements ListIterator<E> {
            private Node<E> cur;
            private int idx;
            private Node<E> lastRet = null;

            ListItr(Node<E> first, int index) {
                this.cur = first;
                this.idx = index;
            }

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                if(this.cur == null)
                    throw new NoSuchElementException("has no more elements when traverse forward");

                E rv = this.cur.v;
                this.lastRet = this.cur;
                this.cur = this.cur.next;
                this.idx++;

                return rv;
            }

            @Override
            public boolean hasPrevious() {
                //return (cur == null && size() > 0) || (cur != null && cur.prev != null);
                return this.idx > 0;
            }

            @Override
            public E previous() {
                if(this.idx <= 0)
                    throw new NoSuchElementException("has no more elements when traverse forward");

                this.cur = this.cur == null ? tail : this.cur.prev;
                E rv = this.cur.v;
                this.lastRet = this.cur;
                this.idx--;

                return rv;
            }

            @Override
            public int nextIndex() {
                return idx;
            }

            @Override
            public int previousIndex() {
                return idx - 1;
            }

            @Override
            public void remove() {
                if(this.lastRet == null)
                    throw new IllegalStateException("can not remove before the next/previous method called " +
                            "or remove/add have been called after the last call to next/previous");

                if(this.lastRet == this.cur)
                    this.cur = this.cur.next;
                removeAt(this.lastRet);
                this.idx--;
                this.lastRet = null;
            }

            @Override
            public void set(E e) {
                if(lastRet == null)
                    throw new IllegalStateException("can not remove before the next/previous method called " +
                            "or remove/add have been called after the last call to next/previous");

                updateAt(lastRet, e);
            }

            @Override
            public void add(E e) {
                insertAt(cur, e);
                this.idx++;
                lastRet = null;
            }
        }

        @Override
        public Spliterator<E> spliterator() {
            return null;  //todo
        }

        @Override
        public Stream<E> stream() {
            return null;  //todo
        }

        @Override
        public Stream<E> parallelStream() {
            return null;  //todo
        }

        static class Node<E> {
            E v;
            Node<E> prev, next;
            Node(E v) {
                this.v = v;
            }
        }
    }

}
