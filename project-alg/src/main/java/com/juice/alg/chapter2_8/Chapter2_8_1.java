package com.juice.alg.chapter2_8;

public class Chapter2_8_1 {

    /**
     *比较排序: 基于元素间的比较来确定元素的相对大小，如，插入排序，归并排序，堆排序，快速排序等
     *比较排序的决策树模型: 记录某一特定比较排序算法在给定输入下的所有元素的比较操作
     *4个元素的插入排序的决策树: 可见插入排序的最大比较次数为 n*(n-1)/2
     *                1:2
     *              </
     *             2:3
     *                \>
     *                 1:3
     *                </
     *               2:4
     *                  \>
     *                  3:4
     *                    \>
     *                    1:4
     *                     \>
     *                  4,1,3,2
     *
     *更优的比较排序有着更矮的决策树。
     *    设决策树最大高度记为 h，叶子节点个数为 n!，高度为h的二叉树的最大叶子节点数为 2^h
     *==> n! <= 2^h
     *==> h > n*lgn
     *
     *结论: 最优的比较排序也至少比较 n*lgn
     */

    //练习8.1-1: n
    //练习8.1-2: 积分求累加和


}
