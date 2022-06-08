package com.juice.alg.chapter1_4;

public class Chapter1_4_3 {

    //思考题4-5,芯片检测

    //证明: 如果超过n/2块芯片是坏的，则任何基于这种逐对检测的策略都无法确定出一块好的芯片
    //两两检测，1对多检

    /*现假定 n个芯片中超过n/2块芯片是好的
    求如何找出一块好的芯片

    两两测试，如果测试结果是好-好，A、B中留一片丢一片。
    如果是其他情况全丢掉，剩下的芯片仍然满足好芯片多于坏芯片的假设。

    T(n) = T(n/2) + O(n)
     */
    //有此假定，该问题才有解
    int find(int a[]) {

        int n = a.length;

        if(n == 1) return a[0];
        if(n == 2) return a[0];

        boolean flag = false;
        if(n % 2 != 0) flag = true; //奇数
        int[] b = new int[flag?n/2+1:n/2];
        //两两配对
        int j = 0;
        for(int i=0; i<n/2; i++) {
            if(check(a[i], a[i+n/2])) { //好-好，保留任意一个
                b[j++] = a[i];
            }
            //其他舍弃
        }
        if(flag) b[j] = a[n-1];

        int r = find(b);

        return r;
    }
    boolean check(int a, int b) {return false;}

}
