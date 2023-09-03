package com.example.unizone.model;

public class MomentModel {
    private String video;
    private String description;

    public MomentModel() {
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public MomentModel(String video, String description, String timeStamp) {
        this.video = video;
        this.description = description;
        this.timeStamp = timeStamp;
    }

    private String timeStamp;

}
