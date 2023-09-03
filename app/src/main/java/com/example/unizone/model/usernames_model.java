package com.example.unizone.model;

public class usernames_model {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id,username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public usernames_model() {
    }

    public usernames_model(String id) {
        this.id = id;
    }
}
