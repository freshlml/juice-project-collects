package com.juice.alg.chapter2_7;

public class Chapter2_7_1 {

    //快速排序，最坏情况运行时间分析
    //有序: O(n);  逆序: O(n^2)
    /*假设每次partition产生9:1划分的递归树
                                    [n]
                    [9/10*n]                        [1/10*n]                 1, n/10               n/(10/9)
          [9^2/10^2*n]     [9/10*1/10*n]  [9/10*1/10*n]     [1^2/10^2*n]     2, n/10^2             n/(10/9)^2
            ...
                                                                     [1]     x, n/10^x
            ...
          [1]                                                                                   y, n/(10/9)^y
        n/10^x = 1      ==> x = log10(n)   <  log2(n)
        n/(10/9)^y = 1  ==> y = log10/9(n) <  Clog2(n); C >= 7

       假设每次partition产生(1-α):α的划分
       log(1/α)[n]，log(1/(1-α))[n]
       不妨令0<α<=1/2，则最坏情况运行时间 =  n*log(1/(1-α))[n]
                                     <= n*C*lgn ===> C >= -(1/lg(1-α)) >= 1 ，越不平衡的划分导致更大的系数

       partition按比例划分的最坏情况运行时间:
            O(n*lgn)

       练习7.2-6: 画图解法
       或  1-α-a = α+a ==> 2*a = 1-2α
     */

    //快速排序，平均情况运行时间分析
    /*输入序列假设:  n个数互异且均匀随机排列
      P(逆序) = 1/n!
      P(有序) = 1/n!
      P(任一位置取任一值) = 1/n

      随机变量X:  第一次partition返回的下标q
          取值:  0     1       2       ...     n-1
         P(X):
      P(X=i) = 1/n, 0<=i<=n-1
      EX = 0*1/n + 1*1/n + ... + (n-1)*1/n = (n-1)/2

      随机变量Y:  第二次partition返回的下标q
          取值:  0     1      2       ...   i    ...   n-1   ,i为第一次partition返回的下标q
         P(Y):
      P(Y=0) = ？？？
    难点:
      第一次partition根据输入序列的假设很好求，但第二次partition因为受第一次partition的影响而不能将输入序列当成均匀随机的，导致概率不好求
      并且当前partition总是受之前一个partition的影响，这样的概率怎么求呢，todo

    另辟它径  ->整体思维法，从算法角度抽象
        每次partition返回的下标，不参与后续的递归，即partition最多执行n次。每次partition总是循环当前序列，每次循环总是比较a[j] <= k
        因此，令X = 所有partition中比较操作的总次数，T = C*X

        输入序列假设: n个数互异且均匀随机排列

        因为partition返回的下标不参与后续的递归，所以序列中i与j(i<j)最多比较一次
        因此 X = Σ(i=0~n-2)Σ(j=i+1~n-1)Xij, Xij表示i与j的比较次数 = 0
                                                               = 1  P(Xij=1)
        EX = E[ Σ(i=0~n-2)Σ(j=i+1~n-1)Xij ] = Σ(i=0~n-2)Σ(j=i+1~n-1)EXij
                                             = Σ(i=0~n-2)Σ(j=i+1~n-1)P(Xij=1)

        P(Xij=1) = 2/(j-i+1)  , todo,在random_partition，与输入序列无关，因此可以使用特殊的序列分析问题？

        EX = Σ(i=0~n-2)Σ(j=i+1~n-1)*2/(j-i+1) ,令k=j-i
           = Σ(i=0~n-2)Σ(k=1~n-i-1)*2/(k+1)
           < Σ(i=0~n)Σ(k=1~n)*2/k
           < n*lgn
     */

    //快速排序，使用随机数创造均匀随机的输入序列, 不一定稳定
    /*
    int random_partition(int a[], int begin, int end) {
        int r = RANDOM(begin, end) //begin,end下标之间等概率的产生一个下标值
        exchange a[r],a[end-1]
        return partition(a, begin, end)
    }
     */


}
