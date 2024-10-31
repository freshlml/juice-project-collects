package com.juice.alg.part1.chapter4;


public class Chapter4_2 {
    /**
     *矩阵乘法
     *  1.暴力法
     *    3层循环，Θ(n^3)
     *  2.分治法
     *    分解: 一横一竖，分成四个
     *         A =  A1,A2  B = B1,B2
     *              A4,A3      B4,B3
     *    解决: 当 ai==1 or aj==1 or bj==1
     *    合并:
     *      A * B = A1*B1 + A2*B4, A1*B2 + A2*B3
     *              A4*B1 + A3*B4, A4*B2 + A3*B3
     */
    //暴力法
    public static int[][] blMatrixMulti(int[][] a, int[][] b) {
        if(a == null || b == null) return null;
        int m, n, l;
        if((m = a.length) == 0 || a[0].length == 0) return null;
        if(b.length == 0 || (n = b[0].length) == 0) return null;
        if((l = a[0].length) != b.length) return null;

        int[][] c = new int[m][n];
        for(int i=0; i<m; i++) {
            for(int j=0; j<n; j++) {
                int sum = 0;
                for(int k=0; k<l; k++) {
                    sum += a[i][k] * b[k][j];
                }
                c[i][j] = sum;
            }
        }
        return c;
    }
    //分治法
    /* T(n) = 8*T(n/2) + Θ(n^2)
    此分治法的递归树
                [n]
        [n/2] ...                   第1次分解,n/2          1次merge, 1*n^2
        [n/4] ...                   第2次分解,n/4          8次merge, 8*(n/2)^2
        ...
                [1]                 第x次分解,n/2^x        8^(x-1)次merge, 8^(x-1) * [n/2^(x-1)]^2

      n/2^x = 1 ==> 2^x = n ==> x=log2(n)
      merge总和: 1*n^2 + 8*(n/2)^2 + ... + 8^(x-1) * [n/2^(x-1)]^2
              = (n-1) * n^2
     */
    public static int[][] fzMatrixMulti(int[][] a, int[][] b) {
        if(a == null || b == null) return null;
        int m, n, l;
        if((m = a.length) == 0 || a[0].length == 0) return null;
        if(b.length == 0 || (n = b[0].length) == 0) return null;
        if((l = a[0].length) != b.length) return null;

        int[][] c = fzMatrixMulti(a, 0, m, 0, l, b, 0, l, 0, n);
        return c;
    }
    public static int[][] fzMatrixMulti(int[][] a, int aiBegin, int aiEnd, int ajBegin, int ajEnd,
                                        int[][] b, int biBegin, int biEnd, int bjBegin, int bjEnd) {

        int ai = aiEnd - aiBegin;
        int bj = bjEnd - bjBegin;
        int bi = biEnd - biBegin;
        int aj = ajEnd - ajBegin;

        if(ai == 1) {
            int[][] c = new int[1][bj];
            for(int l=0; l<bj; l++) {
                int sum = 0;
                for(int k = 0; k<aj; k++) {
                    sum += a[aiBegin][k + ajBegin] * b[biBegin + k][bjBegin + l];
                }
                c[0][l] = sum;
            }
            return c;
        }

        if(aj == 1) {
            int[][] c = new int[ai][bj];
            for(int k=0; k<ai; k++) {
                for(int l=0; l<bj; l++) {
                    c[k][l] = a[aiBegin + k][ajBegin] * b[biBegin][bjBegin + l];
                }
            }
            return c;
        }

        if(bj == 1) {
            int[][] c = new int[ai][1];
            for(int l=0; l<ai; l++) {
                int sum = 0;
                for(int k = 0; k<aj; k++) {
                    sum += a[aiBegin + l][k + ajBegin] * b[biBegin + k][bjBegin];
                }
                c[l][0] = sum;
            }
            return c;
        }

        int[][] A1_B1 = fzMatrixMulti(a, aiBegin, aiBegin + ai/2, ajBegin, ajBegin + aj/2,
                                      b, biBegin, biBegin + bi/2, bjBegin, bjBegin + bj/2);

        int[][] A2_B4 = fzMatrixMulti(a, aiBegin, aiBegin + ai/2, ajBegin + aj/2, ajEnd,
                                      b, biBegin + bi/2, biEnd, bjBegin, bjBegin + bj/2);

        int[][] A1_B2 = fzMatrixMulti(a, aiBegin, aiBegin + ai/2, ajBegin, ajBegin + aj/2,
                                      b, biBegin, biBegin + bi/2, bjBegin + bj/2, bjEnd);

        int[][] A2_B3 = fzMatrixMulti(a, aiBegin, aiBegin + ai/2, ajBegin + aj/2, ajEnd,
                                      b, biBegin + bi/2, biEnd, bjBegin + bj/2, bjEnd);

        int[][] A4_B1 = fzMatrixMulti(a,aiBegin + ai/2, aiEnd, ajBegin, ajBegin + aj/2,
                                      b, biBegin, biBegin + bi/2, bjBegin, bjBegin + bj/2);

        int[][] A3_B4 = fzMatrixMulti(a, aiBegin + ai/2, aiEnd, ajBegin + aj/2, ajEnd,
                                      b, biBegin + bi/2, biEnd, bjBegin, bjBegin + bj/2);

        int[][] A4_B2 = fzMatrixMulti(a, aiBegin + ai/2, aiEnd, ajBegin, ajBegin + aj/2,
                                      b, biBegin, biBegin + bi/2, bjBegin + bj/2, bjEnd);

        int[][] A3_B3 = fzMatrixMulti(a, aiBegin + ai/2, aiEnd, ajBegin + aj/2, ajEnd,
                                      b, biBegin + bi/2, biEnd, bjBegin + bj/2, bjEnd);

        int[][] r = merge(A1_B1, A2_B4, A1_B2, A2_B3, A4_B1, A3_B4, A4_B2, A3_B3, ai, bj);

        return r;
    }
    public static int[][] merge(int[][] A1_B1, int[][] A2_B4,
                                int[][] A1_B2, int[][] A2_B3,
                                int[][] A4_B1, int[][] A3_B4,
                                int[][] A4_B2, int[][] A3_B3,
                                int ai, int bj) {
        int[][] c = new int[ai][bj];

        //1. A1_B1 + A2_B4
        for(int i=0; i<ai/2; i++) {
            for(int j=0; j<bj/2; j++) {
                c[i][j] = A1_B1[i][j] + A2_B4[i][j];
            }
        }

        //2. A1_B2 + A2_B3
        for(int i=0; i<ai/2; i++) {
            for(int j=bj/2; j<bj; j++) {
                c[i][j] = A1_B2[i][j-bj/2] + A2_B3[i][j-bj/2];
            }
        }

        //3. A4_B1 + A3_B4
        for(int i=ai/2; i<ai; i++) {
            for(int j=0; j<bj/2; j++) {
                c[i][j] = A4_B1[i-ai/2][j] + A3_B4[i-ai/2][j];
            }
        }

        //4. A4_B2 + A3_B3
        for(int i=ai/2; i<ai; i++) {
            for(int j=bj/2; j<bj; j++) {
                c[i][j] = A4_B2[i-ai/2][j-bj/2] + A3_B3[i-ai/2][j-bj/2];
            }
        }

        return c;
    }

