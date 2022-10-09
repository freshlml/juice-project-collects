package com.juice.alg.chapter2_9;


public class Chapter2_9_2 {

    /**
     *第i个顺序统计量
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
     *      check2: 非互异，相等有先后
     *          if less + pre_eq == i-1: break //返回A[l],相等有先后的条件下下标l是准确的
     *      check3: 非互异，相等无先后
     *          if less <= i-1 <= less + eq: break  //返回A[l], 真实的值在[l, l+eq]下标范围中
     *最坏情况运行时间：n^2
     *
     *第二: 分治, i * (n-1)
     * select(int[] A, int i) {
     *     if A == null || A.length == 0: return null
     *     if a.length == 1:
     *         if k == 1: return new int[1] {a[0]}
     *         return null
     *
     *     assert 1 <= i <= A.length
     *
     *     return select(A, 0, A.length, k)
     * }
     * select(int[] A, int begin, int end, int i) {
     *     n = end - begin
     *     if n == 1: return int[i] {A[begin], MAX, ...}
     *
     *     left = select(A, begin, begin + n/2, i)
     *     right = select(A, begin + n/2, end, i)
     *
     *     r = int[i]
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
     *
     *
     */
    /**
     *第三: partition
     *  序列中存在相等元素时:
     *   1.相等的元素按出现的先后次序排位，依次确定顺序统计量
     *   2.或者，有些算法是不稳定的，将导致相等元素的排位次序改变，这样，一组相等元素可随意使用一组顺序统计量
     *   3.或者认为，相等的元素是一个顺序统计量
     *  partition版本 符合上述1
     *  random_partition版本 符合上述2，可以考虑搭配range_partition返回一组下标
     *
     *  最坏情况运行时间: n^2
     *
     *  平均情况运行时间分析:
     *     输入序列假设: n个数互异且均匀随机
     *
     *     X: partition次数，概率不好求
     *     X: 所有partition中总比较次数，partition执行多少次是随机的，不好求
     *
     *     todo
     *
     *
     *  int random_partition(int a[], int begin, int end) {
     *     int r = RANDOM(begin, end) //begin,end下标之间等概率的产生一个下标值
     *     exchange a[r],a[end-1]
     *     return partition(a, begin, end)
     *  }
     */
    public int select(int a[], int i) {
        if(a == null || a.length == 0) return -1; //tag
        if(a.length == 1) {
            if(i == 1) return a[0];
            return -1; //tag
        }
        //assert 1 <= i <= a.length

        return select(a, 0, a.length, i);
    }
    public int select(int a[], int begin, int end, int i) {
        if(end - begin == 1) return a[begin];

        int q = partition(a, begin, end);

        int k = q - begin + 1;

        if(i == k) {
            return a[q];
        } else if(i < k) {
            return select(a, begin, q, k);
        } else {
            return select(a, q+1, end, i-k);
        }

    }
    public int partition(int a[], int begin, int end) {
        int i = begin-1;
        int j = begin;
        int k = a[end-1];

        while(j < end) {
            if(a[j] <= k) {
                i++;
                int ex = a[i];
                a[i] = a[j];
                a[j] = ex;
            }

            j++;
        }
        return i;
    }



}
