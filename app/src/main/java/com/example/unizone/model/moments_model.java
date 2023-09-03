package com.example.unizone.model;

public class moments_model {
   private int story,profile_img, story_type;
   private String moments_username,moments_status;

    public int getStory() {
        return story;
    }

    public void setStory(int story) {
        this.story = story;
    }

    public int getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(int profile_img) {
        this.profile_img = profile_img;
    }

    public int getStory_type() {
        return story_type;
    }

    public moments_model() {
    }

    public void setStory_type(int story_type) {
        this.story_type = story_type;
    }

    public String getMoments_username() {
        return moments_username;
    }

    public void setMoments_username(String moments_username) {
        this.moments_username = moments_username;
    }

    public String getMoments_status() {
        return moments_status;
    }

    public void setMoments_status(String moments_status) {
        this.moments_status = moments_status;
    }

    public moments_model(int story, int profile_img, int story_type, String moments_username, String moments_status) {
        this.story = story;
        this.profile_img = profile_img;
        this.story_type = story_type;
        this.moments_username = moments_username;
        this.moments_status = moments_status;
    }



}
