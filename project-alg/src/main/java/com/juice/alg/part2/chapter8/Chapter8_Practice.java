package com.juice.alg.part2.chapter8;


public class Chapter8_Practice {

    //思考题8-2
    //  a: 计数排序
    //  b: 左指针指向 1，右指针指向 0，exchange. or 计算 p = number of 0, <=p 填充0，>p 填充 1
    //  e: O(n+k), 原址的计数排序, 不稳定
    static void counting_sort_merge(int[] a, int[][] c, int begin) {
    /*
     *   2, 1, 2, 3, 2, 1, 1, 3         3, 6, 8 y
     *i= 0, 1, 2, 3, 4, 5, 6, 7         3, 6, 8 x
     */
        int n = a.length;

        for(int i=n-1; i>=0; ) {
            int idx = indexPos(a[i], begin);
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
    static int indexPos(int ai, int begin) {
        return ai - begin;
    }


    //思考题8-3
    //  a: Chapter8_3/radix_sort(int[] a, int d, int r)
    //  b: 1). 使用二维数组存储。按低位到高位。空位的值为字符的最小码点值
    //       a a       --> a
    //       b a a     --> aab
    //       b b       --> b
    //     2). 使用一维数组存储。每次截取若干位进行比较
    //       aa, aab, bb


    //思考题8-4
    //a:
    /*
        for i from 1 to n on A:
            for j from i to n on B:
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break;
     最坏情况运行时间：n + n-1 + n-2 + ... + 1 = n*(n+1) / 2
    */
    //b: 决策树

    //c:
    /*
    1.B序列均匀随机
       for i from 1 to n on A:
            for j from i to n on B:
                r = RANDOM(j, n)
                swap B[j], B[r]
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break;
        随机变量 X：完成配对的总比较次数; Xi: 找到与 A[i] 相等的 B[j] 所花费的比较次数
        X = X1 + X2 + X3 + ... + Xn

        X1   1   2   3   4 ...   n
        P   1/n 1/n 1/n 1/n ... 1/n

        X2   1   2   3   4 ...   n-1
        P   1/n 1/n 1/n 1/n ... 1/n

        EX = n*(n+3)/4

     2.A 序列均匀随机，同时 B 序列均匀随机
       for i from 1 to n on A:
            q = RANDOM(i, n);
            swap A[i], B[q]
            for j from i to n on B:
                r = RANDOM(j, n)
                swap B[j], B[r]
                if A[i] == B[j]:
                    swap B[i], B[j]
                    break;

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
    //  a: A[i] <= A[i+1], 完全有序的
    //  b: A[i] <= A[i+2]
    //     1   3
    //     2   4
    //     5   6
    //     7   8
    //     9  10
    //  c: A[i] <= A[i+k]; i=1,2, ... ,n-k
    /*d:
                   1   2   3   ...   k
        0*k+1     | | | | | |  ...  | |
        1*k+1     | | | | | |  ...  | |
        2*k+1     | | | | | |  ...  | |
        ...                    ...
        (i-1)*k+1 | | | | | |  ...  | |
        i*k+1     | | | | | |  ...  | |

        i = n/k; j = n%k
        T(n) = k*i*lgi = n*lg(n/k)
     */
    //e: k个有序链表合并, O(n*lgk)


    //思考题8-7 todo


}
