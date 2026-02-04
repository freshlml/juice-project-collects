package com.fresh.juice.alg.part2.chapter8;


import com.fresh.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;
import com.fresh.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;

public class Chapter8_Practice {

    //思考题8-2
    //  a: 计数排序
    //  b: 左指针指向 1，右指针指向 0，exchange
    //  c: 插入排序
    //  d: 需稳定且 O(n), 则上述可选 a
    //  e: O(n+k), 原址的计数排序, 不稳定
    public static void counting_sort_exchange(int[] a, int min, int max) { //数组中元素的取值范围为 [min, max)
        if(a == null) return;
        if(a.length == 0 || a.length == 1) return ;

        //assert min < max

        int[][] c = new int[2][max - min];  //初始值为 0

        for(int i=0; i<a.length; i++) {
            int idx = indexPos(min, a[i]);
            c[0][idx] = c[0][idx] + 1;
            c[1][idx] = c[1][idx] + 1;
        }

        for(int i=1; i<c[0].length; i++) {
            c[0][i] = c[0][i] + c[0][i-1];
            c[1][i] = c[1][i] + c[1][i-1];
        }

        exchange(a, c, min);
    }
    static void exchange(int[] a, int[][] c, int min) {
        /*
         *   2, 1, 2, 3, 2, 1, 1, 3         3, 6, 8  y
         *i= 0, 1, 2, 3, 4, 5, 6, 7         3, 6, 8  x
         */
        int n = a.length;

        for(int i=n-1; i>=0; ) {
            int idx = indexPos(min, a[i]);
            int x = c[1][idx] - 1;
            int y = c[0][idx] - 1;

            if(i == x) {
                c[1][idx] = x;
                i--;
            } else if(i > x && i <= y) {
                i--;
            } else { //i < x (×) or i > y
                int ex = a[x];
                a[x] = a[i];
                a[i] = ex;

                c[1][idx] = x;
            }
        }
    }
    static int indexPos(int min, int ai) {
        return ai - min;
    }

    public static void main(String[] argv) {
        int[] a = new int[] {-100, 50, 1, 79, -9, -23, 6, -23, -23, 7, -100};
        IntArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);

        counting_sort_exchange(a, -100, 80);
        IntArrayTraversal.of(a).forEach(ArrayPrinter.of()::print);
    }

    //思考题8-3
    //  a: Chapter8_3/radix_sort(int[] a, int d, int r)
    //  b: 1). 使用二维数组存储。按低位到高位。空位的值为字符的最小码点值
    //       a a       -->  aa
    //       b a a     --> aab
    //       b b       -->  bb
    //     2). 使用一维数组存储。每次截取若干位进行比较
    //       aa, aab, bb


    //思考题8-4
    //a:
    /*
        for i from 1 to n on A:
            for j from i to n on B:
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break
     最坏情况比较次数：n + n-1 + n-2 + ... + 1 = n*(n+1) / 2
    */
    //b:
    /*
     *红色、蓝色各 4 瓶水壶的决策树
     *               [1:1]
     *              =/
     *            [2:2]
     *          =/        \!=
     *        [3:3]        [2:3]
     *      =/    \!=     =/      \!=
     *     [4:4]  [3:4]   [3:2]     [2:4]
     *     /      /      =/   \!=     /
     *   1-1    [4:3]  [4:4]  [3:4] [3:2]
     *   2-2     /     /     /      =/  \!=
     *   3-3    1-1   1-1   [4:2]  [4:3] [3:3]
     *   4-4    2-2   2-3   /       /     /
     *          3-4   3-2  1-1    1-1    [4:2]
     *          4-3   4-4  2-3    2-4    /
     *                     3-4    3-2   1-1
     *                     4-2    4-3   2-4
     *                                  3-3
     *                                  4-2
     *
     *   1. 决策树的每一个内部节点表示两个水壶进行比较
     *   2. 决策树模型可以表示在给定输入规模下，某一特定算法对所有水壶的配对操作
     *   3. 算法的执行对应一条从树的根节点到叶节点的简单路径
     *   4. 任何正确的算法，其决策树的所有叶节点必定包含所有配对方式(n 个水壶共 n! 种配对方式)
     *
     *设决策树的最大高度为 h，叶子节点数量为 l，输入序列有 n 个元素。有:
     *  l >= n!   ①
     *  l <= 2^h  ②         //高度为 h 的二叉树，叶节点数量不多于 2^h
     *
     *  根据 ①、② 可得: n! <= l <= 2^h
     *  解得 h >= lg(n!) = Ω(n*lg(n))              //lg(n!) = Θ(n*lg(n))
     *  即决策树的最大高度 h > Ω(n*lg(n))
     */
    //c:
    /*
    1. B 序列作均匀随机选择
       for i from 1 to n on A:
            for j from i to n on B:
                r = RANDOM(j, n)
                swap B[j], B[r]
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break
        随机变量 X：完成配对的总比较次数; Xi: 找到与 A[i] 相等的 B[j] 所花费的比较次数
        X = X1 + X2 + X3 + ... + Xn

        X1  1       2       3    ...    n-1        n
        P   1/n     1/n     1/n  ...    1/n        1/n         // 首轮 B 序列 n 个数均匀随机

        X2  1       2       3       ... n-1
        P   1/(n-1) 1/(n-1) 1/(n-1) ... 1/(n-1)                // 次轮 B 序列剩余 n-1 个数均匀随机

        EX = n*(n+3)/4

     2. A 序列作均匀随机选择，同时 B 序列作均匀随机选择
       for i from 1 to n on A:
            q = RANDOM(i, n)
            swap A[i], B[q]
            for j from i to n on B:
                r = RANDOM(j, n)
                swap B[j], B[r]
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break

        随机变量 X：完成配对的总比较次数
        X = Σ(i=1~n)Σ(j=i~n) Xij; Xij: A[i], B[j] 的比较次数  = 0
                                                             = 1
        P(Xij = 1) = 1/(n-j+1)

        EX = Σ(i=1~n)Σ(j=i~n)EXij
           = Σ(i=1~n)Σ(j=i~n) 1/(n-j+1)  令 k=n-j; k∈[0, n-i]
           = Σ(i=1~n)Σ(k=0~n-i)1/(k+1)

        Ⅰ：EX < Σ(i=1~n)Σ(k=1~n) 1/k
              < O(n*lgn)
     */


    //思考题8-5
    //  A[i] + A[i+1] + ... + A[i+k-1] <= A[i+1] + ... + A[i+k-1] + A[i+k]
    //  ===> A[i] <= A[i+k]
    //
    //  a: k = 1: A[i] <= A[i+1], 完全有序的
    //  b: k = 2: A[i] <= A[i+2]
    //     1   3
    //     2   4
    //     5   6
    //     7   8
    //     9  10
    //  c: A[i] <= A[i+k]; i=1, 2, ..., n-k
    /*d:
                     1   2   3   ...   k
            1       | | | | | |  ...  | |
        1 + k       | | | | | |  ...  | |
        1 + 2k      | | | | | |  ...  | |
        ...                    ...
        1 + (i-1)k  | | | | | |  ...  | |
        1 + i*k     | | | | | |  ...  | |

        (i + 1)*k = n ==> i + 1 = n/k
        T(n) = k * [(i+1)*lg(i+1)] = n*lg(n/k)
     */
    //e: k 个有序链表合并, O(n*lgk)                                  //@link Chapter6_Practice1/练习6.5-9
    //f: T(n) = n*lg(n/k) + n*lgk = n*lgn


    //思考题8-7 todo


}
