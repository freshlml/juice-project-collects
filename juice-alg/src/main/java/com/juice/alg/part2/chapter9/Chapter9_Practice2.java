package com.juice.alg.part2.chapter9;

public class Chapter9_Practice2 {

    //思考题9-1
    //a: 1.排序; 2.遍历n-1 to n-i. T(n) = O(n*lgn) + O(n-i)
    //b: 1.创建最大优先队列，并将 n 个元素插入; 2.萃取 i 次. T(n) = O(lg1 + ... + lgn) + O(lgn + ... + lg(n-i))
    //c:
    //  int x = select(a, n-i);  //O(n)
    //  range_partition(a, x);   //O(n)
    //  对a[n-i, n)排序;          //O(i*lgi)


    //思考题9-2: 带权中位数
    //c:
    /*
    class Node {
        int v;
        float w;  //改成精确的小数？
    }
    Node select_w(Node[] a, int w, int w_l, int w_r) {
        if a == null || a.length == 0: return null
        if a.length == 1: return a[0]

        assert w == 1
        assert w_l + w_r == w
        assert w_l == 0.5 and w_r == 0.5
        assert for per a[i].w ∈ [0, w)

        return select_w(a, 0, a.length, w, w_l, w_r)
    }
    Node select_w(Node[] a, int begin, int end, int w, int w_l, int w_r) {
        int n = end - begin

        if n<=1: return a[begin]

        int x = select(a, begin, end, (n+1)/2); //a.v
        int q = partition(a, begin, end, x); //a.v

        int q_w = a[q].w
        int l_w_sum = 0
        for(int i=begin; i<q; i++) {
            l_w_sum += a[i].w
        }
        int r_w_sum = w - l_sum - q_w

        if(l_w_sum < w_l && r_w_sum <= w_r) {
            return a[q]
        } else if(r_w_sum > w_r) {
            return select_w(a, q+1, end, r_w_sum, w_l - l_w_sum - q_w, w_r)
        } else {
            return select_w(a, begin, q, l_w_sum, w_l, w_r - r_w_sum - q_w)
        }

    }
     */

    //d. 带权中位数是一维邮局问题得解法
    //一个点：w1 = 1，选择p1
    //两个点：若 w1 >= 1/2, w2 < 1/2，则选择p1
    //      若 w1 < 1/2, w2 > 1/2，选择p2
    //三个点：若 w1 >= 1/2, w2 + w3 <= 1/2，则选择p1
    //      若 w1 < 1/2, w1 + w2 >= 1/2, w3 <= 1/2，则选择p2
    //      若 w1 < 1/2, w1 + w2 < 1/2, w3 > 1/2，则选择p3
    //四个点：若 w1 >= 1/2, w2 + w3 + w4 <= 1/2，则选择p1
    //      若 w1 < 1/2, w1 + w2 >= 1/2, w3 + w4 <= 1/2，则选择p2
    //      若 w1 < 1/2, w1 + w2 < 1/2, w1 + w2 + w3 >= 1/2, w4 <= 1/2，则选择p3
    //      若 w1 < 1/2, w1 + w2 < 1/2, w1 + w2 + w3 < 1/2, w4 > 1/2，则选择p4
    //...
    /*n个点: { //p一定在n个点的范围之间
        w1 < 1/2, w2 + w3 + ... + wn >= 1/2; w1 + w2 >= 1/2, w3 + w4 + ... + wn <= 1/2: 则选择p2
        ...
     }
    */

    //e: 按x坐标求带权中位数Pi, 取Pi的x坐标xi 和 直线X=xi
    //   按y坐标求带权中位数Pj, 取Pj的y坐标yj 和 直线Y=yi
    //   则最终的坐标为P(xi, yj) (也即X=xi, Y=yi两直线的交点)
    //分析过程:
    //1. P位于n个点围成的矩形区域
    //2. 草稿纸画图解法


    //思考题9-3
    /*
    int small_select(int[] A, int begin, int end, int i) {
        int n = end - begin;
        if(i >= n/2) return select(A, begin, end, i);

        int[] N_A = 两个元素为一组，取每组中较小的元素
        return small_select(N_A, 0, N_A.length, i);
    }
     */


    //思考题9-4
    /*
     * 随机变量 Xi: 求第 i 个顺序统计量的总比较次数
     *
     * Xi = Σ(l=0~n-2)Σ(j=l+1~n-1) Xi<l,j>,  Xi<l,j> 表示 l 与 j 比较次数 = 0
     *                                                                  = 1
     * i < j (i < l < j):   P(Xi<l,j> = 1) = 2/(j-i+1)
     * l <= i <= j:         P(Xi<l,j> = 1) = 2/(j-l+1)
     * j < i (l < j < i):   P(Xi<l,j> = 1) = 2/(i-l+1)
     *
     * EXi = E[ Σ(l=0~n-2)Σ(j=l+1~n-1) Xi<l,j> ]
     *     = Σ(l=0~n-2)Σ(j=l+1~n-1) EXi<l,j>
     *     = Σ(l=0~n-2)Σ(j=l+1~n-1) P(Xi<l,j> = 1)
     *    <= Σ(l=i+1~j-1)Σ(j=l+1~n-1) 2/(j-i+1) + Σ(l=0~i)Σ(j=i+1~n-1) 2/(j-l+1) + Σ(l=0~j-1)Σ(j=l+1~i-1) 2/(i-l+1)
     *    <= Σ(j=l+1~n-1) 2(j-i-1)/(j-i+1) + Σ(l=0~i)Σ(j=i+1~n-1) 2/(j-l+1) + Σ(l=0~j-1) 2(i-l-1)/(i-l+1)
     *     < 2 * [ Σ(j=i+1~n-1) (j-i-1)/(j-i+1) + Σ(l=0~i)Σ(j=i+1~n-1) 1/(j-l+1) + Σ(l=0~i-1) (i-l-1)/(i-l+1) ]
     *     < 4n
     */

}
