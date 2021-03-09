package com.example.index.Model;

public class ProductRecord {
    private String date, pid, pname, time, bid,user;
    private int Totalprice, price, quantity;

    public ProductRecord() {
    }

    public ProductRecord(String date, String pid, String pname, String time, String bid, String user, int totalprice, int price, int quantity) {
        this.date = date;
        this.pid = pid;
        this.pname = pname;
        this.time = time;
        this.bid = bid;
        this.user = user;
        Totalprice = totalprice;
        this.price = price;
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getTotalprice() {
        return Totalprice;
    }

    public void setTotalprice(int totalprice) {
        Totalprice = totalprice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

