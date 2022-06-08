package com.juice.alg.chapter1_5;

public class Chapter1_5_4 {

    //思考题5-2

    //f
    /**
     *list[n]
     *j = 1
     *while j < n:
     *    if list[j] == key:
     *        break
     *    j++
     *
     *第一: 最坏情况运行时间
     *  = C*n ;C表示while，while中语句的时间之和
     *  = O(n)
     *
     *第二: 平均情况运行时间
     *  = C*EX ;C表示while，while中语句的时间之和
     *  = C*（n/k）
     *随机变量X:  结束时，j的值
     *      X:  1    2   ,...,   n-k+1
     *    P(X): P(1) P(2)        P(n-k+1)
     *输入序列的分布假设 {
     *     下标 1       2    ...    n
     *     值  lst[1]  lst[2]  ...  lst[n]   lst[1]是什么值与lst[2]是什么值没有关系
     *     A(n,n)种排列,如{
     *         lst[1]  lst[2]  ...  lst[n] 是一种排列
     *         lst[2]  lst[1]  ...  lst[n] 是一种排列
     *         ...
     *     }每一种排列等可能
     *     假设有k个值与key相等,1<=k<=n,这k个值等可能的出现在1~n下标
     *     P(对某一个下标i,其值等于key) = k/n
     *}
     * S(i): 随机变量X=i
     * A(i): 对所有的j<i,lst[j]!=key
     * B(i): 下标i的值lst[i]==key
     * S(i) = A(i) ∩ B(i)
     * P(S(i)) = P(A(i) ∩ B(i))   ,A(i) 与 B(i) 独立
     *         = P(A(i)) * P(B(i))
     *         = (1-k/n)^(i-1) * k/n
     *
     *试验:    依次检查这n个数
     *基本事件: (相等/不相等，相等/不相等，... ，相等/不相等) 共n个
     *样本空间: { (相等/不相等，相等/不相等，... ，相等/不相等) } 2^n个元素
     *随机变量X: 找到第一个相等的所需的比较次数 （和上述随机变量是一个意思）
     *  P(X=i) = (1-k/n)^(i-1) * k/n  X满足几何分布
     *
     *EX = n/k
     *
     */
    //g
    //EX = P(X=n) * n = 1 * n = n


    //c
    /**
     *list[n]
     *j = 1
     *while j < n:
     *    i = RANDOM(1, n)
     *    if list[i] == key:
     *        break
     *    if i第一次出现:
     *        j++
     *if list[i] == key:
     *    return i
     *return -1
     *
     *随机变量X:  结束时，挑选下标的数量(j的值)
     *      X:  1      2    ,,,    n-k+1
     *    P(X): P(1)  P(2)  ,,,   P(n-k+1)
     *输入序列的分布假设 {
     *     下标 1       2    ...    n
     *     值  lst[1]  lst[2]  ...  lst[n]
     *     n^m种输入序列的排列{
     *         lst[1]  lst[1]  ...  lst[1]
     *         lst[1]  lst[1]  ...  lst[2]
     *         ...
     *     }每一种排列等概率
     *     假设有k个值与key相等,1<=k<=n
     *}
     *todo
     *
     */




}
