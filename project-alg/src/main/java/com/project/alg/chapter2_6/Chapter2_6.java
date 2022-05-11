package com.project.alg.chapter2_6;

public class Chapter2_6 {

    /**
     *(二叉)堆
     *  使用数组表示的二叉堆可以看成是一棵近似完全二叉树(除了最底层外，该树是完全充满的)
     *     0,  1,  2,  3, 4, 5, 6, 7, 8, 9
     *   A[16  14  10  8  7  9  3  2  4  1]
     *                       16[0]                           第1层    高度为0
     *                 /               \
     *              14[1]              10[2]                 第2层    高度为1
     *            /        \         /       \
     *         8[3]        7[4]    9[5]       3[6]           第3层    高度为2
     *       /     \       /
     *    2[7]     4[8]  1[9]                                第4层    高度为3
     *
     *  1.对数组从下标0开始的遍历即是对二叉树的广度优先遍历
     *    1).每一层的最左节点的下标: i(x) = 2^0 + 2^1 + ... + 2^(x-2)
     *    2).叶子节点的下标: 下界(n/2), 下界(n/2)+1, ... , n-1
     *  2.下标i的节点，p(i) = 下界((i+1)/2)-1; left(i) = 2*(i+1)-1; right(i) = 2*(i+1)
     *
     *  节点高度:  节点到叶节点的最长简单路径上边的数目
     *  树的高度:  根节点的高度
     *  推论: 树的高度=最大层数-1
     *
     *  堆高度(二叉树高度): 设堆高度为h
     *          2^0 + 2^1 + ... + 2^(h-1) + S(h) = n, S(h)max = 2^h, S(h)min = 1
     *          ===>  2^h =< n <= 2^(h+1)-1
     *          ===>  log2(n+1)-1 =< h <= log2(n)
     *          ===>  2^h =< n <= 2^(h+1)-1 < 2^(h+1) ==> h<=log2(n)<h+1 ==> h = 下界(log2(n))
     *
     */
    /**
     *最大堆
     * 除了根节点，所有节点都要满足A[i] <= A[p(i)]
     * 1.根节点最大值
     * 2.任一子树的所有节点都不大于子树的根节点
     *
     *最小堆
     * 除了根节点，所有节点都要满足A[i] >= A[p(i)]
     * 1.根节点最小值
     * 2.任一字数的所有节点都不小于子树的根节点
     */

    //练习6.1-4
    //位于叶子节点

    //最大堆
    static class MaxHeap {
        private int[] a;

        /**
         *构造
         * @param a  数组
         */
        public MaxHeap(int[] a) {
            //assert a != null
            this.a = a;
            int n = a.length;
            //从最后一个非叶子节点到第一个节点
            for(int i=n/2-1; i>=0; i--) {
                max_heapify(i);
            }
        }

        /*
        根节点运行max_heapify的最坏情况运行时间(时间复杂度)
                  [n, 0]
                  [n/2 ,1/2]                第一次分解 n/2         分解+merge消耗: O(1)
                   ...
                  [1]                       第x次分解 n/2^x
            ===>    x = log2(n)
        树高h，最多递归到叶子节点，即最多递归h次，T(n) = log2(n) * O(1) = O(log2(n))
         */
        private void max_heapify(int i) {
            if(isLeaf(i)) return;

            int li = 2*(i+1)-1;
            int l = this.a[li];
            int ri = 2*(i+1);

            if(this.a[i] >= l && (isOutRange(ri) || this.a[i] >= this.a[ri])) return;

            int pos = li;
            int max = l;
            if(!isOutRange(ri) && this.a[ri] > max) {
                pos = ri;
                max = this.a[ri];
            }

            this.a[pos] = this.a[i];
            this.a[i] = max;

            max_heapify(pos);
        }
        private boolean isLeaf(int i) {
            return i>=this.a.length/2;
        }
        private boolean isOutRange(int i) {
            return i>=this.a.length;
        }


    }




}
