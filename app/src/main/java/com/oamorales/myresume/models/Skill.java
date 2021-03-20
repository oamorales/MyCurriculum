package com.oamorales.myresume.models;

import io.realm.RealmObject;

public class Skill extends RealmObject {

    private String skill;

    public Skill() {  }

    public Skill(String skill) {
        this.skill = skill;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}
