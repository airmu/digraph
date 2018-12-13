package com.dcs.dataStruct;


import com.dcs.model.Edge;
import com.dcs.model.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 图顶点表
 create table vertexs(
 id int primary key auto_increment,
 version varchar(100) not null,
 cq varchar(50),
 upContent varchar(1000),
 bslTime datetime,
 linkDbv varchar(50),
 linkApp varchar(50),
 remark1 varchar(100),
 remark2 varchar(100)
 );

 图边表
 create table edges(
 id int primary key auto_increment,
 head int,
 tail int
 );
 */

/**
 * 邻接表实现
 */
public class DiGraph {

    private static final Logger logger = LoggerFactory.getLogger(DiGraph.class);

    private Vertex[] vertexs;
    private Edge[] edges;

    private VertexNode[] vertexNodes;
    private Map<String, Integer> map = new HashMap<>();

    public DiGraph(Vertex[] vertexs, Edge[] edges) {
        this.vertexs = vertexs;
        this.edges = edges;

        //记录顶点
        vertexNodes = new VertexNode[vertexs.length];
        for (int i = 0; i < vertexs.length; i++) {
            vertexNodes[i] = new VertexNode();
            vertexNodes[i].data = vertexs[i];
            map.put(vertexs[i].getVersion(), i);
        }

        //关联边
        for (int i = 0; i < edges.length; i++) {
            String head = edges[i].getHead();
            String tail = edges[i].getTail();

            EdgeNode edgeNode = new EdgeNode();
            edgeNode.adjvex = tail;

            Integer headIndex = map.get(head);
            VertexNode vn = vertexNodes[headIndex];
            if (vn.first == null) {
                vn.first = edgeNode;
            } else {
                EdgeNode en = vn.first;
                while (en.next != null) {
                    en = en.next;
                }
                en.next = edgeNode;
            }

        }
    }

    public void print() {
        StringBuffer sb = new StringBuffer();
        sb.append("打印有向图：\n");
        for (int i = 0; i < vertexNodes.length; i++) {

            sb.append(String.format("%d(%s):", (i+1), vertexNodes[i].data.getVersion()));

            EdgeNode en = vertexNodes[i].first;
            while (en != null) {
                sb.append(" ");
                sb.append(map.get(en.adjvex));
                sb.append("(");
                sb.append(vertexNodes[map.get(en.adjvex)].data.getVersion());
                sb.append(")");
                en = en.next;
            }
            sb.append("\n");
        }

        logger.info(sb.toString());
    }

