package com.juice.alg.part3.chapter10;

import com.juice.alg.part3.chapter12.Chapter12;

public class Chapter10_4 {

    /**
     *二叉树
     *  二叉树的一个节点使用属性 p, left, right 分别存放指向父节点, 左孩子, 右孩子的指针。如果 x.p = null, 则 x 为根节点。
     *  如果 x 没有左孩子, 则 x.left = null。如果 x 没有右孩子, 则 x.right = null。
     *  使用 root 指向二叉树的根节点，如果 root = null, 则该树为空。
     *
     *  二叉搜索树是一个按照 "左小右大" 的规则组织起来的一个二叉树 {@link Chapter12}
     *
     *分支无限制的有根树
     *  使用 "左孩子右兄弟" 表示: 每个节点有一个父节点指针 p, left_child 指向该节点最左边的孩子节点,
     *  right_sibling 指向该节点右侧相邻的兄弟节点。
     *  如果 x 没有孩子节点, x.left_child = null。如果 x 是其父节点的最右孩子, 则 x.right_sibling = null。
     *
     *树的其他表示方法
     *  - 使用数组表示的二叉堆可以看成是一棵近似的完全二叉树 {@link com.juice.alg.part2.chapter6.Chapter6}
     *  - ...
     */
    public static void main(String[] argv) {

    }

    //左孩子右兄弟
    /*
    class Node {
        int key;
        Node parent;
        Node left_child;
        Node right_sibling;

        public Node(int key, Node parent) {
            this.key = key;
            this.parent = parent;
        }
    }

    //广度优先遍历
    Node t = root;
    while(t != null) {
        sb = t;
        while(sb != null) {
            print(sb.key);
            sb = sb.right_sibling;
        }

        t = t.left_child;
    }

    T_L_R(Node root):
        if root == null: return

        print(root.key)
        T_L_R(root.left_child)
        T_L_R(root.right_sibling)

    //练习10.4-6: left_child, right_sibling，最后一个sibling的right_sibling指向parent
     */

}
