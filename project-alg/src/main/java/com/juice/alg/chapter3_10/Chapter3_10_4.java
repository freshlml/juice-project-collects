package com.juice.alg.chapter3_10;


public class Chapter3_10_4 {

    public static void main(String argv[]) {

        BinaryTree tree = new BinaryTree();
        tree.add(18);
        tree.add(12);
        tree.add(10);
        tree.add(7);
        tree.add(13);
        tree.add(15);
        tree.add(21);
        tree.add(19);
        tree.add(23);

        tree.T_L_R();

        tree.L_T_R();

        tree.L_R_T();


    }


    interface Tree {
        void add(int e);

        void T_L_R();
        void L_T_R();
        void L_R_T();

        int size();
        boolean isEmpty();
    }
    static class BinaryTree implements Tree {
        private Node root;
        private int size;

        public BinaryTree() {}

        @Override
        public void add(int e) {
            if(root == null) {
                root = new Node(e, null);
            } else {
                Node r = root;
                Node p = null;
                while(r != null) {
                    if(e < r.key) {
                        p = r;
                        r = r.left;
                    } else if(e > r.key) {
                        p = r;
                        r = r.right;
                    } else {
                        r.key = e;
                        return;
                    }
                }
                Node newNode = new Node(e, p);
                if(e < p.key) {
                    p.left = newNode;
                } else {
                    p.right = newNode;
                }
            }

            this.size++;
        }

        private static void T_L_R0(Node root) {
            if(root == null) return;

            System.out.print(root.key);
            System.out.print(" ");
            T_L_R0(root.left);
            T_L_R0(root.right);
        }
        private static void T_L_R1(Node root) {
            Node t = root;

            while(t != null) {
                System.out.print(t.key);
                System.out.print(" ");

                if(t.left != null) {
                    t = t.left;
                } else if(t.right != null) {
                    t = t.right;
                } else {
                    Node pt = t.parent; //回溯
                    Node ch = t;

                    while(pt != null && (pt.right == null || pt.right == ch)) {
                        ch = pt;
                        pt = ch.parent;
                    }
                    t = pt;
                    if(pt != null) {
                        t = pt.right;
                    }
                }
            }
        }
        @Override
        public void T_L_R() {
            T_L_R0(this.root);
            System.out.println("T_L_R0");
            T_L_R1(this.root);
            System.out.println("T_L_R1");
        }

        private static void L_T_R0(Node root) {
            if(root == null) return;

            L_T_R0(root.left);
            System.out.print(root.key);
            System.out.print(" ");
            L_T_R0(root.right);
        }
        private static Node firstKey_L_T_R(Node root) {
            if(root == null) return null;

            Node left = root;
            while(left.left != null) {
                left = left.left;
            }
            return left;
        }
        private static void L_T_R1(Node root) {
            Node t = firstKey_L_T_R(root);

            while(t != null) {
                System.out.print(t.key);
                System.out.print(" ");

                if(t.right != null) {
                    t = firstKey_L_T_R(t.right);
                } else {
                    Node pt = t.parent; //回溯
                    Node ch = t;

                    while(pt != null && pt.right == ch) {
                        ch = pt;
                        pt = ch.parent;
                    }
                    t = pt;
                }
            }
        }
        @Override
        public void L_T_R() {
            L_T_R0(this.root);
            System.out.println("L_T_R0");
            L_T_R1(this.root);
            System.out.println("L_T_R1");
        }

        private static void L_R_T0(Node root) {
            if(root == null) return;

            L_R_T0(root.left);
            L_R_T0(root.right);
            System.out.print(root.key);
            System.out.print(" ");
        }
        private static Node firstKey_L_R_T(Node root) {
            if(root == null) return null;

            Node left = root;
            while(left.left != null) {
                left = left.left;
            }

            if(left.right == null) {
                return left;
            } else {
                return firstKey_L_R_T(left.right);
            }

        }
        private static void L_R_T1_2(Node root) {
            Node t = firstKey_L_R_T(root);

            while(t != null) {

                if(t.right != null) {
                    t = firstKey_L_R_T(t.right);
                } else {
                    System.out.print(t.key);
                    System.out.print(" ");

                    Node pt = t.parent; //回溯
                    Node ch = t;

                    while(pt != null && pt.right == ch) {
                        System.out.print(pt.key);
                        System.out.print(" ");

                        ch = pt;
                        pt = ch.parent;
                    }
                    t = pt;
                }
            }
        }
        private static void L_R_T1(Node root) {
            Node t = firstKey_L_R_T(root);

            while(t != null) {
                System.out.print(t.key);
                System.out.print(" ");

                Node pt = t.parent;
                Node ch = t;
                if(pt == null) {
                    break;
                } else if(pt.right == ch) {
                    t = pt;
                } else if(pt.left == ch) {
                    if(pt.right == null) {
                        t = pt;
                    } else {
                        t = firstKey_L_R_T(pt.right);
                    }
                }

            }
        }
        @Override
        public void L_R_T() {
            L_R_T0(this.root);
            System.out.println("L_R_T0");
            L_R_T1(this.root);
            System.out.println("L_R_T1");
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.size == 0;
        }


        static class Node {
            int key;
            Node parent;
            Node left;
            Node right;

            public Node(int key, Node parent) {
                this.key = key;
                this.parent = parent;
            }
        }
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
