package com.juice.alg.part3.chapter10;

import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;
import com.juice.alg.part2.chapter8.Chapter8_4.ArrayTraversal;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

public class Chapter10_1 {

    public static void main(String[] argv) {
        FixedArrayDeque<Integer> stack = new FixedArrayDeque<>();
        stack.push(1);
        stack.push(2);
        ArrayTraversal.of(stack.elements, stack.head, stack.tail).forEach(ArrayPrinter.of()::print);

        System.out.println("----stack-------------------------------------------");

        FixedArrayDeque<Integer> queue = new FixedArrayDeque<>();
        queue.add(null);
        queue.add(3);
        ArrayTraversal.of(queue.elements, queue.head, queue.tail).forEach(ArrayPrinter.of()::print);

        System.out.println("----queue-------------------------------------------");

        FixedArrayDeque<Integer> deque = new FixedArrayDeque<>();
        deque.addFirst(4);
        deque.addFirst(null);
        deque.addFirst(5);
        ArrayTraversal.of(deque.elements, deque.head, deque.tail).forEach(ArrayPrinter.of()::print);

        System.out.println("----deque-------------------------------------------");

    }

    /**
     *Fixed array Deque、Queue、Stack.
     *  - special ordered as an double ended queue
     *  - allow duplicate elements
     *  - permit null elements
     *
     * @param <E> element type
     */
    public static class FixedArrayDeque<E> extends AbstractCollection<E>
                                           implements Deque<E>
    {
        private static final int DEFAULT_CAPACITY = 16;
        private final E[] elements;
        private int head = 0, tail = 0;

        /**
         * Construct a `FixedArrayDeque` with the size `DEFAULT_CAPACITY`.
         */
        @SuppressWarnings("unused")
        public FixedArrayDeque() {
            this(DEFAULT_CAPACITY);
        }

        /**
         * Construct a `FixedArrayDeque` with the specified capacity.
         *
         * @param capacity the capacity, at the range of (0, allowed max array size]
         * @throws IllegalArgumentException if the specified capacity is out of the range of (0, allowed max array size]
         */
        public FixedArrayDeque(int capacity) {
            assert capacity > 0;
            //assert capacity <= allowed max array size;
            @SuppressWarnings("unchecked")
            E[] elms = (E[]) new Object[capacity];
            this.elements = elms;
        }

        /**
         * Construct a `FixedArrayDeque` and add all of the element of the specified collection into this collection.
         *
         * @param collection the specified collection
         * @throws IllegalArgumentException if the size of the specified collection is larger than the allowed max array size
         * @throws NullPointerException     if the specified collection is null
         */
        @SuppressWarnings({"unchecked", "unused"})
        public FixedArrayDeque(Collection<? extends E> collection) {
            int size = collection.size();
            //assert size <= allowed max array size;

            this.elements = (E[]) new Object[size];
            this.addAll(collection);
        }

        private int increment(int index) {
            return (index + 1) % elements.length;
        }
        private int decrement(int index) {
            //return Math.floorMod(index - 1, elements.length);
            return (index - 1 + elements.length) % elements.length;
        }

        /**
         * Returns the number of elements in this collection.
         * The returned value is not greater than and equal to the allowed max array size.
         *
         * @return the number of elements in this collection
         */
        @Override
        public int size() {
            //return Math.floorMod(this.tail - this.head, this.elements.length);
            if(this.head <= this.tail) return this.tail - this.head;
            else return (elements.length - this.head) + this.tail;
        }

        /**
         * Returns true if this collection contains no elements.
         *
         * @return true if this collection contains no elements, otherwise false
         */
        @Override
        public boolean isEmpty() {
            return this.tail == this.head;
        }

        private boolean isFull() {
            return increment(tail) == head;
        }

        /**
         * Returns true if this collection contains the specified element.
         * This collection allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         * If the specified element is incompatible with this collection, return `false` rather than throw `ClassCastException`.
         *
         * @param o the specified element, may null
         * @return true if this collection contains the specified element
         */
        @Override
        public boolean contains(Object o) {
            int idx = this.head;
            while(idx != this.tail) {
                if(Objects.equals(o, this.elements[idx])) return true;
                idx = increment(idx);
            }
            return false;
        }

        /**
         * Returns an array containing all of the elements in this collection, keeping the same order with traversing from head to tail.
         *
         * The returned array will be "safe" in that no references to it are maintained by this collection.
         * (In other words, this method must allocate a new array even if this collection is backed by an array).
         * The caller is thus free to modify the returned array.
         *
         * This method acts as bridge between array-based and collection-based APIs.
         *
         * @return an array containing all of the elements in this collection
         */
        @Override
        public Object[] toArray() {
            Object[] r = new Object[this.size()];
            int idx = this.head;
            for(int i=0; i<r.length; i++) {
                r[i] = this.elements[idx];
                idx = increment(idx);
            }
            return r;
        }

        /**
         * Returns an array containing all of the elements in this collection.
         *
         * If the collection fits in(>=) the specified array, it is returned therein.
         * Otherwise, a new array is allocated with the runtime type of the specified array and the size of this collection.
         *
         * If the specified array has more elements than this collection,
         * the element in the array immediately following the end of the collection is set to null.
         *
         * Keep the same order with traversing from head to tail.
         *
         * This method acts as bridge between array-based and collection-based APIs, and control over the runtime type, save allocation costs.
         *
         * @param a  the specified array
         * @param <T> the type of the array to contain the collection
         * @return an array containing all of the elements in this collection
         * @throws ArrayStoreException  if the runtime type of the specified array is not a supertype of the runtime type of every element in this collection
         * @throws NullPointerException if the specified array is null
         */
        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            int size = this.size();
            T[] r = a.length > size ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), size);

            int idx = this.head, i = 0;
            for(; i < a.length && idx != this.tail; i++) {
                T t = (T) this.elements[idx];
                r[i] = t;
                idx = increment(idx);
            }
            for(; i<a.length; i++) {
                a[i] = null;
            }

            return r;
        }


        /**
         * Returns true if this collection contains all of the elements in the specified collection.
         * If the element'type in the specified collection is incompatible with this collection, return `false` rather than throw `ClassCastException`.
         *
         * @param c collection to be checked for containment in this collection
         * @return true if this collection contains all of the elements in the specified collection
         * @throws NullPointerException   if the specified collection is null
         */
        @Override
        public boolean containsAll(Collection<?> c) {
            return super.containsAll(c);
        }

        /**
         * Adds all of the elements in the specified collection to this collection.
         * Returns true if this collection changed as a result of the call.
         *
         * The behavior of this operation is undefined if the specified collection is modified while the operation is in progress.
         * (This implies that the behavior of this call is undefined if the specified collection is this collection, and this collection is nonempty.)
         *
         * @param c collection containing elements to be added to this collection
         * @return  true if this collection changed as a result of the call
         * @throws NullPointerException    if the specified collection is null
         * @throws IllegalStateException   if some elements cannot be added at this time due to capacity restrictions
         */
        @Override
        public boolean addAll(Collection<? extends E> c) {
            return super.addAll(c);
        }

        /**
         * Removes all of the specified element from this collection.
         * Returns true if this collection changed as a result of the call.
         * After this call returns, this collection will contain no elements in common with the specified collection.
         *
         * @param c collection containing elements to be removed from this collection
         * @return true if this collection changed as a result of the call
         * @throws ClassCastException      if the types of one or more elements in this collection are incompatible with the specified collection(optional)
         * @throws NullPointerException    if this collection contains one or more null elements and the specified collection does not support null elements(optional),
         *                              or if the specified collection is null
         */
        @Override
        public boolean removeAll(Collection<?> c) {
            return super.removeAll(c);
        }

        /**
         * Retains the elements in this collection that are contained in the specified collection.
         * Returns true if this collection changed as a result of the call.
         *
         * @param c collection containing elements to be retained in this collection
         * @return  true if this collection changed as a result of the call
         * @throws ClassCastException      if the types of one or more elements in this collection are incompatible with the specified collection(optional)
         * @throws NullPointerException    if this collection contains one or more null elements and the specified collection does not permit null elements(optional)
         *                              or if the specified collection is null
         */
        @Override
        public boolean retainAll(Collection<?> c) {
            return super.retainAll(c);
        }

        /**
         * Removes all of the elements from this collection.
         * The collection will be empty after this method returns.
         */
        @Override
        public void clear() {
            //this.elements = null;
            this.head = this.tail = 0;
        }

        /**
         * Removes the first occurrence(from head to tail) of the specified element from this collection.
         * Returns true if this collection contains the specified element.
         *
         * This collection allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         *
         * This method is equivalent to `removeFirstOccurrence(Object)`.
         *
         * If the specified element is incompatible with this collection, then no `ClassCastException` is thrown and
         * this method will return false to indicate that the specified element is not within in this collection.
         *
         * @param o element to be removed from this collection, may null
         * @return true if an element was removed as a result of this call
         */
        @Override
        public boolean remove(Object o) {
            return removeFirstOccurrence(o);
        }

        ////for deque

        /**
         * Inserts the specified element at the front of this deque.
         *
         * @param e the element to add
         * @throws IllegalStateException     if the element cannot be added at this time due to capacity restrictions
         */
        @Override
        public void addFirst(E e) {
            if(isFull()) throw new IllegalStateException("has no more space");
            head = decrement(head);
            elements[head] = e;      //no ArrayStoreException
        }

        /**
         * Inserts the specified element at the end of this deque.
         *
         * @param e the element to add
         * @throws IllegalStateException     if the element cannot be added at this time due to capacity restrictions
         */
        @Override
        public void addLast(E e) {
            if(isFull()) throw new IllegalStateException("has no more space");
            elements[tail] = e;
            tail = increment(tail);
        }

        /**
         * Inserts the specified element at the front of this deque.
         *
         * @param e the element to add
         * @return true if the element was added to this deque, else false
         */
        @Override
        public boolean offerFirst(E e) {
            if(isFull()) return false;
            head = decrement(head);
            elements[head] = e;
            return true;
        }

        /**
         * Inserts the specified element at the end of this deque.
         *
         * @param e the element to add
         * @return true if the element was added to this deque, else false
         */
        @Override
        public boolean offerLast(E e) {
            if(isFull()) return false;
            elements[tail] = e;
            tail = increment(tail);
            return true;
        }

        /**
         * Retrieves and removes the first element of this deque.
         *
         * @return the head of this deque
         * @throws NoSuchElementException if this deque is empty
         */
        @Override
        public E removeFirst() {
            if(isEmpty()) throw new NoSuchElementException("there is no elements");
            E e = elements[head];
            head = increment(head);
            return e;
        }

        /**
         * Retrieves and removes the last element of this deque.
         *
         * @return the tail of this deque
         * @throws NoSuchElementException if this deque is empty
         */
        @Override
        public E removeLast() {
            if(isEmpty()) throw new NoSuchElementException("there is no elements");
            tail = decrement(tail);
            return elements[tail];
        }

        /**
         * Retrieves and removes the first element of this deque, or returns null if this deque is empty.
         *
         * @return the head of this deque, or null if this deque is empty
         */
        @Override
        public E pollFirst() {
            if(isEmpty()) return null;
            E e = elements[head];
            head = increment(head);
            return e;  //may null
        }

        /**
         * Retrieves and removes the last element of this deque, or returns null if this deque is empty.
         *
         * @return the tail of this deque, or null if this deque is empty
         */
        @Override
        public E pollLast() {
            if(isEmpty()) return null;
            tail = decrement(tail);
            return elements[tail];  //may null
        }

        /**
         * Retrieves, but does not remove, the first element of this deque.
         *
         * @return the head of this deque
         * @throws NoSuchElementException if this deque is empty
         */
        @Override
        public E getFirst() {
            if(isEmpty()) throw new NoSuchElementException("there is no elements");
            return elements[head];
        }

        /**
         * Retrieves, but does not remove, the last element of this deque.
         *
         * @return the tail of this deque
         * @throws NoSuchElementException if this deque is empty
         */
        @Override
        public E getLast() {
            if(isEmpty()) throw new NoSuchElementException("there is no elements");
            return elements[decrement(tail)];
        }

        /**
         * Retrieves, but does not remove, the first element of this deque, or returns null if this deque is empty.
         *
         * @return the head of this deque
         */
        @Override
        public E peekFirst() {
            if(isEmpty()) return null;
            return elements[head];  //may null
        }

        /**
         * Retrieves, but does not remove, the last element of this deque, or returns null if this deque is empty.
         *
         * @return the tail of this deque
         */
        @Override
        public E peekLast() {
            if(isEmpty()) return null;
            return elements[decrement(tail)];  //may null
        }

        /**
         * Removes the first occurrence(from head to tail) of the specified element from this deque.
         * If the deque does not contain the element, it is unchanged, and will return false.
         *
         * This deque allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         *
         * If the specified element is incompatible with this collection, then no `ClassCastException` is thrown and
         * this method will return false to indicate that the specified element is not within in this collection.
         *
         * @param o element to be removed from this deque, may null
         * @return true if an element was removed as a result of this call
         */
        @Override
        public boolean removeFirstOccurrence(Object o) {
            int removeIdx = -1;
            int idx = this.head;
            while(idx != this.tail) {
                if(Objects.equals(o, this.elements[idx])) {
                    removeIdx = idx;
                    break;
                }
                idx = increment(idx);
            }
            if(removeIdx != -1) {
                removeAndShift(removeIdx);
                return true;
            }
            return false;
        }

        private boolean removeAndShift(int idx) {
            if((head <= idx && idx < tail) || (head > tail && ((head <= idx && idx < elements.length) || (0 <= idx && idx < tail)))) {
                if(idx == head) {
                    head = increment(head);
                } else {
                    tail = decrement(tail);
                    while (idx != tail) {
                        int np = increment(idx);
                        elements[idx] = elements[np];
                        idx = np;
                    }
                }
                return true;
            }
            return false;
        }

        /**
         * Removes the last occurrence(from head to tail) of the specified element from this deque.
         * If the deque does not contain the element, it is unchanged, and will return false.
         *
         * This deque allow null element, so check equality by `o==null ? e==null : o.equals(e)`.
         *
         * If the specified element is incompatible with this collection, then no `ClassCastException` is thrown and
         * this method will return false to indicate that the specified element is not within in this collection.
         *
         * @param o element to be removed from this deque, may null
         * @return true if an element was removed as a result of this call
         */
        @Override
        public boolean removeLastOccurrence(Object o) {
            int removeIdx = -1;
            int idx = this.head;
            while(idx != this.tail) {
                if(Objects.equals(o, this.elements[idx])) {
                    removeIdx = idx;
                }
                idx = increment(idx);
            }
            if(removeIdx != -1) {
                removeAndShift(removeIdx);
                return true;
            }
            return false;
            /*
            if(isEmpty()) return false;
            int removeIdx = -1;
            int idx = tail;
            while(idx != head) {
                if(Objects.equals(o, this.elements[idx])) {
                    removeIdx = idx;
                    break;
                }
                idx = increment(idx);
            }
            if(removeIdx != -1) {
                removeAndShift(removeIdx);
                return true;
            } else if(Objects.equals(o, this.elements[head])) {
                removeAndShift(removeIdx);
                return true;
            }
            return false;*/
        }

        ////for queue

        /**
         * Inserts the specified element into the queue represented by this deque (at the tail of this deque).
         * Returning true if this collection changed as a result of the call.
         *
         * This method is equivalent to `addLast(E)`.
         *
         * @param e the element to add
         * @return true if this collection changed as a result of the call
         * @throws IllegalStateException    if the element cannot be added at this time due to capacity restrictions
         */
        @Override
        public boolean add(E e) {
            addLast(e);
            return true;
        }

        /**
         * Inserts the specified element into the queue represented by this deque (at the tail of this deque).
         * Returning true upon success and will return false if no space is currently available.
         *
         * This method is equivalent to `offerLast(E)`.
         *
         * @param e the element to add
         * @return true if this collection changed as a result of the call
         */
        @Override
        public boolean offer(E e) {
            return offerLast(e);
        }

        /**
         * Retrieves and removes the head of the queue represented by this deque (the first element of this deque).
         * This method is equivalent to `removeFirst()`.
         *
         * @return the head of the queue represented by this deque
         * @throws NoSuchElementException if this deque is empty
         */
        @Override
        public E remove() {
            return removeFirst();
        }

        /**
         * Retrieves and removes the head of the queue represented by this deque (the first element of this deque)
         * or returns null if this deque is empty.
         *
         * This method is equivalent to `pollFirst()`.
         *
         * @return the first element of this deque, or null if this deque is empty
         */
        @Override
        public E poll() {
            return pollFirst();
        }

        /**
         * Retrieves, but does not remove, the head of the queue represented by this deque (the first element of this deque).
         * This method is equivalent to `getFirst()`.
         *
         * @return the head of the queue represented by this deque
         * @throws NoSuchElementException if this queue is empty
         */
        @Override
        public E element() {
            return getFirst();
        }

        /**
         * Retrieves, but does not remove, the head of the queue represented by this deque (the first element of this deque)
         * or returns null if this deque is empty.
         *
         * This method is equivalent to `peekFirst()`.
         * @return the head of the queue represented by this deque, or null if this deque is empty
         */
        @Override
        public E peek() {
            return peekFirst();
        }

        ////for stack

        /**
         * Pushes an element onto the stack represented by this deque (at the head of this deque).
         * This method is equivalent to `addFirst(E)`.
         *
         * @param e the element to push
         * @throws IllegalStateException     if the element cannot be added at this time due to capacity restrictions
         */
        public void push(E e) {
            addFirst(e);
        }

        /**
         * Pops an element from the stack represented by this deque.
         * This method is equivalent to `removeFirst()`.
         *
         * @return the element at the front of this deque (which is the top of the stack represented by this deque)
         * @throws NoSuchElementException if this deque is empty
         */
        public E pop() {
            return removeFirst();
        }

        private class Itr implements Iterator<E> {
            private int idx = head;
            private int prev = -1;

            @Override
            public boolean hasNext() {
                return idx != tail;
            }

            @Override
            public E next() {
                if(idx == tail)
                    throw new NoSuchElementException("has no more elements");
                E e = elements[idx];
                prev = idx;
                idx = increment(idx);
                return e;
            }

            @Override
            public void remove() {
                if(prev == -1) throw new IllegalStateException("can not remove before the next method called");
                else if(prev == -2) throw new IllegalStateException("can not call remove method again");

                removeAndShift(prev);
                idx = decrement(idx);
                prev = -2;
            }
        }

        /**
         * Returns an iterator over the elements in this collection.
         * The elements will be returned in order from first (head) to last (tail).
         *
         * @return an iterator
         */
        @Override
        public Iterator<E> iterator() {
            return new Itr();
        }

        private class DescendingItr implements Iterator<E> {
            private int idx = decrement(tail);
            private int prev = -1;
            private boolean notEnd = (head != tail);

            @Override
            public boolean hasNext() {
                return notEnd;
            }

            @Override
            public E next() {
                if(!notEnd)
                    throw new NoSuchElementException("has no more elements");
                E e = elements[idx];
                if(idx == head) notEnd = false;

                prev = idx;
                idx = decrement(idx);
                return e;
            }

            @Override
            public void remove() {
                if(prev == -1) throw new IllegalStateException("can not remove before the next method called");
                else if(prev == -2) throw new IllegalStateException("can not call remove method again");

                removeAndShift(prev);
                idx = decrement(idx);
                prev = -2;
            }
        }

        @Override
        public Iterator<E> descendingIterator() {
            return new DescendingItr();
        }

        @Override
        public Spliterator<E> spliterator() {
            return null; //todo
        }

        @Override
        public Stream<E> stream() {
            return null;  //todo
        }

        @Override
        public Stream<E> parallelStream() {
            return null;  //todo
        }
    }
}
