package com.juice.alg.chapter3_12;

public class Chapter3_12_4 {

    /**
     *二叉搜索树的高度h(n个数的序列，依次调用{@link com.juice.alg.chapter3_10.Chapter3_10_4.BinaryTree#add(int)}形成的树的高度)
     * 最坏情况
     *    h = n-1
     *    如有序的序列
     *
     * 平均情况
     *   假设: 输入序列是"均匀随机"的
     *   1.定义
     *     输入序列排列   有序   排列1     排列2            ...        逆序
     *     树高h         n-1   xx       xx              ...        n-1
     *     概率         1/n!   1/n!     1/n!            ...        1/n!
     *     树高h的加权平均数 = (n-1) * 1/n! + xx * 1/n! + ...
     *     将每一种列出来，求解通常比较繁琐
     *
     *   2.从概率角度求
     *     定义随机变量h表示树高
     *     h    n-1     n-2    ...   lgn
     *     p
     *     p不太好求
     *
     *   3.todo
     *    E[h] = O(lgn)
     *    练习，todo
     * 期望
     *   使用随机数生成器创造"均匀随机"序列，从而期望树高h = O(lgn)
     *   random_add(int[] es):
     *      for(int i=0; i<es.length; i++):
     *          j = RANDOM(i, es.length);
     *          exchange i,j;
     *          add(es[i]);
     *
     *
     */


}
