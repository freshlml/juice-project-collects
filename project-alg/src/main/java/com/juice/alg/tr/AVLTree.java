package com.juice.alg.tr;


public class AVLTree extends BSTree {

    protected void afterPut(Node pt, Node t) {
        balanced_put(pt);
    }

    private void balanced_put(Node node) {

        while(node != null) {
            int pt_b = getBalance(node);

            if(pt_b == 2) {
                Node left = node.getLeft();
                int t_b = getBalance(left);

                if(t_b == 1) {
                    LL(left, node);
                    break;
                } else if(t_b == -1) {
                    LR(left, node);
                    break;
                } //no possible 0
            } else if(pt_b == -2) {
                Node right = node.getRight();
                int t_b = getBalance(right);

                if(t_b == -1) {
                    RR(right, node);
                    break;
                } else if(t_b == 1) {
                    RL(right, node);
                    break;
                } //no possible 0
            }

            node = node.getParent();
        }

    }

    protected void afterRemove(Node node) {
        balanced_remove(node);
    }

    private void balanced_remove(Node node) {

        while(node != null) {
            int pt_b = getBalance(node);

            if(pt_b == 2) {
                Node left = node.getLeft();
                int t_b = getBalance(left);

                if(t_b == -1) {
                    LR(left, node);
                } else { //1 or 0
                    LL(left, node);
                }
            } else if(pt_b == -2) {
                Node right = node.getRight();
                int t_b = getBalance(right);

                if(t_b == 1) {
                    RL(right, node);
                } else { //-1 or 0
                    RR(right, node);
                }
            }

            node = node.getParent();
        }

    }

    private void RL(Node t, Node pt) {
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

    private void RR(Node t, Node pt) {
        left_rotate(pt);
    }

    private void LR(Node t, Node pt) {
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

    private void LL(Node t, Node pt) {
        right_rotate(pt);
    }

    private int getBalance(Node node) {
        //assert node != null
        return treeHeight(node.getLeft()) - treeHeight(node.getRight());
    }
    private int treeHeight(Node node) {
        if(node == null) return -1;

        int left = treeHeight(node.getLeft());
        int right = treeHeight(node.getRight());

        if(left >= right) return left + 1;
        else return right + 1;
    }

}
