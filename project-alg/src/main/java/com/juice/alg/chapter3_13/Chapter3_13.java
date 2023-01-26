package com.juice.alg.chapter3_13;


import com.juice.alg.tr.AVLHTree;
import com.juice.alg.tr.AVLTree;
import com.juice.alg.tr.BSTree;
import com.juice.alg.tr.RBTree;

public class Chapter3_13 {

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
     *  最小失衡子树: 平衡因子绝对值超过1的最小子树
     *  分析: @see 纸张
     * @see com.juice.alg.tr.AVLTree
     * @see com.juice.alg.tr.AVLHTree
     *第二: 红黑树(RB-tree)
     *  性质:
     *      每个节点带颜色属性，或红色，或黑色
     *      根节点是黑色
     *      叶节点(NIL节点)是黑色的
     *      如果一个节点是红色的，则它的两个子节点是黑色的
     *      对每个节点，从该节点到其所有后代叶节点(NIL节点)的简单路径上，均包含相同数目的黑节点
     *  black-height: 从某一结点出发(不含该节点)，到达任意一个叶节点的简单路径上黑色节点的个数为该节点的black-height
     * @see com.juice.alg.tr.RBTree
     */


    public static void main(String argv[]) {

        System.out.println("###############AVLTree###############");
        AVLTree tree = new AVLTree();

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

        System.out.println("###############AVLHTree###############");
        AVLHTree ht = new AVLHTree();

        ht.put(7, 7);
        ht.put(9, 9);
        ht.put(10, 10);
        ht.put(12, 12);
        ht.put(14, 14);
        ht.put(20, 20);
        ht.put(15, 15);
        ht.put(30, 30);
        ht.put(25, 25);
        ht.put(40, 40);
        ht.put(24, 24);
        ht.put(1, 1);
        ht.put(3, 3);
        ht.put(6, 6);
        ht.put(5, 5);
        ht.put(2, 2);
        ht.put(4, 4);

        ht.BFS();
        System.out.println();
        ht.L_T_R();

        ht.remove(6);
        ht.BFS();
        System.out.println();

        ht.remove(14);
        ht.BFS();
        System.out.println();

        ht.remove(12);
        ht.remove(9);
        ht.BFS();
        System.out.println();

        ht.remove(24);
        ht.remove(20);
        ht.BFS();
        System.out.println();

        System.out.println("###############RBTree###############");
        RBTree rb = new RBTree();

        rb.put(50, 50);
        rb.put(20, 20);
        rb.put(70, 70);
        rb.put(40, 40);
        rb.put(30, 30);
        rb.put(45, 45);
        rb.put(10, 10);
        rb.put(29, 29);
        rb.put(31, 31);
        rb.put(51, 51);
        rb.put(100, 100);
        rb.put(1, 1);
        rb.put(39, 39);
        rb.put(55, 55);
        rb.put(57, 57);
        rb.put(58, 58);

        rb.BFS();
        System.out.println();
        rb.L_T_R();

        /*rb.remove(30);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        /*rb.remove(58);
        rb.remove(51);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        /*rb.remove(1);
        rb.remove(10);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        /*rb.remove(100);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        /*rb.remove(55);
        rb.remove(51);
        rb.remove(58);
        rb.remove(57);
        rb.BFS();
        System.out.println();
        rb.L_T_R();*/

        rb.remove(55);
        rb.remove(51);
        rb.remove(58);
        rb.remove(39);
        rb.remove(31);
        rb.remove(100);
        rb.remove(70);
        rb.remove(45);
        rb.remove(57);
        rb.BFS();
        System.out.println();
        rb.L_T_R();
    }


    //练习13.2-1:
    /**@see com.juice.alg.tr.BSTree#right_rotate(BSTree.Node) */

    //练习13.2-2: 二叉搜索树中，所有可能的旋转数等于边的数量，而对于树，边的数量 = 节点数量-1

    //练习13.2-3: 二叉搜索树中，对某个节点右旋后，α子树深度-1，β子树深度不变，γ子树深度+1

    //二叉搜索树旋转之后，边的变化:
    //  右旋:
    //    1. 左边减一，右边加一，右边减一，左边加一。边总数不变，左边总数不变，右边总数不变
    //    2. 左边减一，右边加一。边总数不变，左边总数减一，右边总数加一
    //  左旋: ...
    //练习13.2-4: 根据如上结论即可证明

    //练习13.2-5:
    //1. 只需让T2的左边总数大于T1的左边总数，则T1不可能通过右旋得到T2。
    


