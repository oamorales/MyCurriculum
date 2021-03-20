package com.oamorales.myresume.models;

import com.oamorales.myresume.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Degree extends RealmObject {
    @PrimaryKey
    private int id;
    private String degreeTittle;
    private String university;
    private String discipline;
    private String startDate;
    private String endDate;
    private int yearBegin;
    private int yearEnd;
    private float gradeAverage;

    public Degree() {  }

    public Degree(String degreeTittle, String university, String discipline, String startDate,
                  String endDate, int yearBegin, int yearEnd, float gradeAverage) {
        this.id = MyApplication.degreeId.incrementAndGet();
        this.degreeTittle = degreeTittle;
        this.university = university;
        this.discipline = discipline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.yearBegin = yearBegin;
        this.yearEnd = yearEnd;
        this.gradeAverage = gradeAverage;
    }

    public int getId() {
        return id;
    }

    public String getDegreeTittle() {
        return degreeTittle;
    }

    public void setDegreeTittle(String degreeTittle) {
        this.degreeTittle = degreeTittle;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
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

    public float getGradeAverage() {
        return gradeAverage;
    }

    public void setGradeAverage(float gradeAverage) {
        this.gradeAverage = gradeAverage;
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
}
