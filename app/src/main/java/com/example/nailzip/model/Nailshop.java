package com.example.nailzip.model;

public class Nailshop {

    String uid;
    String shopname;
    String opentime;
    String shopphone;
    String address;
    String memo;
//    String price_nail;
//    String price_pedi;

    //Todo: 가격 추후 추가


    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopphone() {
        return shopphone;
    }

    public void setShopphone(String shopphone) {
        this.shopphone = shopphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
//
//    public String getPrice_nail() {
//        return price_nail;
//    }
//
//    public void setPrice_nail(String price_nail) {
//        this.price_nail = price_nail;
//    }
//
//    public String getPrice_pedi() {
//        return price_pedi;
//    }
//
//    public void setPrice_pedi(String price_pedi) {
//        this.price_pedi = price_pedi;
//    }
}
