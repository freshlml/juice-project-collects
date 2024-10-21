package com.juice.alg.part1.chapter2;

import java.util.Arrays;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;

public class Chapter2_Practice {

    //练习题 2.1-4
    public static int[] binary_add(int[] a, int[] b) {
        int[] c = new int[a.length+1];

        for(int i=0; i < a.length; i++) {
            int sum = a[i] + b[i] + c[i];
            switch (sum) {
                case 3:
                    c[i+1] = 1;
                    c[i] = 1;
                    break;
                case 1:
                    c[i+1] = 0;
                    c[i] = 1;
                    break;
                case 2:
                    c[i+1] = 1;
                    c[i] = 0;
                    break;
                case 0:
                    c[i+1] = 0;
                    c[i] = 0;
                    break;
                default :
                    c[i+1] = 0;
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
    //  Θ = n^3

    /** 练习题 2.2-2: 选择排序 {@link Chapter2#selection_sort(int[])}*/


    public static void main(String[] argv) {
        int[] a = {1, 2, 2, 3, 4, 4, 5, 6};

        int pos = binary_search_af(a, 3);
        System.out.println(pos);
        System.out.println("#############################################");

        int[] b = {3, 6, 1, 2, 9, 2, 4, 7, 1};
        insertion_sort_binary(b);
        Arrays.stream(b).forEach(ArrayPrinter.of(a.length)::print);
        System.out.println("#############################################");

        int[] c = {9,2,3,2,3,8,6,1};
        System.out.println(merge_reverse_pair(c));
    }


    //练习题 2.3-5，二分查找法,Ω=log2(n)
    public static boolean binary_search(int[] a, int key) {
        if(a == null) return false;
        if(a.length == 0) return false;

        return binary_search(a, 0, a.length, key);
    }
    public static boolean binary_search(int[] a, int begin, int end, int key) {
        int n = end - begin;
        if(n==0) return false;
        if(n==1) return a[begin]==key;

        boolean ret;
        if(a[begin + n/2] > key) {
            ret = binary_search(a, begin, begin + n / 2, key);
        } else if(a[begin + n/2] < key) {
            ret = binary_search(a, begin + n/2 + 1, end, key);
        } else {
            ret = true;
        }

        return ret;
    }

    //练习2.3-6
    //1.递增序列找到第一个(逆序来看)<=的
    public static int binary_search_af(int[] a, int key) {
        if(a == null) return -1;
        if(a.length == 0) return -1;

        return binary_search_af(a, 0, a.length, key);
    }
    public static int binary_search_af(int[] a, int begin, int end, int key) {

        int n = end - begin;
        if(n == 0) return -1;
        if(n == 1) return a[begin]<=key ? begin : -1;

        int pos = -1;
        if(a[begin + n/2] > key) {
            pos = binary_search_af(a, begin, begin + n / 2, key);
        } else if(a[begin + n/2] <= key) {
            pos = binary_search_af(a, begin + n/2 + 1, end, key);
            if(pos == -1) pos = begin + n/2;
        }

        return pos;
    }
    //2.并不能做到Ω=n*log2(n),实际还是Ω=n^2
    public static int binary_search_af_mv(int[] a, int begin, int end, int key) {

        int n = end - begin;
        if(n == 0) return -1;
        if(n == 1) {
            if(a[begin]<=key) return begin;
            else {
                a[begin+1] = a[begin];
                return -1;
            }
        }

        int pos = -1;
        if(a[begin + n/2] > key) {
            for(int k=end; k>(begin + n/2); k--) {
                a[k] = a[k-1];
            }
            pos = binary_search_af_mv(a, begin, begin + n / 2, key);
        } else if(a[begin + n/2] <= key) {
            pos = binary_search_af_mv(a, begin + n/2 + 1, end, key);
            if(pos == -1) pos = begin + n/2;
        }

        return pos;
    }
    public static void insertion_sort_binary(int[] a) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int j = 1; j<a.length; j++) {
            int key = a[j];
            int pos = binary_search_af_mv(a, 0, j, key);
            a[pos+1] = key;
        }

    }


    //练习2.3-7
    //1.先归并排序，再二分查找
    //2.n个元素，任取两个元素: C(2,n)=n*(n-1)/2
    //3.todo


    //思考题2-1
    public static void merge_insert_sort(int[] a, int k) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        merge_insert_sort(a, 0, a.length, k);
    }
    public static void merge_insert_sort(int[] a, int begin, int end, int k) {

        int n = end - begin;
        if(n <= k) {
            sort_by_insert(a, begin, end);
            return;
        }

        merge_insert_sort(a, begin, begin + n/2, k);
        merge_insert_sort(a, begin + n/2, end, k);

        Chapter2.merge(a, begin, begin + n/2, end);
    }
    private static void sort_by_insert(int[] a, int begin, int end) {
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return;

        for(int j = begin+1; j<end; j++) {
            int key = a[j];
            int i = j-1;
            while(i >= 0 && a[i] > key) {
                a[i+1] = a[i];
                i--;
            }
            a[i+1] = key;
        }
    }
    /**
     *merge_insert_sort运行时间分析
     *                        [n]
     *           [n/2]                      [n/2]                   第1次分解   2^1   2^0次merge
     *      [n/4]      [n/4]          [n/4]         [n/4]           第2次分解   2^2   2^1次merge
     *  ...
     *  [k],[k],...[k]                                              第x次分解   2^x   2^(x-1)次merge
     *
     *  n/2^x = 1 ==> 2^x = n ==>  x = log2(n)
     *
     *T(n) = C1*(2^0 + 2^1 +...+ 2^x) + [n/k * T(insert)] + [ 2^(x-1) * [C2+C3*2^1*k+C4*(2^1*k-1))] + 2^(x-2) * [C2+C3*2^2*k+C4*(2^2*k-1))] +...+ 2^0 * [C2+C3*2^x*k+C4*(2^x*k-1))] ]
     *     = 2*C1*n/k - C1 + [n/k * T(insert)] + C2*n/k - C2 + (C3+C4)*k*x*2^x - C4*n/k
     *     ≈ 2*C1*n/k + [n/k * T(insert)] + C2*n/k + (C3+C4)*k*n/k*log2(n/k) - C4*n/k
     *     ≈ 2*C1*n/k + [n/k * [C'1*n + C'2*n + (C'3+C'4)/2*n^2 - (C'3+C'4)/2*n]] + C2*n/k + (C3+C4)*n*log2(n/k) - C4*n/k
     *
     *比较merge_sort与merge_insert_sort
     *令F(n) = T(merge_insert) - T(merge)
     *      = (1/k-1)*2*C1*n + (1/k-1)*C2*n - (1/k-1)*C4*n + (C3+C4)*n*[log2(n/k) - log2(n)] + [ n/k * [C'1*k + C'2*k + (C'3+C'4)/2*k^2 - (C'3+C'4)/2*k]] ]
     *      令C'1=C1, C'2=l*C2(0<l<1), C'3=C3, C'4=C4
     *      ≈ 1/k*2*C1*n - C1*n + 1/k*C2*n - (l+1)*C2*n - 1/k*C4*n + C4*n - (l*C3+C4)/2*n + (l*C3+C4)/2*k*n - (C3+C4)*n*log2(k)
     *      ≈ n * [ 2/k*C1 - C1 + 1/k*C2 - (l+1)*C2 - 1/k*C4 + C4 - (l*C3+C4)/2 + (l*C3+C4)/2*k - (C3+C4)*log2(k) ]
     * 求F(n) <= 0 ==> F(k) = 2/k*C1 - C1 + 1/k*C2 - (l+1)*C2 - 1/k*C4 + C4 - (l*C3+C4)/2 + (l*C3+C4)/2*k - (C3+C4)*log2(k) <= 0
     *                     =  (2*C1+C2-C4)/k - C1 - (l+1)*C2 + C4 - (l*C3+C4)/2 + (l*C3+C4)/2*k - (C3+C4)*log2(k)
     *                     = [2*(2*C1+C2-C4) - 2*C1*k - 2*(l+1)*C2*k + 2*C4*k - (l*C3+C4)*k + (l*C3+C4)*k^2 - 2*(C3+C4)*k*log2(k)] / 2*k
     *
     *             G(k) = 2*(2*C1+C2-C4) - 2*C1*k - 2*(l+1)*C2*k + 2*C4*k - (l*C3+C4)*k + (l*C3+C4)*k^2 - 2*(C3+C4)*k*log2(k)
     *             G'(k) = -2*C1 - 2*(l+1)*C2 + 2*C4 - (l*C3+C4) + 2*(l*C3+C4)*k - 2*(C3+C4)*(log2(k)+1/ln2)
     *             G''(k) = 2*(l*C3+C4) - 2*(C3+C4)/(ln2*k) = 0 ==> k = (C3+C4) / ln2*(l+C3+C4)
     *
     *             令C1=C2=C3=C4=C
     *             G(k) = 4*C - 2*C*k - 2*(l+1)*C*k + 2*C*k - (l+1)*C*k + (l+1)*C*k^2 - 4*C*k*log2(k)
     *             H(k) = 4 - 2*k - 2*(l+1)*k - (l+1)*k + (l+1)*k^2 - 4*k*log2(k) <= 0
     *             H'(k) = -2 - 2*(l+1) - (l+1) + 2*(l+1)*k - 4*(log2(k) + 1/ln2)
     *             H''(k) = 2*(l+1) - 4/(k*ln2)
     *             H''(k) <= 0 ==> k<=2/[(l+1)*ln2], 当k∈(0, 2/[(l+1)*ln2]), H'(k)单调递减, k∈(2/[(l+1)*ln2], +∞), H'(k)单调递增
     *             当k=2/[(l+1)*ln2]时，H'(k)的极小值 = -3l - 9 + 4log2(l+1) + 4log2(ln2)
     *                 G(l) = -3l - 9 + 4log2(l+1) + 4log2(ln2)
     *                 G'(l) = -3 + 4/[(l+1)*ln2]
     *                 G'(l)<=0 ==> l>=4/(3ln2) - 1≈2.08 , l∈(0,2.08)，G(l)单调递增，l∈(2.08, +∞)，G(l)单调递减
     *                 当l=4/(3ln2)时，G(l)的极大值<0
     *             H'(k)的极小值<0
     *                k=1时, H'(k) = -3 + 1/ln2 - l<=0 ==> l>=1/ln2 -3 成立
     *                k=2时, H'(k) = -5 - 4/ln2 + l<=0 ==> l<=5 + 4/ln2 成立
     *                k=3时, H'(k) = 1 + 3l - 4*log2(3) - 4/ln4 <=0 ==> l<=-0.43不成立
     *
     *            当k=1，2时，H'(k)<0, H(k)单调递减
     *            当k>2时，H'(k)>0, H(k)单调递增
     *            当k=1时，H(k) = -2*l <= 0 恒成立
     *            当k=2时，H(k) = -2(l+1) - 8 <= 0 恒成立
     *            当k=3时，H(k) = -2 - 12*log2(3) <=0 恒成立
     *            当k=4时，H(k) = 4*l - 32 <=0 恒成立
     *            当k=5时，H(k) = 10l + 4 - 20*log2(5) <=0 恒成立
     *            当k=6时，H(k) <=0 恒成立
     *            ...
     *            k=11时，H(k) = 88l+70 - 44*log2(11)<=0 ==> l<=0.9345 可成立
     *            ...
     *            k=23时，H(k) = 451*l + 409 - 92*log2(23) <=0 ==> l<=0.0561
     *            k=24时，H(k) = 540*l + 496 - 96*log2(24) <=0 恒不成立
     *
     */


