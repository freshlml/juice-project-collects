package com.juice.alg.chapter2_8;


public class Chapter2_8_3 {

    /**
     *第一：基数排序: 段式排序。最高有效位；最低有效位
     *  1.最高有效位，如年-月-日，先按年排序，年相等的按月排序，月相等的按日排序
     *  2.最低有效位，如年-月-日，先按日排序，再按月排序，再按年排序
     *
     *第二：n个d位数的基数排序
     *  1.最高有效位
     *                                          "临时桶"                      "临时桶"
     *                                          2|329|
     *               "结果桶"                     |328|--------->2||--------->8|328| 🖊只当所有位比较完毕，填充回"结果桶"
     *    329        3|329|        3||---------->...                         9|329| 🖊只当所有位比较完毕，填充回"结果桶"
     *    328         |328|                     5|355| 🖊只有一个元素，无需比较下一位，填充回"结果桶"
     *    355         |355|
     *    657         ...          ...
     *    837        6|657|        6||--------->5|657|-------->7|657| 🖊当所有位比较完毕，结束。填充回"结果桶"
     *                |657|                      |657|          |657|
     *                ...          ...
     *               8|837| 🖊只有一个元素，无需比较下一位
     *   问题：控制流程杂乱，需要很多"临时桶"
     *  2.最低有效位
     *   329         355         328         328
     *   328         657         329         329
     *   355         657         837         355
     *   657         837         355         657
     *   657         328         657         657
     *   837         329         657         837
     *  要求: 每一位的比较都必须是稳定的，否则可能出错，如29，28，第一次对9，8排序得到28，29，第二次排序对2，2排序，如果不稳定，则将可以得到29，28这样的错误结果
     *
     *第三：扩展
     *  n个d位数，每一个位有k种取值 ==> d*(n+k)
     *
     *  扩展1.n个d位数(d>0)，定义 p = d/r      d%r=0,0<r<=d
     *                            d/r + 1  d%r>0,0<r<=d
     *       使用p作为比较次数，每次比较r位
     *     ==> p * (n + 10^r) 需要解出合适的r值，使之取值最小
     *      r <= d < log10(n) ==> r=d时最优 O(n)
     *      log10(n) < r <= d ==> r=log10(n)时最优 O[ d*n/log10(n) ]
     *
     *  扩展2.数字位数不同，使用0填充
     *
     */
    /**
     3 2 9
     3 2 8
     3 5 5
     6 5 7
     6 5 7
     8 3 7
     */
    public void radix_sort(int[][] a) {
        if(a == null || a.length == 0 || a.length == 1) return;

        int n = a.length;
        int d = a[0].length; //safe

        for(int j=d-1; j>=0; j--) {
            radix_counting_sort(a, j, 0, 10);
        }

    }
    public void radix_counting_sort(int a[][], int j, int begin, int end) {
        //assert begin < end

        int[] c = new int[end - begin];  //初始值为0

        for(int i=0; i<a.length; i++) {
            int idx = indexPos(a[i], j);
            c[idx] = c[idx] + 1;
        }

        for(int i=1; i<c.length; i++) {
            c[i] = c[i] + c[i-1];
        }

        int[][] b = new int[a.length][];
        for(int i=a.length-1; i>=0; i--) {
            int idx = indexPos(a[i], j);
            b[ c[idx] - 1 ] = a[i];
            c[idx] = c[idx] - 1;
        }

        //a = b;
        System.arraycopy(b, 0, a, 0, a.length);
    }
    public int indexPos(int[] ai, int j) {
        return ai[j];
    }

    /**
     * n个d位数(d>0)，定义 p = d/r      d%r=0,0<r<=d
     *                      d/r + 1  d%r>0,0<r<=d
     * 使用p作为比较次数，每次比较r位
     */
    public void radix_sort_r(int[][] a, int r) {
        if(a == null || a.length == 0 || a.length == 1) return;

        int n = a.length;
        int d = a[0].length; //safe
        //assert 0 < r <= d
        int p = d%r > 0 ? d/r+1 : d/r;

        for(int t=0; t<p; t++) {
            int end = Double.valueOf(Math.pow(10, Math.min(d-t*r, r))).intValue();
            radix_counting_sort_r(a, t, r, 0, end);
        }
    }
    public void radix_counting_sort_r(int a[][], int t, int r, int begin, int end) {
        //assert begin < end

        int[] c = new int[end - begin];  //初始值为0

        for(int i=0; i<a.length; i++) {
            int idx = indexPos_r(a[i], t, r);
            c[idx] = c[idx] + 1;
        }

        for(int i=1; i<c.length; i++) {
            c[i] = c[i] + c[i-1];
        }

        int[][] b = new int[a.length][];
        for(int i=a.length-1; i>=0; i--) {
            int idx = indexPos_r(a[i], t, r);
            b[ c[idx] - 1 ] = a[i];
            c[idx] = c[idx] - 1;
        }

        System.arraycopy(b, 0, a, 0, a.length);
    }
    public int indexPos_r(int[] ai, int t, int r) {
        int re = 0;
        for(int pos=(ai.length-1) - t*r, k=0; pos>=0 && k<r; pos--, k++) {
            re += ai[pos] * Double.valueOf(Math.pow(10, k)).intValue();
        }
        return re;
    }

    //练习8.3-2: 插入排序，归并排序，堆排序，快速排序都是稳定的，随机化版本快速排序不一定
    //给每一个值记录原始下标，排完序后，对连续的相等区间，根据原始下标排序

    //练习8.3-4: 3*[n + d(n)]


    public static void main(String argv[]) {
        Chapter2_8_3 chapter2_8_3 = new Chapter2_8_3();

        int a[][] = new int[][]{ {3,2,9}, {3,2,8}, {3,5,5}, {6,5,7}, {6,5,7}, {8,3,7} };
        chapter2_8_3.radix_sort(a);
        chapter2_8_3.print(a);

        int a2[][] = new int[][]{ {3,2,9}, {3,2,8}, {3,5,5}, {6,5,7}, {6,5,7}, {8,3,7} };
        chapter2_8_3.radix_sort_r(a2, 2);
        chapter2_8_3.print(a2);

    }

    public void print(int[][] a) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<a.length; i++) {
            sb.append("[ ");
            for(int j=0; j<a[i].length; j++) {
                sb.append(a[i][j]);
                if(j != a[i].length-1) {
                    sb.append(", ");
                    for (int k = 0; k < 11 - (a[i][j] + "").length(); k++)
                        sb.append(" ");
                }
            }
            sb.append(" ]\n");
        }
        System.out.println(sb.toString());
    }

}
