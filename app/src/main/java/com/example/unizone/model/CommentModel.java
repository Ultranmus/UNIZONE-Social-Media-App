package com.example.unizone.model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class CommentModel {
    private String id,timeStamp,comment,documentId;
    private String time;


    public CommentModel(String id, String timeStamp, String comment, String time) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.comment = comment;
        this.time = time;
    }


    public CommentModel() {
    }



    public String getDocumentId() {
        return documentId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
