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
     *     1).最大连续子数组的左，右相邻元素一定<=0
     *     2).最大连续子数组的左，右项分别相加一定<=0
     *     3).merge策略
     *        left(max), right(max), left(max) + left(all) + right(m)
     *        left(max) + left(all) + right(all) + right(max)
     *        left(m) + right(m), left(m) + right(all) + right(max)
     *  4. 分治法的递归式，递归树求解O，递归式推出O
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

        SubArrayNode blSub = blMaxSubArray(a);
        IntArrayTraversal.of(a).forEach(ArraySubArrayPosPrinter.of(blSub)::print);
        System.out.println("#############################################");

        int[] a2 = {-1, -2, -3, -1};
        int[] a3 = {1, -2, -3, 1};
        //System.out.println(fzMaxSubArray(a));
        System.out.println(fzMaxSubArray(a2));
        System.out.println(fzMaxSubArray(a3));
        System.out.println("#############################################");

        IntArrayTraversal.of(a).forEach(ArraySubArrayPosPrinter.of(new SubArrayNode(-1, 2, 7))::print);
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

    static class ArraySubArrayPosPrinter<E> extends ArrayPrinter<E> {
        private final SubArrayNode subArrayNode;
        private int weight = 0;
        private int weightAccum = 0;

        ArraySubArrayPosPrinter(SubArrayNode subArrayNode) {
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
                System.out.print("🚩");
            }
            int weightAccumAdjust = subArrayNode.i >= 0 ? weightAccum-1 : weightAccum;
            for (int i = 0; i < weightAccumAdjust; i++) {
                System.out.print(" ");
            }
            if(subArrayNode.j >= 0 && subArrayNode.j != subArrayNode.i) {
                System.out.print("🏴");
            }
            System.out.println(", " + subArrayNode);
        }

        static <E> ArraySubArrayPosPrinter<E> of(SubArrayNode subArrayNode) {
            return new ArraySubArrayPosPrinter<>(subArrayNode);
        }
    }
}
