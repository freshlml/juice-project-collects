package com.fresh.juice.alg.part1.chapter4;

import com.fresh.juice.alg.part1.chapter2.Chapter2;

public class Chapter4 {
    /**
     *Chapter 4 分治策略
     *
     *分治策略思想
     *  分解(Divide):  将问题划分成若干子问题，子问题的形式和原问题相同，只是规模更小
     *  解决(Conquer): 递归地求解子问题，当子问题的规模足够小，停止递归，直接求解
     *  合并(Combine): 将子问题的解合并成原问题的解
     *
     *递归式
     *  递归式与分治策略是紧密相关的，使用递归式可以很自然的刻画分治算法的运行时间。
     *
     *  例如，归并排序 {@link Chapter2#merge_sort(int[])} 的最坏情况运行时间 T(n) 的递归式为:
     *    T(n) = Θ(1)               若 n == 1
     *         = 2*T(n/2) + Θ(n)    若 n >  1
     *
     *  递归式有几种求解方法:
     *    - Chapter 4.3 用代入法求解递归式, 猜测一个界，然后证明之
     *    - Chapter 4.4 用递归树求解递归式, 将递归式转化成递归树求解
     *    - Chapter 4.5 用主方法求解递归式, 可求解 T(n) = a*T(n/b) + f(n), a>=1, b>1 此种递归式的界
     *
     */
    public static void main(String[] argv) {
        chapter4_3();
        chapter4_4();
        chapter4_5();
    }
    /**
     *Chapter 4.3 用代入法求解递归式
     *  1. 猜测解的形式
     *  2. 用数学归纳法求出解中的常数，并证明解是正确的
     *例子: 求递归式  T(n) = 2T(n/2) + Θ(n)  的解
     *  1. 猜测 T(n) = O(n*lgn), 即要证: T(n) <= c*n*lgn
     *  2. T(n) = 2T(n/2) + Θ(n) <= 2*c*(n/2)*lg(n/2) + Θ(n)
     *                           <= c*n*lgn - c*n*lg2 + Θ(n)
     *     令 c*n*lgn - c*n*lg2 + Θ(n) <= c*n*lgn
     *     即当，- c*n*lg2 + Θ(n) <= 0 时成立，则 c >= Θ(n)/n = 某个常数
     *
     *变量代换:
     *  T(n) = 2*T(根号(n)) + lgn
     *  1. 令 m = lgn, 得: T(2^m) = 2*T(2^(m/2)) + m    自变量代换
     *  2. 令 S(m) = T(2^m), 得 S(m/2) = T(2^(m/2))     因变量代换？
     *       T(2^m) = S(m) = 2*S(m/2) + m
     */
    static void chapter4_3() {}
    /**
     *Chapter 4.4 用递归树求解递归式
     *
     *T(n) = m*T(n/k) + f(n), f(n)为本次分解+合并的消耗。影响速度的因素: m, k, f(n)
     *  m决定树的茂盛，m越小，partition/merge 次数越少
     *  k决定树的高度，k越大，树越矮，每次 partition/merge 的规模越小
     *
     *例子: 求解递归式  T(n) = 3*T(n/4) + c*n^2
     *                                      [n]                                   第一层
     *            [n/4]                    [n/4]                   [n/4]          第二层    第一次分解，n/4      1次merge, 1*cn^2                           , 3份
     *    [n/16]  [n/16]  [n/16]   [n/16]  [n/16] [n/16]   [n/16]  [n/16] [n/16]  第三层    第二次分解，n/4^2    3次merge, 3*c(n/4)^2                       , 3^2份
     *    ...
     *    ...                               [1]                             ...   第x+1层   第x次分解，n/4^x    3^(x-1)次merge, 3^(x-1) * c[n/4^(x-1)]^2   , 3^x份
     *
     * 1.n/4^x = 1, 4^x = n, x=log4(n)
     * 2.分解加合并的消耗之和
     *   = 1*cn^2 + 3*c(n/4)^2 + 3^2*c(n/4^2)^2 + ... + 3^(x-1) * c[n/4^(x-1)]^2
     *   = cn^2 * [1 + 3/16 + (3/16)^2 + (3/16)^3 +...+ (3/16)^(x-1)]
     *   = cn^2 * 16/13 - cn^2 * 16/13 * (3/16)^x
     *   = cn^2 * 16/13 - c * 16/13 * 3^x        ,令 3^x = (4^x)^y = n^y, 3 = 4^y, y = log4(3)
     *   = cn^2 * 16/13 - c * 16/13 * n^(log4(3))
     * 3.解决的消耗之和
     *     第x层数量 3^x <= n，其中 3^x = n^(log4(3))
     *   = T(1) * n^(log4(3))
     *
     *T(n) = m*T(n/k) + Θ(n) 的通项解:
     *        [n]
     *    [n/k]              1, n/k,     1 * cn
     *    [n/k^2]            2, n/k^2    m * cn/k
     *    [n/k^3]            3, n/k^3    m^2 * cn/k^2
     *    ...
     *    [1]                x, n/k^x    m^(x-1) * cn/k^(x-1)
     *
     * 1. n/k^x = 1, k^x = n, x = logk(n)
     * 2. 分解加合并的消耗之和
     *  若 m == k: cn*lgn
     *  若 m != k != 1:
     *    1 * cn + m * cn/k + m^2 * cn/k^2 + ... + m^(x-1) * cn/k^(x-1)
     *  = cn * [ (m/k)^0 + (m/k)^1 +  (m/k)^2 + ... + (m/k)^(x-1) ]
     *  = cn * [1 - (m/k)^x] / [1 - (m/k)]
     *  = cn*k/(m-k) * (m/k)^x - cn*k/(m-k)
     *  = c*k/(m-k) * m^x - cn*k/(m-k)            ,令 m^x = (k^x)^y = n^y,  m = k^y,   y = logk(m)
     *  = c*k/(m-k) * n^(logk(m)) - cn*k/(m-k)
     *
     * m=2, k=2: cn*lgn
     * m=3, k=2: c*2 * n^(lg3) - cn*2 = c*2 * n^(lg3)  >= c * 2/3 * n^(1.5) >= c * 2/3 * n * n^(0.5)     ,lg3 ≈ 1.5849625
     * m=4, k=2: c * n^2 - cn  = c * n^2
     */
    static void chapter4_4() {}
    /**
     *Chapter 4.5 用主方法求解递归式
     * 所谓主方法，即对 T(n) = m*T(n/k) + f(n) 的通项解:
     *        [n]
     *    [n/k]              1, n/k,     1 * f(n)
     *    [n/k^2]            2, n/k^2    m * f(n/k)
     *    [n/k^3]            3, n/k^3    m^2 * f(n/k^2)
     *    ...
     *    [1]                x, n/k^x    m^(x-1) * f(n/k^(x-1))
     *
     * 1. n/k^x = 1, k^x = n, x = logk(n)
     * 2. 分解加合并的消耗之和
     *    1 * f(n) + m * f(n/k) + m^2 * f(n/k^2) + ... + m^(x-1) * f(n/k^(x-1))
     *    todo
     */
    static void chapter4_5() {}

}
