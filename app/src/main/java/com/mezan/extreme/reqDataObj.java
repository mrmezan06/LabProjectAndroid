package com.mezan.extreme;

import android.app.UiAutomation;

public class reqDataObj {
    String name;
    String mobile;
    String distance;
    String lat;
    String lon;
    String reqTime;
    String UID;
    String reqID;
    String category;
    String status;
    String pickAddress;
    public  reqDataObj(){

    }
    public reqDataObj(String name,String mobile,String distance,String lat,String lon,String reqTime,String UID,String reqID,String category,String status,String pickAddress){
        this.name = name;
        this.mobile = mobile;
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
        this.reqTime = reqTime;
        this.UID = UID;
        this.reqID = reqID;
        this.category = category;
        this.status = status;
        this.pickAddress = pickAddress;

    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public String getPickAddress() {
        return pickAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
