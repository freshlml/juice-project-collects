package com.juice.alg.part2.chapter8;

public class Chapter8_1 {

    /**
     *比较排序: 基于元素间的比较来确定元素相对大小的排序算法统称为"比较排序"。如，插入排序、归并排序、堆排序、快速排序等。
     *
     *决策树模型
     *        四个元素的插入排序决策树                   四个元素的归并排序决策树                   四个元素的快速排序决策树
     *               [1:2]                                  [1:2]                                [4:1]
     *           <= /                                   <= /                                   <=/
     *            [2:3]                                   [3:4]                                 [4:2]
     *       <= /       \>                            <= /                                   <=/
     *        [3:4]      [1:3]                           [1:3]                                 [4:3]
     *    <= /    \>     <=/                       <= /         \>                          <=/
     *      1     [2:4]   [2:4]                     [2:3]           [1:4]                    [3:2]
     *      2  <=/    \>     \>                  <= /   \>        <=/  \>                <=/      \>
     *      3   1    [1:4]    [3:4]                 1   [2:4]    [2:4]  3                 [3:1]     [3:1]
     *      4   2   <=/ \>        \>                2  <=/  \> <=/  \   4              <=/     \> <=/   \>
     *          4    1  4         [1:4]             3   1   1   3   3   1               [2:1]  4   4    [1:2]
     *          3    4  1            \>             4   3   3   1   1   2            <=/   \>  1   2  <=/   \>
     *               2  2            1                  2   4   2   4                 1    2   3   3   4    4
     *               3  3            4                  4   2   4   2                 2    1   2   1   1    2
     *                               3                                                3    3           2    1
     *                               2                                                4    4           3    3
     *
     *    1. 决策树的每一个内部节点表示两个元素进行比较，左"边"表示 <=，右"边"表示 >。不同排序算法，谁与谁进行比较，什么时间进行比较是不同的。
     *    2. 决策树模型可以表示在给定输入规模下，某一特定比较排序算法对所有元素的比较操作
     *    3. 排序算法的执行对应一条从树的根节点到叶节点的简单路径
     *    4. 任何正确的排序算法，其决策树的所有叶节点必定包含输入序列的所有排列(n 个元素的输入序列有 n! 种排列)
     *
     *Ⅰ: "比较排序"算法的上界
     *  根据决策树模型，对于 n 个元素的序列，最多需要比较 n(n-1)/2 次即可确定整个序列的顺序
     *
     *Ⅱ: "比较排序"算法的下界
     *  设决策树的最大高度为 h，叶子节点数量为 l，输入序列有 n 个元素。有:
     *  l >= n!   ①
     *  l <= 2^h  ②         //高度为 h 的二叉树，叶节点数量不多于 2^h
     *
     *  根据 ①、② 可得: n! <= l <= 2^h
     *  解得 h >= lg(n!) = Ω(n*lg(n))              //lg(n!) = Θ(n*lg(n))
     *
     *故，比较排序的的运行时间不可能低于 Ω(n*lg(n))
     */
    public static void main(String[] argv) {

    }

    //练习8.1-1: n

    //练习8.1-2
    //  lg(n!) = lg1 + lg2 + ... + lgn = Σ(k=1~n)lgk
    //  得 ∫(0, n) lgk dx  <= Σ(k=1~n)lgk <= ∫(1, n+1) lgk dx    //(x*lgx - x/ln2)' = lgx
    //  得 n*lgn - n/ln2 <= Σ(k=1~n)lgk <= (n+1)lg(n+1) - n/ln2

    //练习8.1-3
    //  设 h = k*n, k 为常量
    //  令 2^h > n!/2 ==> k > lgn，与 k 是常量矛盾


}
