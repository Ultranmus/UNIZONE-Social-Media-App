package com.example.unizone.model;

public class prf_post_model {
    private int post_type=1;
//    0 for video and 1 for image
    private String auth_id,post,description;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPost_type() {
        return post_type;
    }

    public prf_post_model(String post, String description,long time) {
        this.post = post;
        this.description = description;
        this.time=time;
    }

    public prf_post_model() {
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
