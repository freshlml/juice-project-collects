package com.juice.alg.chapter3_11;

public class Chapter3_11_4 {

    //开放寻址法
    /*
    线性探查
      1. h(key, i) = ( hash(key) + i ) mod m , i = 0, 1, ..., m-1
        1.1: next = ( prev + 1 ) mod m
      2. 共m种探查序列, 每一种探查序列都不重复的遍历整个桶下标
      3. 探查序列重合度高，画图示例

    二次探查
      1. h(key, i) = ( hash(key) + c1*i + c2*i^2 ) mod m , i = 0, 1, ..., m-1
        1.1: next = ( prev + (2*c2*i+c1+c2) mod m ) mod m
      2. 共m种探查序列, 如何选择c1, c2？使得探查序列充分利用m个位置
        思考题11-3: 令 next = ( prev + i mod m ) mod m ,c1=-1/2, c2=1/2
                   只需m=2^n, 即可让 探查序列不重复的遍历整个桶下标，证明如下
                   0  1    2      ...  k           ...   m-1
                   p  p+1  p+1+2  ...  p+1+2+...+k ...   p+1+2+...+(m-1)
                   只需证明, 对所有 k=1, 2, ..., m-1, p+1+2+...+k != all prev
                                                                  all prev + m
                                                                  all prev + 2m
                                                                  ...
                   即，对所有 k=1, 2, ..., m-1，k != m, 2m, ...
                                             (k-1)+k != m, 2m, ...
                                             1+...+(k-1) != m, 2m, ...
                   例如m=5
                      k=4                       k=3                  k=2             k=1
                      4 != m                    3 != m               2 != m          1 != m
                      3 + 4 != m                2 + 3 != m           1 + 2 != m
                      2 + 3 + 4 != m            1 + 2 + 3 != m
                      1 + 2 + 3 + 4 != m
                   ===> m 可取 2^n
      3. 降低了探查序列的重合度，如果起点相同，探查序列相同

    双重散列探查
      1. h(key, i) = ( hash1(key) + i*hash2(key) ) mod m , i = 0, 1, ..., m-1
        1.1:  h(key, i+1) = ( hash1(key) + (i+1)*hash2(key) ) mod m
                          = [ h(key, i) + hash2(key) mod m ] mod m
              即 next = [ prev + hash2(key) mod m ] mod m
        1.2:  如果要探查整个散列表，需要 hash2(key) 与 m 互为质数
              a: h(key, 0) = hash1(key) mod m
                 h(key, m) = ( hash1(key) + m*hash2(key) ) mod m
                           = ( hash1(key) mod m + m*hash2(key) mod m ) mod m
                           = ( h(key, 0) + m*hash2(key) mod m ) mod m
                           = h(key, 0)
                 即，h(key, m) == h(key, 0), h(key, m+1) == h(key, 1), ...
              b: 如果hash2(key) 与 m 互为质数, 则依次按 i=0, 1, ..., m-1 探查，将不重复的遍历整个桶下标
                 证明: 对任意i∈[0, m-1], 有h(key, i); 对任意k∈(0, m-1]，有h(key, i+k), 只需证明 h(key, i) != h(key, i+k)
                      h(key, i) = ( hash1(key) + i*hash2(key) ) mod m = a, 0<= a <=m-1
                      h(key, i+k) = ( hash1(key) + (i+k)*hash2(key) ) mod m
                                  = [ a + (k * hash2(key)) mod m ] mod m
                      即，只需 (k*hash2(key)) mod m != 0 即可 ==>
                         对任意k∈(0, m-1] (k * hash2(key)) mod m != 0 ==> hash2(key) 与 m互质
         1.3: 通常为了满足1.2, 取m为质数，让 hash2(key) 的值小于m,如取 1 + (key mod (m-1))
       2. 共m^2种探查序列, 每一种探查序列都不重复的遍历整个桶下标
       3. 重合度低，如果起点相同，探查序列也不一定相同

    todo, 开放寻址分析
     */




}
