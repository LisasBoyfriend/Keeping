package com.yang.tally.db;

public class ChartItemBean {
    int sImageId;
    String type;
    float radio;//所占比例
    float totalMoney;

    public ChartItemBean() {
    }

    public ChartItemBean(int sImageId, String type, float radio, float totalMoney) {
        this.sImageId = sImageId;
        this.type = type;
        this.radio = radio;
        this.totalMoney = totalMoney;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }
}
