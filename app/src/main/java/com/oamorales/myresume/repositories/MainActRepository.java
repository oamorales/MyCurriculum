package com.oamorales.myresume.repositories;

import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.models.PersonInfo;
import com.oamorales.myresume.models.WorkExp;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActRepository {

    public static PersonInfo getPersonInfo(Realm realm){
        return realm.where(PersonInfo.class).findFirst();
    }

    public static Degree getDegree(Realm realm){
        Degree degree = realm.where(Degree.class).findFirst();
        RealmResults<Degree> results = realm.where(Degree.class).findAll()
                .sort("yearEnd", Sort.DESCENDING).sort("yearBegin", Sort.DESCENDING);
        if (results.size() >0)
            degree = results.first();
        return degree;
    }

    public static WorkExp getWorkExp(Realm realm){
        WorkExp workExp = realm.where(WorkExp.class).findFirst();
        RealmResults<WorkExp> results = realm.where(WorkExp.class).findAll()
                .sort("endYear", Sort.DESCENDING).sort("startYear", Sort.DESCENDING);
        if (results.size() > 0)
            workExp = results.first();
        return workExp;
    }

    public static Language getLanguage(Realm realm){
        Language language = realm.where(Language.class).findFirst();
        RealmResults<Language> results = realm.where(Language.class).findAll().sort("level", Sort.DESCENDING);
        if (results.size() > 0)
            language = results.first();
        return language;
    }

}
