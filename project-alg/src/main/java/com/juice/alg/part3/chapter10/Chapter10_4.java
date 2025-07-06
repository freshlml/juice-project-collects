package com.juice.alg.part3.chapter10;


public class Chapter10_4 {

    /**
     *二叉树
     *  二叉树的一个节点使用属性 p, left, right 分别存放指向父节点, 左孩子, 右孩子的指针。如果 x.p = null, 则 x 为根节点。
     *  如果 x 没有左孩子, 则 x.left = null。如果 x 没有右孩子, 则 x.right = null。
     *  使用 root 指向二叉树的根节点，如果 root = null, 则该树为空。
     *
     *  二叉搜索树是一个按照 "左小右大" 的规则组织起来的一个二叉树
     *
     *分支无限制的有根树
     *  使用 "左孩子右兄弟" 表示: 每个节点有一个父节点指针 p, left_child 指向该节点最左边的孩子节点, right_sibling 指向该节点右侧相邻的兄弟节点。
     *
     *  如果 x 没有孩子节点, x.left_child = null。如果 x 是其父节点的最右孩子, 则 x.right_sibling = null。
     *
     *树的其他表示方法
     *  - 使用数组表示的二叉堆可以看成是一棵近似的完全二叉树 {@link com.juice.alg.part2.chapter6.Chapter6}
     *  - ...
     */
    public static void main(String[] argv) {

    }

    //练习10.4-2
    /*void L_T_R(Node t) {
        if(t == null) return;
        L_T_R(t.left);
        print(t);
        L_T_R(t.right);
    }*/

    //练习10-4-3
    /*<E> void L_T_R_stack(Node<E> t) {
        if(t == null) return;
        FixedArrayDeque<E> stack = new FixedArrayDeque<>();
        stack.push(L_T_R_firstKey(t));
        while(!stack.isEmpty()) {
            print(t = stack.pop());
            if(t.right != null) {
                stack.push(L_T_R_firstKey(t.right));
            } else {
                Node<E> pt = t.parent;
                while(pt != null && pt.right == t) {
                    t = pt;
                    pt = t.parent;
                }
                if(pt != null)
                    stack.push(pt);
            }
        }
    }*/

    //练习10.4-4
    /*
    class Node {
        int key;
        Node parent;
        Node left_child;
        Node right_sibling;
    }

    void breadth_first(Node t) {
        while(t != null) {
            Node s = t;
            while(s != null) {
                print(s);
                s = s.right_sibling;
            }
            t = t.left_child;
        }
    }
    */

    //练习10.4-5
    /*private static void L_T_R1(Node root) {
        BSTree.Node t = firstKey_L_T_R(root);

        while(t != null) {
            System.out.print(t);

            if(t.right != null) {
                t = firstKey_L_T_R(t.right);
            } else {
                BSTree.Node pt = t.parent; //回溯
                BSTree.Node ch = t;

                while(pt != null && pt.right == ch) {
                    ch = pt;
                    pt = ch.parent;
                }
                t = pt;

            }

        }

    }*/

    //练习10.4-6
    //  保留 left_child, right_sibling 指针, 最右孩子的 right_sibling 指向 parent.

}
