package com.example.nailzip.model;

public class NailshopData {
    public String img_shop, img_scrap;
    public String uid, shopname, rating, ratingcnt, time, closed, location;

    public String shopphone, memo;

    public String price_nail;
    public String price_pedi;

    public NailshopData(String img_shop, String img_scrap, String uid, String shopname, String rating, String ratingcnt, String time, String closed, String location, String shopphone, String memo, String price_nail, String price_pedi) {
        this.img_shop = img_shop;
        this.img_scrap = img_scrap;
        this.uid = uid;
        this.shopname = shopname;
        this.rating = rating;
        this.ratingcnt = ratingcnt;
        this.time = time;
        this.closed = closed;
        this.location = location;
        this.shopphone = shopphone;
        this.memo = memo;
        this.price_nail = price_nail;
        this.price_pedi = price_pedi;
    }

    public NailshopData() {

    }

    public String getImg_shop() {
        return img_shop;
    }

    public void setImg_shop(String img_shop) {
        this.img_shop = img_shop;
    }

    public String getImg_scrap() {
        return img_scrap;
    }

    public void setImg_scrap(String img_scrap) {
        this.img_scrap = img_scrap;
    }

    public String getShopname() {
        return shopname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getShopphone() {
        return shopphone;
    }

    public void setShopphone(String shopphone) {
        this.shopphone = shopphone;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPrice_nail() {
        return price_nail;
    }

    public void setPrice_nail(String price_nail) {
        this.price_nail = price_nail;
    }

    public String getPrice_pedi() {
        return price_pedi;
    }

    public void setPrice_pedi(String price_pedi) {
        this.price_pedi = price_pedi;
    }
}
