package com.dcs.model;

/**
 * 边
 */
public class Edge {

    private Integer id;

    private String head;

    private String tail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", head='" + head + '\'' +
                ", tail='" + tail + '\'' +
                '}';
    }
}
