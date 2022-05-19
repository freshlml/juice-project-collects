package com.project.alg.chapter2_6;

public class Chapter2_6_4 {

    //思考题6-1
    //a: 不一定一样
    /* 如
    4
   5  6
     */
    //b: lg1 + lg2 + lg3 + ... + lg(n-1)
    // = Σ(1,n-1)lgx
    // <= ∫(1,n) lgx dx
    // = x*lnx - x + C |(1,n)
    // = n*lnn - n + 1


    //思考题6-2
    //d叉堆
    /*a:
                    [0]
            [1] [2] [3] [4] [...] [d]
        [d*1+1]               [d*d+1] ... [d*d+d]
    [d*(d+1)+1] ...
      ...
      最左left(i) = d*i+1
      最右right(i) = d*i+d
      parent(i) = (i+d-1)/2 - 1
    */
    /*b:
    n个元素的d叉堆，设高度为h
        d^0 + d^1 + d^2 + ... + d^(h-1) + s(h) = n, s(h)max = d^h, s(h)min=1
        ===>   (d^h-1)/(d-1) + 1 <= n <= (d^(h+1)-1)/(d-1)
        ===>   d^h/(d-1) <= (d^h+d-2)/(d-1) <= n <= (d(h+1)-1)/(d-1) < d^(h+1)/(d-1)
        ===>   d>=2:      d^h <= n*(d-1) < d^(h+1)
        ===>               h <=  logd(n*(d-1)) < h+1
        ===>          h = 下界(logd(n*(d-1)))
     */
    //c: d*logd(n*(d-1))
    //d: logd(d-1) + logd(2*(d-1)) + logd(3*(d-1)) + ... + logd((n-1)*(d-1))
    //   = Σ(1,n-1) logd(x*(d-1))
    //   <= ∫(1,n) logd(x*(d-1))) dx
    //   ≈ n*ln[(d-1)n]/lnd
    //e: logd(n*(d-1)) ~ d*logd(n*(d-1))





}
