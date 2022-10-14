package com.juice.alg.chapter2_9;


public class Chapter2_9_3 {

    /**
     *第i个顺序统计量, 最坏情况运行时间是线性的
     *  出发点: 如果partition能够返回一个好的划分下标，那么算法的最坏情况将得到优化
     *
     *  步骤:
     *    1. 将输入数组每5个数划分成 n/5 组 或者 n/5 + 1 组(n%5!=0,最后一组是不完全组)
     *    2. 对每组进行插入排序，取每组的中位数  (排序消耗时间最多是 roundup(n/5) * 5^2)
     *    3. 对第2步找出的roundup(n/5)个中位数，"递归"取得中位数，记为x
     *    4. 以第3步返回的x为主元进行划分
     *    5. 如果第i个顺序统计量恰好是第4步的划分下标，返回x，如果第i个顺序统计量小于第4步的划分下标，则按低区递归，否则，按高区递归
     *
     *  解答: 按照第1, 2, 3步得到的中位数的中位数x作为主元的划分 为什么 是一个好的划分？
     *    以x作为主元划分，因为大于x的元素至少是 3*[ roundup( 1/2 * roundup(n/5) ) - 2 ] >= 3/10n-6，小于x的元素也至少是 3/10n-6
     *    则在最坏情况下，第5步的递归的子数组最多 7n/10+6个元素
     *    递归式:
     *    T(n) = T(roundup(n/5)) + T(7n/10+6) + O(n)
     *    求解递归式 {
     *        假设存在常数c>0,对于n>N0，使得T(n) <= cn成立，带入法
     *        T(n) <= c * roundup(n/5) + c * (7/10n+6) + O(n)
     *             <= c * n/5 + c + c*7n/10 + 6c + a*n
     *             = c*9n/10 + 7c + an
     *             = cn + (7c - cn/10 + an)
     *             令 7c - cn/10 + an <= 0
     *             当n>70时，有 c >= 10a( n/(n-70) )
     *             当n=71时，c >= 10a * 71
     *             当n=72时，c >= 10a * 36
     *             ...
     *             当n>=140时，c >= 20a
     *             当n越来越大时，c越来越小
     *    }
     *
     *    todo,why n >= 140, 练习9.3-2
     *
     */
    public int select(int a[], int i) {
        if(a == null || a.length == 0) return -1; //tag
        if(a.length == 1) {
            if(i == 1) return a[0];
            return -1; //tag
        }
        //assert 1 <= i <= a.length

        return select(a, 0, a.length, i);
    }
    public int select(int a[], int begin, int end, int i) {
        if(end - begin == 1) return a[begin];

        int[] medians = range_median(a, begin, end);

        int median_value = select(medians, 0, medians.length, (medians.length - 1)/2 + 1);
        int median_q = 0;
        for(int l=end-1; l>=begin; l++) {
            if(a[l] == median_value) {
                median_q = l;
                break;
            }
        }

        int q = partition(a, begin, end, median_q);

        int k = q - begin + 1;

        if(i == k) {
            return a[q];
        } else if(i < k) {
            return select(a, begin, q, k);
        } else {
            return select(a, q+1, end, i-k);
        }

    }
    public int partition(int a[], int begin, int end, int ex_q) {
        int ex = a[ex_q];
        a[ex_q] = a[end-1];
        a[end-1] = ex;
        return partition(a, begin, end);
    }
    public int partition(int a[], int begin, int end) {
        int i = begin-1;
        int j = begin;
        int k = a[end-1];

        while(j < end) {
            if(a[j] <= k) {
                i++;
                int ex = a[i];
                a[i] = a[j];
                a[j] = ex;
            }

            j++;
        }
        return i;
    }
    private int[] range_median(int[] a, int begin, int end) {
        int n = end - begin;

        int z = n%5 == 0 ? n/5 : n/5 + 1;
        int[] b = new int[z];
        for(int i=0; i<z; i++) {
            int s = begin + 5*i;
            int e = (i < z-1) ? s + 5 : end;
            sort_by_insert(a, s, e);

            int j = s + 2;
            if(i == z-1) {
                switch (n - s) {
                    case 1:
                    case 2:
                        j = s;
                        break;
                    case 3:
                    case 4:
                        j = s + 1;
                        break;
                    case 5:
                        j = s + 2;
                }
            }
            b[i] = a[j];
        }

        return b;
    }
    private void sort_by_insert(int[] a, int begin, int end) {
        if(a == null || a.length == 0 || a.length == 1) return;

        for(int j = begin+1; j<end; j++) {
            int key = a[j];
            int i = j-1;
            while(i >= 0 && a[i] > key) {
                a[i+1] = a[i];
                i--;
            }
            a[i+1] = key;
        }
    }


    //练习9.3-1，每组分成3个元素，不是线性的；每组分成7个元素是线性的
    //                                    n=71时                    当n=72时                当n=140时                    当n=141时
    //每组5个: n>70时，c >= 10a * n/(n-70): c >= 10a * 71 = 710a,     c >= 10a * 36 = 360a,  c >= 10a * 2 = 20a,         c >= 10a * 141/71 = 19.859a
    //每组7个: n>63时，c >= 7a * n/(n-63):  c >= 7a * 71/8 = 62.125a, c >= 7a * 8 = 56a,     c >= 7a * 140/77 = 12.727a, c >= 7a * 141/78 = 12.654a
    //当n > 70时，n/(n-70) 恒大于 n/(n-63)，当 n->∞ 时，n/(n-70) = n/(n-63) = 1，更大的n,两者之间的差值越小
    //结论: 每组5个的效率高于每组7个，因为每组5个的a更小，而且n不断变大，n/(n-70) 与 n/(n-63) 的差不断变小，从而每组5个越来优


