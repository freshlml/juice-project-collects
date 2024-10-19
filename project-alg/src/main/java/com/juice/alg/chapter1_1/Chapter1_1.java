package com.juice.alg.chapter1_1;

public class Chapter1_1 {
    /**
     *第一: 子序列数量计算
     *   n个元素的集合(A1,A2,A3,...,An), 其子集个数: 2^n
     *   n个元素的序列<X1,X2,X3,...,Xn>, 其子序列（如: X1,X3）数量: 2^n = ΣC(i,n)[i=1->m]
     *
     *第二: n 个数，将其依次排列成序列的数量
     *   n * (n-1) * (n-2) * ... * 1 = n!
     *
     *排列数，组合数: @link 计数原理.xmind
     *
     *
     *练习题 1.1-4: 最短路径问题和旅行商问题有哪些相似、不同之处？
     *  最短路径问题只需计算两点之间的最短路径即是该问题的最优解
     *  旅行商问题: 每辆车需要投递货物到几个地址，每辆车的最短总距离是按其最优投递顺序投递货物所行驶的总距离
     *
     *
     *练习题 1.2-2:
     *  插入排序: C1*n*n, 取 8*n*n
     *  归并排序: C2*n*lgN, 取 64*n*lgN
     * 令 F(n) = 8*n*n - 64*n*lgN
     * F'(n) = 0  ==> 极值点，分析单调区间
     *
     *
     */



}
