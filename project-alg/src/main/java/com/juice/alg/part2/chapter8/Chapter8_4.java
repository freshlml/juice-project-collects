package com.juice.alg.part2.chapter8;


import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;
import com.juice.alg.part1.chapter2.Chapter2_Practice2;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Chapter8_4 {

    /**
     *桶排序思想
     *  将 n 个数的序列映射到桶
     *                             桶
     *                          0 |   |
     *                          1 |   |
     *  A⟨ A1, A2, A3, ... An ⟩  2 |   |
     *                            |...|
     *                        k-1 |   |
     *
     *  1. 确定桶的大小 k
     *     k <= n。完美情况是当 k == n 时，n 个数一人占一个位置。若 k < n，无疑增大了碰撞的概率
     *  2. 确定映射关系。"定义域"为输入序列，"值域"为桶下标的集合
     *     映射关系需满足: 大的数映射的桶下标不小于小的数映射的桶下标. (故，常见的 hash 散列此处不适合)
     *  3. 桶下标碰撞处理: 对于映射到同一桶下标的数需要局部排序
     *     如，可以用链表记录，每次发生碰撞时，执行插入排序
     *  4. 将 n 个数映射到桶之后，遍历桶得到排序后的序列
     *
     *
     *输入序列 A 满足 [0, 1) 上的均匀分布:
     *  将 [0, 1) 分成 k 个子区间（称为 k 个桶），保证每个子区间长度相等，如:      [0, 1/k), [1/k, 2/k), ... ,[k-1/k, 1)
     *                                                            桶下标：0，       1，              k-1     故而，桶下标映射关系为: Ai*k
     *
     *  将输入序列 A 的 n 个元素映射到桶之中，因为 A 满足 [0, 1) 上的均匀分布，所以落在桶之中的数的数量一般比较均匀
     *
     *  注: 因为 A 满足的是 [0, 1) 上的均匀分布，所以才对 [0, 1) 区间的划分要保证每个子区间长度相等，这样可以保证桶中数的数量的均衡性
     *     如果 A 满足的是其他分布，则对 [0, 1) 区间的划分方式不尽相同，但都有同一个目的: 使桶中数的数量更加均衡
     *
     *  平均情况运行时间：
     *    随机变量X: 内层循环的总循环次数  //等于 e.key <= nn.key 的执行次数
     *    T(n) = Θ(n) + X
     *
     *    X = Σ(i=0~n-2)Σ(j=i+1~n-1) X<i,j>,  其中 X<i,j> 表示 i 与 j 的比较次数 (i < j) = 0
     *                                                                                = 1
     *    P(X<i,j> = 1) = 1/n^2    //因为 A 满足 [0, 1) 上的均匀分布
     *
     *    EX = E[ Σ(i=0~n-2)Σ(j=i+1~n-1) X<i,j> ]
     *       = Σ(i=0~n-2)Σ(j=i+1~n-1) EX<i,j>
     *       = Σ(i=0~n-2)Σ(j=i+1~n-1) 1/n^2
     *       = Θ(1)
     *    故 T(n) = Θ(n) + Θ(1) = Θ(n)
     *
     *    另: 随机变量Yi: 第 i 个桶中元素个数. T(n) = Θ(n) + Σ(i=1~n) [Yi^2]
     *
     *等价描述: 输入序列 A 满足 [0, n) 上的均匀分布:
     *  将 [0, n) 分成 k 个子区间（称为 k 个桶），保证每个子区间长度相等，如:    [0, n/k), [n/k, 2n/k), [2n/k, 3n/k), ... ,[((k-1)n)/k, n)
     *                                                          桶下标：0，        1，          2，                k-1     故而，桶下标映射关系为: (Ai*k)/n
     */
    public static double[] bucket_sort(double[] a, int k) {
        if(a == null) return null;
        if(a.length == 0 || a.length == 1) return a;

        int n = a.length;
        k = Math.min(k, n);
        Node[] bucket = new Node[k];
        for(int j=0; j<n; j++) {
            int idx = (int) (a[j] * k);  //因为我们假定 a[j] ∈ [0, 1), 所以 a[j] * k ∈ [0, k)
            Node nn = new Node(a[j], null);

            Node prev = null;
            Node e = bucket[idx];
            while(e != null && e.key <= nn.key) {
                prev = e;
                e = e.next;
            }
            nn.next = e;
            if(prev != null) {
                prev.next = nn;
            } else {
                bucket[idx] = nn;
            }
        }

        double[] result = new double[n];
        for(int i=0, j=0; i<k; i++) {
            Node p;
            for(p = bucket[i]; p != null; ) {
                result[j++] = p.key;
                p = p.next;
            }
        }

        return result;
    }

    public static void main(String[] argv) {
        double[] d = new double[] {0.1, 0.01, 0.101, 0.211001, 0.9990001, 0.99999999, 0.987654321};
        ArrayTraversal.of(d).forEach(ArrayPrinter.of()::print);
        double[] result = bucket_sort(d, 10);
        ArrayTraversal.of(result).forEach(ArrayPrinter.of()::print);

        System.out.println("---------------------------------------");

        int[] a = new int[] {6, 5, 5, 2, 1, 3, 6, 7};
        int[] b = counting_sort_using_bucket(a, 0, 10);
        ArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
        ArrayTraversal.of(b).forEach(ArrayPrinter.of()::print);

    }

    //练习8.4-2: 探查 k 次，找空位
    //  (n/k)^2 = n*lgn, k = (n/lgn)^(1/2)

    //练习8.4-3: E[X*Y] = E[X] * E[Y] 当 X, Y 相互独立时
    //          E^2[X] = E[X^2] - 方差

    //练习8.4-4
    /*
    Π(r2)^2 - Π(r1)^2 = Π(r1)^2, 即 r2 = 根号2 * r1, 以此类推:

    区间划分                                     根号m * r1 = 1 ==> r1 = 1/根号m             下标映射
    [0, r1)                      1号区间         [0, 1/根号m)                               (d * 根号m)^2 --> 0
    [r1, 根号2 * r1)              2号区间         [1/根号m, 根号2/根号m)                       (d * 根号m)^2 --> 1
    [根号2 * r1, 根号3 * r1)       3号区间         [根号2/根号m, 根号3/根号m)                    (d * 根号m)^2 --> 2
    [根号3 * r1, 根号4 * r1)       4号区间         [根号3/根号m, 根号4/根号m)                    (d * 根号m)^2 --> 3
    ...
    [根号m-1 * r1, 根号m * r1)     m号区间         [根号m-1/根号m, 根号m/根号m)                  (d * 根号m)^2 --> m-1

    可令 m = n, 共 n 个区间
        [0, 1/根号n), [1/根号n, 根号2/根号n), [根号2/根号n, 根号3/根号n), ... , [根号n-1/根号n, 根号n/根号n) ; 下标映射 (d * 根号n)^2

    或者令 m = n^2, 共 n^2 个区间
        [0, 1/n), [1/n, 根号2/n), [根号2/n, 根号3/n), ... , [根号n-1/n, 根号n/n) ; 下标映射 (d * n)^2

     */

    //练习8.4-5 todo


    public static int[] counting_sort_using_bucket(int[] a, int begin, int end) {
        if(a == null) return null;
        if(a.length == 0 || a.length == 1) return a;

        CountingNode[] bucket = new CountingNode[end - begin];

        for(int i=0; i<a.length; i++) {
            int idx = counting_index(a[i], begin);

            CountingNode head;
            CountingNode nn = new CountingNode(a[i], null, null);
            if((head = bucket[idx]) == null) {
                bucket[idx] = nn;
                nn.next = nn.prev = nn;
            } else {
                CountingNode tail = head.prev;
                tail.next = nn;
                nn.prev = tail;

                head.prev = nn;
                nn.next = head;
            }
        }

        int[] b = new int[a.length];
        for(int k=0, j=0; k<bucket.length; k++) {
            CountingNode p, head = bucket[k];
            for(p = head; p != null; ) {
                b[j++] = p.key;
                p = p.next;
                if(p == head) break;
            }
        }

        return b;
    }
    static int counting_index(int ai, int min) {
        return ai - min;
    }

    private static class CountingNode {
        int key;
        CountingNode next;
        CountingNode prev;

        CountingNode(int key, CountingNode next, CountingNode prev) {
            this.key = key;
            this.next = next;
            this.prev = prev;
        }
    }

    private static class Node {
        double key;
        Node next;

        Node(double key, Node next) {
            this.key = key;
            this.next = next;
        }
    }


    /**
     * A more broad array traversal, not only for int array.
     * @see com.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal
     * @param <ARRAY_TYPE>
     */
    public static class ArrayTraversal<ARRAY_TYPE> {
        private final ARRAY_TYPE array;
        private final int begin;
        private final int end;

        /**
         * Array traversal
         * @param array  the array for traversal
         * @throws NullPointerException    if the specified array is null
         */
        private ArrayTraversal(ARRAY_TYPE array) {
            this(array, 0, Array.getLength(array));
        }

        /**
         * if `begin <= end`, traversal from `begin`   to `end-1` at range [begin, end)
         * if `begin >  end`, traversal from `begin-1` to `end`   at range [end, begin)
         * @param array  the array for traversal
         * @param begin  the start position of traversal
         * @param end    the end position of traversal
         * @throws NullPointerException          if the specified array is null
         * @throws IllegalArgumentException      if the specified begin, end is negative
         *                                    or if begin <= end and end > array.length
         *                                    or if begin >  end and begin > array.length
         *                                    or if the specified array is not an array-type
         * @throws RuntimeException with a nested reflection-based exception
         */
        private ArrayTraversal(ARRAY_TYPE array, int begin, int end) {
            this(array, begin, end, false);
        }

        /**
         * if `begin <= end`, traversal from `begin`   to `end-1` at range [begin, end)
         * if `begin >  end`, traversal from `begin-1` to `end`   at range [end, begin)
         * @param array  the array for traversal
         * @param begin  the start position of traversal
         * @param end    the end position of traversal
         * @param byUnsafe true: by Unsafe api, false: by reflect api
         * @throws NullPointerException          if the specified array is null
         * @throws IllegalArgumentException      if the specified begin, end is negative
         *                                    or if begin <= end and end > array.length
         *                                    or if begin >  end and begin > array.length
         *                                    or if the specified array is not an array-type
         * @throws RuntimeException with a nested reflection-based exception
         */
        private ArrayTraversal(ARRAY_TYPE array, int begin, int end, boolean byUnsafe) {
            if(array == null) throw new NullPointerException("array can not be null");
            int length = Array.getLength(array);

            if(begin < 0 || end < 0)
                throw new IllegalArgumentException("the specified begin or end is negative, begin = " + begin + ", end = " + end);
            if(begin <= end && end > length)
                throw new IllegalArgumentException("the specified end is large than array's length, end = " + end);
            if(begin > end && begin > length)
                throw new IllegalArgumentException("the specified begin is large than array's length, begin = " + begin);

            this.array = array;
            this.begin = begin;
            this.end = end;

            init(array, byUnsafe);
        }

        public static <ARRAY_TYPE> ArrayTraversal<ARRAY_TYPE> of(ARRAY_TYPE array) {
            return new ArrayTraversal<>(array);
        }

        public static <ARRAY_TYPE> ArrayTraversal<ARRAY_TYPE> of(ARRAY_TYPE array, int begin, int end) {
            return new ArrayTraversal<>(array, begin, end);
        }

        public static <ARRAY_TYPE> ArrayTraversal<ARRAY_TYPE> of(ARRAY_TYPE array, int begin, int end, boolean byUnsafe) {
            return new ArrayTraversal<>(array, begin, end, byUnsafe);
        }

        public void forEach(Chapter2_Practice2.IntArrayTraversal.PerElement<Object> handler) {
            if(begin <= end) {
                for(int i = begin; i < end; i++) {
                    handler.per(this.get(i), i, end - 1);
                }
            } else {
                for(int i = begin-1; i >= end; i--) {
                    handler.per(this.get(i), i, end);
                }
            }
        }

        private Object get(int i) {
            if(!byUnsafe) {
                return Array.get(array, i);
            }

            long offset = byteOffset(i);
            Object e;
            if(boolean.class == componentType) {
                e = unsafe.getBoolean(array, offset);
            } else if(byte.class == componentType) {
                e = unsafe.getByte(array, offset);
            } else if(short.class == componentType) {
                e = unsafe.getShort(array, offset);
            } else if(char.class == componentType) {
                e = unsafe.getChar(array, offset);
            } else if(int.class == componentType) {
                e = unsafe.getInt(array, offset);
            } else if(long.class == componentType) {
                e = unsafe.getLong(array, offset);
            } else if(float.class == componentType) {
                e = unsafe.getFloat(array, offset);
            } else if(double.class == componentType) {
                e = unsafe.getDouble(array, offset);
            } else {
                e = unsafe.getObject(array, offset);
            }

            return e;
        }

        private boolean byUnsafe;
        private Unsafe unsafe;
        private int base;
        private int shift;
        private Class<?> componentType;

        private void init(ARRAY_TYPE array, boolean byUnsafe) {
            this.byUnsafe = byUnsafe;
            if(!byUnsafe) return;

            Class<?> arrayType = array.getClass();
            //assert arrayType.isArray();

            componentType = arrayType.getComponentType();
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);

                base = unsafe.arrayBaseOffset(arrayType);
                int scale = unsafe.arrayIndexScale(arrayType);
                if ((scale & (scale - 1)) != 0)
                    throw new Error("data type scale not a power of two");
                shift = 31 - Integer.numberOfLeadingZeros(scale);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private long byteOffset(int i) {
            return ((long) i << shift) + base;
        }

    }
}
