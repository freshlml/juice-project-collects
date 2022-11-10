package com.juice.alg.chapter3_10;

public class Chapter3_10_1 {


    interface Stack {
        void push(int e);
        int pop();
        int peek();
        boolean isEmpty();
        int size();
    }
    //固定大小数组表示的栈
    static class FixedCapacityStack implements Stack {
        private final int[] s;
        private int top = -1;
        private static final int DEFAULT_CAPACITY = 16;

        public FixedCapacityStack() {
            this(DEFAULT_CAPACITY);
        }
        public FixedCapacityStack(int capacity) {
            //assert 0 < capacity <= Integer.MAX_VALUE

            s = new int[capacity];
        }

        @Override
        public void push(int e) {
            if(isFill()) return; //overflow

            this.top++;
            this.s[this.top] = e;
        }

        @Override
        public int pop() {
            if(isEmpty()) return -1; //tag, 空

            return this.s[this.top--];
        }

        @Override
        public int peek() {
            if(isEmpty()) return -1; //tag, 空

            return this.s[this.top];
        }

        protected boolean isFill() {
            return this.top >= this.s.length - 1;
        }

        @Override
        public boolean isEmpty() {
            return this.top < 0;
        }

        @Override
        public int size() {
            int size = this.top + 1;
            return size <= 0 ? 0 : size;
        }

    }

    interface Queue {
        void offer(int e);
        int poll();
        int peek();
        boolean isEmpty();
        int size();
    }
    //固定大小数组表示的队列
    static class FixedCapacityQueue implements Queue {
        protected final int[] s;
        protected int head = 0;
        protected int tail = 0;
        private static final int DEFAULT_CAPACITY = 16;

        public FixedCapacityQueue() {
            this(DEFAULT_CAPACITY);
        }

        public FixedCapacityQueue(int capacity) {
            //assert 0 < capacity <= Integer.MAX_VALUE

            this.s = new int[capacity];
        }

        @Override
        public void offer(int e) {
            if(isFill()) return; //overflow

            this.s[this.tail] = e;
            this.tail = (this.tail + 1) % this.s.length;
        }

        @Override
        public int poll() {
            if(isEmpty()) return -1; //tag, 空

            int v = this.s[this.head];
            this.head = (this.head + 1) % this.s.length;
            return v;
        }

        @Override
        public int peek() {
            if(isEmpty()) return -1; //tag, 空

            return this.s[this.head];
        }

        protected boolean isFill() { //留空一位，用于与empty区分
            return this.head == (this.tail + 1) % this.s.length;
        }

        @Override
        public boolean isEmpty() {
            return this.head == this.tail;
        }

        @Override
        public int size() {
            return Math.floorMod(this.tail - this.head, this.s.length);
        }

    }

    interface Deque extends Queue {
        void offerFirst(int e);
        void offerLast(int e);
        int pollFirst();
        int pollLast();
        int peekFirst();
        int peekLast();
    }
    static class FixedCapacityDeque extends FixedCapacityQueue implements Deque {

        public FixedCapacityDeque() {
            super();
        }

        public FixedCapacityDeque(int capacity) {
            super(capacity);
        }

        @Override
        public void offerFirst(int e) {
            if(isFill()) return; //overflow

            this.head = Math.floorMod(this.head-1, this.s.length);
            this.s[this.head] = e;
        }

        @Override
        public void offerLast(int e) {
            offer(e);
        }

        @Override
        public int pollFirst() {
            return poll();
        }

        @Override
        public int pollLast() {
            if(isEmpty()) return -1; //tag，空

            this.tail = Math.floorMod(this.tail-1, this.s.length);
            return this.s[this.tail];
        }

        @Override
        public int peekFirst() {
            return peek();
        }

        @Override
        public int peekLast() {
            if(isEmpty()) return -1; //tag，空

            int idx = Math.floorMod(this.tail-1, this.s.length);
            return this.s[idx];
        }

    }



}
