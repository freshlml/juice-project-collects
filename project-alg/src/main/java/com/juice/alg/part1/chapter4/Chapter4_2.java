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

        mergeAndPrintMatrix(a, b);
        System.out.println("------------------------------");

        int[][] blc = blMatrixMulti(a, b);
        IntMatrixTraversal.of(blc).forEach(MatrixPrinter.of()::print);
        System.out.println("#############################################");

        mergeAndPrintMatrix(a, b);
        System.out.println("------------------------------");

        int[][] fzc = fzMatrixMulti(a, b);
        IntMatrixTraversal.of(fzc).forEach(MatrixPrinter.of()::print);
        System.out.println("#############################################");

        int[][] c = {{109, 2, 3}, {4, 555, 6}, {7, 8}};
        IntMatrixTraversal.of(c).forEach(MatrixPrinter.of()::print);
        System.out.println("------------------------------");

        IntMatrixTraversal.of(c, IntMatrixTraversal.TraversalMode.TRANSPOSE).forEach(MatrixPrinter.of()::print);
        System.out.println("------------------------------");

        IntMatrixTraversal.of(c, IntMatrixTraversal.TraversalMode.REVERSE).forEach(MatrixPrinter.of()::print);
        System.out.println("------------------------------");

        IntMatrixTraversal.of(c, IntMatrixTraversal.TraversalMode.REVERSE_TRANSPOSE).forEach(MatrixPrinter.of()::print);
        System.out.println("------------------------------");

        IntMatrixTraversal.of(c, IntMatrixTraversal.TraversalMode.ROW_REVERSE).forEach(MatrixPrinter.of()::print);
        System.out.println("------------------------------");

        IntMatrixTraversal.of(c, IntMatrixTraversal.TraversalMode.ROW_REVERSE_TRANSPOSE).forEach(MatrixPrinter.of()::print);
        System.out.println("------------------------------");

        IntMatrixTraversal.of(c, IntMatrixTraversal.TraversalMode.COLUMN_REVERSE).forEach(MatrixPrinter.of()::print);
        System.out.println("------------------------------");

        IntMatrixTraversal.of(c, IntMatrixTraversal.TraversalMode.COLUMN_REVERSE_TRANSPOSE).forEach(MatrixPrinter.of()::print);

        System.out.println("#############################################");

        c = new int[][] {{1}, {1, 2, 3, 4, 5, 10000, 109876}, {4, 555, 600, 4, 5}, {7, 8}, {1, 2, 3, 4, 5, 6}, {1}};
        IntMatrixTraversal.of(c, 1, 5, 2, 6).forEach(MatrixPrinter.of()::print);

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

    public static class MatrixPrinter<E> {
        protected static final String SEP = ", ";
        protected static final char LINE_END = '\n';
        protected int count = 1;

        private MatrixPrinter() {}

        public void print(E e, int i, int iLimit, int j, int jLimit, int width) {
            printElement(e, width);
            if(i == iLimit && j == jLimit) printEnd();

            if(j == jLimit) printLineEnd();
            else printSep();

            this.count++;
        }

        protected void printElement(E e, int width) {
            StringBuilder element = new StringBuilder();

            String s = String.valueOf(e);
            int w = s.length();

            for (int l = 0; l < (width - w); l++)
                element.append(' ');
            element.append(s);

            System.out.print(element);
        }

        protected void printEnd() {
            //printLineEnd();
        }

        protected void printLineEnd() {
            System.out.print(LINE_END);
        }

        protected void printSep() {
            System.out.print(SEP);
        }

        public static <E> MatrixPrinter<E> of() {
            return new MatrixPrinter<>();
        }
    }


    public static class IntMatrixTraversal {
        private final int[][] array;
        private final int rowBegin;
        private final int rowEnd;
        private final int columnBegin;
        private final int columnEnd;
        private final TraversalMode mode;
        private int[] rowWidth;
        private int[] columnWidth;

        /**
         * @param array  the array for traversal
         * @param mode   矩阵的 traversal 方式: 正序，转置，逆序，逆序-转置，行-逆序，行-逆序-转置，列-逆序，列-逆序-转置
         * @throws NullPointerException          if the specified `array` is null
         */
        private IntMatrixTraversal(int[][] array, TraversalMode mode, int[]... widths) {
            //this(array, 0, array.length, 0, array[0].length, mode, widths);
            if(array == null/* || array[0] == null*/) throw new NullPointerException("array can not be null");
            this.array = array;
            this.rowBegin = 0;
            this.rowEnd = array.length;
            this.columnBegin = 0;
            //this.columnEnd = array[0].length;
            this.mode = mode;

            // 遍历 array, 寻找 the largest column length
            int largest_column_length = 0;
            for (int i=this.rowBegin; i < this.rowEnd; i++) {
                if(largest_column_length < array[i].length)
                    largest_column_length = array[i].length;
            }
            this.columnEnd = largest_column_length;

            calWidth(widths);
        }

        /**
         * @param array          the array for traversal
         * @param rowBegin       the start row position of traversal, inclusive
         * @param rowEnd         the end row position of traversal,   exclusive
         * @param columnBegin    the start column position of traversal, inclusive
         * @param columnEnd      the end column position of traversal,   exclusive
         * @param mode           矩阵的 traversal 方式: 正序，转置，逆序，逆序-转置，行-逆序，行-逆序-转置，列-逆序，列-逆序-转置
         * @throws NullPointerException          if the specified `array` is null
         * @throws IllegalArgumentException      if the specified `rowBegin`, `rowEnd`, `columnBegin`, `columnEnd` is negative
         *                                    or if `rowBegin` > `rowEnd` or `rowEnd` > `array.length`
         *                                    or if `columnBegin` > `columnEnd` or `columnEnd` > the largest column length
         */
        private IntMatrixTraversal(int[][] array, int rowBegin, int rowEnd, int columnBegin, int columnEnd, TraversalMode mode, int[]... widths) {
            if(array == null/* || array[0] == null*/) throw new NullPointerException("array can not be null");
            if(rowBegin < 0 || rowEnd < 0 || columnBegin < 0 || columnEnd < 0)
                throw new IllegalArgumentException("the specified rowBegin, rowEnd, columnBegin, columnEnd is negative, rowBegin = "
                        + rowBegin + ", rowEnd = " + rowEnd + ", columnBegin = " + columnBegin + ", rowEnd = " + rowEnd);
            if(rowBegin > rowEnd || rowEnd > array.length)
                throw new IllegalArgumentException("the specified rowBegin is large than the specified rowEnd or, rowEnd is large than array's row length");
            if(columnBegin > columnEnd/* || columnEnd > array[0].length*/)
                throw new IllegalArgumentException("the specified columnBegin is large than the specified columnEnd");

            this.array = array;
            this.rowBegin = rowBegin;
            this.rowEnd = rowEnd;
            this.columnBegin = columnBegin;
            //this.columnEnd = columnEnd;
            this.mode = mode;

            // 遍历 array, 寻找 the largest column length
            int largest_column_length = 0;
            for (int i=this.rowBegin; i < this.rowEnd; i++) {
                if(largest_column_length < array[i].length)
                    largest_column_length = array[i].length;
            }
            if(columnEnd > largest_column_length)
                throw new IllegalArgumentException("the specified columnEnd [" + columnEnd + "] is large than the largest column length [" + largest_column_length + "]");
            this.columnEnd = columnEnd;

            calWidth(widths);
        }

        private void calWidth(int[]... widths) {
            int rows = this.rowEnd - this.rowBegin;
            int columns = this.columnEnd - this.columnBegin;

            //计算行最大位宽和列最大位宽
            if(widths != null && widths.length >= 2 && widths[0].length >= rows && widths[1].length >= columns) {
                this.rowWidth = widths[0];
                this.columnWidth = widths[1];
            } else {
                this.rowWidth = new int[rows];
                this.columnWidth = new int[columns];

                for(int i=this.rowBegin; i < this.rowEnd; i++) {
                    for (int j=this.columnBegin; j < Math.min(this.columnEnd, array[i].length); j++) {
                        int w = String.valueOf(this.array[i][j]).length();
                        if(columnWidth[j - this.columnBegin] < w) {
                            columnWidth[j - this.columnBegin] = w;
                        }
                        if(rowWidth[i - this.rowBegin] < w) {
                            rowWidth[i - this.rowBegin] = w;
                        }
                    }
                }
            }
        }

        public static IntMatrixTraversal of(int[][] a, int[]... widths) {
            return of(a, TraversalMode.NORMAL, widths);
        }

        public static IntMatrixTraversal of(int[][] a, TraversalMode mode, int[]... widths) {
            return new IntMatrixTraversal(a, mode, widths);
        }

        public static IntMatrixTraversal of(int[][] a, int rowBegin, int rowEnd, int columnBegin, int columnEnd, int[]... widths) {
            return of(a, rowBegin, rowEnd, columnBegin, columnEnd, TraversalMode.NORMAL, widths);
        }

        public static IntMatrixTraversal of(int[][] a, int rowBegin, int rowEnd, int columnBegin, int columnEnd, TraversalMode mode, int[]... widths) {
            return new IntMatrixTraversal(a, rowBegin, rowEnd, columnBegin, columnEnd, mode, widths);
        }

        public TraversalMode getMode() {
            return this.mode;
        }

        public boolean reverse() {
            return rowReverse() && columnReverse();
        }

        public boolean rowReverse() {
            return rowReverse(this.mode);
        }

        public static boolean rowReverse(TraversalMode mode) {
            return  mode == TraversalMode.ROW_REVERSE
                    || mode == TraversalMode.ROW_REVERSE_TRANSPOSE
                    || mode == TraversalMode.REVERSE
                    || mode == TraversalMode.REVERSE_TRANSPOSE
                    ;
        }

        public boolean columnReverse() {
            return columnReverse(this.mode);
        }

        public static boolean columnReverse(TraversalMode mode) {
            return mode == TraversalMode.COLUMN_REVERSE
                    || mode == TraversalMode.COLUMN_REVERSE_TRANSPOSE
                    || mode == TraversalMode.REVERSE
                    || mode == TraversalMode.REVERSE_TRANSPOSE
                    ;
        }

        public boolean transpose() {
            return transpose(this.mode);
        }

        public static boolean transpose(TraversalMode mode) {
            return mode == TraversalMode.TRANSPOSE
                    || mode == TraversalMode.REVERSE_TRANSPOSE
                    || mode == TraversalMode.ROW_REVERSE_TRANSPOSE
                    || mode == TraversalMode.COLUMN_REVERSE_TRANSPOSE
                    ;
        }

        public void forEach(PerElement<Object> handler) {
            TraversalNode node = new TraversalNode(
                    this.rowBegin, this.rowEnd, this.rowEnd - 1, IntBiFunction::less, IntFunction::preIncrement,
                    this.columnBegin, this.columnEnd, this.columnEnd - 1, IntBiFunction::less, IntFunction::preIncrement,
                    ElementGather::getIndexSafe, columnWidth);

            if(rowReverse()) {
                node.rowReverse(this.rowEnd - 1, this.rowBegin, this.rowBegin, IntBiFunction::greaterEqual, IntFunction::preDecrement);
            }
            if(columnReverse()) {
                node.columnReverse(this.columnEnd - 1, this.columnBegin, this.columnBegin, IntBiFunction::greaterEqual, IntFunction::preDecrement);
            }

            if(transpose()) {
                node = node.transpose(this.rowWidth);
            }

            for (int i = node.i; node.iComp.cal(i, node.ie); i = node.iAccum.cal(i)) {
                for (int j = node.j; node.jComp.cal(j, node.je); j = node.jAccum.cal(j)) {
                    //如果是转置输出，则 i 不在表示 array 的行号，而是列号
                    handler.per(node.gather.get(this.array, i, j), i, node.iLimit, j, node.jLimit, node.getWidth(j));
                }
            }
        }

        @FunctionalInterface
        public interface PerElement<E> {
            void per(E e, int i, int iLimit, int j, int jLimit, int width);
        }

        public enum TraversalMode {
            NORMAL,                     //矩阵的正序输出
            TRANSPOSE,                  //矩阵的转置输出
            REVERSE,                    //矩阵的逆序输出
            REVERSE_TRANSPOSE,          //矩阵的逆序-转置输出
            ROW_REVERSE,                //矩阵的行-逆序输出
            ROW_REVERSE_TRANSPOSE,      //矩阵的行-逆序-转置输出
            COLUMN_REVERSE,             //矩阵的列-逆序输出
            COLUMN_REVERSE_TRANSPOSE    //矩阵的列-逆序-转置输出
        }

        @FunctionalInterface
        interface IntBiFunction {
            boolean cal(int a, int b);

            static boolean less(int a, int b) {
                return a < b;
            }
            static boolean greaterEqual(int a, int b) {
                return a >= b;
            }
        }
        @FunctionalInterface
        interface IntFunction {
            int cal(int a);

            static int preIncrement(int a) {
                return ++a;
            }
            static int preDecrement(int a) {
                return --a;
            }
        }
        @FunctionalInterface
        interface ElementGather {
            Object get(int[][] array, int i, int j);

            static String getIndexSafe(int[][] array, int i, int j) {
                try {
                    return String.valueOf(array[i][j]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    return "";
                }
            }
            static String getIndexSafeTranspose(int[][] array, int i, int j) {
                try {
                    return String.valueOf(array[j][i]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    return "";
                }
            }
        }

        private static class TraversalNode {
            int i, ie, iLimit;
            int j, je, jLimit;

            IntBiFunction iComp;
            IntFunction iAccum;

            IntBiFunction jComp;
            IntFunction jAccum;

            ElementGather gather;
            private int[] width;

            private boolean rowReverse = false;
            private boolean columnReverse = false;
            private boolean transpose = false;

            TraversalNode(int i, int ie, int iLimit, IntBiFunction iComp, IntFunction iAccum,
                          int j, int je, int jLimit, IntBiFunction jComp, IntFunction jAccum,
                          ElementGather gather, int[] width) {
                this.i = i;
                this.ie = ie;
                this.iLimit = iLimit;
                this.j = j;
                this.je = je;
                this.jLimit = jLimit;
                this.iComp = iComp;
                this.iAccum = iAccum;
                this.jComp = jComp;
                this.jAccum = jAccum;
                this.gather = gather;
                this.width = width;
            }

            int getWidth(int a) {
                if(transpose) {
                    if(rowReverse) {
                        return this.width[a - je];
                    } else {
                        return this.width[a - j];
                    }
                } else {
                    if(columnReverse) {
                        return this.width[a - je];
                    } else {
                        return this.width[a - j];
                    }
                }
            }

            void rowReverse(int i, int ie, int iLimit, IntBiFunction iComp, IntFunction iAccum) {
                this.iComp = iComp;
                this.iAccum = iAccum;

                this.i = i;
                this.ie = ie;
                this.iLimit = iLimit;
                this.rowReverse = true;
            }

            void columnReverse(int j, int je, int jLimit, IntBiFunction jComp, IntFunction jAccum) {
                this.jComp = jComp;
                this.jAccum = jAccum;

                this.j = j;
                this.je = je;
                this.jLimit = jLimit;
                this.columnReverse = true;
            }

            TraversalNode transpose(int[] rowWidth) {
                TraversalNode node = new TraversalNode(
                        this.j, this.je, this.jLimit, this.jComp, this.jAccum,
                        this.i, this.ie, this.iLimit, this.iComp, this.iAccum,
                        ElementGather::getIndexSafeTranspose, rowWidth);

                node.transpose = true;
                node.rowReverse = this.rowReverse;
                node.columnReverse = this.columnReverse;
                return node;
            }
        }
    }
}