    public List<List<Vertex>> showData() {
//        List<Vertex>[] lists = (List<Vertex>[]) new Object[this.vertexNodes.length];
        List<List<Vertex>> lists =  new ArrayList<>();
        for (int i = 0; i < vertexNodes.length; i++) {
            List<Vertex> list = new ArrayList<>();
            list.add(vertexNodes[i].data);
            EdgeNode en = vertexNodes[i].first;
            while (en != null){
                list.add(vertexNodes[map.get(en.adjvex)].data);
                en = en.next;
            }
            lists.add(list);
        }


        for (int i = 0; i < lists.size(); i++) {
            for (Vertex v : lists.get(i)) {
                System.out.print(v.getVersion());
                System.out.print("-->");
            }
            System.out.println("");
        }

//        List<List<Vertex>> collect = lists.stream().filter(e -> e.get(0).getMaster() > 0).sorted(
//                (x, y) -> {
//                    if (x.get(0).getMaster() > y.get(0).getMaster()) {
//                        return -1;
//                    } else {
//                        return 1;
//                    }
//                }).collect(Collectors.toList());

//        List<List<Vertex>> vvvs = new ArrayList<>();
//        List<List<Vertex>> vvvv = new ArrayList<>();
//        for (List<Vertex> list : lists) {
//            if (list.get(0).getMaster() > 0) {
//                vvvs.add(list);
//            } else {
//                vvvv.add(list);
//            }
//        }
//        vvvs.sort(new Comparator<List<Vertex>>() {
//            @Override
//            public int compare(List<Vertex> o1, List<Vertex> o2) {
//                if (o1.get(0).getMaster() > o2.get(0).getMaster()) {
//                    return 1;
//                } else {
//                    return -1;
//                }
//            }
//        });
//        vvvs.addAll(vvvv);

        lists.sort(new Comparator<List<Vertex>>() {
            @Override
            public int compare(List<Vertex> o1, List<Vertex> o2) {
                if (o1.get(0).getMaster() == 0) {
                    return 1;
                }
                if (o2.get(0).getMaster() == 0) {
                    return -1;
                }
                if (o1.get(0).getMaster() > o2.get(0).getMaster()) {
                    return 1;
                } else if (o1.get(0).getMaster().intValue() == o2.get(0).getMaster().intValue()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return lists;

    }

    public DiGraph reverse() {

        Edge[] edges = new Edge[this.edges.length];
        for (int i = 0; i < this.edges.length; i++) {
            Edge edge = new Edge();
            String temp = this.edges[i].getHead();
            edge.setHead(this.edges[i].getTail());
            edge.setTail(temp);
            edges[i] = edge;
        }

        return new DiGraph(this.vertexs, edges);
    }


    /**
     * 获取需要更新需要的节点
     */
    public List<Vertex> needVertex(Vertex oldVertex, Vertex newVertex) {

        Queue<VertexNode> queueYbl = new LinkedList<>();
//        Set<Vertex> set = new HashSet<>();
        DiGraph graph = reverse();

        //1.查询oldVertex节点的所有父节点，加入过滤队列
        VertexNode oldVt = null;
        for (VertexNode vn : graph.vertexNodes) {
             if (oldVertex.getVersion().equals(vn.data.getVersion())) {
                 oldVt = vn;
                 queueYbl.add(vn);
//                 set.add(vn.data);
                 break;
             }
        }

        Queue<VertexNode> queue = new LinkedList<>();
        Map<String, Integer> map = graph.map;

        queue.add(oldVt);
        while (queue.size() != 0) {
            EdgeNode en = queue.remove().first;
            while (en != null) {
                Integer integer = map.get(en.adjvex);
                VertexNode vn = graph.vertexNodes[integer];
                if (!queueYbl.contains(vn)) {
                    queue.add(vn);
                    queueYbl.add(vn);
                }
                en = en.next;
            }
        }

        //degug
        System.out.println("------------");
        for (VertexNode vn : queueYbl) {
            System.out.println(vn.data.getVersion());
        }
        System.out.println("------------");


        //2.从newVertex节点开始遍历所有父节点，过滤掉oldVertex的所有父节点，就是需要更新的节点
        List<Vertex> needList = new ArrayList<>();
        Queue<VertexNode> queueYbl2 = new LinkedList<>();
        VertexNode newVt = null;
        for (VertexNode vn : graph.vertexNodes) {
            if (newVertex.getVersion().equals(vn.data.getVersion())) {
                newVt = vn;
                queueYbl2.add(vn);
                needList.add(vn.data);
//                 set.add(vn.data);
                break;
            }
        }
        queue.add(newVt);
        while (queue.size() != 0) {
            EdgeNode en = queue.remove().first;
            while (en != null) {
                Integer integer = map.get(en.adjvex);
                VertexNode vn = graph.vertexNodes[integer];
                if (!queueYbl2.contains(vn)) {
                    queue.add(vn);
                    queueYbl2.add(vn);

                    if (!queueYbl.contains(vn)) {
                        needList.add(vn.data);
                    }
                }
                en = en.next;
            }
        }

        //debug
        System.out.println("----------------需要更新的版本---------------");
        for (Vertex vertex : needList) {

            System.out.println(vertex.getVersion());
        }
        System.out.println("----------------需要更新的版本结束---------------");

//        queueYbl2.removeAll(queueYbl);


        return needList;
    }



    /**
     * 边表节点
     */
    class EdgeNode{
        String adjvex;     //邻接点域，存储该节点对应的下标
        EdgeNode next;  //链域，指向下一个边表节点
    }

    /**
     * 顶点表结构
     */
    class VertexNode {
        Vertex data;    //顶点域，存储顶点的信息值
        EdgeNode first; //边表头指针
    }


}
