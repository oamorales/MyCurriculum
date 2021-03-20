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
    private String startDate;
    private String endDate;
    private int startYear;
    private int endYear;

    public WorkExp() {  }

    public WorkExp(String companyName, String position, String positionDesc, String startDate, String endDate, int startYear, int endYear) {
        this.id = MyApplication.workExpId.incrementAndGet();
        this.companyName = companyName;
        this.position = position;
        this.positionDesc = positionDesc;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startYear = startYear;
        this.endYear = endYear;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
