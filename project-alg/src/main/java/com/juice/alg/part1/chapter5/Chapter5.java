package com.juice.alg.part1.chapter5;

public class Chapter5 {
    /**
     *算法运行时间分析
     *   特定操作的执行次数 * 每次执行的时间，得到该操作的总运行时间
     *   影响执行次数的因素: 1. 输入规模 n
     *                   2. 输入序列的排列。注意: 输入序列的取值并不算成影响因素
     *   概率论相关知识: part8/appendix.md
     *
     *一. 最坏情况运行时间
     *   算法在最坏情况下的运行时间（算法在特定排列的输入序列下的最长运行时间）
     *
     *   如，插入排序中，逆序的输入序列，里层循环最坏情况的比较次数总是达到最多
     *   如，快速排序中，逆序的输入序列，每次 partition 返回的 q 位置总是只能并掉一个元素(而不是最好情况的一半元素)
     *
     *   意义: 最坏情况运行时间是一个保底时间，一旦正确给出，就不会有比它更长的运行时间
     *   考虑点: 最坏情况可能经常出现，这取决于输入序列是否更加倾向于选择使算法达到最坏情况的排列
     *
     *二. 平均情况运行时间
     *   算法在输入序列的不同排列下，具有不同的运行时间，平均情况运行时间为算法在输入序列的所有可能排列下运行时间的加权平均数
     *
     *   输入序列的排列  排列1   排列2   ...   排列i   ...
     *           概率   P1     P2    ...    Pi    ...
     *        运行时间   T1     T2    ...    Ti    ...
     *   平均情况运行时间 = P1*T1 + P2*T2 + ... + Pi*Ti + ...
     *
     *   平均情况，是对算法进行 "概率分析": 特定操作的执行次数随输入序列排列的不同而不同，是一个变化的量(简称变量)，将执行次数表示成 "随机变量"
     *   如，插入排序中，输入序列的不同排列，致里层循环的比较次数不一样
     *     ①: 若每次取最大比较次数，则可得出最坏情况的比较次数
     *     ②: 而平均情况，则是将比较次数表示成 "随机变量" X，求 X 的数学期望 EX (EX 即"随机变量X"的加权平均数，权为其概率)，用于代表平均情况下的比较次数
     *
     *   在实际情况中，输入序列的所有可能排列和其概率通常是不知道的，则需要做出假设。如，假设输入序列是 "均匀随机" 的: 输入序列取全排列中任意一种排列的概率都相等
     *
     *   意义: 小的平均情况运行时间意味着出现 "更大运行时间" 的排列的概率更小
     *   考虑点: 通常需要采用"随机算法"将序列打乱，从而使得输入序列满足假设的分布，以此来得到更小的平均情况运行时间
     *
     *三. 期望运行时间
     *   通过随机数生成器将输入序列打乱，使打乱后的序列满足一定的分布假设而计算出来的平均情况运行时间，叫做期望运行时间。
     *
     *   对于一个算法，最坏情况运行时间通常是固定的，但可以优化平均情况运行时间。
     *   可以假设输入序列满足特定的分布，并且在该分布下有比较小的平均情况运行时间。
     *   然后使用随机数生成器创造满足该分布的输入序列，就可以得到比较小的期望运行时间(等同于期望的比较小的平均情况运行时间)。
     *
     */
    /*
     *变量与随机变量
     *  变量，即变化的量，可取多个值。
     *  平均值: (1 + 2)/2, 从随机变量的数学期望角度看: 1 * 1/2 + 2 * 1/2
     *  随机变量，变量取某个值及其概率是多少
     */
    public static void main(String[] argv) {
        sl();
        chapter5_1And5_2And5_3();
    }

