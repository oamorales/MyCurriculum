package com.oamorales.myresume.models;

import io.realm.RealmObject;

public class Language extends RealmObject {
    //@PrimaryKey
    private String language;
    private int level;
    //1 básico
    //2 básico limitado
    //3 básico profesional
    //4 profesional completo
    //5 bilingüe o nativo

    public Language() {   }

    public Language(String language, int level) {
        this.language = language;
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
