package com.juice.alg.chapter2_9;

public class Chapter2_9_1 {

    /**
     *找最值
     * min(int[] A) {
     *     min = A[0]
     *     for i from 1 to n-1:
     *         if min > A[i]:
     *             min = A[i]
     * } //比较n-1次
     * minimum(int[] A, int begin, int end) {
     *     n = end - begin
     *     if n == 1: return A[begin]
     *
     *     left = minimum(A, begin, begin + n/2)
     *     right = minimum(A, begin + n/2, end)
     *
     *     if left <= right: return left
     *     else: return right
     * } //比较n-1次
     *找最值比较n-1次是渐进最优的: n个数候选，每比较一次排除一个
     *
     *同时找最大值和最小值
     * min = A[0]
     * max = A[0]
     * for i from 1 to n-1:
     *     if min > A[i]:
     *         min = A[i]
     *     if max < A[i]:
     *        max = A[i]
     *总比较次数: 2 * (n-1)
     *
     * min = A[0]  //n=A.length为奇数
     * max = A[0]
     * for i from 1 to n-2 step by 2:
     *     if A[i] <= A[i+1]:
     *         if min > A[i]:
     *             min = A[i]
     *         if max < A[i+1]:
     *             max = A[i+1]
     *     else:
     *         if min > A[i+1]:
     *             min = A[i+1]
     *         if max < A[i]:
     *             max = A[i]
     *总计较次数: 3*n/2
     *
     *同时找最大值和最小值的比较次数的下界(练习9.1-2)
     * 初始: 最大值候选n，最小值候选n
     * 第一次比较: A1 < A2，比较一次，可以确定当前最大值最小值
     * 第二次比较: A3 < A4，比较 1 + 2(3个位置选两个) 次，可以确定当前最大值最小值
     * ...
     * 第i次比较: Ai < Ai+1，比较 1 + 2(3个位置选两个) 次，可以确定当前最大值最小值
     *总比较次数: 1 + 3 + 3 + ... + 3
     *  n为奇数: 0 + (n-1)/2 * 3 = 3n/2 - 3/2
     *  n为偶数: 1 + (n-2)/2 * 3 = 3n/2 - 2
     *
     */

    /*练习9.1-1
     *1: 除了min之外的最小为第二小
     * min = A[0]
     * for i from 1 to n-1:
     *     if min > A[i]:
     *         min = A[i]
     * min2 = MAX
     * for i from 0 to n-1:
     *     if A[i] == min: continue
     *     if min2 > A[i]:
     *         min2 = A[i]
     * assert min2 != MAX
     *比较次数: n-1 + n
     *
     *2: 根据min的信息，同时找min2
     * min = A[0]
     * min2 = MAX
     * for i from 1 to n-1:
     *     if min > A[i]:
     *         min2 = min
     *         min = A[i]
     *     else if min < A[i]:
     *         if min2 > A[i]:
     *             min2 = A[i]
     * assert min2 != MAX
     *最坏情况序列: min,...
     *最坏情况比较次数: 2*(n-1)
     *
     *3: 2 * (n-1)
     * minimum_1_2(int[] A, int begin, int end) {
     *     n = end - begin
     *     if n == 1: return int[2] {A[begin], MAX}
     *
     *     left = minimum_1_2(A, begin, begin + n/2)
     *     right = minimum_1_2(A, begin + n/2, end)
     *
     *     r = int[2]
     *     for(i=0, j=0, l=0; l<2; l++) {
     *         if(left[i] < right[j]) {
     *             r[l] = left[i]
     *             i++
     *         } else if(left[i] > right[j]) {
     *             r[l] = right[j]
     *             j++
     *         } else {
     *             r[l] = left[i]
     *             i++
     *             j++
     *         }
     *     }
     *
     *     return r
     * }
     *
     *4: n + lgn - 2
     *1.创建比较树
     * create_tree(int[] A, int begin, int end) {
     *     n = end - begin
     *     if n == 1: return Node{A[begin], null, null}
     *
     *     left = minimum(A, begin, begin + n/2)
     *     right = minimum(A, begin + n/2, end)
     *
     *     node = Node()
     *     node.value = Min(left.value, right.value)
     *     node->l = left
     *     node->r = right
     *
     *     return node
     * }
     * eg: 5 3 9 7 6 2 1 3
     *                 1
     *        3                  1
     *   3        7         2         1
     * 5   3    9   7     6   2     1   3
     *
     *2.遍历tree
     *  p = head
     *  candidate = MAX
     *  while(p.l != null && p.r != null) {
     *      current_candidate = Max(p.l.value, p.r.value)
     *      if current_candidate < candidate: candidate = current_candidate
     *      p = p.l.value < p.r.value ? p.l : p.r
     *  }
     *
     *
     *
     */




}
