package com.juice.alg.part3.chapter11;

public class Chapter11_3 {

    /**
     *hash 函数（散列函数）
     *
     *  确定性: hash(key) 始终返回相同的结果
     *  相等性: hash(key1) == hash(key2) where key1 == key2 (optional)
     *
     *  设计指标: hash 函数需要尽量满足对 n 个元素中每一个元素等可能(或近似等可能)的选择 m 个槽中的任意一个槽
     *
     *    1. 如果 n 个元素的 key 独立均匀的分布于 [0, 1) 中，那么 hash 函数可以是: `key * m`, m 为桶大小
     *
     *    2. 假设 key 满足等可能取自全域 U 中任意一个值（或者, {0, 10, ...}, {1, 11, ...}... 这些集合的可能性之和相等），如下所示:
     *
     *      Table: 0    1    2    3    4    5    6    7    8    9
     *            | |  | |  | |  | |  | |  | |  | |  | |  | |  | |
     *      全域U:  {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
     *              10,11,...,
     *              ...}
     *      则，`key % m` 将等可能的落在"桶"的任意一个槽位上
     *
     *    3. 如果知道 key 满足全域 U 上的某分布，则通过 hash 函数调控使得 key 等可能的选择槽位
     */
    public static void main(String[] argv) {

    }
}
