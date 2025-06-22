package com.juice.alg.part1.chapter2;

import java.util.Arrays;

public class Chapter2 {

    public static void main(String[] argv) {

        int[] a = {7, 3, 2, 4, 5, 2};
        insertion_sort(a);
        Arrays.stream(a).forEach(ArrayPrinter.of(a.length)::print);
        System.out.println("#############################################");

        int[] b = {7, 3, 2, 4, 5, 2};
        selection_sort(b);
        Arrays.stream(b).forEach(ArrayPrinter.of(a.length)::print);
        System.out.println("#############################################");

        int[] c = {7, 3, 2, 4, 5, 2};
        merge_sort(c);
        Arrays.stream(c).forEach(ArrayPrinter.of(a.length)::print);
        System.out.println("#############################################");

        Arrays.stream(c).forEach(ArrayPrinter.of(3)::print);  //ArrayPrinter.of(3): parameter 3 is not a matched length
    }


    public static void insertion_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int i=1; i < a.length; i++) {
            int ka = a[i];
            int j = i - 1;
            while(j >= 0 && a[j] > ka) {  //如果使用 a[j] >= ka: 相等的值的原相对位置发生逆转
                a[j+1] = a[j];
                j--;
            }
            a[j+1] = ka;
        }

    }
    /**
     *"循环不变时式"的数学归纳法证明
     *
     * 初始化: 初始化令i=1，因为A[0]是有序的
     * 保持: A[0,1...,k]有序，循环一次后，A[0,1,...,k,k+1]有序
     * 终止: i=n时终止，此时A{0,1,...,n-1}有序
     *
     *insertion_sort运行时间分析( 假设每条语句运行的时间是一个常数 C )
     * T(n) = C1*n + C2*(n-1) + C3*ΣPi + C4*(ΣPi - 1)   , i=1 to n-1
     * 其中ΣPi'max = 1 + 2 + 3 + ... + (n-1) = n*(n-1)/2
     *    ΣPi'min = 1 + 1 + 1 + ... + 1 = n-1
     *
     * T(n)'max = C1*n + C2*(n-1) + C3*[n*(n-1)/2] + C4*[n*(n-1)/2 - 1]
     *          = C1*n + C2*n - C2 + (C3+C4)/2*n^2 - (C3+C4)/2*n - C4
     *          = C1*n + C2*n + (C3+C4)/2*n^2 - (C3+C4)/2*n - C2 - C4
     *          ≈ n^2              //忽略低阶项和常数倍数
     *
     * 注: C3=while循环执行时间; C4=while中所有语句执行一次的时间之和; C1=for; C2=for中除while之外的语句
     */


    public static void selection_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int i=0; i < a.length-1; i++) {
            int j = i+1;
            int p = i;
            while(j < a.length) {
                if(a[j] < a[p]) {
                    p = j;
                }
                j++;
            }
            int t = a[i];
            a[i] = a[p];
            a[p] = t;
        }

    }


    public static void merge_sort(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        merge_sort(a, 0, a.length);
    }
    /**
     *merge_sort运行时间分析 (递归树)
     *                        [n]
     *           [n/2]                      [n/2]                   第 1 次分解, n/2^1   2^0 次 merge
     *      [n/4]      [n/4]          [n/4]         [n/4]           第 2 次分解, n/2^2   2^1 次 merge
     *  [n/8]  [n/8] [n/8]  [n/8]  [n/8]  [n/8] [n/8]  [n/8]        第 3 次分解, n/2^3   2^2 次 merge
     *  ...
     *  [1],[1],...[1]                                              第 x 次分解, n/2^x   2^(x-1) 次 merge
     *
     *  n/2^x = 1 ==> 2^x = n ==>  x = lg(n)
     *
     *T(n) = C1*(2^0 + 2^1 +...+ 2^x) + [ 2^(x-1) * [C2+C3*2^1+C4*(2^1-1))] + 2^(x-2) * [C2+C3*2^2+C4*(2^2-1)] +...+ 2^0 * [C2+C3*2^x+C4*(2^x-1)] ]
     *     = 2*C1*n - C1 + C2*n - C2 + (C3+C4)*x*2^x - C4*n
     *     = 2*C1*n + C2*n + (C3+C4)*n*lg(n) - C4*n - C1 - C2
     *     ≈ n*lg(n)           //忽略低阶项和常数倍数
     *
     *注: C3=merge,for比较; C4=merge,for中语句一次执行的时间之和; C1=merge_sort中外围的语句之和; C2=merge中for之外的语句之和
     */
    public static void merge_sort(int[] a, int begin, int end) {
        int n = end - begin;
        if(n == 1) return;

        int mi = begin + n/2;   // ⌊n÷2⌋
        merge_sort(a, begin, mi);
        merge_sort(a, mi, end);

        merge(a, begin, mi, end);
    }
    public static void merge(int[] a, int p, int q, int r) {

        /*for(int i=p, j=q; i<j && j<r; ) {

            if(a[i] >= a[j]) {
                int t = a[j];
                for(int l=j; l>i; l--) {
                    a[l] = a[l-1];
                }
                a[i] = t;
                j++;
            }
            i++;
        }*/

        int[] left = new int[q-p+1];
        left[left.length-1] = Integer.MAX_VALUE;
        int[] right = new int[r-q+1];
        right[right.length-1] = Integer.MAX_VALUE;
        System.arraycopy(a, p, left, 0, left.length-1);
        System.arraycopy(a, q, right, 0, right.length-1);

        for(int i=0, j=0, l=p; l<r; l++) {
            if(left[i] > right[j]) {
                a[l] = right[j];
                j++;
            } else {
                a[l] = left[i];
                i++;
            }
        }

        /*int[] left = new int[q-p];
        System.arraycopy(a, p, left, 0, left.length);

        int[] right = new int[r-q];
        System.arraycopy(a, q, right, 0, right.length);

        for(int i = 0, j = 0, k = p; k < r; k++) {
            if(j == right.length || (i != left.length && left[i] < right[j])) {
                a[k] = left[i];
                i++;
            } else if(i == left.length || left[i] > right[j]) {
                a[k] = right[j];
                j++;
            } else {  //j != right.length && i != left.length && left[i] == right[j]
                a[k] = left[i];
                i++;
                a[++k] = right[j];
                j++;
            }
        }*/
    }


    public static class ArrayPrinter<E> {
        protected static final String SEP = ", ";
        @Deprecated
        final int length;
        protected int count = 1;

        ArrayPrinter(int length) {
            this.length = length;
        }
        protected ArrayPrinter() {
            this.length = -1;
        }

        public void print(E e, int pos, int limit) {
            printElement(e);
            if(pos == limit) printEnd();
            else printSep();
            count++;
        }

        void print(E t) {
            printElement(t);
            if(count < length) {
                printSep();
            } else {
                printEnd();
            }
            count++;
        }

        protected void printElement(E e) {
            System.out.print(e);
        }
        protected void printSep() {
            System.out.print(SEP);
        }
        protected void printEnd() {
            System.out.println();
        }

        static <E> ArrayPrinter<E> of(int length) {
            return new ArrayPrinter<>(length);
        }

        public static <E> ArrayPrinter<E> of() {
            return new ArrayPrinter<>();
        }
    }
}
