package com.juice.alg.part2.chapter6;


public class Chapter6_Practice2 {

    //思考题6-1
    //a: 不总是一样。如
    //    4
    //   5  6
    //b: @link Chapter6/MaxHeap#max_heapify(int, int)


    //思考题6-2: d叉堆
    /*a:
                    [0]                                       第 1 层    高度为 0
            [1] [2] [3] [4] [...] [d]                         第 2 层    高度为 1
        [d*1+1] ... [d*1+d]   ... [d*d+1] ... [d*d+d]         第 3 层    高度为 2
    [d*(d*1+1)+1] ...                                         第 4 层    高度为 3
      ...
      每一层的最左节点的下标: i(x) = i(x-1) + d^(x-2)   x 表示层数，x = 2, ..., h+1
      下标 i 的节点，parent(i) = ⌊(i+d-1)/d⌋ - 1; first_child(i) = i*d + 1; last_child(i) = i*d + d
    */
    /*b: n 个元素的 d 叉堆，设高度为 h
        n = d^0 + d^1 + d^2 + ... + d^(h-1) + s(h),    s(h)'max = d^h, s(h)'min = 1
        可得: d^0 + d^1 + ... + d^(h-1) + 1 <= n <= d^0 + d^1 + ... + d^(h-1) + d^h
        ===>   (d^h-1)/(d-1) + 1 <= n <= (d^(h+1)-1)/(d-1)    ,d>=2
        ===>   d^h/(d-1) <= (d^h+d-2)/(d-1) <= n <= (d^(h+1)-1)/(d-1) < d^(h+1)/(d-1)
        ===>   d^h <= n*(d-1) < d^(h+1)
        ===>   logd(n*(d-1)) - 1 < h <= logd(n*(d-1))
        ===>   h = ⌊logd(n*(d-1))⌋
     */
    //c: d*logd(n*(d-1))
    //d: logd(d-1) + logd(2*(d-1)) + logd(3*(d-1)) + ... + logd((n-1)*(d-1))
    //   = Σ(1,n-1) logd(x*(d-1))
    //   <= ∫(1,n) logd(x*(d-1))) dx
    //   ≈ n*ln[(d-1)n]/lnd
    //e: logd(n*(d-1)) ~ d*logd(n*(d-1))


    //思考题6-3: young 矩阵
    //  每一行数据从左到右排序，每一列数据从上到下排序，矩阵中不存在值的位置使用无穷大(或无穷小)表示
    //  如果 y[0][0]  =  ∞ , 则任意 y[i][j]  =  ∞
    //  如果 y[m-1][n-1] !=  ∞, 则任意 y[i][j] !=  ∞
    //  最小值: y[0][0], 最大值: 边界值之中。i, j位置是否是边界值: (i=m-1 || y[i+1][j]=∞ ) && (j=n-1 || y[i][j+1]=∞)
    /*young 矩阵看成二叉堆, 从 [0,0] 看是最小堆(最大堆), 从 [m-1,n-1] 看就是最大堆(最小堆)
        [
            [1,  3,  4,  ∞]
            [2,  3,  10, ∞]
            [10, 11, 16, ∞]
            [11  13, ∞,  ∞]
            [∞,  ∞,  ∞,  ∞]
        ]
                                                [0,0]
                   [1,0]                                                    [0,1]
         [2,0]                        [1,1]                             [1,1]                     [0,2]
    [3,0]          [2,1]       [2,1]            [1,2]              [2,1]           [1,2]      [1,2]       [0,3]
[4,0]    [3,1] [3,1]  [2,2] [3,1]   [2,2]    [2,2]  [1,3]      [3,1]  [2,2]  [2,2]  [1,3] [2,2]   [1,3] [1,3]
   [4,1]                        [3,2]     [2,3]                                                       [2,3]
      [4,2]                       [3,3] [3,3] [2,4]                                                [3,3]
        [4,3]                   [4,3]                                                            [4,3]

     下标计算: y[i,j]
             left(i,j)  =  y[i+1, j], i+1<=m-1, 最后一行没有 left
             right(i,j) =  y[i, j+1], j+1<=n-1, 最有一列没有 right

             p_left(i,j)  = y[i, j-1], j-1>=0, 第一列没有 p_left
             p_right(i,j) = y[i-1, j], i-1>=0, 第一行没有 p_right

     树的最大高度: m+n-2
     两个头的美妙的树
     */
    static class Young {
        private final int[][] a;
        private final int m;
        private final int n;

        public Young(int m, int n) {
            //assert m>0
            //assert n>0
            this.m = m;
            this.n = n;
            a = new int[m][n];
            init();
        }

