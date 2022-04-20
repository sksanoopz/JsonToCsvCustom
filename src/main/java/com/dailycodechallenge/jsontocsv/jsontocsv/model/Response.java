package com.dailycodechallenge.jsontocsv.jsontocsv.model;


public class Response {
    public Response(String status) {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    String status;
}
