package com.example.milk_store_app.models.entities;

public class Message {
    private String senderId;
    private String receiverId;
    private String content;
    private long timestamp;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String senderId, String receiverId, String content, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
// Getters and setters...
}
