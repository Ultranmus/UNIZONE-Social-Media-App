package com.example.unizone.model;

public class MsgModel {
    private String timeStamp,byWho,msg,date,viewed,documentId;


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getViewed() {
        return viewed;
    }

    public MsgModel(String timeStamp, String byWho, String msg, String date, String viewed) {
        this.timeStamp = timeStamp;
        this.byWho = byWho;
        this.msg = msg;
        this.date = date;
        this.viewed = viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public MsgModel() {
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getByWho() {
        return byWho;
    }

    public void setByWho(String byWho) {
        this.byWho = byWho;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MsgModel(String timeStamp, String byWho, String msg, String date) {
        this.timeStamp = timeStamp;
        this.byWho = byWho;
        this.msg = msg;
        this.date = date;
    }
}
