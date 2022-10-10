package com.example.nailzip.model;

public class NotificationModel {

    public String to;

    public Notification notification = new Notification();
    public Data data = new Data();

    public static class Notification{
        public String title;
        public String body;
    }

    public static class Data{
        public String title;
        public String body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
