package com.juice.alg.part2.chapter9;

public class Chapter9_Practice1 {

    //练习9.3-1
    //  每组 5 个元素，最坏情况下: T(n) = T(⌈n/5⌉) + T(7n/10 + 6) + Θ(n)
    //                            <= cn + (7c - cn/10 + Θ(n))
    //                            令 7c - cn/10 + Θ(n) <= 0，即 7c - cn/10 + a1*n <= 0，得 c >= 10*a1 * n/(n-70)
    //                            解得，当 n > 70 时，存在 c>0，使得 T(n) <= cn 成立
    //
    //  每组 7 个元素，最坏情况下: T(n) = T(⌈n/7⌉) + T(5n/7 + 8) + Θ(n)
    //                            <= cn + (9c - cn/7 + Θ(n))
    //                            令 9c - cn/7 + Θ(n) <= 0，即 9c - cn/7 + a2*n <= 0，得 c >= 7*a2 * n/(n-63)
    //                            解得，当 n > 63 时，存在 c>0，使得 T(n) <= cn 成立
    //
    //  另: 当 n->∞ 时，[n/(n-70)] / [n/(n-63)] -> 1，则，[10*a1 * n/(n-70)] / [7*a2 * n/(n-63)] -> 10a1 / 7a2
    //
    //
    //  每组 3 个元素，最坏情况下: T(n) = T(⌈n/3⌉) + T(2n/3 + 4) + Θ(n)
    //                            <= cn + 5c + Θ(n)
    //                            5c + Θ(n) > 0 恒成立，不能得出 T(n) <= cn


    //练习9.3-2
    //  ⌈n/4⌉ < n/4 + 1，当 n >= 140 时，n/4 + 1 <= 3n/10 -6 <= 3*(⌈⌈n/5⌉/2⌉ - 2)


    //练习9.3-3
    //  修改 partition，选择中位数作为划分主元


    //练习9.3-4
    //  求第 i 个顺序统计量时，同时找出 i-1 个顺序统计量？


    //练习9.3-5
    /*
     median_value = CAL_MEDIAN_VALUE(...)
     median_q = median_value对应的下标
     按median_q划分，即二分

     if 当前划分 == 顺序统计量i: 返回
     else if 顺序统计量i在低区: 低区递归
     else: 高区递归
     */


    //练习9.3-6 "k 分位数"（区间有序）
    // [0, n/k) <= [n/k, 2n/k) <= ...
    /*
     multi_select(int[] A, int k) {
        if A==null || A.length==0 || A.length==1: return empty list
        assert 0 < k <= A.length

        result = new list
        multi_select(A, 0, A.length, k ,result)

        return result
     }
     void multi_select(int[] A, int begin, int end, int k, List result) {
         if k == 1: return;

         int n = end - begin;

         int md = k/2;
         int x = select(A, begin, end, md * n/k);
         result.add(md-1, x);

         int x_q = 0;
         for(int l=end-1; l>=begin; l++) {
            if(A[l] == x) {
                x_q = l;
                break;
            }
         }
         int q = partition(A, begin, end, x_q);

         multi_select(A, begin, q, md, result);
         multi_select(A, q + 1, end, k%2==0 ? md : md+1, result);
     }
     */


    //练习9.3-7
    /*
    class Node {
        int distance;
        int value;
    }
    multi_select_k(int[] A, int begin, int end, int k):
        int n = end-begin;
        assert k <= n;

        int x = select(A, begin, end, (n+1)/2); //求中位数

        Node[] distances = new Node[n]; //distance数组
        for i from 0 to n-1:
            distance = | A[i]-x |;
            distances[i] = new Node {distance, A[i]};

        int q_v = select(distances, 0, distances.length, k+1);

        int x_q = 0;
        for(int l=end-1; l>=begin; l++) {
           if(A[l] == q_v) {
               x_q = l;
               break;
           }
        }
        int q = partition(A, begin, end, x_q)

        for j from 0 to k:
            print(distances[j].value)

     */


    //练习9.3-8
    /*
    int two_seq_median(int[] x, int[] y) {
        return two_seq_median(x, 0, x.length, y, 0, y.length);    //there is need for x.length == y.length
    }
    int two_seq_median(int[] x, int x_begin, int x_end, int[] y, int y_begin, int y_end) {
        int x_n = x_end - x_begin;
        int y_n = y_end - y_begin;
        assert x_n > 0 && y_n > 0;

        int y_q = y_begin + (y_n+1)/2 - 1;
        int y_median = y[y_q];

        int x_q = x_begin + (x_n+1)/2 - 1;
        int x_median = x[x_q];

        if(y_median == x_median) {
            return x_median;
        } else if(x_median < y_median) {

            if(odd(x_n) && odd(y_n)) {
                if( out_of_range(y_q-1, y_begin, y_end) || x_median >= y[y_q-1] ) {
                    return x_median;
                }
            } else {
                if( out_of_range(x_q+1, x_begin, x_end) || y_median <= x[x_q+1] ) {
                    return y_median;
                }
            }

            return two_seq_median(x, x_q+1, x_end, y, y_begin, y_q);

        } else {
            if(odd(x_n) && odd(y_n)) {
                if( out_of_range(x_q - 1, x_begin, x_end) || y_median >= x[x_q-1] ) {
                    return y_median;
                }
            } else {
                if( out_of_range(y_q+1, y_begin, y_end) || x_median <= y[y_q+1] ) {
                    return x_median;
                }
            }

            return two_seq_median(x, x_begin, x_q, y, y_q+1, y_end);

        }

    }
    boolean odd(int n) {
        return n%2 != 0;
    }
    boolean out_of_range(int i, int begin, int end) {
        return i<begin || i>=end;
    }
    */


    //练习9.3-9: 实际就是一个求中位数的问题
    //  当 n 为奇数时，求第 (n+1)/2 个顺序统计量
    //  当 n 为偶数时，求第 ⌊(n+1)/2⌋ 个顺序统计量和第 ⌊(n+2)/2⌋ 个顺序统计量
    //证明:
    //一口油井：d = 0
    //两口油井：d = d1 + d2; 取两口油井之间任意位置
    //三口油井：d = d1 + d2 + d3; 取最中间位置油井
    //          加法结合率
    //          = (d1 + d2) + d3
    //          = d1 + (d2 + d3)
    //          = (d1 + d3) + d2
    //四口油井：d = d1 + d2 + d3 + d4; 取最中间两口油井之间的任意位置
    //          加法结合律
    //          = (d1 + d2) + (d3 + d4)
    //          = (d1 + d3) + (d2 + d4)
    //          = (d1 + d4) + (d2 + d3)

}
