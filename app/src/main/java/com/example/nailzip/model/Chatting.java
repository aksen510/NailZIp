package com.example.nailzip.model;

import java.util.HashMap;
import java.util.Map;

public class Chatting {

//    public String uid;          // 채팅 보는 사람
//    public String chatUid;      // 채팅 받는 네일샵

    public Map<String, Boolean> users = new HashMap<>();    // 채팅방의 유저들
    public Map<String, Comment> comments = new HashMap<>(); // 채팅방의 대화내용

    public static class Comment {
        public String uid;          // 채팅 보내는 uid
        public String message;
        public Object timestamp;
        public Map<String, Object> readUsers = new HashMap<>();
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }

    public Map<String, Comment> getComments() {
        return comments;
    }

    public void setComments(Map<String, Comment> comments) {
        this.comments = comments;
    }
}
