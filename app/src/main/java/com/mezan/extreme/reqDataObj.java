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
    public  reqDataObj(){

    }
    public reqDataObj(String name,String mobile,String distance,String lat,String lon,String reqTime,String UID){
        this.name = name;
        this.mobile = mobile;
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
        this.reqTime = reqTime;
        this.UID = UID;

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
