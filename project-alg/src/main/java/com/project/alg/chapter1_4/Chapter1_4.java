package com.project.alg.chapter1_4;

import lombok.Getter;
import lombok.Setter;

public class Chapter1_4 {

    /**
     *分治策略思想
     *  分解: 原问题划分成若干子问题，子问题的形式和原问题相同只是规模更小
     *  解决: 递归的求解子问题，当子问题的规模足够小，停止递归，直接求解
     *  合并: 子问题的解合并成原问题的解
     */

    /**
     *递归式
     * T(n) = {
     *     O(1) , n=1
     *     k*T(n/m)+f(n) , n>1
     * }
     */


    /**
     *最大子数组: 所有连续子数组中和最大的
     * 1. 连续子数组的数量 n + (n-1) + ... + 1 = n*(n+1)/2
     * 2. 暴力解法
     *    遍历每一个连续子数组，利用先前计算的子数组和计算当前子数组的和，O(n^2)
     * 3. 分治法
     *  1).最大连续子数组的左，右相邻元素一定<=0
     *  2).最大连续子数组的左，右项分别相加一定<=0
     *  3).merge策略
     *    left(max), right(max), left(max) + left(all) + right(m)
     *    left(max) + left(all) + right(all) + right(max)
     *    left(m) + right(m), left(m) + right(all) + right(max)
     * 4. 分治法的递归式，递归树求解O，递归式推出O，todo
     */
    //暴力法
    public static SubArrayNode blMaxSubArray(int a[]) {
        if(a == null) return new SubArrayNode();
        if(a.length == 0 || a.length == 1) return new SubArrayNode();

        SubArrayNode result = new SubArrayNode();
        for(int i=0; i<a.length; i++) {
            int cur_sum = 0;
            for(int j=i; j<a.length; j++) {
                cur_sum = cur_sum + a[j];
                System.out.println("cur_sum: " + cur_sum + ";i=" + i + ",j=" + j);
                if(cur_sum > result.getSum()) {
                    result.setSum(cur_sum);
                    result.setI(i);
                    result.setJ(j);
                }
            }
        }
        return result;
    }
    //分治法
    public static SubArrayNode fzMaxSubArray(int a[]) {
        if(a == null) return new SubArrayNode();
        if(a.length == 0 || a.length == 1) return new SubArrayNode();

        return fzMaxSubArray(a, 0, a.length);
    }
    public static SubArrayNode fzMaxSubArray(int a[], int begin, int end) {

        int n = end - begin;
        if(n == 1) return new SubArrayNode(a[begin], begin, end);

        SubArrayNode left_max = fzMaxSubArray(a, begin, begin + n / 2);
        SubArrayNode right_max = fzMaxSubArray(a, begin + n / 2, end);

        SubArrayNode max = merge(left_max, right_max, a, begin, begin + n/2, end);
        return max;
    }
    public static SubArrayNode merge(SubArrayNode left_max, SubArrayNode right_max, int a[], int p, int q, int r) {
        SubArrayNode max = left_max;
        if(right_max.getSum() > left_max.getSum()) {
            max = right_max;
        }

        SubArrayNode left_all = new SubArrayNode(0, left_max.getJ(), q);
        SubArrayNode left_m = new SubArrayNode(left_max.getJ()==q?0:a[q-1],left_max.getJ()==q?q:q-1, q);
        for(int i=q-1; i>=left_max.getJ(); i--) {
            int cur = left_all.getSum() + a[i];
            if(cur > left_m.getSum()) {
                left_m.setSum(cur);
                left_m.setI(i);
            }
            left_all.setSum(cur);
        }

        SubArrayNode right_all = new SubArrayNode(0, q, right_max.getI());
        SubArrayNode right_m = new SubArrayNode(right_max.getI()==q?0:a[q], q, right_max.getI()==q?q:q+1);
        for(int j=q; j<right_max.getI(); j++) {
            int cur = right_all.getSum() + a[j];
            if(cur > right_m.getSum()) {
                right_m.setSum(cur);
                right_m.setJ(j+1);
            }
            right_all.setSum(cur);
        }

        if(left_max.getSum() + left_all.getSum() + right_m.getSum() > max.getSum()) {
            max = new SubArrayNode(left_max.getSum() + left_all.getSum() + right_m.getSum(), left_max.getI(), right_m.getJ());
        }
        if(left_max.getSum() + left_all.getSum() + right_all.getSum() + right_max.getSum() > max.getSum()) {
            max = new SubArrayNode(left_max.getSum() + left_all.getSum() + right_all.getSum() + right_max.getSum(), left_max.getI(), right_max.getJ());
        }
        if( (left_m.getSum() != 0 || right_m.getSum() != 0) && (left_m.getSum() + right_m.getSum() > max.getSum()) ) {
            max = new SubArrayNode(left_m.getSum() + right_m.getSum(), left_m.getI(), right_m.getJ());
        }
        if(left_m.getSum() + right_all.getSum() + right_max.getSum() > max.getSum()) {
            max = new SubArrayNode(left_m.getSum() + right_all.getSum() + right_max.getSum(), left_m.getI(), right_max.getJ());
        }

        return max;
    }


    public static void main(String argv[]) {

        int[] a = {13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7};
        int[] a2 = {-1, -2, -3, -1};
        int[] a3 = {1, -2, -3, 1};
        System.out.println(blMaxSubArray(a));

        //System.out.println(fzMaxSubArray(a));
        System.out.println(fzMaxSubArray(a2));
        System.out.println(fzMaxSubArray(a3));

    }

    @Getter
    @Setter
    private static class SubArrayNode {
        private int sum;
        private int i;
        private int j;
        public SubArrayNode() {}
        public SubArrayNode(int sum, int i, int j) {
            this.sum = sum;
            this.i = i;
            this.j = j;
        }
        @Override
        public String toString() {
            return "SubArrayNode{" +
                    "sum=" + sum +
                    ", i=" + i +
                    ", j=" + j +
                    '}';
        }
    }
}
