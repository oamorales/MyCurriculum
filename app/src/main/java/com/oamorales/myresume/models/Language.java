package com.oamorales.myresume.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Language extends RealmObject {
    @PrimaryKey
    private int id;
    private String language;
    private String nivel;
    //básico
    //básico limitado
    //básico profesional
    //profesional completo
    //bilingüe o nativo
}
