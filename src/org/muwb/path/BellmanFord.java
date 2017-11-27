package org.muwb.path;

import java.util.HashMap;
import java.util.Map;

/**
 * Dijkstra 算法是处理单源最短路径的有效算法，但它对存在负权回路的图就会失效。这时候，
 * 就需要使用其他的算法来应对这个问题，Bellman-Ford（中文名：贝尔曼 - 福特）算法就是其中一个。
 * Created by muwb on 2017/11/27.
 */
public class BellmanFord {
    private static final int max = 1000;

    public static void main(String[] args){
        BellmanFord bf = new BellmanFord();
        bf.bellmanFord();
    }

    public void bellmanFord(){
        Node[] nodes = initData();
        Map<Integer, Integer> map = new HashMap<Integer,Integer>();
        for(Node n : nodes){
            if(map.get(n.u) == null)
                map.put(n.u, 1);
            if(map.get(n.v) == null)
                map.put(n.v, 1);

        }

        int[] d = new int[map.size()];
        int[] p = new int[map.size()];

        d[0] = 0;//到终点的权重值 p[终点]=权重
        p[0] = 0;//终点对应的起点 index是终点  value是起点. p[终点]=起点
        for (int i = 1; i < d.length; i++) {
            d[i] = max;
            p[i] = -1;

        }

        boolean flag;
        for(int i = 1; i < d.length; i++){
            flag = true;
            for(int j = 0; j < nodes.length; j++){
                if(d[nodes[j].v] > d[nodes[j].u] + nodes[j].w) {
                    d[nodes[j].v] = d[nodes[j].u] + nodes[j].w;
                    p[nodes[j].v] = nodes[j].u;
                    flag = false;
                }
            }

            if(flag)
                break;
        }

        //计算路径的起始位置为0
        int start = 0;
        for (int i = 1; i < d.length; i++) {
            if(printPath(p, start, i))
                System.out.println(" 距离：" + d[i]);
            else
                System.out.println(start + "  " + i + " 距离：无群大");
        }
    }

    private boolean printPath(int[] path, int from, int to) {
        int pre;
        if (from == to) {
            System.out.print(to + " ");
            return true;
        }

        int preIdx = to - 1;

        if(preIdx < 0)
            return false;

        pre = path[preIdx];
        if(pre == to)
            return false;

        printPath(path, from, pre);
        System.out.print(" " + to + " ");
        return true;
    }

    private Node[] initData(){
//        Node n1 = new Node(0, 1, 1);
//        Node n2 = new Node(0, 2, -3);
//        Node n3 = new Node(1, 3, 3);
//        Node n4 = new Node(2, 1, 5);
//        Node n5 = new Node(4, 5, -1);
//        Node n6 = new Node(5, 3, 3);
//        Node n7 = new Node(3, 2, 1);
//        Node n8 = new Node(3, 0, 1);
//        Node n9 = new Node(2, 5, -5);
//        Node n10 = new Node(4, 2, -2);
//        Node n11 = new Node(4, 0, 4);
//        return new Node[]{n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11};

        Node n1 = new Node(0, 1, -1);
        Node n2 = new Node(0, 2, 4);
        Node n3 = new Node(1, 2, 3);
        Node n4 = new Node(3, 1, 1);
        Node n5 = new Node(1, 3, 2);
        Node n6 = new Node(3, 2, 5);
        Node n7 = new Node(1, 4, 2);
        Node n8 = new Node(4, 3, -3);
        return new Node[]{n1,n2,n3,n4,n5,n6,n7,n8};
    }


    private class Node{
        int u;//起点
        int v;//终点
        int w;//边的权值

        public Node(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }
}

