package com.dcs.model;


import java.sql.Date;

/**
 * 顶点
 */
public class Vertex {

    private Integer id;
    private String version; //当前版本号
    private String cq;
    private String upContent; //当前版本更新内容
    private Date bslTime; //入基线时间
    private String linkDbv;   //关联的数据库版本
    private String linkApp;    //关联的应用
    private String remark1;     //备注1
    private String remark2;     //备注2
    private Integer master;      //主干版本>0, 非主干=0

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCq() {
        return cq;
    }

    public void setCq(String cq) {
        this.cq = cq;
    }

    public String getUpContent() {
        return upContent;
    }

    public void setUpContent(String upContent) {
        this.upContent = upContent;
    }

    public Date getBslTime() {
        return bslTime;
    }

    public void setBslTime(Date bslTime) {
        this.bslTime = bslTime;
    }

    public String getLinkDbv() {
        return linkDbv;
    }

    public void setLinkDbv(String linkDbv) {
        this.linkDbv = linkDbv;
    }

    public String getLinkApp() {
        return linkApp;
    }

    public void setLinkApp(String linkApp) {
        this.linkApp = linkApp;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public Integer getMaster() {
        return master;
    }

    public void setMaster(Integer master) {
        this.master = master;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", cq='" + cq + '\'' +
                ", upContent='" + upContent + '\'' +
                ", bslTime=" + bslTime +
                ", linkDbv='" + linkDbv + '\'' +
                ", linkApp='" + linkApp + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", master=" + master +
                '}';
    }
}
