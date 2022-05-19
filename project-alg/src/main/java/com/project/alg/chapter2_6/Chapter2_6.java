package com.project.alg.chapter2_6;

import java.util.Arrays;

public class Chapter2_6 {

    /**
     *(二叉)堆
     *  使用数组表示的二叉堆可以看成是一棵近似完全二叉树(除了最底层外，该树是完全充满的,最底层从左往右填充)
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
     *  3.A.length，堆中元素个数(数组大小)
     *    heap_size: heap_size<=A.length
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
                max_heapify(i, n);
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
        private void max_heapify(int i, int heap_size) { //heap_size必须<=this.a.length,否则IndexOutOfRangeError

            int li = 2*(i+1)-1;
            int ri = 2*(i+1);

            int max = i;
            if(!isOutRange(li, heap_size) && /*li<this.a.length &&*/ this.a[i] < this.a[li]) {
                max = li;
            }
            if(!isOutRange(ri, heap_size) && /*ri<this.a.length &&*/ this.a[max] < this.a[ri]) {
                max = ri;
            }

            if(max != i) {
                int ex = this.a[max];
                this.a[max] = this.a[i];
                this.a[i] = ex;
                max_heapify(max, heap_size);
            }

        }
        private boolean isLeaf(int i) {
            return i>=this.a.length/2;
        }
        private boolean isOutRange(int i, int heap_size) {
            return i>=heap_size;
        }


        public void heap_sort_1() {

            for(int i=this.a.length-1; i>0; i--) {

                int pi = (i+1)/2 - 1;
                int min_i = i;
                for(int j=i-1; j>pi; j--) {
                    if(this.a[j] < this.a[min_i]) {
                        min_i = j;
                    }
                }

                if(min_i != i) {
                    int ex = this.a[min_i];
                    this.a[min_i] = this.a[i];
                    this.a[i] = ex;

                    for(int ex_pi = (min_i+1)/2 - 1; ex_pi>0 && this.a[ex_pi] < this.a[min_i]; ) {
                        ex = this.a[ex_pi];
                        this.a[ex_pi] = this.a[min_i];
                        this.a[min_i] = ex;

                        min_i = ex_pi;
                        ex_pi = (min_i+1)/2 - 1;
                    }
                }
            }
        }

        /*
        heap_size:  2       C1+max_heapify(0, 2)    ; C1为每次while执行时间之和
                    3       C1+max_heapify(0, 3)
                    4       C1+max_heapify(0, 4)
                  ...
                    n       C1+max_heapify(0, n)

                 max_heapify(0, i) = C2*log2(i)   ; C2为max_heapify执行依次时间之和

         最坏情况运行时间: C1*(n-1) + C2*[log2(2) + log2(3) + ... + log2(n)]
                      = C1*(n-1) + C2*[n*ln(n)/ln2 - (n+2ln2-2)/ln2]
                      ≈ C*n*lnn
         */
        public void heap_sort() {

            int heap_size = this.a.length;
            while(heap_size > 1) {
                heap_size--;

                int ex = this.a[0];
                this.a[0] = this.a[heap_size];
                this.a[heap_size] = ex;

                max_heapify(0, heap_size);
            }

        }

        @Override
        public String toString() {
            return "MaxHeap{" +
                    "a=" + Arrays.toString(a) +
                    '}';
        }
    }

    public static void main(String argv[]) {
        int[] a = {1000, 90000, 7, 3, 6, 2, 1, 4, 5, 1, 100, 1000, 0, -1 ,9000000};
        MaxHeap maxHeap = new MaxHeap(a);
        System.out.println(maxHeap);
        maxHeap.heap_sort_1();
        System.out.println(maxHeap);

        int[] b = {1000, 90000, 7, 3, 6, 2, 1, 4, 5, 1, 100, 1000, 0, -1 ,9000000};
        MaxHeap maxHeap2 = new MaxHeap(b);
        System.out.println(maxHeap2);
        maxHeap2.heap_sort();
        System.out.println(maxHeap2);
    }

}
