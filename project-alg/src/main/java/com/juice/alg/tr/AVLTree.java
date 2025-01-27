package com.juice.alg.tr;


public class AVLTree extends BSTree {

    @Override
    protected void afterPut(Node pt, Node t) {
        balanced_put(pt);
    }

    protected void balanced_put(Node node) {

        Node pt = node;
        while(pt != null) {
            int pt_b = getBalance(pt);

            if(pt_b == 2) {
                Node left = pt.getLeft();
                int t_b = getBalance(left);

                if(t_b == 1) {
                    LL(pt, left);
                    break;
                } else if(t_b == -1) {
                    LR(pt, left);
                    break;
                } //no possible 0
            } else if(pt_b == -2) {
                Node right = pt.getRight();
                int t_b = getBalance(right);

                if(t_b == -1) {
                    RR(pt, right);
                    break;
                } else if(t_b == 1) {
                    RL(pt, right);
                    break;
                } //no possible 0
            }

            pt = pt.getParent();
        }

    }

    @Override
    protected void afterRemove(Node pt, Node t, Node r) {
        super.afterRemove(pt, t, r);
        balanced_remove(pt);
    }

    protected void balanced_remove(Node node) {

        Node pt = node;
        while(pt != null) {
            int pt_b = getBalance(pt);

            if(pt_b == 2) {
                Node left = pt.getLeft();
                int t_b = getBalance(left);

                if(t_b == -1) {
                    Node right = left.getRight();
                    LR(pt, left);

                    pt = right.getParent();
                } else { //1 or 0
                    LL(pt, left);

                    pt = left.getParent();
                }
            } else if(pt_b == -2) {
                Node right = pt.getRight();
                int t_b = getBalance(right);

                if(t_b == 1) {
                    Node left = right.getLeft();
                    RL(pt, right);

                    pt = left.getParent();
                } else { //-1 or 0
                    RR(pt, right);

                    pt = right.getParent();
                }
            } else {
                pt = pt.getParent();
            }
        }
    }

    protected void RL(Node pt, Node t) {
        Node ppt = pt.parent;
        Node t_left = t.left;
        Node t_left_left = t_left.left;
        Node t_left_right = t_left.right;

        t_left.parent = ppt;
        if(ppt == null) {
            this.root = t_left;
        } else if(ppt.left == pt) {
            ppt.left = t_left;
        } else if(ppt.right == pt) {
            ppt.right = t_left;
        }

        t_left.left = pt;
        pt.parent = t_left;

        t_left.right = t;
        t.parent = t_left;

        t.left = t_left_right;
        if(t_left_right != null) {
            t_left_right.parent = t;
        }
        pt.right = t_left_left;
        if(t_left_left != null) {
            t_left_left.parent = pt;
        }

    }

    protected void RR(Node pt, Node t) {
        left_rotate(pt);
    }

    protected void LR(Node pt, Node t) {
        Node ppt = pt.parent;
        Node t_right = t.right;
        Node t_right_left = t_right.left;
        Node t_right_right = t_right.right;

        t_right.parent = ppt;
        if(ppt == null) {
            this.root = t_right;
        } else if(ppt.left == pt) {
            ppt.left = t_right;
        } else if(ppt.right == pt) {
            ppt.right = t_right;
        }

        t_right.left = t;
        t.parent = t_right;

        t_right.right = pt;
        pt.parent = t_right;

        t.right = t_right_left;
        if(t_right_left != null) {
            t_right_left.parent = t;
        }
        pt.left = t_right_right;
        if(t_right_right != null) {
            t_right_right.parent = pt;
        }

    }

    protected void LL(Node pt, Node t) {
        right_rotate(pt);
    }

    protected int getBalance(Node node) {
        //assert node != null
        return treeHeight(node.getLeft()) - treeHeight(node.getRight());
    }
    protected int treeHeight(Node node) {
        if(node == null) return -1;

        int left = treeHeight(node.getLeft());
        int right = treeHeight(node.getRight());

        if(left >= right) return left + 1;
        else return right + 1;
    }

}
