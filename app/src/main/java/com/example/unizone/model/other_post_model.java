package com.example.unizone.model;

public class other_post_model {
    private String post_by_id;
    private String timestamp;

    public other_post_model() {
    }



    public String getPost_by_id() {
        return post_by_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setPost_by_id(String post_by_id) {
        this.post_by_id = post_by_id;
    }

    public other_post_model(String post_by_id, String timestamp) {
        this.post_by_id = post_by_id;
        this.timestamp = timestamp;
    }
}
