package com.example.nailzip;

public class NailshopData {
    private int img_shop, img_scrab;
    private String shopname, rating, ratingcnt, time, closed, location;

    public NailshopData(int img_shop, int img_scrab, String shopname, String rating, String ratingcnt, String time, String closed, String location) {
        this.img_shop = img_shop;
        this.img_scrab = img_scrab;
        this.shopname = shopname;
        this.rating = rating;
        this.ratingcnt = ratingcnt;
        this.time = time;
        this.closed = closed;
        this.location = location;
    }

    public int getImg_shop() {
        return img_shop;
    }

    public void setImg_shop(int img_shop) {
        this.img_shop = img_shop;
    }

    public int getImg_scrab() {
        return img_scrab;
    }

    public void setImg_scrab(int img_scrab) {
        this.img_scrab = img_scrab;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingcnt() {
        return ratingcnt;
    }

    public void setRatingcnt(String ratingcnt) {
        this.ratingcnt = ratingcnt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
