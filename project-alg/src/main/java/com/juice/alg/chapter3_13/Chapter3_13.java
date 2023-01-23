package com.juice.alg.chapter3_13;


import com.juice.alg.tr.AVLTree;
import com.juice.alg.tr.BSTree;
import com.juice.alg.tr.RBTree;

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

        System.out.println("##############################");
        RBTree rb = new RBTree();

        rb.put(50, 50);
        rb.put(20, 20);
        rb.put(70, 70);
        rb.put(40, 40);
        rb.put(30, 30);
        rb.put(45, 45);
        rb.put(10, 10);
        rb.put(29, 29);
        rb.put(31, 31);
        rb.put(51, 51);
        rb.put(100, 100);
        rb.put(1, 1);
        rb.put(39, 39);
        rb.put(55, 55);
        rb.put(57, 57);
        rb.put(58, 58);

        rb.BFS();
        System.out.println();
        rb.L_T_R();

        /*rb.remove(30);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        /*rb.remove(58);
        rb.remove(51);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        /*rb.remove(1);
        rb.remove(10);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        /*rb.remove(100);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        /*rb.remove(55);
        rb.remove(51);
        rb.remove(58);
        rb.remove(57);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        rb.remove(55);
        rb.remove(51);
        rb.remove(58);
        rb.remove(39);
        rb.remove(31);
        rb.remove(100);
        rb.remove(70);
        rb.remove(45);
        rb.remove(57);
        rb.BFS();
        System.out.println();
        rb.L_T_R();
    }


    //练习13.2-1:
    /**@see com.juice.alg.tr.BSTree#right_rotate(BSTree.Node) */

    //练习13.2-2: 二叉搜索树中，所有可能的旋转数等于边的数量，而对于树，边的数量 = 节点数量-1

    //练习13.2-3: 二叉搜索树中，对某个节点右旋后，α子树深度-1，β子树深度不变，γ子树深度+1

    //二叉搜索树旋转之后，边的变化:
    //  右旋:
    //    1. 左边减一，右边加一，右边减一，左边加一。边总数不变，左边总数不变，右边总数不变
    //    2. 左边减一，右边加一。边总数不变，左边总数减一，右边总数加一
    //  左旋: ...
    //练习13.2-4: 根据如上结论即可证明

    //练习13.2-5:
    //1. 只需让T2的左边总数大于T1的左边总数，则T1不可能通过右旋得到T2。
    


}
