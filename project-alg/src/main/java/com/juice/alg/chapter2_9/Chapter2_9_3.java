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

        int ex = a[median_q];
        a[median_q] = a[end-1];
        a[end-1] = ex;
        int q = partition(a, begin, end);

        int k = q - begin + 1;

        if(i == k) {
            return a[q];
        } else if(i < k) {
            return select(a, begin, q, k);
        } else {
            return select(a, q+1, end, i-k);
        }

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




}