    //思考题13-1
    /*
        Node {
            int key;
            int value;
            Node left;
            Node right;
            Node(int key, int value, Node left, Node right):
                this.key = key;
                this.value = value;
                this.left = left;
                this.right = right;
        }
     */
    //a: 新插入节点的祖先链；替代节点的祖先链
    /*b:
    *   Node persistent-tree-insert(Node root, int key, int value):
    *       Node newRoot = null
    *
    *       if(root == null):
    *           newRoot = Node(key, value, null, null)
    *       else:
    *           newRoot = Node(root.key, root.value, root.left, root.right)
    *
    *           Node x = newRoot
    *           Node t = root
    *           while t != null:
    *               if(key < t.key):
    *                   t = t.left
    *                   if(t != null):
    *                       n = Node(t.key, t.value, t.left, t.right)
    *                       x.left = n
    *                       x = n
    *               else if(key > t.key):
    *                   t = t.right
    *                   if(t != null):
    *                       n = Node(t.key, t.value, t.left, t.right)
    *                       x.right = n
    *                       x = n
    *               else:
    *                   x.value = value
    *                   return newRoot
    *
    *           n = Node(key, value, null, null)
    *           if(key < x.key):
    *               x.left = n
    *           else: x.right = n
    *
    *       return newRoot
    */
    //c: O(h)
    /*
    *Node persistent-tree-remove(Node root, int key):
    *   if(root == null) return null
    *   Node newRoot = Node(root.key, root.value, root.left, root.right)
    *
    *   px = null
    *   x = newRoot
    *   t = root
    *   while t != null:
    *       if(key < t.key):
    *           t = t.left
    *           if(t != null):
    *               n = Node(t.key, t.value, t.left, t.right)
    *               x.left = n
    *               px = x
    *               x = n
    *       else if(key > t.key):
    *           t = t.right
    *           if(t != null):
    *               n = Node(t.key, t.value, t.left, t.right)
    *               x.right = n
    *               px = x
    *               x = n
    *       else:
    *           break
    *
    *   if(t == null) return root
    *
    *   Node r = null
    *   if(t.left != null && t.right != null):
    *       t = t.right
    *       n = Node(t.key, t.value, t.left, t.right)
    *       Node l = x //context
    *       x.right = n
    *       px = x
    *       x = n
    *
    *       t = t.left
    *       while(t != null):
    *           n = Node(t.key, t.value, t.left, t.right)
    *           x.left = n
    *           px = x
    *           x = n
    *           t = t.left
    *
    *       l.key = x.key
    *       l.value = x.value
    *       r = x.right
    *   else:
    *       if(t.left == null && t.right == null)
    *           //px, x, null
    *           r = null
    *       else: //(left != null && right == null) || (left == null && right != null)
    *           Node single_side = left != null ? left : right;
    *           //px, x, single_side
    *           r = single_side
    *
    *   if px.left == x:
    *       px.left = r
    *   else if px.right == x:
    *       px.right = r
    *   x.left = null
    *   x.right = null
    *
    *   return newRoot
    */
    //d: 需要将所有节点复制一份
    //e: 红黑树，left,right,color属性变化的节点需要复制一份


    //思考题13-2
    //a: fixup_put修改       ((Node) this.root).color = BLACK; 改成
    //            if ((Node) this.root).color == RED: bh++; ((Node) this.root).color = BLACK;
    //   fixup_remove修改  if node == null: bh--;
    //                    if(root == null): bh = 0;
    //   沿T下降时，可以对每个访问节点在O(1)时间内确定bh:
    //         r = root
    //         bh_count = bh
    //         while r != null:
    //             r = r.left/r.right
    //             if(r != null && r.color == BLACK) bh_count--
    /*
    RBTree rb-join(RBTree t1, int key, RBTree t2):

        RBTree one = cut_branch(t1, key, less than and equals)
        RBTree two cut_branch(t2, key, great than and equals)

        RBTree ret = merge_branch(one, key, two)

        return ret

     RBTree cut_branch(RBTree t, int key, boolean lessOrGreat):
        Node x = t.root
        int x_bh = x.bh
        Node y = null
        RBTree ret = new RBTree

      lessOrGreat == less and equals:
        while x != null:
            if(x.key <= key):
                Node left = x.left
                int left_bh = left == null || left.color == BLACK ? x_bh-1 : x_bh
                x.left = null
                if left != null: left.parent = null

                if(x.key == key):
                    Node right = null
                else:
                    Node right = x.right
                    int right_bh = right == null || right.color == BLACK ? x_bh-1 : x_bh

                if(y == null):
                    ret.root = left
                    ret.bh = left_bh
                else:
                    ret = merge_branch(ret, y.key, new RBTree(left, left_bh))

                y = x
                x = right
                x_bh = right_bh
            else: //x.key > key
                x = x.left
                x_bh = right == null || right.color == BLACK ? x_bh-1 : x_bh
        if y != null
            ret.put(y.key, y.value)
      lessOrGreat == great and equals:
        same...

        return ret

    RBTree merge_branch(RBTree one, int key, RBTree two):
        if(one.bh >= two.bh):
            left = one
            right = two
        else:
            left = two
            right = one

        if(left.root != null && right.root != null):
            Node x = left.root
            int x_bh = left.bh

            while(x_bh > right.bh):
                x = x.right
                int x_bh = x == null || x.color == BLACK ? x_bh-1 : x_bh

            Node p = x.parent

            Node n = new Node(key, key, RED)

            n.left = x
            x.parent = n
            n.right = right
            right.parent = n

            p.right = n
            n.parent = p

            if(p == null) left.root = n
            fixup_put(left.root)
            return left
        else if(left.root == null && right == null):
            ...
        else:
            ...
     */
    //b: 见402行
    //c: 以x为根
    //d: 白色, fixup_put
    //e: 选择T1.bh，T2.bh中更大的即可
    //f: n = n1 + n2; h1 = lgn1, h2 = lgn2; h1 + h2 = lgn1 + lgn2 = lg(n1*n2) <= lg[ [(n1+n2)/2]^2 ] = 2*lg(n/2) = 2*(lgn - 1)

    //思考题13-3
    /**@see AVLHTree*/
    
}
