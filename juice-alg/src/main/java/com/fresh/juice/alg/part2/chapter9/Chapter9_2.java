package com.fresh.juice.alg.part2.chapter9;


import com.fresh.juice.alg.part1.chapter2.Chapter2_Practice.PositionArrayPrinter;
import com.fresh.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;
import com.fresh.juice.alg.part2.chapter7.Chapter7_1;

public class Chapter9_2 {

    /**
     *求第 i 个顺序统计量
     *  基本思想(分治): 给定数组 a[p, ..., r)，运行 partition，得到下标 q，使得 a[p, ..., q) <= a[q] < a[q+1,..,r)，则下标 q 为第 k=q-p+1 个顺序统计量
     *               比较 i 与 k。如果 i == k，则求得解。如果 i < k，则在 [p, q) 子数组上递归求 i。如果 i > k，则在 [q+1, r) 子数组上递归求 i-k
     *
     *  关于相等元素
     *    0  1  2  3  4
     *    5, 5, 7, 8, 9
     *  如上序列，下标 0，下标 1 的值都是 5。不稳定的算法，将导致元素的原有排位次序改变，从而可能返回: 下标 0 或者下标 1 为第 1 个顺序统计量；下标 0 或者下标 1 为第 2 个顺序统计量
     */
    public static int select(int[] a, int i) {
        if(a == null || a.length == 0 || i <= 0 || i > a.length) return -1;

        return select(a, 0, a.length, i);
    }
    /*
     *最坏情况运行时间
     *  当 partition 每次划分都只去掉一个元素时，是其最坏情况
     *  [n]
     *  [n-1]                1,   n-1,  c*n + Θ(1)
     *  [n-2]                2,   n-2,  c*(n-1) + Θ(1)
     *  ...
     *  [1]                  x,   1,    c*2 + Θ(1)
     *
     *  T(n) = (c*n + c*(n-1) + ... + c*2) + x*Θ(1) + Θ(1)
     *       = c*(n + n-1 + ... + 2) + n*Θ(1)
     *       = Θ(n^2)
     *
     *平均情况运行时间
     *  假设 n 个数的输入序列互异且均匀随机排列
     *
     *  随机变量 X: 所有 partition 中`a[k] <= e` 比较操作的总次数   //等于所有 partition 循环次数的总和
     *
     *  因为 partition 返回的下标和大于(或小于)该下标的元素不参与后续的递归，所以序列中 l 与 j 最多比较一次
     *
     *  X = Σ(l=0~n-2)Σ(j=l+1~n-1) Xlj,  Xlj 表示 l 与 j 比较次数 = 0
     *                                                          = 1
     *  P(Xlj = 1) = MAX{ MAX{ (n-q)/n * 2/(q-l+1) } q∈[j+1 ~ n), MAX{ (p+1)/n * 2/(j-p+1) } p∈[0 ~ l), (j-l+1)/n * 2/(j-l+1) }
     *             = MAX{ 2(n-j-1)/n(j-l+2), 2l/n(j-l+2), 2/n }                              //todo 假设第 i 个顺序统计量等概率的落于序列中每一个值, 牵强 🙂
     *     如果 l,j 在某次划分中被划分到不同分区或者划分到同一死亡分区，则 l,j 一次比较也不会发生
     *     假定一个有序序列 1 3 5 6 7 15 17 ... n-1 n, l,j 为其任意下标
     *       如果第 i 个顺序统计量位于 >q 的位置 q∈[j+1 ~ n)，则需要 l 位置或 j 位置先于 (l, j) 和 (j, q] 中所有位置被选为主元，就能保证 l 与 j 比较一次
     *       如果第 i 个顺序统计量位于 <p 的位置 p∈[0 ~ l)，则需要 l 位置或 j 位置先于 (l, j) 和 [p, l) 中所有位置被选为主元，就能保证 l 与 j 比较一次
     *       如果第 i 个顺序统计量位于 [l, j] 中任意位置，则需要 l 位置或 j 位置先于 (l, j) 中所有位置被选为主元，就能保证 l 与 j 比较一次
     *     如上过程可推广至任意顺序的序列
     *
     *  EX = E[ Σ(l=0~n-2)Σ(j=l+1~n-1) Xlj ]
     *     = Σ(l=0~n-2)Σ(j=l+1~n-1) EXlj
     *     = Σ(l=0~n-2)Σ(j=l+1~n-1) P(Xlj = 1)
     *     = Σ(l=0~n-2)Σ(j=l+1~n-1) MAX{ 2(n-j-1)/n(j-l+2), 2l/n(j-l+2), 2/n }
     *     = Θ(n)
     *
     *  T(n) = c*Θ(n) + c'*f(X)       //f(X) 表示 partition 被调用次数, f(X)'max = Θ(n)
     *       = Θ(n)
     */
    public static int select(int[] a, int begin, int end, int i) {
        //assert a != null;
        //assert begin ∈ [0, a.length); assert end ∈ [0, a.length];
        if(begin >= end) return -1;

        int q = partition(a, begin, end);
        int k = q - begin + 1;

        if(i == k) {
            return q;
        } else if(i < k) {
            return select(a, begin, q, i);
        } else {
            return select(a, q+1, end, i-k);
        }

    }
    static int partition(int[] a, int begin, int end) {
        return Chapter7_1.partition(a, begin, end);
    }
    //随机化. 期望运行时间: Θ(n)
    static int random_partition(int[] a, int begin, int end) {
        //int r = RANDOM(begin, end);   //[begin, end) 下标之间等概率的产生一个下标值
        //exchange a[r], a[end-1];
        return partition(a, begin, end);
    }

