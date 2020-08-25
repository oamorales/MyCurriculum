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
    private int startYear;
    private int endYear;

    public WorkExp() {  }

    public WorkExp(String companyName, String position, String positionDesc, int yearBegin, int yearEnd) {
        this.id = MyApplication.workExpId.incrementAndGet();
        this.companyName = companyName;
        this.position = position;
        this.positionDesc = positionDesc;
        this.startYear = yearBegin;
        this.endYear = yearEnd;
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

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}
