package com.juice.alg.chapter3_12;


import com.juice.alg.tr.BSTree;

public class Chapter3_12_1 {

    public static void main(String[] argv) {

        BSTree tree = new BSTree();
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

        /*tree.remove(7);
        tree.BFS();*/

        /*tree.remove(13);
        tree.BFS();*/

        /*tree.remove(21);
        tree.BFS();*/

        tree.remove(19);
        tree.remove(18);
        tree.BFS();
    }

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


}
