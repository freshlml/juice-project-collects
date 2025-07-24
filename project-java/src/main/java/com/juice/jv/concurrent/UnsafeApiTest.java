package com.juice.jv.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeApiTest {

    static class NormalReadTest {
        private static int x;

        public static void main(String[] argv) {

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("update begin...");
                //x = 1;                                   //normal-write
                unsafe.putIntVolatile(NormalReadTest.class, xOffset, 1);  //volatile-write
                //unsafe.putOrderedInt(NormalReadTest.class, xOffset, 1);    //normal-write with store-store、load-store barrier
                //unsafe.putInt(NormalReadTest.class, xOffset, 1);           //normal-write
                System.out.println("update end...");
            }).start();

            int i = 0;
            while(x == 0) {                              //normal-read
                if(i == 0) {
                    System.out.println("running begin...");
                    i = 1;
                }
            }
            System.out.println("running end...");        //不会执行到此
        }

        private static final Unsafe unsafe;
        private static final long xOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);

                xOffset = unsafe.staticFieldOffset(NormalReadTest.class.getDeclaredField("x"));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class NormalPlusReadTest {
        private static int x;

        public static void main(String[] argv) {

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("update begin...");
                //x = 1;                                   //normal-write
                unsafe.putIntVolatile(NormalPlusReadTest.class, xOffset, 1);  //volatile-write
                //unsafe.putOrderedInt(NormalPlusReadTest.class, xOffset, 1);    //normal-write with store-store、load-store barrier
                //unsafe.putInt(NormalPlusReadTest.class, xOffset, 1);           //normal-write
                System.out.println("update end...");
            }).start();

            int i = 0;
            while(unsafe.getInt(NormalPlusReadTest.class, xOffset) == 0) {      //Is normalPlus-read? no, is normal-read
                if(i == 0) {
                    System.out.println("running begin...");
                    i = 1;
                }
            }
            System.out.println("running end...");        //不会执行到此
        }

        private static final Unsafe unsafe;
        private static final long xOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);

                xOffset = unsafe.staticFieldOffset(NormalPlusReadTest.class.getDeclaredField("x"));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class VolatileReadTest {
        private static int x;

        public static void main(String[] argv) {

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("update begin...");
                x = 1;                                   //normal-write
                //unsafe.putIntVolatile(VolatileReadTest.class, xOffset, 1);   //volatile-write
                //unsafe.putOrderedInt(VolatileReadTest.class, xOffset, 1);    //normal-write with store-store、load-store barrier
                //unsafe.putInt(VolatileReadTest.class, xOffset, 1);           //normal-write
                System.out.println("update end...");
            }).start();

            int i = 0;
            while(unsafe.getIntVolatile(VolatileReadTest.class, xOffset) == 0) {      //volatile-read
                if(i == 0) {
                    System.out.println("running begin...");
                    i = 1;
                }
            }
            System.out.println("running end...");
        }

        private static final Unsafe unsafe;
        private static final long xOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);

                xOffset = unsafe.staticFieldOffset(VolatileReadTest.class.getDeclaredField("x"));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


    static class CompareAndSwapTest {
        private static int x;

        public static void main(String[] argv) {

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("update begin...");
                x = 1;                                   //normal-write
                //unsafe.putIntVolatile(CompareAndSwapTest.class, xOffset, 1);   //volatile-write
                //unsafe.putOrderedInt(CompareAndSwapTest.class, xOffset, 1);    //normal-write with store-store、load-store barrier
                //unsafe.putInt(CompareAndSwapTest.class, xOffset, 1);           //normal-write
                System.out.println("update end...");
            }).start();

            int i = 0;
            while(!unsafe.compareAndSwapInt(CompareAndSwapTest.class, xOffset, 1, 0)) {
                                            //CAS 原子指令, 类 volatile-read, do not need variable modified with `volatile`
                if(i == 0) {
                    System.out.println("running begin...");
                    i = 1;
                }
            }
            System.out.println("running end...");
        }

        private static final Unsafe unsafe;
        private static final long xOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);

                xOffset = unsafe.staticFieldOffset(CompareAndSwapTest.class.getDeclaredField("x"));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class CompareAndSwapTest1 {
        private static int x;

        public static void main(String[] argv) {
            new Thread(() -> {
                for(int i=0; i < 100000; i++) {
                    int xv;
                    do {
                        xv = x;
                    } while(!unsafe.compareAndSwapInt(CompareAndSwapTest1.class, xOffset, xv, xv + 1));
                                                    //CAS 原子指令, 类 volatile-write, do not need variable modified with `volatile`
                    //x = x + 1;
                }
                System.out.println("one: " + x);
            }).start();

            /*try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            for(int i=0; i < 100000; i++) {
                int xv;
                do {
                    xv = x;
                } while(!unsafe.compareAndSwapInt(CompareAndSwapTest1.class, xOffset, xv, xv + 1));
                //x = x + 1;
            }
            System.out.println("two: " + x);
        }

        private static final Unsafe unsafe;
        private static final long xOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);

                xOffset = unsafe.staticFieldOffset(CompareAndSwapTest1.class.getDeclaredField("x"));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


    static class CompareAndSwapTestTODO {
        private static int x;

        public static void main(String[] argv) {
            new Thread(() -> {
                if(x == 0) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("one': " + x);
                    //unsafe.compareAndSwapInt(CompareAndSwapTestTODO.class, xOffset, 0, 1);
                    x = 1;
                }
                while(x == 1) {}  //System.out.println("one: " + x);
                System.out.println("one end...");
            }).start();

            if(x == 0) {
                try {
                    Thread.sleep(2010);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("two': " + x);
                //unsafe.compareAndSwapInt(CompareAndSwapTestTODO.class, xOffset, 0, 2);
                x = 2;
            }
            while(x == 2) {}  //System.out.println("two: " + x);
            System.out.println("two end...");
        }

        private static final Unsafe unsafe;
        private static final long xOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);

                xOffset = unsafe.staticFieldOffset(CompareAndSwapTestTODO.class.getDeclaredField("x"));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
