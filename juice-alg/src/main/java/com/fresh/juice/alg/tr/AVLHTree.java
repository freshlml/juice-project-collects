package com.fresh.juice.alg.tr;


public class AVLHTree extends AVLTree {

    @Override
    public void put(int key, int value) {
        if(this.root == null) {
            this.root = new Node(key, value, null, 0);
        } else {
            Node t = (Node) this.root;
            Node pt = null;
            while(t != null) {
                if(key < t.key) {
                    pt = t;
                    t = (Node) t.left;
                } else if(key > t.key) {
                    pt = t;
                    t = (Node) t.right;
                } else { //duplicate key
                    t.value = value;
                    return;
                }
            }
            Node newNode = new Node(key, value, pt, 0);
            if(key < pt.key) {
                pt.left = newNode;
            } else {
                pt.right = newNode;
            }

            afterPut(pt, newNode);
        }

        this.size++;
    }

    @Override
    protected void balanced_put(BSTree.Node node) {

        Node pt = (Node) node;
        while(pt != null) {
            int pt_b = getBalance(pt);

            if(pt_b == 2) {
                Node left = (Node) pt.left;
                int t_b = getBalance(left);

                if(t_b == 1) {
                    LL(pt, left);

                    pt.h = calHeight(pt);
                    left.h = calHeight(left);
                    break;
                } else if(t_b == -1) {
                    Node right = (Node) left.right;
                    LR(pt, left);

                    pt.h = calHeight(pt);
                    left.h = calHeight(left);
                    right.h = calHeight(right);
                    break;
                } //no possible 0
            } else if(pt_b == -2) {
                Node right = (Node) pt.right;
                int t_b = getBalance(right);

                if(t_b == -1) {
                    RR(pt, right);

                    pt.h = calHeight(pt);
                    right.h = calHeight(right);
                    break;
                } else if(t_b == 1) {
                    Node left = (Node) right.left;
                    RL(pt, right);

                    pt.h = calHeight(pt);
                    right.h = calHeight(right);
                    left.h = calHeight(left);
                    break;
                } //no possible 0
            } else {
                pt.h = calHeight(pt);
            }

            pt = (Node) pt.parent;
        }

    }

    @Override
    protected void balanced_remove(BSTree.Node node) {

        Node pt = (Node) node;
        while(pt != null) {
            int pt_b = getBalance(pt);

            if(pt_b == 2) {
                Node left = (Node) pt.left;
                int t_b = getBalance(left);

                if(t_b == -1) {
                    Node right = (Node) left.right;
                    LR(pt, left);

                    pt.h = calHeight(pt);
                    left.h = calHeight(left);
                    right.h = calHeight(right);
                    pt = (Node) right.parent;
                } else { //1 or 0
                    LL(pt, left);

                    pt.h = calHeight(pt);
                    left.h = calHeight(left);
                    pt = (Node) left.parent;
                }
            } else if(pt_b == -2) {
                Node right = (Node) pt.right;
                int t_b = getBalance(right);

                if(t_b == 1) {
                    Node left = (Node) right.left;
                    RL(pt, right);

                    pt.h = calHeight(pt);
                    right.h = calHeight(right);
                    left.h = calHeight(left);
                    pt = (Node) left.parent;
                } else { //-1 or 0
                    RR(pt, right);

                    pt.h = calHeight(pt);
                    right.h = calHeight(right);
                    pt = (Node) right.parent;
                }
            } else {
                pt.h = calHeight(pt);
                pt = (Node) pt.parent;
            }
        }

    }

    private int calHeight(Node node) {
        //assert node != null
        return Math.max(treeHeight(node.left), treeHeight(node.right)) + 1;
    }

    @Override
    protected int treeHeight(BSTree.Node node) {
        if(node == null) return -1;
        if(node instanceof Node)
            return ((Node) node).h;
        else return super.treeHeight(node);
    }

    static class Node extends BSTree.Node {
        int h;

        public Node(int key, int value, Node parent) {
            super(key, value, parent);
        }

        public Node(int key, int value, Node parent, int height) {
            super(key, value, parent);
            this.h = height;
        }

        public int getHeight() {
            return this.h;
        }
        public int setHeight(int h) {
            int oldHeight = this.h;
            this.h = h;
            return oldHeight;
        }

        @Override
        public String toString() {
            return this.key + "(" + this.h + ")" + " ";
        }
    }
}
