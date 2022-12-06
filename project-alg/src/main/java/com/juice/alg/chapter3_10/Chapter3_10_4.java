package com.juice.alg.chapter3_10;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

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
        tree.add(24);
        tree.add(22);
        tree.add(25);
        tree.add(23);

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


    interface Tree {
        void addAll(int[] es);
        void add(int e);
        void remove(int e);

        void T_L_R();
        void L_T_R();
        void L_R_T();
        void BFS();

        int size();
        boolean isEmpty();

        int min(); //二叉搜索树的最小节点
        int max(); //二叉搜索树的最大节点
        int search(int e);

        int next(int e); //后继: 大于给定节点值的最小节点
        int prev(int e); //前驱: 小于给定节点值的最大节点

    }
    /*
    树: 树，子树，完美契合分治与递归
    二叉搜索树: 有根二叉树，对任意一个节点p，其左子树.key <= p.key；其右子树.key >= p.key
     */
    static class BinaryTree implements Tree {
        private Node root;
        private int size;

        public BinaryTree() {}

        /*
        一个序列A{a1, a2, a3, ..., an}依次插入，形成一棵树
        1. 如果序列有序或者逆序，将得到完全单链形式的二叉树，树高 h = n-1
         */
        @Override
        public void addAll(int[] es) {
            if (es == null) return;
            for(int i=0; i<es.length; i++) {
                add(es[i]);
            }
        }

        @Override
        public void add(int e) {
            if(root == null) {
                root = new Node(e, null);
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
                Node newNode = new Node(e, pt);
                if(e < pt.key) {
                    pt.left = newNode;
                } else {
                    pt.right = newNode;
                }
            }

            this.size++;
        }

        private void transplant(Node t, Node pt, Node next) {
            if(next != null) {
                next.parent = pt;
            }
            if(pt != null) {
                if(t == pt.left) {
                    pt.left = next;
                } else {
                    pt.right = next;
                }
            } else {
                this.root = next;
            }
        }

        @Override
        public void remove(int e) {
            Node t = node(this.root, e);

            Node pt = t.parent;
            Node left = t.left;
            Node right = t.right;

            t.parent = null;
            t.left = null;
            t.right = null;

            if(left != null && right != null) {
                //使用 后继 替换
                Node next = firstKey_L_T_R(right);

                next.left = left;
                left.parent = next;

                if(next != right) {
                    Node next_pt = next.parent;
                    Node next_r = next.right;

                    next_pt.left = next_r;
                    if(next_r != null) {
                        next_r.parent = next_pt;
                    }

                    next.right = right;
                    right.parent = next;
                }

                transplant(t, pt, next);
                /*next.parent = pt;
                if(pt != null) {
                    if(t == pt.left) {
                        pt.left = next;
                    } else {
                        pt.right = next;
                    }
                } else {
                    this.root = next;
                }*/

                //使用right替换
                /*Node right_l = right.left;

                right.left = left;
                left.parent = right;

                if(right_l != null) {
                    Node left_max = firstKey_R_T_L(left);
                    left_max.right = right_l;
                    right_l.parent = left_max;
                }

                transplant(t, pt, right);
//                right.parent = pt;
//                if(pt != null) {
//                    if(t == pt.left) {
//                        pt.left = right;
//                    } else {
//                        pt.right = right;
//                    }
//                } else {
//                    this.root = right;
//                }
                */
            } else { //left == null || right == null
                if(left == null && right == null) {
                    transplant(t, pt, null);
                    /*if(pt != null) {
                        if(t == pt.left) {
                            pt.left = null;
                        } else {
                            pt.right = null;
                        }
                    } else {
                        this.root = null;
                    }*/
                } else { //(left != null && right == null) || (left == null && right != null)
                    Node single_side = left != null ? left : right;
                    transplant(t, pt, single_side);
                    /*single_side.parent = pt;
                    if(pt != null) {
                        if(t == pt.left) {
                            pt.left = single_side;
                        } else {
                            pt.right = single_side;
                        }
                    } else {
                        this.root = single_side;
                    }*/
                }

            }

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
        private static void T_L_R2(Node root) {
            if(root == null) return;

            ArrayDeque<Node> stack = new ArrayDeque<>(4);
            stack.push(root);

            while(!stack.isEmpty()) {
                Node top = stack.pop();
                System.out.print(top.key);
                System.out.print(" ");

                if(top.right != null) {
                    stack.push(top.right);
                }
                if(top.left != null) {
                    stack.push(top.left);
                }

            }

        }
        @Override
        public void T_L_R() {
            T_L_R0(this.root);
            System.out.println("T_L_R0");
            T_L_R1(this.root);
            System.out.println("T_L_R1");
            T_L_R2(this.root);
            System.out.println("T_L_R2");
        }

        private static void L_T_R0(Node root) {
            if(root == null) return;

            L_T_R0(root.left);
            System.out.print(root.key);
            System.out.print(" ");
            L_T_R0(root.right);
        }

        private static Node firstKey_R_T_L(Node root) {
            if(root == null) return null;

            Node p = root;
            while(p.right != null) {
                p = p.right;
            }
            return p;
        }

        private static Node firstKey_L_T_R(Node root) {
            if(root == null) return null;

            Node p = root;
            while(p.left != null) {
                p = p.left;
            }
            return p;
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
        private static void push_L_T_R(ArrayDeque<Node> stack, Node root) {
            if(root == null) return;

            Node p = root;
            stack.push(p);
            while(p.left != null) {
                p = p.left;
                stack.push(p);
            }
        }
        private static void L_T_R2(Node root) {
            if(root == null) return;

            ArrayDeque<Node> stack = new ArrayDeque<>();
            push_L_T_R(stack, root);

            while(!stack.isEmpty()) {
                Node top = stack.pop();
                System.out.print(top.key);
                System.out.print(" ");

                if(top.right != null) {
                    push_L_T_R(stack, top.right);
                }

            }

        }
        @Override
        public void L_T_R() {
            L_T_R0(this.root);
            System.out.println("L_T_R0");
            L_T_R1(this.root);
            System.out.println("L_T_R1");
            L_T_R2(this.root);
            System.out.println("L_T_R2");
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
        private static void push_L_R_T(ArrayDeque<Node> stack, Node root) {
            if(root == null) return;

            Node p = root;
            stack.push(p);
            while(p.left != null) {
                p = p.left;
                stack.push(p);
            }

            if(p.right != null) {
                push_L_R_T(stack, p.right);
            }
        }
        private static void L_R_T2(Node root) {
            if(root == null) return;

            ArrayDeque<Node> stack = new ArrayDeque<>();
            push_L_R_T(stack, root);

            while(!stack.isEmpty()) {
                Node top = stack.pop();
                System.out.print(top.key);
                System.out.print(" ");

                Node pt = top.parent;
                if(pt != null && pt.left == top && pt.right != null) {
                    push_L_R_T(stack, pt.right);
                }

            }

        }
        @Override
        public void L_R_T() {
            L_R_T0(this.root);
            System.out.println("L_R_T0");
            L_R_T1(this.root);
            System.out.println("L_R_T1");
            L_R_T2(this.root);
            System.out.println("L_R_T2");
        }

        @Override
        public void BFS() {
            if(this.root == null) return;

            ArrayDeque<Node> queue = new ArrayDeque<>();
            queue.offer(this.root);

            while(!queue.isEmpty()) {
                Node current = queue.poll();
                System.out.print(current.key);
                System.out.print(" ");

                if(current.left != null) {
                    queue.offer(current.left);
                }
                if(current.right != null) {
                    queue.offer(current.right);
                }

            }

        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.size == 0;
        }

        @Override
        public int min() {
            Node min = firstKey_L_T_R(this.root);
            if(min == null) throw new NoSuchElementException();

            return min.key;
        }

        @Override
        public int max() {
            Node max = firstKey_R_T_L(this.root);
            if(max == null) throw new NoSuchElementException();

            return max.key;
        }

        private static Node node(Node root, int e) {
            Node t = root;
            while(t != null) {
                if(t.key == e) {
                    break;
                } else if(t.key < e) {
                    t = t.right;
                } else {
                    t = t.left;
                }
            }
            if(t == null) throw new NoSuchElementException("e not exists");

            return t;
        }

        @Override
        public int search(int e) {
            Node t = node(this.root, e);
            return t.key;
        }

        /*
        后继: 大于给定节点值的最小节点
            通过二叉搜索树的性质(节点p，其左子树.key <= p.key；其右子树.key >= p.key)，画图可证明next代码的逻辑正确 (练习12.2-5，练习12.2-6，练习12.2-9)

        此处"后继"与"L_T_R序列中的后继节点"有相同的逻辑
        推论: 二叉搜索树中，L_T_R序列是有序序列
         */
        @Override
        public int next(int e) {
            //find e
            Node t = node(this.root, e);

            //right
            if(t.right != null) {
                Node min = firstKey_L_T_R(t.right);
                return min.key;
            }

            //parent
            Node pt = t.parent;
            while(pt != null && pt.right == t) {
                t = pt;
                pt = t.parent;
            }
            if(pt == null) {
                throw new NoSuchElementException("e'next not exists");
            }

            return pt.key;
        }

        /*
        前驱: 小于给定节点值的最大节点
            通过二叉搜索树的性质(节点p，其左子树.key <= p.key；其右子树.key >= p.key)，画图可证明prev代码的逻辑正确 (练习12.2-5，练习12.2-6，练习12.2-9)

        此处"前驱"与"L_T_R序列中的前驱节点"有相同的逻辑
        同时因为，L_T_R序列中的前驱节点  <==>  R_T_L序列中的后继节点
        因此此处"前驱"与"R_T_L序列中的后继节点"有相同的逻辑

        推论: 二叉搜索树中，R_T_L序列是逆序序列
         */
        @Override
        public int prev(int e) {
            //find e
            Node t = node(this.root, e);

            //left
            if(t.left != null) {
                Node max = firstKey_R_T_L(t.left);
                return max.key;
            }

            //parent
            Node pt = t.parent;
            while(pt != null && pt.left == t) {
                t = pt;
                pt = t.parent;
            }
            if(pt == null) {
                throw new NoSuchElementException("e'prev not exists");
            }

            return pt.key;
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
    add(int e): 额外维护succ指针，画图(两种情况)
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