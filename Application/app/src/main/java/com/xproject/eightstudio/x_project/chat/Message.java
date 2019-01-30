package com.xproject.eightstudio.x_project.chat;

public class Message {
    String sender_id;
    String name;
    String data;
    boolean isLoading;

    public void setData(String data) {
        this.data = data;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }
}