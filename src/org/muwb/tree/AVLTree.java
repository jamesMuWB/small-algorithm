package org.muwb.tree;

/**
 * AVL 树是一棵平衡的二叉查找树，于 1962 年，G. M. Adelson-Velsky 和 E. M. Landis
 * 在他们的论文《An algorithm for the organization of information》中发表
 *
 * 所谓的平衡之意，就是树中任意一个结点下左右两个子树的高度差不超过 1。
 *
 * Created by muwb on 2017/11/22.
 */
public class AVLTree {
    private Node root;//根节点

    class Node<T extends Comparable<T>>{
        T key;
        int height;
        Node<T> left;
        Node<T> right;

        public Node(T key, Node<T> left, Node<T> right){
            this.key = key;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }

    private int max(int n, int m){
        return n > m ? n : m;
    }

    private int getHeight(Node node){
        if(node == null)
            return 0;
        return node.height;
    }

    public int getHeight(){
        if (root == null)
            return 0;
        return root.height;
    }

    public void preOrder(Node node){
        if(node != null){
            System.out.println(node.key);
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    public void midOrder(Node node){
        if(node != null){
            midOrder(node.left);
            System.out.println(node.key);
            midOrder(node.right);
        }
    }

    public void postOrder(Node node){
        if(node != null){
            postOrder(node.left);
            postOrder(node.right);
            System.out.println(node.key);
        }
    }

    public <T> Node search(T key){
        return search(root, key);
    }

    private <T> Node search(Node node, T key){
        if(root == null)
            return null;

        int comp = node.key.compareTo(key);
        if(comp < 0)
            search(node.right, key);
        else if(comp > 0)
            search(node.left, key);
        else
            return node;

        return null;
    }

    public <T> Node traversalSearch(T key){
        return traversalSearch(root, key);
    }

    private <T> Node traversalSearch(Node node, T key){
        while(node != null){
            int comp = node.key.compareTo(key);
            if(comp < 0)
                node = node.right;
            else if(comp > 0)
                node = node.left;
            else
                return node;
        }
        return node;
    }

    private <T> Node llRotation(Node node){
        Node n1 = null;

        n1 = node.left;
        node.left = n1.right;
        n1.right = node;

        node.height = max(getHeight(node.right), getHeight(node.left)) + 1;
        n1.height = max(node.height, getHeight(n1.left)) + 1;

        return n1;
    }

    private <T> Node rrRotation(Node node){
        Node n1 = null;

        n1 = node.right;
        node.right = n1.left;
        n1.left = node;

        node.height = max(getHeight(node.right), getHeight(node.left)) + 1;
        n1.height = max(getHeight(n1.right), node.height) + 1;

        return n1;
    }

    private <T> Node lrRotation(Node node){
        node.left = rrRotation(node.left);

        return llRotation(node);
    }

    private <T> Node rlRotation(Node node){
        node.right = llRotation(node.right);

        return rrRotation(node);
    }

    private <T extends Comparable> Node insert(Node node, T key){
        if(node == null){
            node = new Node(key, null, null);
            node.height = 1;
            return node;
        }

        int comp = node.key.compareTo(key);
        if(comp > 0){
            node.left = insert(node.left, key);
            if(getHeight(node.left) - getHeight(node.right) > 1){
                if(key.compareTo(node.left.key) > 0)
                    node = lrRotation(node);
                else
                    node = llRotation(node);
            }
        }else if(comp < 0){
            node.right = insert(node.right, key);
            if(getHeight(node.right) - getHeight(node.left) > 1){
                if(key.compareTo(node.right.key) > 0)
                    node = rrRotation(node);
                else
                    node = rlRotation(node);
            }
        }else{
            System.out.println("节点已存在,不能重复添加");
        }

        node.height = max(getHeight(node.left), getHeight(node.right)) + 1;

        return node;
    }

    public <T extends Comparable> void insert(T key){
        root = insert(root, key);
    }

    private <T extends Comparable> Node remove(Node node, T key){
        if (node == null || key == null)
            return null;

        int comp = node.key.compareTo(key);
        if(comp > 0){
            node.left = remove(node.left, key);
            if(getHeight(node.right) - getHeight(node.left) > 1){
                Node r = node.right;
                if(getHeight(r.right) > getHeight(node.left))
                    node = rrRotation(node);
                else
                    node = lrRotation(node);
            }
        }else if(comp < 0){
            node.right = remove(node.right, key);
            if(getHeight(node.left) - getHeight(node.right) > 1){
                Node l = node.left;
                if(getHeight(l.right) > getHeight(l.left))
                    node = lrRotation(node);
                else
                    node = llRotation(node);
            }
        }else{
            if(node.left != null && node.right != null){
                if(getHeight(node.left) > getHeight(node.right)){
                    Node n1 = maxNode(node.left);
                    node.key = n1.key;
                    node.left = remove(node.left, n1.key);
                }else{
                    Node n1 = minNode(node.right);
                    node.key = n1.key;
                    node.right = remove(node.right, n1.key);
                }
            }else{
                Node n1 = node;
                node = node.left != null ? node.left : node.right;
                n1 = null;
            }
        }

        node.height = max(getHeight(node.right), getHeight(node.left)) + 1;

        return node;
    }

    public <T extends  Comparable> void remove(T key){
        if(search(root, key) == null)
            return;

        root = remove(root, key);
    }

    private Node maxNode(Node node){
        if(node == null)
            return null;

        while (node.right != null)
            node = node.right;

        return node;
    }
    private Node minNode(Node node){
        if(node == null)
            return null;

        while (node.left != null)
            node = node.left;

        return node;
    }

    private void destroy(Node node){
        if(node == null)
            return;

        if(node.left != null)
            destroy(node.left);

        if(node.right != null)
            destroy(node.right);

        node = null;
    }

    public void destroy(){
        destroy(root);
    }

    public void print(){
        print(root, 0, 0);
    }

    //direction  0根节点  -1左子树  1右子树
    public <T> void print(Node node, T parentKey, int direction){
        if(node != null){
            if(direction == 0)
                System.out.println(node.key + " is root");
            else if(direction == -1)
                System.out.println(parentKey + " left child :" + node.key);
            else
                System.out.println(parentKey + " right child :" + node.key);

            print(node.left, node.key, -1);
            print(node.right, node.key, 1);

        }
    }
}