    public static void main(String[] argv) {
        int[] a = new int[] {100, 45, 56, 23, 1, 4, 3, 78, 3987, 242342, 23, 1, 489, 500, 110, 343};

        int q = select(a, 5);  //select(a, 0, a.length, -1); select(a, 0, a.length, 100);
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(q)::print);
        System.out.println("------select------------------------------------------------------------------");

        others();
    }

    /**
     *求第 i 个顺序统计量 others
     *
     *第一: 依次比对
     *  for l from 0 to n-1:
     *      for j from 0 to n-1:
     *          if j==l: continue
     *          if A[j] < A[l]
     *              less++
     *          if A[j] == A[l]:
     *              eq++
     *              if j < l: pre_eq++
     *
     *      check1: 互异
     *          if less == i-1: break //返回A[l]
     *
     *      check2: 非互异，相等的元素按出现的先后次序排位，依次确定顺序统计量
     *          if less + pre_eq == i-1: break //返回A[l], 相等有先后的条件下下标 l 是准确的
     *
     *      check3: 非互异，相等元素可随意使用一组顺序统计量
     *          if less <= i-1 <= less + eq: break  //返回A[l], 真实的值在 [l, l+eq] 下标范围中
     *
     *最坏情况运行时间：n^2
     *
     *
     *第二: 同时找出第 1, 2, 3, ..., i 个顺序统计量（在 n 个数中求排名前 100 的数）
     *  int select(int[] A, int i) {
     *      if(A == null || A.length == 0 || i <= 0 || i > A.length) return -1;
     *
     *      int[] r = new int[i];
     *      r[0] = A[0];
     *
     *      for(int k=1; k < A.length; k++) {
     *          insert(r, Math.min(k-1, r.length-1), A[k]);
     *      }
     *
     *      return r[i-1];
     *  }
     *  void insert(int[] r, int j, int v) {
     *      while(j >= 0 && r[j] > v) {
     *          if(j + 1 < r.length)
     *              r[j + 1] = r[j];
     *          j--;
     *      }
     *      if(j + 1 < r.length)
     *          r[j + 1] = v;
     *  }
     *最坏情况下: T(n) = (0 + 1 + ... + i-1 + i) + (i + ... + i) = i*(i+1)/2 + i*(n-i-1) = i*n - (i^2)/2 - i/2 = Θ(i*n)
     *
     *
     *第三: 分治
     * select(int[] A, int i) {
     *     if A == null || A.length == 0: return null
     *     if a.length == 1:
     *         if i == 1: return new int[1] {a[0]}
     *         return null
     *
     *     assert 1 <= i <= A.length
     *
     *     return select(A, 0, A.length, k)
     * }
     * select(int[] A, int begin, int end, int i) {
     *     n = end - begin
     *     if n == 1: return new int[i] {A[begin], MAX, ...}
     *
     *     left = select(A, begin, begin + n/2, i)
     *     right = select(A, begin + n/2, end, i)
     *
     *     r = new int[i]
     *     for(t=0, j=0, l=0; l<i; l++) {
     *         if(left[t] <= right[j]) {
     *             r[l] = left[t]
     *             t++
     *         } else {
     *             r[l] = right[j]
     *             j++
     *         }
     *     }
     *
     *     return r
     * }
     *画出递归树, T(n) = ci * (1 + 2 + 4 + ... + 2^(x-1)) + Θ(1) * (1 + 2 + ... + 2^x)    //x=lgn
     *               = (c/2)*i*n - ci + Θ(1) * (n-1)
     *               = Θ(i*n)
     */
    static void others() {}

}
