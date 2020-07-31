package com.oamorales.myresume.models;

import com.oamorales.myresume.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Degree extends RealmObject {
    @PrimaryKey
    private int id;
    private int imageLogo;
    private String degreeTittle;
    private String university;
    private String discipline;
    private int yearBegin;
    private int yearEnd;
    private double gradeAverage;

    public Degree() {  }

    public Degree(int imageLogo, String degreeTittle, String university, String discipline, int yearBegin, int yearEnd, double gradeAverage) {
        this.id = MyApplication.degreeId.incrementAndGet();
        this.imageLogo = imageLogo;
        this.degreeTittle = degreeTittle;
        this.university = university;
        this.discipline = discipline;
        this.yearBegin = yearBegin;
        this.yearEnd = yearEnd;
        this.gradeAverage = gradeAverage;
    }

    public int getId() {
        return id;
    }

    public int getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(int imageLogo) {
        this.imageLogo = imageLogo;
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

    public double getGradeAverage() {
        return gradeAverage;
    }

    public void setGradeAverage(float gradeAverage) {
        this.gradeAverage = gradeAverage;
    }
}
