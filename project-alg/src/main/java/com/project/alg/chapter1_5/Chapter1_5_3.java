package com.project.alg.chapter1_5;

public class Chapter1_5_3 {

    /**
     *一些概率求解问题
     */

    /**
     *一: 一年有n天(通常取365)，现有k个人，使得其中两个人生日相同的概率>50%，求k
     *
     *问题分析{
     *  k个人，编号从1 ~ k，每个人的生日与其他人无关，都是n天中的某一天
     *  1   2  ... k
     *  n   n  ... n
     * 共 n^k 种组合，每一种等概率
     * P(i=r) = 1/n, 第i个人的生日是第r天的概率
     *}
     *P(至少两个人生日相同) = 1 - P(每个人生日都不相同) = 1 - n*(n-1)*...*(n-k+1)/n^k
     * k个人生日都不相同的事件: B(k) = ∩(i=1->k)A(i) , 其中Ai表示对所有的j<i，j与i生日不相同
     *                     B(k) = A(k) ∩ B(k-1)
     *       P(B(k)) = P(A(k) | B(k-1)) * P(B(k-1)), 其中P(A(k) | B(k-1)) = (n-k+1)/n
     *
     *
     *试验:    k个人，依次检测i,j的生日，其中i < j (两两检测)
     *基本事件:  (是同一天/不是同一天，是同一天/不是同一天，... ，是同一天/不是同一天)      共 (k-1) + (k-2) +...+ 1 = k*(k-1)/2 个<i,j>组合
     *样本空间S: { (是同一天/不是同一天，是同一天/不是同一天，... ，是同一天/不是同一天) }  共 2^[k*(k-1)/2] 个元素
     *随机变量X: 同一天生日的<i,j>对的个数
     *    X    0               ，1                 ，2 ...
     *  描述   每个人生日都不相同   ，仅存在两个人生日相同  ，存在两对<i,j>生日相同，但这两队的生日不一定相同 ...
     *   P(X)  P(0)            , P(1) ...
     *
     *随机变量X(i,j): 1，   i,j是同一天,  P(i,j是同一天) = C(1,n) * 1/n * 1/n = 1/n
     *              0，   i,j不是同一天, P(i,j不是同一天) = 1 - 1/n
     *
     *   X = ΣX(i,j)
     *  EX = EΣX(i,j) = k*(k-1)/2*n
     *  另EX>=1 ==> k*(k-1) >= 2*n
     */

    /**
     *[1,n]的序列，选择k，1<=k<n; 在[1,k]中选出最大值max，在[k+1,n]中选出第一个>max的
     *求: 选到最好应聘者事件的概率
     *
     *输入序列的分布决定事件的概率，这里合理假定输入序列是均匀随机的{
     *     1,    2, ..., n
     *     rank(i)表示第i位置的排名
     *     <rank(1), rank(2), ..., rank(n)>是其中一种排列方式
     *     <rank(2), rank(1), ..., rank(n)>也是
     *     这样的排列有A(n,n) = n!, 每一种排列方式等概率
     *}
     *
     *S:  选到最好应聘者的事件
     *Si: 最好应聘者是第i位置的事件
     *S = ∪(i=1->n)S(i)
     *P(S) = P( S(1) + S(2) + S(k) + S(k+1) + ... + S(n) )  ,S(i)互斥
     *  = P(S(1)) + P(S(2)) + ... + P(S(k+1)) + ... + P(S(n)) , P(S(1)) ~ P(S(k)) = 0
     *  = P(S(k+1)) + ... + P(S(n))
     *
     *S(i) = A(i) ∩ B(i) , i∈[k+1, n]，A(i)表示第i位置最大的事件，B(i)表示1~k位置的最大值>任意[k+1,i-1]位置的值
     *
     *P(S(i)) = P( A(i) ∩ B(i) ) ,A(i),B(i)独立
     *        = P(A(i)) * P(B(i))
     *
     *P(A(i)) = A(n-1,n-1) / A(n,n) = 1/n
     *P(B(i)) = P(1~i-1范围内，最大值出现在0~k) = C(1,k) * A(i-2,i-2) / A(i-1,i-1) = k/(i-1)
     *则P(S(i)) = k/n*(i-1)
     *
     *P(S) = k/n*k + k/n*(k+1) + ... + k/n*(n-1)
     *     = k/n*[1/k + 1/(k+1) + ... + 1/(n-1)]
     *  求得P(S)的下界k/n*(ln(n) - ln(k))
     *  f(k) = k/n*(ln(n) - ln(k))
     *  f'(k) = 1/n*(ln(n) - ln(k) -1) = 0 ==> k=n/e
     *  当k>n/e时，f'(k) < 0 , 递减
     *  当k<n/e时，f'(k) > 0 , 递增
     *  k=n/e时取到最大值f(n/e) = 1/e
     *
     *当k=n/e时，选到最好应聘者事件的概率至少是1/e
     */

}
