package com.juice.alg.part2.chapter8;

import com.juice.alg.part1.chapter2.Chapter2_Practice2;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Chapter8_Practice {

    //思考题8-2
    //a: 计数，计数桶思想
    //b: 左-1指针，右-0指针相互交换

    //e: O(n+k),原址的计数排序，但不稳定
    public void counting_sort_merge(int[] a, int[][] c, int begin) {
        int n = a.length;
        //int k = c[0].length;

        for(int i=n-1; i>=0; ) {
            int idx = indexPos(begin, a[i]);
            int x = c[1][idx] - 1;
            int y = c[0][idx] - 1;

            if(i == x) {
                c[1][idx] = c[1][idx] - 1;
                i--;
            } else if(i > x && i <= y) {
                i--;
            } else { //i < x(×) or i > y
                int ex = a[idx];
                a[idx] = a[i];
                a[i] = ex;
                c[1][idx] = c[1][idx] - 1;
            }
        }
    }
    public int indexPos(int begin, int ai) {
        if(begin == 0) return ai;
        else if(begin > 0) return ai - begin;
        else return ai + begin;
    }


    //思考题8-3, link Chapter2_8_3 扩展2
    //a: 遍历一遍，得到最大位max-d，创建二维数组，按低位对齐，不足的高位填充0
    //b: 遍历一遍，得到最大位max-d，创建二维数组，按高位对齐，不足的低位填充任意<a的字符
    //todo: 更好的算法？


    //思考题8-4
    //a:
    /*
        for i from 1 to n on A:
            for j from 1 to n on B:
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break;
     最坏情况运行时间：n + n-1 + n-2 + ... + 1 = n*(n+1) / 2
    */
    //c:
    /*
    1.B序列均匀随机
       for i from 1 to n on A:
            for j from 1 to n on B:
                r = RANDOM(j, n)
                swap B[j], B[r]
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break;
        随机变量X：完成配对的总比较次数; Xi: 找到与A[i]相等的B[j]所花费的比较次数
        X = X1 + X2 + X3 + ... + Xn

        X1   1   2   3   4 ...   n
        P   1/n 1/n 1/n 1/n ... 1/n

        X2   1   2   3   4 ...   n-1
        P   1/n 1/n 1/n 1/n ... 1/n

        EX = n*(n+3)/4

     2.A序列均匀随机，同时B序列均匀随机
       for i from 1 to n on A:
            q = RANDOM(i, n);
            swap A[i], B[q]
            for j from 1 to n on B:
                r = RANDOM(j, n)
                swap B[j], B[r]
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break;

        随机变量X：完成配对的总比较次数
        X = Σ(i=1~n)Σ(j=i~n)Xij; Xij: A[i],B[j]是否比较  = 0
                                                        = 1
        P(Xij=1) = 1/(n-j+1)

        EX = Σ(i=1~n)Σ(j=i~n)EXij
           = Σ(i=1~n)Σ(j=i~n) 1/(n-j+1)  令 k=n-j; k∈[0, n-i]
           = Σ(i=1~n)Σ(k=0~n-i)1/(k+1) 求解link Chapter2_7_1

     */

    //思考题8-5
    //a: 全排序
    //b: 1  3
    //   2  4
    //   5  6
    //   7  8
    //   9  10
    //c: A[i] <= A[i+k]; i=1,2, ... ,n-k
    /*
    d:             1   2   3   ...   k
        0*k+1     | | | | | |  ...  | |
        1*k+1     | | | | | |  ...  | |
        2*k+1     | | | | | |  ...  | |
        .         ...
        (i-1)*k+1 | | | | | |  ...  | |
        i*k+1     | |...
        i = n/k; j = n%k
    k*i*lgi = n*lg(n/k)

    e: k个有序链表合并
     */

    //思考题8-7 todo

    public static void main(String[] argv) {

        int[] a = new int[] {1, 3, 2, 5, 6};
        ArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
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
