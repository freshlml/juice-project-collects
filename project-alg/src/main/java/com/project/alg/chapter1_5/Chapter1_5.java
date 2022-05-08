package com.project.alg.chapter1_5;

public class Chapter1_5 {

    /**
     *总结
     *  1.概率分析: 依题意求解概率
     *  2.某些问题可抽象出一个合适的随机变量，则使用随机变量，必要时给出其试验
     *  3.要求解的量受输入序列的影响，即不同的输入序列导致不同的结果时，应该进行概率分析
     *  4.输入序列的分布决定事件的概率，输入序列已经满足什么样的分布或者合理假设输入序列的分布(如均匀随机的排列)
     *  5.随机算法通常可以给我们构造一个随机的输入序列，该输入序列满足输入序列的分布
     */

    /**
     *雇用问题(找最值)
     *  依次n个应聘者，如果发现当前面试者更合适，就雇用当前的，直到面完第n个
     *
     *list[n]
     *person = lowest;
     *for i from 1 to n:
     *   if(decide(list[i], person)):  # 如果当前面试者更合适
     *      person = list[i]  # 辞掉person, 雇用当前面试者，雇用一个面试者花费费用记为C
     *
     *第一: 运行时间分析(时间复杂度)的角度
     *  = C1*n + C2*m ;C1为for,if语句的时间之和, C2为雇用面试者语句的时间
     *  = O(n)  ,m<=n
     *
     *第二: 面试n个人花费的总费用的问题，每雇用一个面试者需花费C的费用
     *  面试n个人，可能第一个就是最佳的，将最多雇用一次，花费1*C
     *  面试n个人，可能第二个才是最佳的，将最多雇用两次，花费2*C
     *  面试n个人，可能第n个才是最佳的，将最多雇用n次，花费n*C
     *===>输入序列的不同导致不同的总费用，像这类输入序列导致不同结果的问题，应该进行概率分析
     * 随机变量X: 雇用的人数
     * X的取值: 1,   2,    3,   ...,  n
     *  P(X): P(1) p(2)  p(3)       P(n)
     *
     * 总费用: C*X
     * 总费用期望: C*EX
     *
     * 试验:    依次独立重复的面试n个人
     * 基本事件: (雇，雇/不雇，... ，雇/不雇)，共n个人
     * 样本空间: { (雇，雇/不雇，... ，雇/不雇) }, 共2^(n-1)个元素
     *
     *如何求P(X)
     * 基本事件概率: 1. P(第i个人被雇用的概率) = 1/i 这建立在输入序列的分布假设上，可见面试每一个人被雇佣的概率都不一样
     *            2. P(基本事件) 也很不一样
     *  则   P(X=1) = (n-1)! / n! = 1/n
     *      P(X=2) = (n-1)! + (n-1)!/2 + (n-1)!/3 +...+ (n-1)!/(n-1)
     *      P(X=3) 不好求 ...
     *
     *输入序列的分布假设 { 输入序列的分布决定事件的概率
     *     均匀随机的排列
     * 下标 1,       2, ..., n
     * 排名 rank(1) rank(2)  rank(n)  rank(i)表示第i位应聘者的排名
     *     <rank(1), rank(2), ..., rank(n)>是其中一种排列方式
     *     <rank(2), rank(1), ..., rank(n)>也是
     *     这样的排列有A(n,n) = n!, 每一种排列方式等概率
     *}
     *当P(X)不好求时，作如下分解
     *    X = X1 + X2 +...+ Xi +...+ Xn, Xi表示第i个人是否被雇用 = 1， 被雇佣
     *                                                         0，不被雇用
     *       P(Xi) = 1/i
     *
     *    EX = EX1 + EX2 +...+ EXi +...+ EXn
     *       = 1/1 + 1/2 +...+ 1/i +...+ 1/n
     *
     *    令f(x) = 1/x, f(x)递减
     *      上界: ∫(1,n) f(x)dx = ∫(1,n) 1/xdx = ln(n)
     *      下界: ∫(1,n+1) f(x)dx = ∫(1,n+1) 1/xdx = ln(n+1)
     *
     */

    /**
     *随机算法
     * RANDOM(1,n): 等可能(1/n概率)的随机的产生数字1 ~ n
     *    将RANDOM(1, n)调用n次
     *            i1 i2 i3 ... in     1/n*1/n*...*1/n= 1/n^n 概率  @link Chapter1_5-1
     *
     *    将RANDOM(1, n)调用n次，且i不重复
     *            i1 i2 i3 ... in     1/n * [(1/n) / (n-1)/n] * [(1/n) / (n-2)/n] *... = 1/n!
     *
     *
     *为雇用问题创造随机输入序列: 使用随机数生成器产生n个数的随机序列，且该序列满足上述输入序列的分布假设
     *rd[n] = F[RANDOM(1, n)]... 不重复的产生n个数的随机序列
     *list[n]
     *person = lowest;
     *for i from 1 to n:
     *    r = rd[i]
     *    if(decide(list[r], person)):  # 如果当前面试者更合适
     *       person = list[r]  # 辞掉person, 雇用当前面试者，雇用一个面试者花费费用记为C
     *
     *
     *person = lowest;
     *list[n]
     *for i from 1 to n:
     *    r = RANDOM(i, n)
     *    swap list[i], list[r]  ##秀
     *
     *
     *
     */


}
