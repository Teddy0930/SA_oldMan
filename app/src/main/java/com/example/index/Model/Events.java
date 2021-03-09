package com.example.index.Model;

public class Events {

    private String eDate, eName, eid, eImage, admin, eDescription,ePlace;
    private int eMoney, ePeople;

    public Events() {
    }

    public Events(String eDate, String eName, String eid, String eImage, String admin, String eDescription, String ePlace, int eMoney, int ePeople) {
        this.eDate = eDate;
        this.eName = eName;
        this.eid = eid;
        this.eImage = eImage;
        this.admin = admin;
        this.eDescription = eDescription;
        this.ePlace = ePlace;
        this.eMoney = eMoney;
        this.ePeople = ePeople;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String geteImage() {
        return eImage;
    }

    public void seteImage(String eImage) {
        this.eImage = eImage;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String geteDescription() {
        return eDescription;
    }

    public void seteDescription(String eDescription) {
        this.eDescription = eDescription;
    }

    public String getePlace() {
        return ePlace;
    }

    public void setePlace(String ePlace) {
        this.ePlace = ePlace;
    }

    public int geteMoney() {
        return eMoney;
    }

    public void seteMoney(int eMoney) {
        this.eMoney = eMoney;
    }

    public int getePeople() {
        return ePeople;
    }

    public void setePeople(int ePeople) {
        this.ePeople = ePeople;
    }
}
