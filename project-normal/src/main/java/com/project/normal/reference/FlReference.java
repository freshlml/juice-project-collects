package com.project.normal.reference;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FlReference {

    public static void main(String argv[]) throws Exception {

        /**同一个ThreadLocal有相同的threadLocalHashCode值
         * 不同ThreadLocal在同一个Thread中发生hash冲突时，找下一个空位 */
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("111");
        Thread thread = Thread.currentThread();
        threadLocal.set("222");

        ThreadLocal<String> theOtherThreadLocal = new ThreadLocal<>();
        theOtherThreadLocal.set("333");

        //threadLocal中ThreadLocalMap对key(ThreadLocal对象)的弱引用
        threadLocal = null;
        System.gc(); //threadLocal指向的对象被清理，Thread中ThreadLocalMap的key变成null,"value仍然指向原来的引用",在ThreadLocalMap代码中会判断key==null就发生清理行为
        System.out.println("threadLocal");

        /**WeakReference: 被WeakReference引用的对象不会影响其gc，即如果没有其他强引用指向该对象，该对象在gc时会被回收，就算该对象存在WeakReference */
        /**弱引用ref指向对象Obj，下一次gc时，仅被弱引用指向的对象Obj被回收*/
        WeakReference<Object> ref = new WeakReference<>(new Obj());
        System.out.println(ref.get());
        System.gc();
        System.out.println(ref.get());

        /**HashMap 强引用 */
        Map<Object, String> map = new HashMap<>();
        map.put(new Obj(), "Map");
        System.out.println(map);
        System.gc();
        System.out.println(map);

        /**WeakHashMap (key的)弱引用 */
        Map<Object, Object> weakMap = new WeakHashMap<>();
        //String weakKey = new String("123"); //String weakKey = "123";此处weakKey指向常量池中字符串123
        Obj weakKey = new Obj();
        Object value = new Object();
        weakMap.put(weakKey, new Object());
        System.out.println(weakMap);
        weakKey = null;  //如果还存在其他的强引用，被指向的对象不会被回收
        System.gc();
        System.out.println(weakMap); //weakKey被回收之后，weakMap的value置为空(防止内存泄露)[weakMap的整个entry都会被置空]



    }
    static class Obj {
        private static AtomicInteger nextHashCode = new AtomicInteger();
        private final int hash = nextHashCode();
        private static final int HASH_INCREMENT = 1;
        private static int nextHashCode() {
            return nextHashCode.getAndAdd(HASH_INCREMENT);
        }

        @Override
        public String toString() {
            return hash+"";
        }
    }
}
