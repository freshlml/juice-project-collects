package com.juice.alg.chapter1_5;

public class Chapter1_5_2 {

    /**
     *练习5.2-5: n个数1~n的均匀随机排列的输入序列,i<j&A[i]>A[j],则<i,j>是逆序对,求逆序对的数量
     *
     *随机变量X: 逆序对数量
     * X的取值: 0,   1,   2,    ...,  n*(n-1)/2
     * P(X)   P(0) p(1)  p(2)  ...
     *
     *试验:    依次取出<i,j>对，其中i<j
     *基本事件: (是逆序对/不是，是逆序对/不是，... ，是逆序对/不是) 共n*(n-1)/2个
     *样本空间: { (是逆序对/不是，是逆序对/不是，... ，是逆序对/不是) }  2^[n*(n-1)/2]个元素
     *
     *如何求P(X)
     *试验中基本事件概率: 1.任何一次取出<i,j>对是逆序对的概率都相等 = 1/2  此建立在输入序列的分布假设上
     *                2.基本事件的概率都相等 = 1/2^[n*(n-1)/2]
     *P(k) = C(k,n*(n-1)/2) * (1/2)^k * (1/2)^[n*(n-1)/2-k]
     *
     *输入序列的分布假设 {
     *     均匀随机排列
     *     <1,2,3,4,...,n> 是一种排列
     *     <2,1,3,4,...,n> 是一种排列
     *     ...
     *     共A(n,n) = n! 种排列，每种排列等概率
     *     1.对任意<i,j>i<j对，是逆序对的概率 = [(n-1)*(n-2)! + (n-2)*(n-2)! + ... + 1*(n-2)!] / n! = 1/2
     *}
     *依次计算EX，太复杂，使用分解法
     *
     *X(i,j) = 1 <i,j>是逆序对    P=1/2
     *       = 0 <i,j>不是逆序对  P=1/2
     *
     *X = ΣX(i,j) = X(0,1) + X(0,2) + ...
     *
     *EX = ΣEX(i,j) = n*(n-1)/4
     *
     */

}
