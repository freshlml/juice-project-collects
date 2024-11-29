package com.juice.alg.part2.chapter7;

public class Chapter7 {

    /**
     *Chapter 7_2 and 7_3 and 7_4
     */
    public static void main(String[] argv) {
        chapter7_2();
        chapter7_3();
        chapter7_4();
    }
    /*
     *Chapter 7_2
     *
     *最坏情况:
     *  当 partition 每次划分都产生 0 个元素数组时，是其最坏情况。得到递归式:
     *  T(n) = T(n-1) + Θ(n)
     *
     *  [n]
     *  [n-1] [0]               1,   n-1,  c*n + Θ(1)
     *  [n-2] [0] ...           2,   n-2,  c*(n-1) + Θ(1)
     *  ...
     *  [1] [0] ...             x,   1,    c*2 + Θ(1)
     *
     *  T(n) = (c*n + c*(n-1) + ... + c*2) + x*Θ(1) + Θ(1)
     *       = c*(n + n-1 + ... + 2) + n*Θ(1)
     *       = Θ(n^2)                                               //练习 7.2-1
     *
     *
     *假设 partition 总是产生 9:1 的划分，此时的运行时间:
     *  T(n) = T(9n/10) + T(1n/10) + Θ(n)
     *
     *  [n]
     *  [n/(10/9)]    [n/10]                               1, n/10           c*n
     *  [n/(10/9)^2] [n/(10^2/9)]   [n/(10^2/9)] [n/10^2]  2, n/10^2         c*(n-1)  < c*n
     *  ...
     *  [n/(10/9)^x]                             [1]       x, n/10^x         c*(n-x+1)  < c*n
     *  ...
     *  [1] ...                                            y, n/(10/9)^y     c*(n-y+1)  < c*n
     *
     *  n/10^x = 1, x = log10(n) < log2(n)
     *  n/(10/9)^y = 1, y = log10/9(n)  <  C*log2(n); C >= 7
     *
     *  解递归式得 T(n) = O(n*lg(n))
     */
    static void chapter7_2() {}
    /*
     *Chapter 7_3
     *
     *  static int randomized_partition(int[] a, int begin, int end) {
     *      int r = RANDOM(begin, end) //[begin, end) 之间等概率的产生一个下标值
     *      exchange a[r], a[end-1]
     *      return partition(a, begin, end)
     *  }
     */
    static void chapter7_3() {}
    /*
     *Chapter 7_4
     *
     *平均情况: 假设 n 个数的输入序列互异且均匀随机排列
     *
     *  第一层划分，partition 方法中循环的总执行次数为 n
     *  第二层划分，partition 方法中循环的总执行次数为 n-1
     *  ...
     *  随机变量 X: 所需要的划分层数
     *  X    lg(n)  lg(n) + 1  ...  n
     *  P
     *  概率不好求...
     *-----
     *  随机变量 X: 所有 partition 方法调用中循环的循环次数的总和        //每次 partition 方法中循环的循环次数是一个常量，partition 方法的调用次数是一个变量
     *
     *  partition 方法中循环的循环次数等同于 `a[k] <= e` 这个比较操作的次数  ①
     *
     *  随机变量 Y: 所有 partition 方法调用中比较操作的次数            //根据 ① 可得 Y == X
     *
     *  因为 partition 返回的下标不参与后续的递归，所以序列中 i 与 j 最多比较一次  ②
     *
     *  Y = Σ(i=0~n-2)Σ(j=i+1~n-1) Y<i,j>,  其中 Y<i,j> 表示 i 与 j 的比较次数 (i < j) = 0  //根据 ②，Y<i,j> 只能取 0 或者 1
     *                                                                              = 1
     *  P(Y<i,j> = 1) = 2/(j-i+1)             //Y<i,j> = 1: <i, ..., j> 被划分在同一个分区，并且 i 或 j 是 <i, ..., j> 中选出的第一个主元
     *
     *  EY = E[ Σ(i=0~n-2)Σ(j=i+1~n-1) Y<i,j> ]
     *     = Σ(i=0~n-2)Σ(j=i+1~n-1) EY<i,j>
     *     = Σ(i=0~n-2)Σ(j=i+1~n-1) [2/(j-i+1)]  ,令 k=j-i
     *     = Σ(i=0~n-2)Σ(k=1~n-i-1) [2/(k+1)]
     *
     *     Ⅰ：EY < Σ(i=0~n)Σ(k=1~n) 2/k
     *           < O(n*lgn)
     *     Ⅱ：EY >                                            //练习7.4-4, todo
     *  得, 平均情况下的运行时间: Θ(n*lgn)
     *
     *注1: 为什么 Y 可分解成 Σ(i=0~n-2)Σ(j=i+1~n-1)？
     *     这是由整个算法的运行过程中所有 partition 的行为决定的
     *注2: 为什么 Y 分解成 Σ(i=0~n-2)Σ(j=i+1~n-1), 而不是 Σ(i=0~n-1)Σ(j=0~n-1)？
     *     根据 ② 可得
     */
    static void chapter7_4() {}

}
