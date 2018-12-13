package com.mu.controller;

import com.mu.dataStruct.DiGraph;
import com.mu.model.Edge;
import com.mu.model.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private DiGraph diGraph;
    private List<Vertex> vertices;

    @RequestMapping("/index")
    public ModelAndView index() {

        List<Edge> edges = jdbcTemplate.query("SELECT * from edges", new RowMapper<Edge>() {

            @Override
            public Edge mapRow(ResultSet resultSet, int i) throws SQLException {
                Edge edge = new Edge();
                edge.setId(resultSet.getInt("id"));
                edge.setHead(resultSet.getString("head"));
                edge.setTail(resultSet.getString("tail"));

                return edge;
            }
        });
        Edge[] edgeArr = new Edge[edges.size()];
        edges.toArray(edgeArr);

        vertices = jdbcTemplate.query("SELECT * from vertexs", new RowMapper<Vertex>() {

            @Override
            public Vertex mapRow(ResultSet resultSet, int i) throws SQLException {
                Vertex vertex = new Vertex();
                vertex.setId(resultSet.getInt("id"));
                vertex.setVersion(resultSet.getString("version"));
                vertex.setCq(resultSet.getString("cq"));
                vertex.setUpContent(resultSet.getString("upContent"));
                vertex.setBslTime(resultSet.getDate("bslTime"));
                vertex.setLinkDbv(resultSet.getString("linkDbv"));
                vertex.setLinkApp(resultSet.getString("linkApp"));
                vertex.setRemark1(resultSet.getString("remark1"));
                vertex.setRemark2(resultSet.getString("remark2"));
                vertex.setMaster(resultSet.getInt("master"));

                return vertex;
            }
        });
        Vertex[] vertexArr = new Vertex[vertices.size()];
        vertices.toArray(vertexArr);

        diGraph = new DiGraph(vertexArr, edgeArr);
        List<List<Vertex>> lists = diGraph.showData();
        Map<String, Object> map = new HashMap<>();
        map.put("datas", lists);
        return new ModelAndView("index", map);
    }

    @ResponseBody
    @RequestMapping("/insertVersion")
    public Object insertVersion(Vertex vertex) {
        Map<String, Object> map = new HashMap<>();
        if (vertex.getVersion() == null || "".equals(vertex.getVersion())) {
            map.put("msg", "version不能为空");
            return map;
        }

        List<Vertex> version = jdbcTemplate.query("select * from vertexs where version='" + vertex.getVersion()+"'", new RowMapper<Vertex>() {

            @Override
            public Vertex mapRow(ResultSet resultSet, int i) throws SQLException {
                Vertex vt = new Vertex();
                vt.setVersion(resultSet.getString("version"));
                return vt;
            }
        });
        if (version != null && version.size() > 0) {
            map.put("msg", "版本已存在");
            return map;
        } else {
//            new Object()[]{vertex.getVersion(), vertex.getUpContent(),vertex.getCq(),
//                    vertex.getBslTime(), vertex.getLinkApp(),vertex.getLinkDbv(),vertex.getRemark1(),vertex.getRemark2()};
            jdbcTemplate.update("insert into vertexs(version,upcontent,cq,bsltime,linkapp,linkdbv,remark1,remark2) values(?,?,?,?,?,?,?,?)",
                    new PreparedStatementSetter(){
                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, vertex.getVersion());
                            ps.setString(2, vertex.getUpContent());
                            ps.setString(3, vertex.getCq());
                            ps.setDate(4, vertex.getBslTime());
                            ps.setString(5, vertex.getLinkApp());
                            ps.setString(6, vertex.getLinkDbv());
                            ps.setString(7, vertex.getRemark1());
                            ps.setString(8, vertex.getRemark2());
                            ps.setInt(9, vertex.getMaster());
                        }
                    });
            map.put("msg", "OK");
            return map;
        }
    }

    @ResponseBody
    @RequestMapping("/insertEdge")
    public Object insertVersion(Edge edge) {
        Map<String, Object> map = new HashMap<>();
        if (edge.getHead() == null || "".equals(edge.getHead())
                || edge.getTail() == null || "".equals(edge.getTail())) {
            map.put("msg", "不要字段不能为空");
            return map;
        }

        if (edge.getHead().equals(edge.getTail())) {
            map.put("msg", "不能自己指向自己");
            return map;
        }

        List<Vertex> version = jdbcTemplate.query("select * from vertexs where version='" + edge.getHead()+"'", new RowMapper<Vertex>() {
            @Override
            public Vertex mapRow(ResultSet resultSet, int i) throws SQLException {
                Vertex vt = new Vertex();
                vt.setVersion(resultSet.getString("version"));
                return vt;
            }
        });

        if (version == null || version.size() == 0) {
            map.put("msg", "边的头版本不存在");
            return map;
        }

        List<Vertex> versionTail = jdbcTemplate.query("select * from vertexs where version='" + edge.getTail()+"'", new RowMapper<Vertex>() {
            @Override
            public Vertex mapRow(ResultSet resultSet, int i) throws SQLException {
                Vertex vt = new Vertex();
                vt.setVersion(resultSet.getString("version"));
                return vt;
            }
        });

        if (versionTail == null || versionTail.size() == 0) {
            map.put("msg", "边的尾版本不存在");
            return map;
        }


        List<Edge> edges = jdbcTemplate.query("select * from edges where head='" + edge.getHead()+"' and tail='" + edge.getTail() + "'", new RowMapper<Edge>() {
            @Override
            public Edge mapRow(ResultSet resultSet, int i) throws SQLException {
                Edge eg = new Edge();
                eg.setId(resultSet.getInt("id"));
                return eg;
            }
        });
        if (edges != null && edges.size() > 0) {
            map.put("msg","已经存在此边");
            return map;
        } else {
            jdbcTemplate.update("insert into edges(head,tail) values(?,?)",
                    new PreparedStatementSetter(){
                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setString(1, edge.getHead());
                            ps.setString(2, edge.getTail());
                        }
                    });
            map.put("msg", "OK");
            return map;
        }


    }


    @ResponseBody
    @RequestMapping("/queryUpContent")
    public Object queryUpContent(String startNodeName, String endNodeName) {
        Map<String, Object> map = new HashMap<>();
        if (startNodeName == null || "".equals(startNodeName)) {
            map.put("msg", "开始节点不能为空");
            return map;
        }
        if (endNodeName == null || "".equals(endNodeName)) {
            map.put("msg", "结束节点不能为空");
            return map;
        }

        Vertex startVertex = null;
        Vertex endVertex = null;
        for (Vertex v : vertices) {
            if (startNodeName.equals(v.getVersion())) {
                startVertex = v;
            }

            if (endNodeName.equals(v.getVersion())) {
                endVertex = v;
            }
        }

        if (startVertex == null) {
            map.put("msg", "开始节点不存在");
            return map;
        }
        if (endVertex == null) {
            map.put("msg", "结束节点不存在");
            return map;
        }


        List<Vertex> list = diGraph.needVertex(startVertex, endVertex);
        list.sort(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) {
                if (o1.getId() > o2.getId()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        map.put("msg", "ok");
        map.put("data", list);

        return map;
    }

//    private String verify(Vertex vertex) {
//
//    }

    private void ss() {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into vertexs(version,upcontent,cq,bsltime,linkapp,linkdbv,remark1,remark2,idnum) ");
        sql.append(" values");
        for (int i = 1; i < 10; i++) {
            String s = "'2.8." + i + "'";
            sql.append("(").append(s).append(",").append(s).append(",").append(s)
                    .append(",").append(s).append(",").append(s).append(",").append(s)
                    .append(",").append(s).append(",").append(s).append(",").append(i).append("),");
        }
//        System.out.println(sql.toString().substring(0, sql.length()-1));

//        jdbcTemplate.execute(sql.toString().substring(0, sql.length()-1));

        sql = new StringBuffer();
        sql.append("insert into edges(head,tail) values('2.8.1','2.8.2'),('2.8.2','2.8.3'),('2.8.2','2.8.4')," +
                "('2.8.3','2.8.6'),('2.8.4','2.8.6'),('2.8.4','2.8.5'),('2.8.6','2.8.7'),('2.8.6','2.8.8')," +
                "('2.8.5','2.8.8'),('2.8.7','2.8.9'),('2.8.8','2.8.9')");
//        jdbcTemplate.execute(sql.toString());
    }
}
