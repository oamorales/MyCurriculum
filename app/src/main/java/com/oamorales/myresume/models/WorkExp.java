package com.oamorales.myresume.models;

import com.oamorales.myresume.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WorkExp extends RealmObject {

    @PrimaryKey
    private int id;
    private String companyName;
    private String position;
    private String positionDesc;
    private int yearBegin;
    private int yearEnd;

    public WorkExp() {  }

    public WorkExp(String companyName, String position, String positionDesc, int yearBegin, int yearEnd) {
        this.id = MyApplication.workExpId.incrementAndGet();
        this.companyName = companyName;
        this.position = position;
        this.positionDesc = positionDesc;
        this.yearBegin = yearBegin;
        this.yearEnd = yearEnd;
    }

    public int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositionDesc() {
        return positionDesc;
    }

    public void setPositionDesc(String positionDesc) {
        this.positionDesc = positionDesc;
    }

    public int getYearBegin() {
        return yearBegin;
    }

    public void setYearBegin(int yearBegin) {
        this.yearBegin = yearBegin;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }
}
