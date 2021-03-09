package com.example.index.Model;

public class AdminsNewEventRecord {

    private String date, eDate, eName, eid, time, elid,ePlace,eUser,eStatus,eUserName;

    public AdminsNewEventRecord() {
    }

    public AdminsNewEventRecord(String date, String eDate, String eName, String eid, String time, String elid, String ePlace, String eUser, String eStatus, String eUserName) {
        this.date = date;
        this.eDate = eDate;
        this.eName = eName;
        this.eid = eid;
        this.time = time;
        this.elid = elid;
        this.ePlace = ePlace;
        this.eUser = eUser;
        this.eStatus = eStatus;
        this.eUserName = eUserName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getElid() {
        return elid;
    }

    public void setElid(String elid) {
        this.elid = elid;
    }

    public String getePlace() {
        return ePlace;
    }

    public void setePlace(String ePlace) {
        this.ePlace = ePlace;
    }

    public String geteUser() {
        return eUser;
    }

    public void seteUser(String eUser) {
        this.eUser = eUser;
    }

    public String geteStatus() {
        return eStatus;
    }

    public void seteStatus(String eStatus) {
        this.eStatus = eStatus;
    }

    public String geteUserName() {
        return eUserName;
    }

    public void seteUserName(String eUserName) {
        this.eUserName = eUserName;
    }
}