    //练习9.3-3，partition之前，调用select返回主元，绝配


    //练习9.3-4
    //假设A[i]是第i个顺序统计量
    //对所有j != i,     A[j] < A[i]   ,只需记录此类j中值最大(相等最大值取下标最大的)的即为第 i-1 个顺序统计量
    //             或者 A[j] > A[i]   ,只需记录此类j中值最小(相等最大值取下标最小的)的即为第 n-i 个顺序统计量
    //             或者 A[j] == A[i]  ,1. 如果j<i，归到A[j] < A[i]；如果j > i，归到A[j] > A[i]
    //                                2. 或者，忽略；1还是2取决于问题的设定，1表示相等的元素按出现先后次序排位，依次取顺序统计量，2表示相等元素使用一个顺序统计量


    //练习9.3-5
    /*
     median_value = CAL_MEDIAN_VALUE(...)  //黑箱
     median_q = median_value对应的下标
     按median_q划分，即二分

     if 当前划分 == 顺序统计量i: 返回
     else if 顺序统计量i在低区: 低区递归
     else: 高区递归
     */


    //练习9.3-6，lgk，考虑对k分治
    /*
     multi_select(int[] A, int k) {
        if A==null || A.length==0 || A.length==1: return empty list
        assert 0 < k <= A.length

        result = new list
        multi_select(A, 0, A.length, k ,result)

        return result
     }
     void multi_select(int[] A, int begin, int end, int k, List result) {
         if k == 1: return;

         int n = end - begin;

         int md = k/2;
         int x = select(A, begin, end, md * n/k);
         result.add(md-1, x);

         int x_q = 0;
         for(int l=end-1; l>=begin; l++) {
            if(A[l] == x) {
                x_q = l;
                break;
            }
         }
         int q = partition(A, begin, end, x_q)

         multi_select(A, begin, q, md, result);
         multi_select(A, q + 1, end, k%2==0 ? md : md+1, result);
     }
     */
    

    //练习9.3-7
    /*
    class Node {
        int distance;
        int value;
    }
    multi_select_k(int[] A, int begin, int end, int k):
        int n = end-begin;
        assert k <= n;

        int x = select(A, begin, end, (n-1)/2 + 1); //求中位数

        Node[] distances = new Node[n]; //distance数组
        for i from 0 to n-1:
            distance = | A[i]-x |;
            distances[i] = new Node {distance, A[i]};

        int q_v = select(distances, 0, distances.length, k+1);

        int x_q = 0;
        for(int l=end-1; l>=begin; l++) {
           if(A[l] == q_v) {
               x_q = l;
               break;
           }
        }
        int q = partition(A, begin, end, x_q)

        for j from 0 to k:
            print(distances[j].value)

     */

    //练习9.3-8
    /*
    int two_seq_median(int[] x, int[] y) {

        //x, y size必须相等？
        return two_seq_median(x, 0, x.length, y, 0, y.length);
    }
    int two_seq_median(int[] x, int x_begin, int x_end, int[] y, int y_begin, int y_end) {
        int x_n = x_end - x_begin;
        int y_n = y_end - y_begin;

        if(x_n <= 0 || y_n <= 0) return -1; //tag

        int y_q = y_begin + (y_n-1)/2;
        int y_median = y[y_q];

        int x_q = x_begin + (x_n-1)/2;
        int x_median = x[x_q];

        if(y_median == x_median) {
            return y_median;
        } else if(x_median < y_median) {

            if(odd(x_n) && odd(y_n)) {
                if( out_of_range(y_q-1, y_begin, y_end) || x_median >= y[y_q-1] ) {
                    return x_median;
                }
            } else {
                if( out_of_range(x_q+1, x_begin, x_end) || y_median <= x[x_q+1] ) {
                    return y_median;
                }
            }

            return two_seq_median(x, x_q+1, x_end, y, y_begin, y_q);

        } else {
            if(odd(x_n) && odd(y_n)) {
                if( out_of_range(x_q - 1, x_begin, x_end) || y_median >= x[x_q-1] ) {
                    return y_median;
                }
            } else {
                if( out_of_range(y_q+1, y_begin, y_end) || x_median <= y[y_q+1] ) {
                    return x_median;
                }
            }

            return two_seq_median(x, x_begin, x_q, y, y_q+1, y_end);

        }

    }
    boolean odd(int n) {
        return n%2 != 0;
    }
    boolean out_of_range(int i, int begin, int end) {
        return i<begin || i>=end;
    }
    */

    //练习9.3-9: 如果n为奇数，求第(n-1)/2 + 1个顺序统计量
    //          如果n为偶数，求第(n-1)/2 + 1个，(n-1)/2 + 2个顺序统计量
    //证明:
    //一口油井：d = 0
    //两口油井：d = d1 + d2; 取两口油井之间任意位置
    //三口油井：d = d1 + d2 + d3; 取最中间位置油井
    //          加法结合率
    //          = (d1 + d2) + d3
    //          = d1 + (d2 + d3)
    //          = (d1 + d3) + d2
    //四口油井：d = d1 + d2 + d3 + d4; 取最中间两口油井之间的任意位置
    //          加法结合律
    //          = (d1 + d2) + (d3 + d4)
    //          = (d1 + d3) + (d2 + d4)
    //          = (d1 + d4) + (d2 + d3)



}
