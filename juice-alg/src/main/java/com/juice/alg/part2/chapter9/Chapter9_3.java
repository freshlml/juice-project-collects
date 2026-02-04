package com.juice.alg.part2.chapter9;


import com.juice.alg.part1.chapter2.Chapter2_Practice.PositionArrayPrinter;
import com.juice.alg.part1.chapter2.Chapter2_Practice2.IntArrayTraversal;
import com.juice.alg.part2.chapter7.Chapter7_1;

public class Chapter9_3 {

    /**
     *求第 i 个顺序统计量，使最坏情况运行时间是线性的
     *
     *  出发点: 如果 partition 能够返回一个好的划分下标，那么算法的最坏情况将得到优化
     *
     *  select 算法:
     *    1. 将输入数组 a[p, ..., r) 的 n 个元素分成 ⌈n/5⌉ 组，每组 5 个元素，且至多只有一组由剩下的 n%5 个元素组成
     *
     *    2. 对 ⌈n/5⌉ 组中每一组取其中位数: 对每组进行插入排序，取每组的中位数
     *
     *    3. 对第 2 步找出的 ⌈n/5⌉ 个中位数，递归调用 select 以找出它们的中位数，记为 x（如果 ⌈n/5⌉ 为偶数，取其下中位数）
     *
     *    4. 以第 3 步返回的 x 为主元对输入数组 a[p, ..., r) 进行划分，得到划分下标 q，令 k = q-p+1
     *
     *    5. 如果 i == k，则求得解。如果 i < k，则在 [p, q) 子数组上递归求 i。如果 i > k，则在 [q+1, r) 子数组上递归求 i-k
     *
     *  select 算法运行时间:
     *
     *    至少有 3*(⌈⌈n/5⌉/2⌉ - 2) >= 3n/10 - 6 个元素大于 x。至少有 3n/10 - 6 个元素小于 x。
     *    在最坏情况下，第 5 步的递归最多作用于 7n/10 + 6 个元素。
     *    得到递归式:
     *      T(n) = T(⌈n/5⌉) + T(7n/10 + 6) + Θ(n)
     *
     *      假设存在常数 c>0，对于 n>N0，使得 T(n) <= cn 成立，使用带入法:
     *      T(n) <= c * ⌈n/5⌉ + c * (7/10n+6) + Θ(n)
     *           <= c * n/5 + c + c * 7n/10 + 6c + Θ(n)
     *            = c * 9n/10 + 7c + Θ(n)
     *            = cn + (7c - cn/10 + Θ(n))
     *
     *            令 7c - cn/10 + Θ(n) <= 0
     *            即 7c - cn/10 + an <= 0
     *            得 c >= 10a * n/(n-70)
     *      解得，当 n > 70 时，存在 c>0，使得 T(n) <= cn 成立
     *
     *      故 T(n) = Θ(n)
     */
    public static int select(int[] a, int i) {
        if(a == null) throw new NullPointerException("a 不能为空");
        if(i < 1 || i > a.length)
            throw new IllegalArgumentException("无法在 [" + 0 + ", " + a.length + ") 范围内求第 " + i + " 个顺序统计量");

        return select(a, 0, a.length, i);
    }
    public static int select(int[] a, int begin, int end, int i) {
        if(a == null) throw new NullPointerException("a 不能为空");
        if(begin < 0 || end > a.length)
            throw new IllegalArgumentException("begin 不能小于0，end 不能大于 a.length");
        if(i < 1 || i > (end - begin))
            throw new IllegalArgumentException("无法在 [" + 0 + ", " + a.length + ") 范围内求第 " + i + " 个顺序统计量");

        return select(a, begin, end, i, 0);
    }
    static int select(int[] a, int begin, int end, int i, int top) {
        //assert a != null;
        int n = end - begin;
        if(i < 1 || i > n) throw new IllegalArgumentException("无法在 [" + begin + ", " + end + ") 范围内求第 " + i + " 个顺序统计量");
        if(n == 1) return a[begin];

        int[] medians = median(a, begin, end);
        int x = select(medians, 0, medians.length, (medians.length + 1)/2, top + 1);
        if(top != 0) return x;

        int q = partition(a, begin, end, x);
        int k = q - begin + 1;

        if(i == k) {
            return a[q];
        } else if(i < k) {
            return select(a, begin, q, i, top);
        } else {
            return select(a, q+1, end, i-k, top);
        }

    }

    static int partition(int[] a, int begin, int end, int x) {
        for(int i=end-1; i >= begin; i--) {
            if(a[i] == x) {
                a[i] = a[end-1];
                a[end-1] = x;
                break;
            }
        }
        return Chapter7_1.partition(a, begin, end);
    }

    static int[] median(int[] a, int begin, int end) {
        //assert begin ∈ [0, a.length); assert end ∈ [0, a.length];
        //assert begin <= end;
        int n = end - begin;

        int z = n%5 == 0 ? n/5 : n/5 + 1;
        int[] result = new int[z];  //z may be zero

        for(int i=0; i < z; i++) {
            int s = begin + 5*i;
            int e = (i < z-1) ? s + 5 : end;
            sort_by_insert(a, s, e);

            int j = s + (e - s + 1)/2 - 1;
            result[i] = a[j];
        }

        return result;
    }

    static void sort_by_insert(int[] a, int begin, int end) {
        //assert a != null;
        //assert begin ∈ [0, a.length); assert end ∈ [0, a.length];
        for(int j = begin+1; j < end; j++) {
            int key = a[j];
            int i = j-1;
            while(i >= begin && a[i] > key) {
                a[i+1] = a[i];
                i--;
            }
            a[i+1] = key;
        }
    }

    public static void main(String[] argv) {
        int[] a = new int[] {100, 45, 56, 23, 1, 4, 3, 78, 3987, 242342, 23, 1, 489, 500, 110, 343};

        int i = 5;
        int v = select(a, i);
        IntArrayTraversal.of(a).forEach(PositionArrayPrinter.of(i-1)::print);
        System.out.println("第 " + i + " 个顺序统计量: " + v);

        System.out.println("------select------------------------------------------------------------------");
    }

}
