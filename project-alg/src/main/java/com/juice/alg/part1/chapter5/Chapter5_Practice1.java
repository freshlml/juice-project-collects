package com.juice.alg.part1.chapter5;

import java.util.Arrays;
import java.util.Random;

public class Chapter5_Practice1 {

    //练习5.1-2
    /*
    RANDOM(0, 1) = 0, 1/2 概率
                 = 1, 1/2 概率

    将RANDOM(0, 1)调用两次
           0 0     1/2*1/2 = 1/4 概率
           0 1     1/2*1/2 = 1/4 概率
           1 0     1/2*1/2 = 1/4 概率
           1 1     1/2*1/2 = 1/4 概率
       2^2 个 1/4
    将RANDOM(0, 1)调用三次
           0 0 0    1/2*1/2*1/2 = 1/8 概率
           0 0 1    1/2*1/2*1/2 = 1/8 概率
           0 1 0    1/2*1/2*1/2 = 1/8 概率
           1 0 0    1/2*1/2*1/2 = 1/8 概率
           0 1 1    1/2*1/2*1/2 = 1/8 概率
           1 0 1    1/2*1/2*1/2 = 1/8 概率
           1 0 0    1/2*1/2*1/2 = 1/8 概率
           1 1 1    1/2*1/2*1/2 = 1/8 概率
       2^3 个 1/8
       利用条件概率，可以创造出1/7, 1/6, 1/5
            A: 出现0 0 0的事件
            B: 1 1 1不出现的事件
            P(A|B) = P(A ∩ B) / P(B)  : 在1 1 1不出现的条件之下出现0 0 0的概率
                   = 1/8 / 7/8
                   = 1/7
     */

    int RANDOM(int from, int to) {
        int n = to - from + 1;
        //assert n > 0

        //求 n <= 2^m 的最小m
        int m = tableSizeFor(n);
        //从大到小定2^m-n个无效的组合
        int[][] invalids = invalids2(m, n);

        int result = 0;
        boolean out = false;
        /*
         *随机变量X: 外层循环执行次数
         *      X    1     2                 3                    ...
         *      P    1     (n-1)/n * 1/n     [(n-1)/n]^2 * 1/n    ...
         *
         * X ~ 几何分布
         * EX = 1/n
         */
        while(!out) {
            int[] a = new int[m];
            for(int i=0; i < m; i++) {  //2^(m-1) < n < 2^m  ==> lgn <= m < lgn +1
                a[i] = RANDOM_0_1();
            }
            if(!contains(invalids, a)) {   // 1/n 的概率 out
                result = calBinary(a);
                out = true;
            }
        }
        return from + result;
    }

    int[][] invalids2(int m ,int n) {
        int[][] invalids = new int[(int) Math.pow(2, m) - n][m];

        int[] sub = new int[m];
        int i=0;
        for(; i<m; i++) {
            sub[i] = 0;
        }
        sub[0] = 1;

        //2^(m-1) < n <= 2^m  ==>  0<= 2^m - n < 2^(m-1)
        for (int j = 0; j < (Math.pow(2, m) - n); j++) {

            int[] r = new int[m];

            if(j == 0) {
                for(int k=0; k<m; k++) {
                    r[k] = 1;
                }
                invalids[j] = r;
            } else {
                int[] rr = binary_sub(invalids[j-1], sub);
                r = Arrays.copyOf(rr, rr.length-1);
                invalids[j] = r;
            }
        }
        return invalids;
    }
    public static int[] binary_sub(int[] a, int[] b) {
        int[] c = new int[a.length+1];

        for(int i=0; i<a.length; i++) {
            int sum = a[i] - b[i] - c[i];
            switch (sum) {
                case -1:
                    c[i+1] = 1;
                    c[i] = 1;
                    break;
                case -2:
                    c[i+1] = 1;
                    c[i] = 0;
                    break;
                case 1:
                    c[i+1] = 0;
                    c[i] = 1;
                    break;
                case 0:
                    c[i+1] = 0;
                    c[i] = 0;
                    break;
                default :
                    c[i+1] = 0;
                    c[i] = -1;
            }
        }
        return c;
    }

    int[][] invalids_o(int m ,int n) {
        int[][] invalids = new int[(int)Math.pow(2, m)-n][m];
        for(int j=0, k=0, l=m; j < (Math.pow(2, m)-n) && k < m; j++) {

            int[] r = new int[m];
            for(int a=0; a<k; a++) {
                r[a] = 0;
            }
            for(int p=k; p<l; p++) {
                r[p] = 1;
            }
            for(int a=l; a<m; a++) {
                r[a] = 0;
            }

            invalids[j] = r;
            l--;
            if(l <= k) {
                k++;
                l = m;
            }
        }

        return invalids;
    }

