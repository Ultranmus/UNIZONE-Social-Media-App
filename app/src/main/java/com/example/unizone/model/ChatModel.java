package com.example.unizone.model;

import java.util.HashMap;
import java.util.List;

public class ChatModel {
    private String lastMsg,id,timeStamp;

    public ChatModel(String lastMsg, String id, String timeStamp) {
        this.lastMsg = lastMsg;
        this.id = id;
        this.timeStamp = timeStamp;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
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

    public ChatModel() {
    }


}
