package com.juice.alg.tr;

public class RBTree extends BSTree {

    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    @Override
    public void put(int key, int value) {
        if(this.root == null) {
            this.root = new Node(key, value, null, BLACK);
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
            Node newNode = new Node(key, value, pt, RED);
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
    protected void afterPut(BSTree.Node pt, BSTree.Node t) {
        fixup_put((Node) t);
    }

    private void fixup_put(Node z) {

        Node pz = (Node)z.parent;
        while(pz != null && pz.color == RED) {
            Node ppz = (Node) pz.parent; //not null

            if(ppz.left == pz) {
                Node y = (Node) ppz.right; //may null
                if(y != null && y.color == RED) {
                    ppz.color = RED;
                    pz.color = BLACK;
                    y.color = BLACK;

                    z = ppz;
                    pz = (Node) z.parent;
                } else {
                    if(pz.right == z) {
                        z = pz;
                        left_rotate(z);
                        pz = (Node) z.parent;
                        ppz = (Node) pz.parent;
                    }
                    pz.color = BLACK;
                    ppz.color = RED;
                    right_rotate(ppz);
                }

            } else if(ppz.right == pz) {
                Node y = (Node) ppz.left; //may null
                if(y != null && y.color == RED) {
                    ppz.color = RED;
                    pz.color = BLACK;
                    y.color = BLACK;

                    z = ppz;
                    pz = (Node) z.parent;
                } else {
                    if(pz.left == z) {
                        z = pz;
                        right_rotate(z);
                        pz = (Node) z.parent;
                        ppz = (Node) pz.parent;
                    }
                    pz.color = BLACK;
                    ppz.color = RED;
                    left_rotate(ppz);
                }

            }
        }

        ((Node) this.root).color = BLACK;
    }

/*
    @Override
    public void remove(int key) {
        Node t = (Node) node(this.root, key);
        if(t == null) return;

        Node pt = (Node) t.parent;
        Node left = (Node) t.left;
        Node right = (Node) t.right;

        t.parent = null;
        t.left = null;
        t.right = null;

        Position position = null;
        boolean color = t.color;
        Node node = pt;
        if(node != null) {
            if(t == node.left) position = Position.LEFT;
            else position = Position.RIGHT;
        }

        if(left != null && right != null) {
            //使用 后继 替换
            Node next = (Node) firstKey_L_T_R(right);

            next.left = left;
            left.parent = next;

            color = next.color;
            node = next;
            position = Position.RIGHT;
            if(next != right) {
                Node next_pt = (Node) next.parent;
                Node next_r = (Node) next.right;

                node = next_pt;
                position = Position.LEFT;

                next_pt.left = next_r;
                if(next_r != null) {
                    next_r.parent = next_pt;
                }

                next.right = right;
                right.parent = next;
            }

            next.color = t.color;
            transplant(t, pt, next);
        } else { //left == null || right == null
            if(left == null && right == null) {
                transplant(t, pt, null);
            } else { //(left != null && right == null) || (left == null && right != null)
                Node single_side = left != null ? left : right;
                color = single_side.color;
                single_side.color = t.color;
                transplant(t, pt, single_side);
            }
        }

        fixup_remove(color, position, node);
        this.size--;
    }
*/

    @Override
    protected void afterRemove(BSTree.Node pt, BSTree.Node t, BSTree.Node r) {
        boolean color = ((Node) t).color;
//        if(r != null && ((Node) r).color == RED && pt == null && ((Node) t).color == BLACK) {
//            color = RED;
//            ((Node) r).color = BLACK;
//        }
        if(r != null) {
            color = RED;
            ((Node) r).color = BLACK;
        }
        Position position = null;
        if(pt != null) position = pt.left == t ? Position.LEFT : Position.RIGHT;

        super.afterRemove(pt, t, r);

        fixup_remove(color, position, (Node) pt);
    }

    /*
    param color: node 的position方向子树，少了一个color颜色节点
     */
    private void fixup_remove(boolean color, Position position, Node node) {

        while(color == BLACK && node != null) {
            if(position == Position.LEFT) {
                Node w = (Node) node.right;
                if(w.color == RED) {
                    node.color = RED;
                    w.color = BLACK;
                    left_rotate(node);
                    w = (Node) node.right;
                }

                if( (w.left == null && w.right == null) ||
                    (w.left != null && ((Node) w.left).color == BLACK && w.right != null && ((Node) w.right).color == BLACK) ) {

                    w.color = RED;
                    if(node.color == BLACK) {
                        //color = BLACK;
                        Node node_p = (Node) node.parent;
                        if(node_p != null) position = node_p.left == node ? Position.LEFT : Position.RIGHT;
                        else position = null;
                        node = node_p;
                    } else {
                        node.color = BLACK;
                        color = RED;
                    }
                } else {
                    if(w.right == null || ((Node) w.right).color == BLACK) {
                        ((Node) w.left).color = BLACK;
                        w.color = RED;
                        right_rotate(w);
                        w = (Node) w.parent;
                    }
                    w.color = node.color;
                    node.color = BLACK;
                    ((Node) w.right).color = BLACK;
                    left_rotate(node);

                    node = null;
                }
            } else if(position == Position.RIGHT) {
                Node w = (Node) node.left;
                if(w.color == RED) {
                    node.color = RED;
                    w.color = BLACK;
                    right_rotate(node);
                    w = (Node) node.left;
                }

                if( (w.left == null && w.right == null) ||
                    (w.left != null && ((Node) w.left).color == BLACK && w.right != null && ((Node) w.right).color == BLACK) ) {

                    w.color = RED;
                    if(node.color == BLACK) {
                        //color = BLACK;
                        Node node_p = (Node) node.parent;
                        if(node_p != null) position = node_p.left == node ? Position.LEFT : Position.RIGHT;
                        else position = null;
                        node = node_p;
                    } else {
                        node.color = BLACK;
                        color = RED;
                    }
                } else {
                    if(w.left == null || ((Node) w.left).color == BLACK) {
                        ((Node) w.right).color = BLACK;
                        w.color = RED;
                        left_rotate(w);
                        w = (Node) w.parent;
                    }
                    w.color = node.color;
                    node.color = BLACK;
                    ((Node) w.left).color = BLACK;
                    right_rotate(node);

                    node = null;
                }
            }
        }

    }

    enum Position {
        LEFT,RIGHT
    }

    static class Node extends BSTree.Node {
        boolean color;

        public Node(int key, int value, BSTree.Node parent) {
            super(key, value, parent);
        }

        public Node(int key, int value, BSTree.Node parent, boolean color) {
            super(key, value, parent);
            this.color = color;
        }

        public boolean getColor() {
            return this.color;
        }

        public boolean setColor(boolean color) {
            boolean oldColor = this.color;
            this.color = color;
            return oldColor;
        }

        @Override
        public String toString() {
            return this.key + "(" + (this.color ? "BLACK" : "RED") + ")" + " ";
        }

    }
}
