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
        Node root = Node.random();

        L_T_R0(root);
        System.out.println("\n--------L_T_R0----------------------------------------------");

        L_T_R1(root);
        System.out.println("\n--------L_T_R1----------------------------------------------");

        L_T_R2(root);
        System.out.println("\n--------L_T_R2----------------------------------------------");
    }

    //练习10.4-2
    static void L_T_R0(Node node) {
        if(node == null) return;

        L_T_R0(node.left);
        System.out.print(node.key);
        System.out.print(" ");
        L_T_R0(node.right);
    }

    //练习10-4-3
    static void L_T_R2(Node node) {
        if(node == null) return;

        Chapter10_1.FixedArrayDeque<Node> stack = new Chapter10_1.FixedArrayDeque<>();
        push_L_T_R(stack, node);
        while(!stack.isEmpty()) {
            Node t = stack.pop();
            System.out.print(t.key);
            System.out.print(" ");

            if(t.right != null) {
                push_L_T_R(stack, t.right);
            }
        }

    }

    //练习10.4-5
    static void L_T_R1(Node root) {
        if(root == null) return;

        Node t = firstKey_L_T_R(root);
        while(t != null) {
            System.out.print(t.key);
            System.out.print(" ");

            if(t.right != null) {
                t = firstKey_L_T_R(t.right);
            } else {
                Node pt = t.parent;
                while(pt != null && pt.right == t) {
                    t = pt;
                    pt = t.parent;
                }
                t = pt;
            }

        }
    }

    static Node firstKey_L_T_R(Node node) {
        //assert node != null
        while(node.left != null) {
            node = node.left;
        }
        return node;
    }

    static void push_L_T_R(Chapter10_1.FixedArrayDeque<Node> stack, Node node) {
        //assert node != null

        stack.push(node);
        while(node.left != null) {
            node = node.left;
            stack.push(node);
        }
    }

    static class Node {
        int key;
        Node parent, left, right;

        public Node(int key, Node parent) {
            this.key = key;
            this.parent = parent;
        }

        static Node random() {
            Node node1 = new Node(1, null);

            Node node2 = new Node(2, node1);
            Node node3 = new Node(3, node1);
            node1.left = node2;
            node1.right = node3;

            Node node4 = new Node(4, node2);
            Node node5 = new Node(5, node2);
            node2.left = node4;
            node2.right = node5;

            Node node6 = new Node(6, node3);
            Node node7 = new Node(7, node3);
            node3.left = node6;
            node3.right = node7;

            Node node8 = new Node(8, node4);
            Node node9 = new Node(9, node4);
            node4.left = node8;
            node4.right = node9;

            Node node10 = new Node(10, node6);
            Node node11 = new Node(11, node6);
            node6.left = node10;
            node6.right = node11;

            Node node12 = new Node(12, node9);
            Node node13 = new Node(13, node9);
            node9.left = node12;
            node9.right = node13;

            node10.right = new Node(14, node10);

            node13.left = new Node(15, node13);

            return node1;
        }
    }

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

    //练习10.4-6
    //  保留 left_child, right_sibling 指针, 最右孩子的 right_sibling 指向 parent.

}
