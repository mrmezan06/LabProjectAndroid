package com.mezan.extreme.food;

public class infoObject {
    infoObject(){}
    String name,uid;

    public infoObject(String name,String uid){
        this.name = name;
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