    public static void main(String[] argv) {
        int[][] a = {{120, 2, 3}, {3, 4, 5}};
        int[][] b = {{1, 2}, {4, 5}, {6, 8}};

        int[][] blc = blMatrixMulti(a, b);

        mergeAndPrintMatrix(a, b);
        System.out.println("------------------------------");
        printMatrix(blc);
        System.out.println("#############################################");

        int[][] fzc = fzMatrixMulti(a, b);

        mergeAndPrintMatrix(a, b);
        System.out.println("------------------------------");
        printMatrix(fzc);
        System.out.println("#############################################");

        /*int[][] a2 = {{1, 2, 3}, {3, 4, 5}, {6, 7, 8}};
        int[][] b2 = {{1, 2, 3}, {3, 4, 5}, {6, 7, 8}};

        int[][] bl2 = blMatrixMulti(a2, b2);
        int[][] fz2 = fzMatrixMulti(a2, b2);


        int[][] a3 = {{1, 2, 3, 4, 101}, {3, 4, 5, 6, 102}, {103, 6, 7, 8, 9}, {104, 1, 2, 3, 4}, {104111, 1, 2, 3, 4}};
        int[][] b3 = {{1, 2, 3, 4, 101}, {3, 4, 5, 6, 102}, {103, 6, 7, 8, 9}, {104, 1, 2, 3, 4}, {104111, 1, 2, 3, 4}};

        int[][] bl3 = blMatrixMulti(a3, b3);
        int[][] fz3 = fzMatrixMulti(a3, b3);

        System.out.println(1);*/
    }

