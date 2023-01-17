package com.juice.alg.tr;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;


/*
树(tree): 树，子树，完美契合分治与递归
二叉搜索树(BST, binary-search-tree): 有根二叉树，对任意一个节点p，其左子树.key <= p.key；其右子树.key >= p.key

一个有序(逆序)序列A{a1, a2, a3, ..., an}依次插入，形成一棵树，树高 h = n-1
*/
public class BSTree implements Tree {
    protected Node root;
    protected int size;

    public BSTree() {}

    @Override
    public void putAll(int[][] es) {
        if (es == null) return;
        //assert es.length==0 || es[0].length >= 2
        for(int i=0; i<es.length; i++) {
            put(es[i][0], es[i][1]);
        }
    }

    @Override
    public void put(int key, int value) {
        if(this.root == null) {
            this.root = new Node(key, value, null);
        } else {
            Node t = root;
            Node pt = null;
            while(t != null) {
                if(key < t.key) {
                    pt = t;
                    t = t.left;
                } else if(key > t.key) {
                    pt = t;
                    t = t.right;
                } else { //duplicate key
                    t.value = value;
                    return;
                }
            }
            Node newNode = new Node(key, value, pt);
            if(key < pt.key) {
                pt.left = newNode;
            } else {
                pt.right = newNode;
            }

            afterPut(pt, newNode);
        }

        this.size++;
    }

    protected void afterPut(Node pt, Node t) {}

    protected void transplant(Node t, Node pt, Node next) {
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
    public void remove(int key) {
        Node t = node(this.root, key);
        if(t == null) return;

        Node node = null;

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

            node = next;
            if(next != right) {
                Node next_pt = next.parent;
                Node next_r = next.right;

                next_pt.left = next_r;
                if(next_r != null) {
                    next_r.parent = next_pt;
                }

                next.right = right;
                right.parent = next;

                node = next_pt;
            }

            transplant(t, pt, next);

            /*//使用right替换
            Node right_l = right.left;

            right.left = left;
            left.parent = right;

            if(right_l != null) {
                Node left_max = firstKey_R_T_L(left);
                left_max.right = right_l;
                right_l.parent = left_max;
            }

            transplant(t, pt, right);*/

        } else { //left == null || right == null
            if(left == null && right == null) {
                transplant(t, pt, null);
            } else { //(left != null && right == null) || (left == null && right != null)
                Node single_side = left != null ? left : right;
                transplant(t, pt, single_side);
            }

            node = pt;
        }

        afterRemove(node);
        this.size--;
    }

    protected void afterRemove(Node node) {}

    @Override
    public void update_key(int key, int key_added) {
        /*
        Node t = node(this.root, key);

        newKey = t.key + key_added;
        if key no change or not need move: return;
        else:
            remove;
            put;
         */
        throw new UnsupportedOperationException();
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
    protected static Node firstKey_L_T_R(Node root) {
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
    protected static Node firstKey_L_R_T(Node root) {
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
    /*private static void L_R_T1_2(Node root) {
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

    }*/
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

    /*
    二叉搜索树的旋转不改变二叉搜索树的性质
     1. left_rotate(Node pt):  对节点pt左旋
     2. right_rotate(Node pt): 对节点pt右旋
     */
    public void left_rotate(Node pt) {
        //assert pt != null && pt.right != null
        Node t = pt.right;
        Node ppt = pt.parent;
        Node t_left = t.left;

        t.parent = ppt;
        if(ppt == null) {
            this.root = t;
        } else if(ppt.left == pt) {
            ppt.left = t;
        } else if(ppt.right == pt) {
            ppt.right = t;
        }

        t.left = pt;
        pt.parent = t;
        pt.right = t_left;
        if(t_left != null) {
            t_left.parent = pt;
        }
    }
    public void right_rotate(Node pt) {
        //assert pt != null && pt.left != null
        Node t = pt.left;
        Node ppt = pt.parent;
        Node t_right = t.right;

        t.parent = ppt;
        if(ppt == null) {
            this.root = t;
        } else if(ppt.left == pt) {
            ppt.left = t;
        } else if(ppt.right == pt) {
            ppt.right = t;
        }

        t.right = pt;
        pt.parent = t;
        pt.left = t_right;
        if(t_right != null) {
            t_right.parent = pt;
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

    protected static Node firstKey_R_T_L(Node root) {
        if(root == null) return null;

        Node p = root;
        while(p.right != null) {
            p = p.right;
        }
        return p;
    }

    @Override
    public int max() {
        Node max = firstKey_R_T_L(this.root);
        if(max == null) throw new NoSuchElementException();

        return max.key;
    }

    protected static Node node(Node root, int key) {
        Node t = root;
        while(t != null) {
            if(t.key == key) {
                break;
            } else if(t.key < key) {
                t = t.right;
            } else {
                t = t.left;
            }
        }
        //if(t == null) throw new NoSuchElementException("key not exists");

        return t;
    }

    @Override
    public int search(int key) {
        Node t = node(this.root, key);
        if(t == null) throw new NoSuchElementException("key not exists");

        return t.value;
    }

    /*
    后继: 大于给定节点值的最小节点
        通过二叉搜索树的性质(节点p，其左子树.key <= p.key；其右子树.key >= p.key)，画图可证明next代码的逻辑正确 (练习12.2-5，练习12.2-6，练习12.2-9)

    此处"后继"与"L_T_R序列中的后继节点"有相同的逻辑
    推论: 二叉搜索树中，L_T_R序列是有序序列
     */
    @Override
    public int next(int key) {
        //find e
        Node t = node(this.root, key);
        if(t == null) throw new NoSuchElementException("key not exists");

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
            throw new NoSuchElementException("key'next not exists");
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
    public int prev(int key) {
        //find e
        Node t = node(this.root, key);
        if(t == null) throw new NoSuchElementException("key not exists");

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
            throw new NoSuchElementException("key'prev not exists");
        }

        return pt.key;
    }

    public static class Node implements Tree.Entry {
        int key;
        int value;
        Node parent;
        Node left;
        Node right;

        public Node(int key, int value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public int getValue() {
            return this.value;
        }

        @Override
        public Node getParent() {
            return this.parent;
        }

        @Override
        public Node getLeft() {
            return this.left;
        }

        @Override
        public Node getRight() {
            return this.right;
        }

        @Override
        public int setValue(int v) {
            int oldValue = this.value;
            this.value = v;
            return oldValue;
        }

    }
    
}
