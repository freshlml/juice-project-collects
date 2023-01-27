package com.juice.alg.tr;


public interface Tree {

    void putAll(int[][] es);
    void put(int key, int value);
    void remove(int key);
    void update_key(int key, int key_added); //optional

    void T_L_R();
    void L_T_R();
    void L_R_T();
    void BFS();

    int size();
    boolean isEmpty();

    int min(); //二叉搜索树的最小节点
    int max(); //二叉搜索树的最大节点
    int search(int key);

    int next(int key); //successor(后继): 有序序列中给定节点的后面一个节点(互异序列中，大于给定节点值的最小节点)
    int prev(int key); //predecessor(前驱): 有序序列中给定节点的前面一个节点(互异序列中，小于给定节点值的最大节点)

    interface Entry {
        int getKey();
        int getValue();
        Entry getParent();
        Entry getLeft();
        Entry getRight();

        int setValue(int v);
    }


}
