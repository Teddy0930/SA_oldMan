package com.example.index.Model;

public class RedEnvelopeRecord {
    private String DeliveryAccount, ReceiveAccount, RecordDate, RecordThing, RecordTime, did, rid,DeliveryName,ReceiveName;
    private int RecordMoney;

    public RedEnvelopeRecord() {
    }

    public RedEnvelopeRecord(String deliveryAccount, String receiveAccount, String recordDate, String recordThing, String recordTime, String did, String rid, String deliveryName, String receiveName, int recordMoney) {
        DeliveryAccount = deliveryAccount;
        ReceiveAccount = receiveAccount;
        RecordDate = recordDate;
        RecordThing = recordThing;
        RecordTime = recordTime;
        this.did = did;
        this.rid = rid;
        DeliveryName = deliveryName;
        ReceiveName = receiveName;
        RecordMoney = recordMoney;
    }

    public String getDeliveryAccount() {
        return DeliveryAccount;
    }

    public void setDeliveryAccount(String deliveryAccount) {
        DeliveryAccount = deliveryAccount;
    }

    public String getReceiveAccount() {
        return ReceiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        ReceiveAccount = receiveAccount;
    }

    public String getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(String recordDate) {
        RecordDate = recordDate;
    }

    public String getRecordThing() {
        return RecordThing;
    }

    public void setRecordThing(String recordThing) {
        RecordThing = recordThing;
    }

    public String getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(String recordTime) {
        RecordTime = recordTime;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDeliveryName() {
        return DeliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        DeliveryName = deliveryName;
    }

    public String getReceiveName() {
        return ReceiveName;
    }

    public void setReceiveName(String receiveName) {
        ReceiveName = receiveName;
    }

    public int getRecordMoney() {
        return RecordMoney;
    }

    public void setRecordMoney(int recordMoney) {
        RecordMoney = recordMoney;
    }
}
