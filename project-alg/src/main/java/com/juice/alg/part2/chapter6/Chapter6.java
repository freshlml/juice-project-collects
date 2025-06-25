package com.juice.alg.part2.chapter6;

import java.util.Arrays;
import com.juice.alg.part1.chapter2.Chapter2.ArrayPrinter;
import com.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;

public class Chapter6 {

    /**
     *chapter 6.1 and 6.2 and 6.3 and 6.4
     *
     *(二叉)堆
     *  使用数组表示的二叉堆可以看成是一棵近似的完全二叉树(除了最底层外，该树是完全充满的，最底层从左往右填充)
     *     0,  1,  2,  3, 4, 5, 6, 7, 8, 9
     *   A[16  14  10  8  7  9  3  2  4  1]
     *                       16[0]                    深度为 0       第 1 层    高度为 0
     *                 /               \
     *              14[1]              10[2]          深度为 1       第 2 层    高度为 1
     *            /        \         /       \
     *         8[3]        7[4]    9[5]       3[6]    深度为 2       第 3 层    高度为 2
     *       /     \       /
     *    2[7]     4[8]  1[9]                         深度为 3       第 4 层    高度为 3
     *
     *  1.对数组从下标 0 开始的遍历即是对二叉树的广度优先遍历
     *
     *  2.下标计算法
     *      1).每一层的最左节点的下标: i(x) = i(x-1) + 2^(x-2)   x 表示层数，x = 2, ..., h+1
     *      2).叶子节点的下标: ⌊(n/2)⌋, floor(n/2) + 1, ..., n-1                                               //练习6.1-7
     *         设第一个叶子节点的下标为 x, left(x) = 2x + 1, 2x + 1 = n + 1 (n 为偶数) 或者 2x + 1 = n (n 为奇数) ==> x = ⌊(n/2)⌋
     *      3).下标 i 的节点，parent(i) = ⌊(i+1)/2⌋ - 1; left_child(i) = 2*i + 1; right_child(i) = 2*i + 2
     *
     *  3.A.length: 堆中元素个数(数组大小)。heap_size: 堆大小，heap_size <= A.length
     *
     *  4.二叉堆的高度公式
     *      节点高度: 节点到叶节点的最长简单路径上"边"的数目
     *      树的高度: 根节点的高度
     *      推论: 树的高度 = 最大层数 - 1
     *           数的高度 = 最大深度
     *
     *      堆高度(二叉树高度): 设堆高度为 h，节点个数为 n
     *          n = 2^0 + 2^1 + ... + 2^(h-1) + S(h), S(h)'max = 2^h, S(h)'min = 1             //练习6.1-1
     *          可得: 2^0 + 2^1 + ... + 2^(h-1) + 1 <= n <= 2^0 + 2^1 + ... + 2^(h-1) + 2^h
     *          ===>  2^h <= n <= 2^(h+1) - 1 < 2^(h+1)
     *          ===>  lg(n)-1 < h <= lg(n)
     *          故, h = ⌊lg(n)⌋                          //练习6.1-2
     *
     *  5.最大堆
     *      除了根节点，所有节点都要满足 A[i] <= A[parent(i)]
     *      1.根节点最大值
     *      2.任一子树的所有节点都不大于子树的根节点          //练习6.1-3 使用数学归纳法证明
     *
     *  6.最小堆
     *      除了根节点，所有节点都要满足 A[i] >= A[parent(i)]
     *      1.根节点最小值
     *      2.任一子树的所有节点都不小于子树的根节点
     *
     */
    static class MaxHeap {  //最大堆
        private final int[] a;

        //构造最大堆
        MaxHeap(int[] a) {
            //assert a != null
            this.a = a;
            int n = a.length;
            //从最后一个非叶子节点到第一个节点
            for(int i=n/2-1; i>=0; i--) {
                max_heapify(i, n);
            }
        }
        /*
        根节点运行 max_heapify 的最坏情况运行时间
                  [n, 0]
                  [n/2 ,1/2]                第一次分解 n/2         分解+merge消耗: Θ(1)
                   ...
                  [1]                       第x次分解 n/2^x
            ===>    x = lg(n)
        树高 h，最多递归到叶子节点，即最多递归 h 次，T(n) = C * lg(n) * Θ(1) = Θ(lg(n))
         */
        private void max_heapify(int i, int heap_size) {
            //assert heap_size <= this.a.length : IndexOutOfRangeError
            int li = 2*i + 1;
            int ri = 2*i + 2;

            int max = i;
            if(li < heap_size && /*li < this.a.length &&*/ this.a[max] < this.a[li]) {
                max = li;
            }
            if(ri < heap_size && /*ri < this.a.length &&*/ this.a[max] < this.a[ri]) {
                max = ri;
            }

            if(max != i) {
                int ex = this.a[max];
                this.a[max] = this.a[i];
                this.a[i] = ex;
                max_heapify(max, heap_size);
            }
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

                 max_heapify(0, i) = C2*lg(i)   ; C2为max_heapify执行依次时间之和

         最坏情况运行时间: C1*(n-1) + C2*[lg(2) + lg(3) + ... + lg(n)]
                      = C1*(n-1) + C2*[n*ln(n)/ln2 - (n+2ln2-2)/ln2]
                      = Θ(n*lnn)
         */
        public void heap_sort() {  //原址

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

    public static void main(String[] argv) {
        int[] a = {1000, 90000, 7, 3, 6, 2, 1, 4, 5, 1, 100, 1000, 0, -1 ,9000000};
        MaxHeap maxHeap = new MaxHeap(a);
        IntArrayTraversal.of(maxHeap.a).forEach(ArrayPrinter.of()::print);
        maxHeap.heap_sort_1();
        IntArrayTraversal.of(maxHeap.a).forEach(ArrayPrinter.of()::print);

        int[] b = {1000, 90000, 7, 3, 6, 2, 1, 4, 5, 1, 100, 1000, 0, -1 ,9000000};
        MaxHeap maxHeap2 = new MaxHeap(b);
        IntArrayTraversal.of(maxHeap2.a).forEach(ArrayPrinter.of()::print);
        maxHeap2.heap_sort();
        IntArrayTraversal.of(maxHeap2.a).forEach(ArrayPrinter.of()::print);
    }

}
