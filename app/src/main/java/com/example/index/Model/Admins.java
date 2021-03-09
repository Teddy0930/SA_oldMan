package com.example.index.Model;

public class Admins {

    private String name, phone, password, image, account;
    private int money;
    public Admins() {
    }

    public Admins(String name, String phone, String password, String image, String account, int money) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.account = account;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
