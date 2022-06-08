package com.juice.normal.test.gc;

public class GcTest {

    //private GcTest instance;
    private static final int _1M = 1024 * 1024;
    //private byte[] buffer = new byte[2 * _1M];

    public static void main(String argv[]) {
        /*GcTest gc1 = new GcTest();
        GcTest gc2 = new GcTest();

        gc1.instance = gc2;
        gc2.instance = gc1;

        gc1 = null;
        gc2 = null;

        System.gc();*/

        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1M];
        allocation2 = new byte[2 * _1M];
        allocation3 = new byte[2 * _1M];
        allocation4 = new byte[3 * _1M];

        //byte[] allocation5 = new byte[5* _1M];



    }
}
