package com.juice.alg.part1.chapter4;

import lombok.Getter;
import lombok.Setter;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;
import com.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;

public class Chapter4_1 {
    /**
     *最大子数组: 所有连续子数组中和最大的子数组
     *  1. 连续子数组的数量
     *     n + (n-1) + ... + 1 = n*(n+1)/2
     *  2. 暴力解法
     *     遍历每一个连续子数组，利用先前计算的子数组和计算当前子数组的和，O(n^2)
     *  3. 分治法
     *     1). left_gap、right_gap 的和必定 < 0
     *     2). merge策略
     *        left_max, right_max, left_max + left_gap_sum + right_gap_max
     *        left_max + left_gap_sum + right_gap_sum + right_max
     *        left_gap_max + right_gap_max, left_gap_max + right_gap_sum + right_max
     *  4. 分治法的递归式
     *     T(n) = 2*T(n/2) + Θ(n)
     *     解得 T(n) = Θ(n*lgn)
     */
    //暴力法
    public static SubArrayNode blMaxSubArray(int[] a) {
        if(a == null || a.length == 0) return new SubArrayNode();

        int i = 0;
        SubArrayNode result = new SubArrayNode(a[0], i, i);
        for(; i < a.length; i++) {
            int accum = 0;
            for(int j=i; j < a.length; j++) {
                accum += a[j];
                if(result.sum < accum) {
                    result.sum = accum;
                    result.i = i;
                    result.j = j;
                }
            }
        }
        return result;
    }
    //分治法
    public static SubArrayNode fzMaxSubArray(int[] a) {
        if(a == null || a.length == 0) return new SubArrayNode();

        return fzMaxSubArray(a, 0, a.length);
    }
    public static SubArrayNode fzMaxSubArray(int[] a, int begin, int end) {

        int n = end - begin;
        if(n == 1) return new SubArrayNode(a[begin], begin, end);

        int mi = begin + n/2;
        SubArrayNode left_max  = fzMaxSubArray(a, begin, mi);
        SubArrayNode right_max = fzMaxSubArray(a, mi, end);

        SubArrayNode max = merge(left_max, right_max, a, begin, mi, end);
        return max;
    }
    static SubArrayNode merge(SubArrayNode left_max, SubArrayNode right_max, int[] a, int p, int q, int r) {
        int sum = left_max.getSum();
        int sum_i = left_max.getI();
        int sum_j = left_max.getJ();

        if(right_max.getSum() > sum) {
            sum = right_max.getSum();
            sum_i = right_max.getI();
            sum_j = right_max.getJ();
        }

        SubArrayNode left_gap_sum = new SubArrayNode(0, left_max.getJ(), q);
        SubArrayNode left_gap_max = new SubArrayNode(left_max.getJ()==q ? 0 : a[q-1], left_max.getJ()==q ? q : q-1, q);
        for(int i=q-1; i >= left_max.getJ(); i--) {
            int cur = left_gap_sum.getSum() + a[i];
            if(cur > left_gap_max.getSum()) {
                left_gap_max.setSum(cur);
                left_gap_max.setI(i);
            }
            left_gap_sum.setSum(cur);
        }

        SubArrayNode right_gap_sum = new SubArrayNode(0, q, right_max.getI());
        SubArrayNode right_gap_max = new SubArrayNode(right_max.getI()==q ? 0 : a[q], q, right_max.getI()==q ? q : q+1);
        for(int j=q; j < right_max.getI(); j++) {
            int cur = right_gap_sum.getSum() + a[j];
            if(cur > right_gap_max.getSum()) {
                right_gap_max.setSum(cur);
                right_gap_max.setJ(j+1);
            }
            right_gap_sum.setSum(cur);
        }

        int m;
        if((m = left_max.getSum() + left_gap_sum.getSum() + right_gap_max.getSum()) > sum) {
            sum = m;
            sum_i = left_max.getI();
            sum_j = right_gap_max.getJ();
        }
        if((m = left_max.getSum() + left_gap_sum.getSum() + right_gap_sum.getSum() + right_max.getSum()) > sum) {
            sum = m;
            sum_i = left_max.getI();
            sum_j = right_max.getJ();
        }
        if((left_gap_max.getSum() != 0 || right_gap_max.getSum() != 0) && ((m = left_gap_max.getSum() + right_gap_max.getSum()) > sum)) {
            sum = m;
            sum_i = left_gap_max.getI();
            sum_j = right_gap_max.getJ();
        }
        if((m = left_gap_max.getSum() + right_gap_sum.getSum() + right_max.getSum()) > sum) {
            sum = m;
            sum_i = left_gap_max.getI();
            sum_j = right_max.getJ();
        }

        return new SubArrayNode(sum, sum_i, sum_j);
    }


    public static void main(String[] argv) {
        int[] a = {13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7};

        SubArrayNode blSub = blMaxSubArray(a);
        IntArrayTraversal.of(a).forEach(SubArrayNodeArrayPrinter.of(blSub)::print);
        System.out.println("##########################################################################################");

        //a = new int[] {-13, -3, -25, -1, -1, -5};
        SubArrayNode fzSub = fzMaxSubArray(a);
        IntArrayTraversal.of(a).forEach(SubArrayNodeArrayPrinter.of(fzSub)::print);
        System.out.println("##########################################################################################");

        IntArrayTraversal.of(a).forEach(SubArrayNodeArrayPrinter.of(new SubArrayNode(-1, a.length, 7000))::print);
    }

    @Getter
    @Setter
    static class SubArrayNode {
        int sum;
        int i;
        int j;
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

    static class SubArrayNodeArrayPrinter<E> extends ArrayPrinter<E> {
        private final SubArrayNode subArrayNode;
        private int weight = 0;
        private int weightAccum = 0;

        private SubArrayNodeArrayPrinter(SubArrayNode subArrayNode) {
            super();
            this.subArrayNode = subArrayNode;
        }

        @Override
        protected void printElement(E t) {
            System.out.print(t);
            String s = String.valueOf(t);
            if(count <= subArrayNode.i) {
                weight += s.length();  //may overflow
            } else if(count <= subArrayNode.j) { //assume subArrayNode.j >= subArrayNode.i
                weightAccum += s.length();  //may overflow
            }
        }

        @Override
        protected void printSep() {
            super.printSep();
            if(count <= subArrayNode.i) {
                weight += SEP.length();  //may overflow
            } else if(count <= subArrayNode.j) { //assume subArrayNode.j >= subArrayNode.i
                weightAccum += SEP.length();  //may overflow
            }
        }

        @Override
        protected void printEnd() {
            System.out.println();
            for (int i = 0; i < weight; i++) {
                System.out.print(" ");
            }
            if(subArrayNode.i >= 0) {
                System.out.print("€");
            }
            int weightAccumAdjust = subArrayNode.i >= 0 ? weightAccum-1 : weightAccum;
            for (int i = 0; i < weightAccumAdjust; i++) {
                System.out.print(" ");
            }
            if(subArrayNode.j >= 0 && subArrayNode.j != subArrayNode.i) {
                System.out.print("¶");
            }
            System.out.println(", " + subArrayNode);
        }

        static <E> SubArrayNodeArrayPrinter<E> of(SubArrayNode subArrayNode) {
            return new SubArrayNodeArrayPrinter<>(subArrayNode);
        }
    }
}
