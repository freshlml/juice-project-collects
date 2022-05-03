package com.project.alg.chapter1_5;

public class Chapter1_5_2 {

    /**
     *练习5.2-5: n个数1~n的均匀随机排列的输入序列,i<j&A[i]>A[j],则<i,j>是逆序对
     *
     *<i,j>对，共 n*(n-1)/2 对
     * 如 <0,1>,<0,2>...
     *   <1,2>,<1,3>...
     *   ...
     *
     *n个数1~n的均匀随机排列的输入序列 {
     *     <1,2,3,4,...,n> 是一种排列
     *     <2,1,3,4,...,n> 是一种排列
     *     ...
     *     共A(n,n) = n! 种排列，每种排列等概率
     *}
     *
     *随机变量X: 逆序对数量
     * X的取值: 0,   1,   2,    ...,  n*(n-1)/2
     * P(X)   P(0) p(1)  p(2)  ...
     * P(X)不好求
     *==>分解法
     * X(i,j) = 1 <i,j>是逆序对
     *        = 0 <i,j>不是逆序对
     *
     * X = ΣX(i,j) = X(0,1) + X(0,2) + ...
     * P(X(i,j)) = [(n-1)*(n-2)! + (n-2)*(n-2)! + ... + 1*(n-2)!] / n! = 1/2
     *
     *EX = n*(n-1)/4
     *
     *
     *试验:     取出所有的<i,j>对 (或者独立重复n*(n-1)/2次取出<i,j>对)
     *基本事件:  (是逆序对/不是，是逆序对/不是，...)   共n*(n-1)/2对
     *样本空间: {(是逆序对/不是，是逆序对/不是，...)}  共2^[n*(n-1)/2]个元素
     *随机变量X: 逆序对数量，link如上
     *
     */

}