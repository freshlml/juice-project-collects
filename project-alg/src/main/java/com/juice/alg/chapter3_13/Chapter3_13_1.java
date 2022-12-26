package com.juice.alg.chapter3_13;

import com.juice.alg.chapter3_10.Chapter3_10_4;

public class Chapter3_13_1 {

    /**
     *起因: 二叉搜索树，其高度h决定了其效率
     *目标: 找到某种方法，保证树的高度h尽量小
     *
     *第一: 平衡二叉树(AVL)
     *  性质:
     *      可为空树
     *      一个平衡二叉树的任何子树也是平衡的
     *      任何节点两棵子树的高度差不超过1
     *  平衡因子: 节点的左子树高度 - 右子树高度 为 节点的平衡因子
     *  最小失衡子树: 平衡因子超过1的最小子树
     *  分析: @see 纸张
     *
     *第二: 红黑树(RB-tree)
     *
     */


    static class AVL extends Chapter3_10_4.BinarySearchTree {

        @Override
        public void put(int key, int value) {
            if(root == null) {
                root = new Node(key, value, null);
            } else {
                Node t = root;
                Node pt = null;
                while(t != null) {
                    if(key < t.getKey()) {
                        pt = t;
                        t = t.getLeft();
                    } else if(key > t.getKey()) {
                        pt = t;
                        t = t.getRight();
                    } else { //duplicate key
                        t.setValue(value);
                        return;
                    }
                }
                Node newNode = new Node(key, value, pt);
                if(key < pt.getKey()) {
                    pt.setLeft(newNode);
                } else {
                    pt.setRight(newNode);
                }

                balanced(pt);

            }

            this.size++;
        }

        @Override
        public void remove(int key) {
            Node t = node(this.root, key);
            if(t == null) return;

            Node node = null;

            Node pt = t.getParent();
            Node left = t.getLeft();
            Node right = t.getRight();

            t.setParent(null);
            t.setLeft(null);
            t.setRight(null);

            if(left != null && right != null) {
                //使用 后继 替换
                Node next = firstKey_L_T_R(right);

                next.setLeft(left);
                left.setParent(next);

                node = next;
                if(next != right) {
                    Node next_pt = next.getParent();
                    Node next_r = next.getRight();

                    next_pt.setLeft(next_r);
                    if(next_r != null) {
                        next_r.setParent(next_pt);
                    }

                    next.setRight(right);
                    right.setParent(next);

                    node = next_pt;
                }

                transplant(t, pt, next);
            } else { //left == null || right == null
                if(left == null && right == null) {
                    transplant(t, pt, null);
                } else { //(left != null && right == null) || (left == null && right != null)
                    Node single_side = left != null ? left : right;
                    transplant(t, pt, single_side);
                }

                node = pt;
            }

            this.size--;
            balanced_remove(node);
        }

        private void balanced(Node node) {

            while(node != null) {
                int pt_b = getBalance(node);

                if(pt_b == 2) {
                    Node left = node.getLeft();
                    int t_b = getBalance(left);

                    if(t_b == 1) {
                        LL(left, node);
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
            Node ppt = pt.getParent();
            Node t_left = t.getLeft();
            Node t_left_left = t_left.getLeft();
            Node t_left_right = t_left.getRight();

            t_left.setParent(ppt);
            if(ppt == null) {
                this.root = t_left;
            } else {
                if(t_left.getKey() < ppt.getKey()) {
                    ppt.setLeft(t_left);
                } else {
                    ppt.setRight(t_left);
                }
            }

            t_left.setLeft(pt);
            pt.setParent(t_left);

            t_left.setRight(t);
            t.setParent(t_left);

            t.setLeft(t_left_right);
            if(t_left_right != null) {
                t_left_right.setParent(t);
            }
            pt.setRight(t_left_left);
            if(t_left_left != null) {
                t_left_left.setParent(pt);
            }

        }

        private void RR(Node t, Node pt) {
            Node ppt = pt.getParent();
            int flag = 0;
            if(ppt != null && pt.getKey() < ppt.getKey()) flag = 1;
            else if(ppt != null) flag = -1;

            Node t_left = t.getLeft();

            t.setParent(ppt);
            if(ppt == null) {
                this.root = t;
            } else {
                if(flag == 1) {
                    ppt.setLeft(t);
                } else if(flag == -1) {
                    ppt.setRight(t);
                }
            }

            t.setLeft(pt);
            pt.setParent(t);
            pt.setRight(t_left);
            if(t_left != null) {
                t_left.setParent(pt);
            }

        }

        private void LR(Node t, Node pt) {
            Node ppt = pt.getParent();
            Node t_right = t.getRight();
            Node t_right_left = t_right.getLeft();
            Node t_right_right = t_right.getRight();

            t_right.setParent(ppt);
            if(ppt == null) {
                this.root = t_right;
            } else {
                if(t_right.getKey() < ppt.getKey()) {
                    ppt.setLeft(t_right);
                } else {
                    ppt.setRight(t_right);
                }
            }

            t_right.setLeft(t);
            t.setParent(t_right);

            t_right.setRight(pt);
            pt.setParent(t_right);

            t.setRight(t_right_left);
            if(t_right_left != null) {
                t_right_left.setParent(t);
            }
            pt.setLeft(t_right_right);
            if(t_right_right != null) {
                t_right_right.setParent(pt);
            }

        }

        private void LL(Node t, Node pt) {
            Node ppt = pt.getParent();
            int flag = 0;
            if(ppt != null && pt.getKey() < ppt.getKey()) flag = 1;
            else if(ppt != null) flag = -1;

            Node t_right = t.getRight();

            t.setParent(ppt);
            if(ppt == null) {
                this.root = t;
            } else {
                if(flag == 1) {
                    ppt.setLeft(t);
                } else if(flag == -1) {
                    ppt.setRight(t);
                }
            }

            t.setRight(pt);
            pt.setParent(t);
            pt.setLeft(t_right);
            if(t_right != null) {
                t_right.setParent(pt);
            }

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


    public static void main(String argv[]) {

        AVL tree = new AVL();

        tree.put(7, 7);
        tree.put(9, 9);
        tree.put(10, 10);
        tree.put(12, 12);
        tree.put(14, 14);
        tree.put(20, 20);
        tree.put(15, 15);
        tree.put(30, 30);
        tree.put(25, 25);
        tree.put(40, 40);
        tree.put(24, 24);
        tree.put(1, 1);
        tree.put(3, 3);
        tree.put(6, 6);
        tree.put(5, 5);
        tree.put(2, 2);
        tree.put(4, 4);

        tree.BFS();
        System.out.println();
        tree.L_T_R();

        tree.remove(6);
        tree.BFS();
        System.out.println();

        tree.remove(14);
        tree.BFS();
        System.out.println();

        tree.remove(12);
        tree.remove(9);
        tree.BFS();
        System.out.println();

        tree.remove(24);
        tree.remove(20);
        tree.BFS();
        System.out.println();

    }


}
