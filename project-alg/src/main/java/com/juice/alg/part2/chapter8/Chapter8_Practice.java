package com.juice.alg.part2.chapter8;

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

}