    static void mergeAndPrintMatrix(int[][] a, int[][] b) {
        boolean leftNone = a.length <= b.length;
        int rowLimit = Math.min(a.length, b.length);
        int columnLimit;

        int m = Math.max(a.length, b.length);
        int n = (columnLimit = a[0].length) + b[0].length;  //may overflow
        int[][] c = new int[m][n];

        int[] width = new int[n];
        for(int i=0; i<a.length; i++) {
            for(int j=0; j<a[i].length; j++) {
                c[i][j] = a[i][j];
                int w = String.valueOf(a[i][j]).length();
                if(i == 0 || width[j] < w) {
                    width[j] = w;
                }
            }
        }
        for(int i=0; i<b.length; i++) {
            for(int j=0; j<b[i].length; j++) {
                c[i][j+columnLimit] = b[i][j];
                int w = String.valueOf(b[i][j]).length();
                if(i == 0 || width[j+columnLimit] < w) {
                    width[j+columnLimit] = w;
                }
            }
        }

        StringBuilder element = new StringBuilder();
        String sep;
        final String split = "    ";
        final char LF = '\n';
        for(int i=0; i<c.length; i++) {
            for(int j=0; j<c[i].length; j++) {
                String s;
                int w;
                if(i >= rowLimit && ((j < columnLimit) == leftNone)) {
                    s = "";
                    w = width[j];
                    sep = "  ";
                } else {
                    s = String.valueOf(c[i][j]);
                    w = width[j] - s.length();
                    sep = ", ";
                }
                for(int l=0; l<w; l++)
                    element.append(' ');
                element.append(s);

                if(j != (columnLimit - 1) && j != (c[i].length - 1)) {
                    element.append(sep);
                } else if(j == (columnLimit - 1)) {
                    element.append(split);
                } else {
                    element.append(LF);
                }
            }
        }
        System.out.print(element);
    }

    static void printMatrix(int[][] c) {
        StringBuilder element = new StringBuilder();
        int m = c.length;
        int n = c[0].length;
        int[] width = new int[n];

        //先扫描一遍，求得最大位宽
        for(int i=0; i<m; i++) {
            for (int j = 0; j < n; j++) {
                int w = String.valueOf(c[i][j]).length();
                if (i == 0 || width[j] < w) {
                    width[j] = w;
                }
            }
        }
        for(int i=0; i<m; i++) {
            for (int j = 0; j<n; j++) {

                String s = String.valueOf(c[i][j]);
                int w = s.length();

                for (int l = 0; l < (width[j] - w); l++)
                    element.append(' ');
                element.append(s);

                if (j != (c[i].length - 1)) {
                    element.append(", ");
                } else {
                    element.append('\n');
                }
            }
        }
        System.out.print(element);
    }
}