    int[][] invalids(int m ,int n) {

        if(m <= 0) return null;
        int s = (int)Math.pow(2, m);
        if(s <= n) return null;
        if(n < 0) n = 0;

        int[][] invalids = new int[s-n][m];

        for(int j=0, l=m; l>=0; l--) {
            int[] r = new int[m];

            for(int k=0; k<l; k++) {
                r[k] = 1;
            }

            if(l == m) {
                invalids[j++] = r;
                if(j >= invalids.length) break;
            } else if(l == m-1) {
                r[l] = 0;
                invalids[j++] = r;
                if(j >= invalids.length) break;
            } else {
                r[l] = 0;
                int c_m = m - l - 1;
                int c_n = (int)Math.pow(2, c_m) - (invalids.length-j);
                int[][] c_invalids = invalids(c_m, c_n);
                //if(c_invalids == null) continue;

                boolean end = false;
                for(int p=0; p<c_invalids.length; p++) {
                    int[] c_r = Arrays.copyOf(r, r.length);
                    for(int q=0; q < c_invalids[p].length; q++) {
                        c_r[l+1+q] = c_invalids[p][q];
                    }
                    invalids[j++] = c_r;
                    if(j >= invalids.length) {end = true; break;}
                }
                if(end) break;

            }

        }

        return invalids;
    }

    int RANDOM_0_1() {
        Random random = new Random();
        return random.nextInt(2);  //todo, 以 1/2 概率，返回 0 或 1？
    }
    boolean contains(int[][] b, int[] a) {
        for(int i=0; i<b.length; i++) {
            boolean flag = true;
            for(int j=0; j<b[i].length; j++) {
                if(b[i][j] != a[j]) {
                    flag = false;
                    break;
                }
            }
            if(flag) return true;
        }
        return false;
    }

    int calBinary(int[] a) {
        int result = 0;
        for(int i=a.length-1; i>=0; i--) {
            result = result * 2 + a[i];
        }
        return result;
    }

    final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : n + 1;
    }

    public static void main(String[] argv) {
        Chapter5_Practice1 c = new Chapter5_Practice1();
        int[][] ins = c.invalids_o(3, 1);

        ins = c.invalids2(3, 1);
        ins = c.invalids2(3, 2);
        ins = c.invalids2(3, 5);
        ins = c.invalids2(3, 8);


        ins = c.invalids(-1, 10);
        ins = c.invalids(0, 10);

        ins = c.invalids(1, -1);
        ins = c.invalids(1, 0);
        ins = c.invalids(1, 1);
        ins = c.invalids(1, 2);

        ins = c.invalids(4, -100);
        ins = c.invalids(4, 0);
        ins = c.invalids(4, 1);
        ins = c.invalids(4, 4);
        ins = c.invalids(4, 5);
        ins = c.invalids(4, 16);
        ins = c.invalids(4, 17);


        System.out.println("hello");
    }

    /*
     *练习 5.1-3
     *  BIASED_RANDOM(0, 1) = 0, p   概率
     *                      = 1, 1-p 概率
     *
     *  将 BIASED_RANDOM(0, 1) 调用两次，得
     *  0 0     p^2
     *  0 1     p*(1-p) = p - p^2
     *  1 0     (1-p)*p = p - p^2
     *  1 1     (1-p)^2 = 1- 2p - p^2
     *
     *  即，要确定事件 A，B，使得下列式子成立，从而得到 1/2 的概率:
     *    P(A|B) = P(A∩B) / P(B) = 1/2
     *
     *  事件是样本空间的子集，从集合的角度很容易得出 (p - p^2) / 2(p- p^2) = 1/2
     *  从而确定事件 A 为: 出现 0 1，事件 B 为出现 0 1 或 1 0
     */
    int RANDOM_5_1_3() {
       int result;
       /*
        *随机变量X: 循环执行次数
        *      X     1     2                           3                              ...
        *      P     1     [1 - 2p(1-p)] * 2p(1-p)     [1 - 2p(1-p)]^2 * 2p(1-p)      ...
        *
        * X ~ 几何分布
        * EX = 2p(1-p)
        */
       while(true) {
           int first = BIASED_RANDOM();
           int second = BIASED_RANDOM();

           if((first == 0 && second == 1) || (first == 1 && second == 0)) { // 2p(1-p) 的概率 out
               result = first;
               break;
           }
       }
       return result;
    }
    int BIASED_RANDOM() {
        return 0;  //todo，以 p 概率返回 0，1-p 概率返回 1
    }
}
