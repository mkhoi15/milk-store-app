package com.example.milk_store_app.models.entities;

public class UserChat {
    private String userId;
    private String userName; // or customer name
    private String lastMessage;

    // Default constructor
    public UserChat() {}

    public UserChat(String userId, String userName, String lastMessage) {
        this.userId = userId;
        this.userName = userName;
        this.lastMessage = lastMessage;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
