package com.juice.alg.part1.chapter2;

import java.util.Arrays;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;

public class Chapter2_Practice {

    //练习题 2.1-4
    public static int[] binary_add(int[] a, int[] b) {
        int[] c = new int[a.length + 1];

        for (int i = 0; i < a.length; i++) {
            int sum = a[i] + b[i] + c[i];
            switch (sum) {
                case 3:
                    c[i + 1] = 1;
                    c[i] = 1;
                    break;
                case 1:
                    c[i + 1] = 0;
                    c[i] = 1;
                    break;
                case 2:
                    c[i + 1] = 1;
                    c[i] = 0;
                    break;
                case 0:
                    c[i + 1] = 0;
                    c[i] = 0;
                    break;
                default:
                    c[i + 1] = 0;
                    c[i] = -1;
            }
        }
        return c;
    }
    /*
     *"循环不不变式":
     * 初始化: c[a.length+1] all赋值为0; i=0
     * 保持: c[k+1,k,...,0]为a[k,...,0]与b[k,...,0]的和 ===> c[k+2,k+1,...,0]为a[k+1,...,0]与b[k+1,...,0]
     * 终止: i=a.length终止
     */


    //练习题 2.2-1: 用Θ记号表示 n^3/1000 - 100*n^2 - 100*n + 3
    //  Θ(n^3)

    /**
     * 练习题 2.2-2: 选择排序 {@link Chapter2#selection_sort(int[])}
     */

    //练习题 2.3-4
    //T(n) = T(n-1) + D(n) + C(n)
    //     = T(n-1) + Θ(1) + Θ(n)


    public static void main(String[] argv) {
        int[] a = {100, 200, 200, 300, 301, 400, 400, 500, 600};

        int pos = binary_search(a, 4);
        Arrays.stream(a).forEach(PositionArrayPrinter.of(a.length, pos)::print);
        System.out.println("#############################################");

        pos = binary_search_af(a, 300);
        Arrays.stream(a).forEach(PositionArrayPrinter.of(a.length, pos)::print);
        System.out.println("#############################################");

        int[] b = {3, 6, 1, 2, 9, 2, 4, 7, 1};
        insertion_sort_binary(b);
        Arrays.stream(b).forEach(ArrayPrinter.of(a.length)::print);
        System.out.println("#############################################");

        Arrays.stream(b).forEach(PositionArrayPrinter.of(3, 5)::print);  //ArrayPosPrinter.of(3): parameter 3 is not a matched length
    }


    //练习题 2.3-5: 二分查找法, Θ(lg(n))
    public static int binary_search(int[] a, int key) {
        if (a == null) return -1;
        if (a.length == 0) return -1;

        return binary_search(a, 0, a.length, key);
    }

    public static int binary_search(int[] a, int begin, int end, int key) {
        int n = end - begin;
        if (n == 0) return -1;
        if (n == 1) return a[begin] == key ? begin : -1;

        int ret;
        int mi = begin + n / 2;
        if (a[mi] > key) {
            ret = binary_search(a, begin, mi, key);
        } else if (a[mi] < key) {
            ret = binary_search(a, mi + 1, end, key);
        } else {
            ret = mi;
        }

        return ret;
    }

    //练习2.3-6
    //1.递增序列找到第一个(反向来看)<=的
    public static int binary_search_af(int[] a, int key) {
        if (a == null) return -1;
        if (a.length == 0) return -1;

        return binary_search_af(a, 0, a.length, key);
    }

    public static int binary_search_af(int[] a, int begin, int end, int key) {

        int n = end - begin;
        if (n == 0) return -1;
        if (n == 1) return a[begin] <= key ? begin : -1;

        int pos;
        int mi = begin + n / 2;
        if (a[mi] > key) {
            pos = binary_search_af(a, begin, mi, key);
        } else {  //a[mi] <= key
            pos = binary_search_af(a, mi + 1, end, key);
            if (pos == -1) pos = mi;
        }

        return pos;
    }

    //2.并不能做到Θ(n*lgn), 实际还是Θ(n^2)
    public static int binary_search_af_mv(int[] a, int begin, int end, int key) {

        int n = end - begin;
        if (n == 0) return -1;
        if (n == 1) {
            if (a[begin] <= key) return begin;
            else {
                a[begin + 1] = a[begin];
                return -1;
            }
        }

        int pos = -1;
        int mi = begin + n / 2;
        if (a[mi] > key) {
            for (int k = end; k > mi; k--) {
                a[k] = a[k - 1];
            }
            pos = binary_search_af_mv(a, begin, mi, key);
        } else if (a[mi] <= key) {
            pos = binary_search_af_mv(a, mi + 1, end, key);
            if (pos == -1) pos = mi;
        }

        return pos;
    }

    public static void insertion_sort_binary(int[] a) {
        if (a == null) return;
        if (a.length == 0 || a.length == 1) return;

        for (int j = 1; j < a.length; j++) {
            int key = a[j];
            int pos = binary_search_af_mv(a, 0, j, key);
            a[pos + 1] = key;
        }

    }

    //练习 2.3-7
    // 1. 两重循环: n个元素，任取两个元素: C(2,n)=n*(n-1)/2，Θ(n^2)
    /* 2. 先排序，再查找(类二分查找):
         for(int i = 1; i < a.length; i++) {
             int ret = find(a, 0, a.length, i, key);
             if(ret != -1) return ret;
         }
                  i
         口 ... 口 口 口 ... 口
         int find(int[] a, int begin, int end, int i, int key) {
             if(end - begin <= 1) return -1;
             int prevSum = a[i] + a[i-1];
             if(prevSum == key) {
                 return i-1;
             } else if(prevSum < key) {
                 return find(a, begin + i, end, (end - begin - i)/2, key);  // ×
             } else {
                 return find(a, begin, i, (i - begin)/2, key);
             }
         }
     */
    // 3. 先排序，在查找
    //    for(int i = 0; i < a.length - 1; i++) {
    //       int ret = binary_search(a, i + 1, a.length, key - a[i]);
    //       if(ret != -1) return ret;
    //    }


    public static class PositionArrayPrinter<E> extends ArrayPrinter<E> {
        private final int position;
        private int weight = 0;
        private boolean tag = true;

        PositionArrayPrinter(int length, int position) {
            super(length);
            this.position = position;
        }
        PositionArrayPrinter(int position) {
            super();
            this.position = position;
        }

        @Override
        protected void printElement(E t) {
            System.out.print(t);
            if(count <= position) {
                String s = String.valueOf(t);
                weight += s.length();  //may overflow
            }

            if(count == position + 1) {
                tag = true;
            }
        }

        @Override
        protected void printSep() {
            super.printSep();
            if(count <= position) {
                weight += SEP.length();  //may overflow
            }
        }

        @Override
        protected void printEnd() {
            System.out.println();
            if(tag) {
                for (int i = 0; i < weight; i++) {
                    System.out.print(" ");
                }
                if (position >= 0) {
                    System.out.println("🚩, at position " + position);
                } else {
                    System.out.println("could not find, position = " + position);
                }
                tag = false;
            }
            //weight = 0;
        }

        static <E> PositionArrayPrinter<E> of(int length, int position) {
            return new PositionArrayPrinter<>(length, position);
        }

        public static <E> PositionArrayPrinter<E> of(int position) {
            return new PositionArrayPrinter<>(position);
        }

    }
}
