package com.juice.alg.chapter3_10;

import com.juice.alg.tr.BinarySearchTree;

public class Chapter3_10_4 {

    public static void main(String argv[]) {

        BinarySearchTree tree = new BinarySearchTree();
        tree.put(18, 18);
        tree.put(12, 12);
        tree.put(10, 10);
        tree.put(7, 7);
        tree.put(13, 13);
        tree.put(15, 15);
        tree.put(21, 21);
        tree.put(19, 19);
        tree.put(24, 24);
        tree.put(22, 22);
        tree.put(25, 25);
        tree.put(23, 23);

        tree.T_L_R();

        tree.L_T_R();

        tree.L_R_T();


        System.out.println("prev: " + tree.prev(19));
        System.out.println("next: " + tree.next(19));

        tree.BFS();
        System.out.println();
        tree.remove(10);
        tree.BFS();

    }

    /**
     *二叉搜索树, {@link com.juice.alg.tr.BinarySearchTree}
     */


    //练习12.3-5
    /*
    static class Node {
        int key;
        Node parent;
        Node left;
        Node right;
        Node succ; //额外succ指针，指向后继

        public Node(int key, Node parent) {
            this.key = key;
            this.parent = parent;
        }
    }
    L_T_R:
        if(this.root == null) return;
        Node t = firstKey_L_T_R(this.root);
        while(t != null) {
            print t.key;
            t = t.succ;
        }
    put(int e): 额外维护succ指针，画图(两种情况)
        if(this.root == null) {
            this.root = new Node(e, null);
        } else {
            Node t = root;
            Node pt = null;
            while(t != null) {
                if(e < t.key) {
                    pt = t;
                    t = t.left;
                } else if(e > t.key) {
                    pt = t;
                    t = t.right;
                } else { //duplicate key
                    t.key = e;
                    return;
                }
            }
            Node ppt = pt.parent;
            Node t = new Node(e, pt);
            if(e < pt.key) {
                pt.left = t;

                if(ppt != null && ppt.right == pt) {
                    ppt.succ = t;
                    t.succ = pt
                } else {
                    t.succ = pt;
                }
            } else {
                pt.right = t;

                if(ppt != null && ppt.left == pt) {
                    pt.succ = t;
                    t.succ = ppt;
                } else {
                    pt.succ = t;
                }
            }
        }

        this.size++;
     remove(int e):
        只需额外维护succ指针，将t的前驱.succ = next即可
     */


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
