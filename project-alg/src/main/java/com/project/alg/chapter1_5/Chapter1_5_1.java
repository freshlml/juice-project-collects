package com.project.alg.chapter1_5;

import java.util.Arrays;

public class Chapter1_5_1 {

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
       利用条件概率，可以创造出1/7,1/6,1/5
            A: 出现0 0 0的事件
            B: 1 1 1不出现的事件
            P(A|B) = P(A ∩ B) / P(B)  : 在1 1 1不出现的条件之下出现0 0 0的概率
                   = 1/8 / 7/8
                   = 1/7
     */

    int RANDOM(int from, int to) {
        boolean flag = true;
        int n = to - from + 1;
        //assert n > 0

        //求 n <= 2^m 的最小m
        int m = -1;
        //从大到小定2^m-n个无效的组合
        int[][] invalids = invalids2(m, n);

        int result = 0;
        while(flag) {
            int[] a = new int[m];
            for(int i=0; i < m; i++) {
                a[i] = RANDOM_0_1();
            }
            if(!contains(invalids, a)) {
                //result = a to 二进制值;
                flag = false;
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
        int c[] = new int[a.length+1];

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


    int RANDOM_0_1() {return 0/1;}
    boolean contains(int[][] b, int[] a) {return false;}


    public static void main(String argv[]) {
        Chapter1_5_1 c = new Chapter1_5_1();
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

}