    //思考题2-4
    //b. {1,2,3,...,n}的递减数列最多，共(n-1)*n/2个逆序对
    public static int merge_reverse_pair(int[] a) {
        if(a == null) return 0;
        if(a.length == 0 || a.length == 1) return 0;

        return merge_reverse_pair(a, 0, a.length);
    }
    public static int merge_reverse_pair(int[] a, int begin, int end) {

        int n = end-begin;
        if(n == 1) return 0;

        int left = merge_reverse_pair(a, begin, begin + n / 2);
        int right = merge_reverse_pair(a, begin + n/2, end);

        int re = merge_reverse(a, begin, begin + n / 2, end);

        return left + right + re;
    }
    public static int merge_reverse(int[] a, int p, int q, int r) {
        int[] left = new int[q-p+1];
        left[left.length-1] = Integer.MAX_VALUE;
        int[] right = new int[r-q+1];
        right[right.length-1] = Integer.MAX_VALUE;
        System.arraycopy(a, p, left, 0, left.length-1);
        System.arraycopy(a, q, right, 0, right.length-1);

        int re = 0;
        for(int i=0, j=0, l=p; l<r; l++) {
            if(left[i] > right[j]) {
                re += (q-p-i);
                a[l] = right[j];
                j++;
            } else {
                a[l] = left[i];
                i++;
            }
        }
        return re;
    }


}
