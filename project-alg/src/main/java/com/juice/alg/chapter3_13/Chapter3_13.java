package com.juice.alg.chapter3_13;


import com.juice.alg.tr.AVLTree;

public class Chapter3_13 {

    /**
     *起因: 二叉搜索树，其高度h决定了其效率
     *目标: 找到某种方法，保证树的高度h尽量小
     *
     *第一: 平衡二叉树(AVL)
     *  性质:
     *      可为空树
     *      一个平衡二叉树的任何子树也是平衡的
     *      任何节点两棵子树的高度差不超过1
     *  平衡因子: 节点的左子树高度 - 右子树高度 为 节点的平衡因子
     *  最小失衡子树: 平衡因子绝对值超过1的最小子树
     *  分析: @see 纸张
     * @see com.juice.alg.tr.AVLTree
     *第二: 红黑树(RB-tree)
     *  性质:
     *      每个节点带颜色属性，或红色，或黑色
     *      根节点是黑色
     *      叶节点(NIL节点)是黑色的
     *      如果一个节点是红色的，则它的两个子节点是黑色的
     *      对每个节点，从该节点到其所有后代叶节点(NIL节点)的简单路径上，均包含相同数目的黑节点
     *  black-height: 从某一结点出发(不含该节点)，到达任意一个叶节点的简单路径上黑色节点的个数为该节点的black-height
     * @see com.juice.alg.tr.RBTree
     */



    public static void main(String argv[]) {

        AVLTree tree = new AVLTree();

        tree.put(7, 7);
        tree.put(9, 9);
        tree.put(10, 10);
        tree.put(12, 12);
        tree.put(14, 14);
        tree.put(20, 20);
        tree.put(15, 15);
        tree.put(30, 30);
        tree.put(25, 25);
        tree.put(40, 40);
        tree.put(24, 24);
        tree.put(1, 1);
        tree.put(3, 3);
        tree.put(6, 6);
        tree.put(5, 5);
        tree.put(2, 2);
        tree.put(4, 4);

        tree.BFS();
        System.out.println();
        tree.L_T_R();

        tree.remove(6);
        tree.BFS();
        System.out.println();

        tree.remove(14);
        tree.BFS();
        System.out.println();

        tree.remove(12);
        tree.remove(9);
        tree.BFS();
        System.out.println();

        tree.remove(24);
        tree.remove(20);
        tree.BFS();
        System.out.println();

    }


}