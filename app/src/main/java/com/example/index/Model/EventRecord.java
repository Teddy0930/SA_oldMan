package com.example.index.Model;

public class EventRecord {

    private String date, eDate, eName, eid, time, elid,ePlace,eStatus;

    public EventRecord() {
    }

    public EventRecord(String date, String eDate, String eName, String eid, String time, String elid, String ePlace, String eStatus) {
        this.date = date;
        this.eDate = eDate;
        this.eName = eName;
        this.eid = eid;
        this.time = time;
        this.elid = elid;
        this.ePlace = ePlace;
        this.eStatus = eStatus;
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

    public String geteStatus() {
        return eStatus;
    }

    public void seteStatus(String eStatus) {
        this.eStatus = eStatus;
    }
}