        private void init() {
            for(int i=0; i<m; i++) {
                for(int j=0; j<n; j++) {
                    a[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        //d
        public void insert(int v) {
            a[m-1][n-1] = v;
            insert_heapify(m - 1, n - 1);
        }
        private void insert_heapify(int i, int j) {

            int l = j;
            int r = i;
            if((j-1) >= 0 && a[i][j-1] > a[i][j]) {
                l = j-1;
            }
            if((i-1) >= 0 && a[i-1][j] > a[i][l]) {
                r = i-1;
            }

            //InsertTag cu_ret = null;
            if(r != i) {
                int ex = a[r][j];
                a[r][j] = a[i][j];
                a[i][j] = ex;
                insert_heapify(r, j);
                //cu_ret = insert_heapify(r, j);
            } else if (l != j) {
                int ex = a[i][l];
                a[i][l] = a[i][j];
                a[i][j] = ex;
                insert_heapify(i, l);
                //cu_ret = insert_heapify(i, l);
            }

            /*InsertTag insertTag = new InsertTag();
            insertTag.i = r;
            insertTag.j = l;
            if(cu_ret == null) return insertTag;

            if(cu_ret.i < insertTag.i)
                insertTag.i = cu_ret.i;
            if(cu_ret.j < insertTag.j)
                insertTag.j = cu_ret.j;

            return insertTag;*/
        }

        public int min() {
            return this.a[0][0];
        }

        public int max() {
            return -1;  //todo, 根据插入标记可快速得到
        }

        //c
        public int next() {
            int ret = this.a[0][0];
            this.a[0][0] = Integer.MAX_VALUE;
            min_heapify(0, 0, m, n);
            return ret;
        }
        private boolean isOutRange(int i, int j, int heap_size_i, int heap_size_j) {
            return  i > heap_size_i ||
                    (i == heap_size_i && j >= heap_size_j)
                    ;
        }
        private void min_heapify(int i, int j, int heap_size_i, int heap_size_j) {

            int l = i;
            int r = j;
            if(!isOutRange(i+1, j, heap_size_i, heap_size_j) && (i+1)<m && j<n && a[i+1][j] < a[i][j]) {
                l = i+1;
            }
            if(!isOutRange(i, j+1, heap_size_i, heap_size_j) && (j+1)<n && i<m && a[i][j+1] < a[l][j]) {
                r = j+1;
            }

            if(r != j) {
                int ex = a[i][r];
                a[i][r] = a[i][j];
                a[i][j] = ex;
                min_heapify(i, r, heap_size_i, heap_size_j);
            } else if(l != i) {
                int ex = a[l][j];
                a[l][j] = a[i][j];
                a[i][j] = ex;
                min_heapify(l, j, heap_size_i, heap_size_j);
            }

        }

        //e
        public void sort() {

            int heap_size_i = m-1;
            int heap_size_j = n;

            while(true) {

                heap_size_j--;
                if(heap_size_j < 0) {
                    heap_size_j = n-1;
                    heap_size_i--;

                    if(heap_size_i < 0)
                        break;
                }

                int ex = a[0][0];
                a[0][0] = a[heap_size_i][heap_size_j];
                a[heap_size_i][heap_size_j] = ex;

                min_heapify(0, 0, heap_size_i, heap_size_j);
            }
        }

        //f
        public YoungContains contains(int v) {
            YoungContains ret = new YoungContains(v);

            for(int i=m-1, j=0; i>=0 && j<n; ) {
                if (this.a[i][j] == v) {
                    ret.i = i;
                    ret.j = j;
                    ret.contains = true;
                    break;
                } else if(this.a[i][j] > v) {
                    i--;
                } else {
                    j++;
                }
            }

            return ret;
        }
        private YoungContains contains(int v, int i, int j) {
            YoungContains ret = new YoungContains(v);
            if(i < 0 || j >= n) return ret;

            if(this.a[i][j] == v) {
                ret.i = i;
                ret.j = j;
                ret.contains = true;
                return ret;
            } else if(this.a[i][j] > v) {
                return contains(v, i-1, j);
            } else {
                return contains(v, i, j+1);
            }
        }

        static class YoungContains {
            int v;
            int i = -1;
            int j = -1;
            boolean contains = false;

            public YoungContains(int v) {
                this.v = v;
            }

            @Override
            public String toString() {
                return "YoungContains{" +
                        "v=" + v +
                        ", i=" + i +
                        ", j=" + j +
                        ", contains=" + contains +
                        '}';
            }
        }

        static class InsertTag {
            public int i;
            public int j;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<m; i++) {
                sb.append("[ ");
                for(int j=0; j<n; j++) {
                    sb.append(a[i][j]);
                    if(j != n-1)
                        sb.append(", ");
                    for(int k=0; k<11-(a[i][j]+"").length(); k++)
                        sb.append(" ");
                }
                sb.append("]\n");
            }
            return sb.toString();
        }
    }


    public static void main(String[] argv) {
        Young young = new Young(5, 4);

        System.out.println(young);

        young.insert(2);
        young.insert(3);
        young.insert(11111);
        young.insert(1333);
        young.insert(1);
        young.insert(1223131);
        young.insert(1);
        young.insert(14343);
        young.insert(12224123);
        young.insert(Integer.MIN_VALUE);
        young.insert(9990);
        young.insert(678);
        young.insert(567);
        young.insert(345);
        young.insert(1);
        young.insert(0);
        young.insert(-100);
        young.insert(-10000);

        System.out.println(young);


        for(int i=0; i<1; i++)
            young.next();

        System.out.println(young);

        System.out.println(young.contains(1333));
        System.out.println();

        young.sort();

        System.out.println(young);

    }

}
