package com.example.unizone.model;

public class connection_model {
    private String prf_img_connection;
    private String auth_id;
    private long date;

    public String getPrf_img_connection() {
        return prf_img_connection;
    }

    public connection_model() {
    }

    public long getDate() {
        return date;
    }

    public connection_model(String auth_id, long date) {
        this.auth_id = auth_id;
        this.date = date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setPrf_img_connection(String prf_img_connection) {
        this.prf_img_connection = prf_img_connection;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public connection_model(String prf_img_connection, String auth_id) {
        this.prf_img_connection = prf_img_connection;
        this.auth_id = auth_id;
    }
}
