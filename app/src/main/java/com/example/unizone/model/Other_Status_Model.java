package com.example.unizone.model;

public class Other_Status_Model {
    private String post_by_id;
    private String timestamp;
    private String viewed,documentId;

    public String getPost_by_id() {
        return post_by_id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setPost_by_id(String post_by_id) {
        this.post_by_id = post_by_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public Other_Status_Model(String post_by_id, String timestamp, String viewed) {
        this.post_by_id = post_by_id;
        this.timestamp = timestamp;
        this.viewed = viewed;
    }

    public Other_Status_Model() {
    }
}
