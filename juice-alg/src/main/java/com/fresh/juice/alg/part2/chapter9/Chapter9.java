package com.fresh.juice.alg.part2.chapter9;

public class Chapter9 {

    /**
     *chapter 9
     *
     *顺序统计量
     *  n 个元素的序列中，第 i 个顺序统计量为该序列中第 i 小的元素。i = 1, 2, ..., n
     *
     *  在一个序列中，最小值为第 1 个顺序统计量。最大值为第 n 个顺序统计量
     *
     *  若序列中包含重复元素，则某个顺序统计量可能不是唯一的，即重复元素均可作为该顺序统计量
     *
     *  顺序统计量的"数量"意义: 第 i 个顺序统计量，意味着有 i-1 个数小于等于它
     *
     *中位数
     *  当 n 为奇数时，中位数是唯一的: 第 (n+1)/2 个顺序统计量
     *  当 n 为偶数时，下中位数为: 第 n/2 = ⌊(n+1)/2⌋ 个顺序统计量
     *              上中位数为: 第 n/2 + 1 = ⌊(n+2)/2⌋ 个顺序统计量
     *
     */
    public static void main(String[] argv) {
        chapter9_1();
    }
    /**
     *chapter 9.1
     *
     *找最值
     *  ①
     *   min(int[] A) {
     *       min = A[0]
     *       for i from 1 to n-1:
     *           if min > A[i]:
     *               min = A[i]
     *   }
     *   运行时间 T(n) = Θ(1) * (n-1) = Θ(n)
     *
     *  ②
     *   minimum(int[] A, int begin, int end) {
     *       n = end - begin
     *       if n == 1: return A[begin]
     *
     *       left = minimum(A, begin, begin + n/2)
     *       right = minimum(A, begin + n/2, end)
     *
     *       if left <= right: return left
     *       else: return right
     *   }
     *   画出递归树: T(n) = 1*Θ(1) + 2*Θ(1) + 4*Θ(1) + ... + 2^(x-2) * Θ(1) + 2^(x-1) * Θ(1)  + n*Θ(1), x = lgn
     *                  = Θ(1) * (2n - 1)
     *                  = Θ(n)
     *
     *
     *同时找最大值和最小值
     *  ①
     *    min = A[0]
     *    max = A[0]
     *    for i from 1 to n-1:
     *        if min > A[i]:
     *            min = A[i]
     *        if max < A[i]:
     *           max = A[i]
     *
     *    最坏情况下，总比较次数: 2 * (n-1)
     *
     *  ②
     *   A.length为奇数:
     *      min = A[0]
     *      max = A[0]
     *      start = 1
     *   A.length为偶数:
     *      if A[0] > A[1]:
     *          min = A[1]
     *          max = A[0]
     *      else:
     *          min = A[0]
     *          max = A[1]
     *      start = 2
     *   end = n - 2
     *
     *   for i from start to end step by 2:
     *       if A[i] <= A[i+1]:
     *           if min > A[i]:
     *               min = A[i]
     *           if max < A[i+1]:
     *               max = A[i+1]
     *       else:
     *           if min > A[i+1]:
     *               min = A[i+1]
     *           if max < A[i]:
     *               max = A[i]
     *
     *   最坏情况下，总计较次数:                                                               //练习9.1-2
     *     A.length为奇数: 1 + (⌊n/2⌋ - 1) * 3 = ⌊n/2⌋ * 3 - 2
     *     A.length为偶数: ⌊n/2⌋ * 3
     */
    static void chapter9_1() {}


    //练习9.1-1
    /*1:
     *  min = A[0]
     *  im = 0
     *  for i from 1 to n-1:
     *      if min > A[i]:
     *          min = A[i]
     *          im = i
     *  SWAP A[0], A[im]
     *
     *  min2 = A[1]
     *  for i from 2 to n-1:
     *      //if A[i] == min: continue
     *      if min2 > A[i]:
     *          min2 = A[i]
     *
     *最坏情况下，比较次数: n-1 + n-2
     *
     *
     *2:
     *  if A[0] <= A[1]:
     *      min  = A[0]
     *      min2 = A[1]
     *  else:
     *      min  = A[1]
     *      min2 = A[0]
     *
     *  for i from 2 to n-1:
     *      if min2 > A[i]:
     *          if min <= A[i]:
     *              min2 = A[2]
     *          else:
     *              min2 = min
     *              min = A[2]
     *
     *最坏情况下，比较次数: 1 + 2 * (n-2)
     *
     *
     *3:
     *  minimum_1_2(int[] A, int begin, int end) {
     *      n = end - begin
     *      if n == 1: return int[] {A[begin]}
     *
     *      left = minimum_1_2(A, begin, begin + n/2)
     *      right = minimum_1_2(A, begin + n/2, end)
     *
     *      r = int[2]
     *      for(i=0, j=0, l=0; l<2; l++) {
     *          if(j >= right.length || (i < left.length && left[i] <= right[j])) {
     *              r[l] = left[i]
     *              i++
     *          } else {
     *              r[l] = right[j]
     *              j++
     *          }
     *      }
     *      return r
     *  }
     *画出递归树，比较次数 = 1*2 + 2*2 + 4*2 + ... + 2^(x-1) * 2, x = lgn
     *                 = 2 * (n-1)
     *         T(n) = 1*2 + 2*2 + 4*2 + ... + 2^(x-1) * 2 + Θ(n) = 2 * (n-1) + Θ(n)
     *
     *
     *4: 比较次数 = n + ⌈lgn⌉ - 2‌
     *Ⅰ.创建比较树
     *  class Node {int value, Node l, Node r}
     *
     *  create_tree(int[] A, int begin, int end) {
     *      n = end - begin
     *      if n == 1: return Node{A[begin], null, null}
     *
     *      left = minimum(A, begin, begin + n/2)
     *      right = minimum(A, begin + n/2, end)
     *
     *      node = Node()
     *      node.value = Min(left.value, right.value)
     *      node->l = left
     *      node->r = right
     *
     *      return node
     *  }
     *  eg: 5 3 9 7 6 2 1 3
     *                  1
     *         3                  1
     *    3        7         2         1
     *  5   3    9   7     6   2     1   3
     *
     *Ⅱ.遍历tree
     *  p = create_tree(A, 0, A.length)
     *  if p.l ==null && p.r == null: return p
     *
     *  candidate = null
     *  while(p.l != null && p.r != null) {
     *      current_candidate = p.l.value >= p.r.value ? p.l : p.r
     *      if candidate == null or current_candidate.value < candidate.value: candidate = current_candidate
     *      p = p.l.value >= p.r.value ? p.r : p.l
     *  }
     *  return candidate
     */

}