    /**
     *插入排序运行时间分析
     *
     *一. 最坏情况运行时间
     *   当每一轮里层循环的比较次数均取最大值时，是其最坏情况:
     *   里层循环最坏情况的总比较次数 = 1 + 2 + ... + n-1 = (n-1)*n/2
     *
     *二. 平均情况运行时间
     *   输入序列的假设: 均匀随机
     *
     *   Ⅰ: 求在输入序列所有可能排列下运行时间的加权平均数
     *      排列       有序    排列2     排列3    ...    逆序
     *      运行时间    n-1                            (n-1)*n/2
     *      概率       1/n!                           1/n!
     *
     *      n 个数的全排共 n! 种，将每一种列出来，然后求解通常比较繁琐
     *
     *    Ⅱ: 使用随机变量 X 表示里层循环的总比较次数
     *       X = X1 + X2 + ... + X(n-1), 其中随机变量 Xi 表示: 第 i 轮里层循环的比较次数
     *
     *       Xi   1          2      ...      i
     *       P   P(Xi=1)   P(Xi=2)  ...    P(Xi = i)
     *
     *       里层循环平均情况的总比较次数 = EX
     */
    /*
     *从"随机变量"的定义出发，论证如上过程中为什么可以使用随机变量的数学期望来代表平均情况下的执行次数:
     *  插入排序的外层循环 n-1 次是 n-1 次重复试验
     *  记里层循环的比较次数为基本事件: (1, 1/2, ..., 1/2/.../i, ...), n 元组
     *  样本空间: { (1, 1/2, ..., 1/2/.../i, ...) }  共 (n-1)! 个元素
     *
     *  随机变量 X，表示里层循环的总比较次数。基本事件-X的映射关系为 n 元组中每个元素求和
     */
    static void sl() {}

