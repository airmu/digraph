package com.mu.controller;

import com.mu.dataStruct.DiGraph;
import com.mu.model.Edge;
import com.mu.model.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseBody
    @RequestMapping("/test")
    public Object test() {
//        jdbcTemplate.execute("SELECT * from vertexs;");
//        List<Edge> list = jdbcTemplate.query("SELECT * from edges", new RowMapper<Edge>() {
//
//            @Override
//            public Edge mapRow(ResultSet resultSet, int i) throws SQLException {
//                Edge edge = new Edge();
//                edge.setId(resultSet.getInt("id"));
//                edge.setHead(resultSet.getInt("head"));
//                edge.setTail(resultSet.getInt("tail"));
//
//                return edge;
//            }
//        });
//
//        for (Edge e : list) {
//            System.out.println(e.toString());
//        }



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



        List<Vertex> vertexs = jdbcTemplate.query("SELECT * from vertexs", new RowMapper<Vertex>() {

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
//                vertex.setIdNum(resultSet.getInt("idNum"));

                return vertex;
            }
        });
        Vertex[] vertexArr = new Vertex[vertexs.size()];
        vertexs.toArray(vertexArr);

        DiGraph dg = new DiGraph(vertexArr, edgeArr);
        dg.print();
//        DiGraph reverse = dg.reverse();
//        reverse.print();

//        Vertex vertex = vertexArr[0];
//        Vertex vertex3 = vertexArr[8];
//        System.out.println("------");
//        System.out.println(vertex.getVersion());
//        System.out.println(vertex3.getVersion());
//        System.out.println("------");
//
//        dg.needVertex(vertex, vertex3);

        dg.showData();

        return "ssss";
    }

}
