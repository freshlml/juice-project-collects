package com.juice.alg.part1.chapter5;

public class Chapter5_Practice3 {

    //练习题5.4-1
    //一年有 n 天(通常取 365 )，一屋子里面必须达到多少人，才能使某人的生日和特定日期相同的概率 >= 1/2？
    //  k 个人，编号从 1 ~ k，每个人的生日与其他人无关，都是 n 天中的某一天
    //  令 i = 1, 2, ..., k. 令 bi 表示编号为 i 的人的生日是某个特定日期
    //
    //  对任意的 i = 1, 2, ..., k. P(bi) = 1/n    即任意某个人生日是特定日期的概率为 1/n
    //
    //①
    //  定义事件A: 没有人的生日和特定日期相同. 求 1 - P(A) >= 1/2
    //  定义事件Ai: 第 i 个人生日和特定日期不同. P(Ai) = (n-1)/n
    //  P(A) = P(A1 ∩ A2 ∩ ... ∩ Ai ∩ ...)        Ai 之间相互独立
    //       = P(A1) * ... * P(Ai) * ...
    //       = (n-1)/n * ... * (n-1)/n * ...
    //
    //  1 - P(A) >= 1/2 ==> k >= ln2*n
    //
    //② 近似求解
    //  试验:     依次检测每个人的生日是否和特定日期相同. 共检测 k 次
    //  基本事件:  (1/0, 1/0, ...)      k元组, 其中 1 表示某个人生日和特定日期相同
    //  样本空间S: { (1/0, 1/0, ...) }  共 2^k 个元素
    //
    //  随机变量X: k 个人中和特定日期生日相同的人的数量
    //    X    0  1  2  ...  k
    //    P
    //    X = X1 + X2 + ... + Xi + ...   其中 Xi 表示第 i 个人的生日是否与某个特定日期相同 = 1, 相同
    //                                                                            = 0, 不相同
    //    P(Xi = 1) = 1/n
    //
    //    EX = EX1 + ... + EXi + ...
    //       = 1/n + ... + 1/n + ...
    //       = k/n
    //    令 EX >= 1, k/n > 1, k > n

    //练习题5.4-2
    //将相同的球随机投到 b 个箱子，箱子编号为 1, 2, ..., b. 每次投球都是独立的，即某一次投球不知道前面投的球投到了哪个箱子.
    //每一次投球，球等可能的落在每一个箱子中，概率为 1/b, 假设一个箱子可以容纳多个球. 投球的过程是伯努利试验，成功的概率为 1/b, 其中，成功是指球落入指定的箱子.
    //
    //  随机变量X: 直到某个箱子有两个球，的投球次数
    //    X    2           3                     4                              ...  b                                     b+1
    //    P    1/b * 1/1   1/b * 1/(b-1) * 1/2   1/b * 1/(b-1) * 1/(b-2) * 1/3  ...  1/b * 1/(b-1) * ... * 1/2 * 1/(b-1)   1
    //  EX
    //    令 A1 = 1/2 * b/(b-1), A2 = 1/3 * ((b-1)/(b-2) + A1), A3 = 1/4 * ((b-2)/(b-3) + A2), ..., A[b-1] = 1/b * (2/1 + A[b-2])
    //  EX = A[b-1] + (b+1)

    //练习题5.4-4
    //一次聚会需要邀请多少人，才能让其中三人的生日很可能相同？
    //  1 - P(每个人生日都不相同) - P(只有两个人生日相同) >= a%
    //  P(每个人生日都不相同) = n*(n-1)*...*(n-k+1)/n^k
    //  P(只有两个人生日相同) = ?

    //练习题5.4-5
    //  当 k 字符串所有字符都不相同的时候，即可构成大小为 n 的集合的某一个 k 排列？

    //练习题5.4-6
    //  随机变量X: 空箱子数目
    //  X = X1 + X2 + ... + Xn
    //  Xi 表示第 i 个箱子是否为空: 1 //为空
    //                         0 //不为空
    //  P(Xi = 1) = P(第 i 个箱子中球的数目为 0) = C(0,n) * (1/n)^0 * (n-1/n)^n
    //
    //  随机变量Y: 正好有一个球的箱子的数目
    //  同理...


    /*
     *思考题5-2
     *
     *a:
     *  A[n]
     *  tag[n] = {0}
     *  found = false
     *  while true:
     *    i = random(0, n)  //[0, n)
     *    if A[i] == x:
     *      found = true
     *      break
     *    tag[i] = 1
     *    for t in tag:
     *      if t == 0 break
     *    else break
     *  if found: return A[i]
     *
     *b:
     *  令 P(A[random(0, n)] = x) = p //p = 1/n?
     *  随机变量X ~ 几何分布(p)
     *  EX = 1/p
     *
     *c:
     *  令 P(A[random(0, n)] = x) = p //p = k/n?
     *
     *d:
     *  X   1  2   n
     *  P   0  0   1
     *  EX = n
     *
     *
     *A[n]
     *j = 1
     *while j < n:
     *    if A[j] == x:
     *        break
     *    j++
     *
     *e:
     *  最坏情况: n
     *  平均情况:
     *    P(A[i] = x) = 1/n
     *    EX = n
     *
     *f:
     *  第一: 最坏情况
     *    n - k + 1
     *
     *  第二: 平均情况
     *    P(A[i] = x) = k/n
     *
     *    X    1   2   ...  n-k+1
     *    p
     *    X ~ 几何分布(k/n)
     *    EX = n/k
     *    ②
     *    A(i): 对所有的 j < i, A[j] != x
     *    B(i): 下标 i 的值 A[i] == x
     *    P(A(i) ∩ B(i))              A(i) 与 B(i) 独立
     *    = P(A(i)) * P(B(i))
     *    = (1-k/n)^(i-1) * k/n
     *
     *g:
     *  EX = P(X=n) * n = 1 * n = n
     *
     *
     *A[n]
     *j = 1
     *while j < n:
     *    r = random(j, n)  //[j, n)
     *    if A[r] == x:
     *        break
     *    exchange A[j] A[r]
     *    j++
     *
     *h:
     *  P(A[i] = x) = 1/n
     *    最坏情况: n
     *    平均情况: n
     *
     *  P(A[i] = x) = k/n
     *    最坏情况: n-k+1
     *    最坏情况: k/n
     *
     *  P(A[i] = x) = 0
     *    最坏情况: n
     *    平均情况: n
     *
     *i:
     *  若 k = 1: n/k = n-k+1 恒成立
     *  若 k = n: n/k = n-k+1 恒成立
     *  若 1 < k < n: n/k < n-k+1 恒成立。令 f(k) = (n - k + 1) / n/k = (-k^2 + nk + 1) / n，当 k = n/2 时，f(k) 极值点，两者差距达到最大
     *
     */

}