    /**Chapter 5.1 and 5.2 and 5.3
     *
     *雇用问题(找最值)
     *  依次面试 n 个应聘者，如果发现当前面试者更合适，就雇用当前的，直到面完第 n 个
     *
     *list[n]
     *person = list[0]
     *for i from 1 to n:
     *   if(decide(list[i], person)):        # 面试 list[i], 面试费用记为 Di
     *      person = list[i]                 # 辞退 person, 辞退费用记为 A(i-1). 雇用 list[i], 雇用费用记为 Ci
     *
     *面试总费用: n * D
     *雇佣总费用: m * C, 假设共雇用 m 个人
     *  以三个人为例子，他们的编号分别是 1 2 3，他们的排名是全排列中的任意一种 1 2 3； 1 3 2； 2 1 3； 2 3 1； 3 1 2； 3 2 1
     *  eg 对于 3 2 1 这样的序列，需要雇用 1 次，花费 1*C
     *     对于 1 2 3 这样的序列，需要雇用 n 次，花费 n*C
     *
     *第一: 最坏情况
     *  最坏情况下，雇佣人数为 n。雇佣总花费: n*C
     *
     *第二: 平均情况(以3个人为例)
     *  输入序列假设: 均匀随机
     *
     *  1).求在输入序列所有可能排列下运行时间的加权平均数(以3个人为例)
     *     排列     1 2 3； 1 3 2； 2 1 3； 2 3 1； 3 1 2； 3 2 1
     *     雇佣次数  3      2       2       2      1       1
     *     概率     1/3!   1/3!    1/3!    1/3!   1/3!    1/3!
     *
     *     加权平均数 = 1/3! * 3 + 1/3! * 2 + 1/3! * 2 + 1/3! * 2 + 1/3! * 1 + 1/3! * 1
     *              = 1/3! * (3 + 2 + 2 + 2 + 1 + 1)
     *              = 11/6
     *
     *  2).使用随机变量 X 表示雇用的次数(以3个人为例)
     *     X   1      2      3
     *     P  2!/3!  3/3!   1/3!
     *
     *     平均情况下，雇佣人数为 EX = 1/3! * 3 + (1/2) * 2 + 1/3 * 1
     *                          = 1/3! * 3 + (1/3! * 2 + 1/3! * 2 + 1/3! * 2) + (1/3! * 1 + 1/3! * 1)
     *                          = 11/6
     *     平均情况下，雇佣总花费: EX * C = 11C/6
     *
     *第三: 平均情况
     *  输入序列假设: 均匀随机
     *    rank(1), rank(2), ..., rank(n).    rank(i) 表示第 i 位应聘者的排名
     *    <rank(1), rank(2), ..., rank(n)> 是其中一种排列方式，<rank(2), rank(1), ..., rank(n)> 也是
     *    这样的排列有 A(n,n) = n! 种, 每一种排列方式等概率
     *
     *  随机变量X: 雇用的人数 (雇佣次数)
     *  X      1       2       3     ...     n
     *  P    P(X=1)  P(X=2)  P(X=3)        P(X=n)
     *
     *  X=1 表示雇佣一次的事件: 第 1 位是最优秀的，剩余 n-1 为任意排列。P(X=1) = (n-1)! / n! = 1/n                                         //练习5.2-1
     *  X=2 表示雇佣两次的事件: C(n-1, 1) * 1 * (n-2)! + C(n-1, 2)/A(2,2) * 1 * (n-3)! + C(n-1, 3)/A(3,3) * 1 * (n-4)! + ...        //练习5.2-2
     *                    = (n-1)! * (1 + 1/(2!*2!) + 1/(3!*3!) + ... + 1/((n-1!)*(n-1)!))
     *  P(X=i) 不好求
     *
     *  令 X = X1 + X2 +...+ Xi +...+ Xn.       Xi 表示第 i 个人是否被雇用 = 1，被雇佣
     *                                                                  0，不被雇用
     *  Xi = 1 表示第 i 个人被雇佣的事件：第 i 个人被雇佣，则需要第 i 个人是前 i 个人中最优秀的
     *  P(Xi = 1) = 1/i
     *
     *  EX = EX1 + EX2 +...+ EXi +...+ EXn
     *     = 1/1 + 1/2 +...+ 1/i +...+ 1/n
     *
     *  (1/1 + 1/2 +...+ 1/i +...+ 1/n) >= ∫(1,n+1) (1/x)dx = ln(n+1)
     *  (1/2 +...+ 1/i +...+ 1/n) <= ∫(1,n) (1/x)dx = lnn
     *
     *  平均情况下，雇佣人数为 EX = lnn
     *  平均情况下，雇佣总花费: EX * C = C*lnn
     *
     *第四: 期望费用
     *   输入序列如果是均匀随机的，则平均情况下，雇佣总花费为 C*lnn
     *   而实际情况是，输入序列不一定是均匀随机的(通常，我们对输入序列一无所知)，则可使用随机数创造一个均匀随机的序列
     *
     *
     *为雇用问题创造随机输入序列: 使用随机数生成器产生 n 个数的随机序列，且该序列满足上述输入序列的假设
     *
     *list[n]
     *person = list[0]
     *for i from 1 to n:
     *    r = RANDOM(i, n)              # 等概率的在 i ~ n 中产生数字
     *    if(decide(list[r], person)):
     *       person = list[r]
     *    swap list[i], list[r]         # 秀
     *
     *
     *RANDOM(1, n): 等可能( 1/n 概率)的随机的产生数字 1 ~ n
     *    将 RANDOM(1, n) 调用 n 次
     *            i1 i2 i3 ... in     1/n * 1/n * ... * 1/n = 1/n^n
     *
     *    将 RANDOM(1, n) 调用 n 次，且 i 不重复
     *            i1 i2 i3 ... in     1/n * [(1/n) / (n-1)/n] * [(1/n) / (n-2)/n] *... = 1/n!
     *
     *
     *rd[n] = F[RANDOM(1, n)]... 不重复的产生n个数的随机序列
     *list[n]
     *person = lowest
     *for i from 1 to n:
     *    r = rd[i]
     *    if(decide(list[r], person)):
     *       person = list[r]
     *
     */
    /*
     *随机变量X: 雇用的人数 (雇佣次数)
     *
     *试验:    依次独立重复的面试 n 个人
     *基本事件: (雇，雇/不雇，... ，雇/不雇), n 元组
     *样本空间: { (雇，雇/不雇，... ，雇/不雇) }, 共 2^(n-1) 个元素
     *
     *X = i 表示"独立重复的面试 n 个人"试验中雇佣 i 个人的事件(事件是样本空间的子集)
     */
    static void chapter5_1And5_2And5_3() {}
}
