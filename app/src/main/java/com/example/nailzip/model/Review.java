package com.example.nailzip.model;

public class Review {

    public String uid;          // 채팅 보내는 uid
    public String username;
    public int starPoint;
    public float reviewPoint;
    public String content;
    public Object timestamp;

    public Review(String uid, String username, int starPoint, float reviewPoint, String content, Object timestamp) {
        this.uid = uid;
        this.username = username;
        this.starPoint = starPoint;
        this.reviewPoint = reviewPoint;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Review() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStarPoint() {
        return starPoint;
    }

    public void setStarPoint(int starPoint) {
        this.starPoint = starPoint;
    }

    public float getReviewPoint() {
        return reviewPoint;
    }

    public void setReviewPoint(float reviewPoint) {
        this.reviewPoint = reviewPoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
