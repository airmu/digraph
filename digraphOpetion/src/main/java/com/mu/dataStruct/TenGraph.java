package com.mu.dataStruct;


import java.util.ArrayList;
import java.util.List;


/**
 * 邻接矩阵实现
 */
public class TenGraph {

    private int V;  // 节点数
    private int E;  // 边的数目
    private List<Integer>[] adj; // 邻接表矩阵

    public TenGraph(int V) { // 创建节点个数为V的没有边的有向图
        this.V = V;
        this.E = 0;
        adj = (List<Integer>[])new List[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<Integer>();
        }
    }

    public void addEdge(int v, int w) { // 在有向图中增加边v->w
        adj[v].add(w);
        E++;
    }

    public List<Integer> adj(int v) { // 返回v节点的相邻节点
        return adj[v];
    }

    public int V() { // 返回节点数
        return V;
    }

    public int E() { // 返回边的数目
        return E;
    }

    public String toString() { // 打印图
        String s = V + " 个顶点, " + E + " 条边\n";
        for (int i = 0; i < V; i++) {
            s += i + ": ";
            for (Integer node : adj(i)) {
                s += node + " ";
            }
            s += "\n";
        }
        return s;
    }

    public TenGraph reverse() {
        TenGraph g = new TenGraph(V);
        for (int i = 0; i < V; i++) {
            for (Integer node : adj[i]) {
                g.addEdge(node, i);
            }
        }
        return g;
    }


    public static void main(String[] args) {
        TenGraph tenGraph = new TenGraph(4);
        tenGraph.addEdge(0,3);
        tenGraph.addEdge(2,1);
        tenGraph.addEdge(2,3);
        tenGraph.addEdge(3,0);
        System.out.println(tenGraph.toString());
        System.out.println(tenGraph.adj(2));
    }
}
