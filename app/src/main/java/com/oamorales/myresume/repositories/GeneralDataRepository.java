package com.oamorales.myresume.repositories;

import androidx.lifecycle.MutableLiveData;

import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.models.PersonInfo;
import com.oamorales.myresume.models.WorkExp;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class GeneralDataRepository {

    private static GeneralDataRepository instance;
    private int year;

    public static GeneralDataRepository getInstance(){
        if (instance == null)
            instance = new GeneralDataRepository();
        return instance;
    }

    public MutableLiveData<Degree> getDegree(){
        Calendar c = Calendar.getInstance();
        Realm realm = Realm.getDefaultInstance();
        Degree degree = realm.where(Degree.class).findFirst();
        RealmResults<Degree> results = realm.where(Degree.class).findAll()
                .sort("yearEnd", Sort.DESCENDING).sort("yearBegin", Sort.DESCENDING);
        if (results.size() >0)
            degree = results.first();
        MutableLiveData<Degree> data = new MutableLiveData<>();
        data.setValue(degree);
        realm.close();
        return data;
    }

    public MutableLiveData<PersonInfo> getPersonInfo(){
        Realm realm = Realm.getDefaultInstance();
        PersonInfo person = realm.where(PersonInfo.class).findFirst();
        MutableLiveData<PersonInfo> data = new MutableLiveData<>();
        data.setValue(person);
        realm.close();
        return data;
    }

    public MutableLiveData<WorkExp> getWorkExp(){
        Calendar c = Calendar.getInstance();
        Realm realm = Realm.getDefaultInstance();
        WorkExp workExp = realm.where(WorkExp.class).findFirst();
        RealmResults<WorkExp> results = realm.where(WorkExp.class).findAll()
                .sort("endYear", Sort.DESCENDING).sort("startYear", Sort.DESCENDING);
        if (results.size() > 0)
            workExp = results.first();
        MutableLiveData<WorkExp> data = new MutableLiveData<>();
        data.setValue(workExp);
        realm.close();
        return data;
    }

    public MutableLiveData<Language> getLanguage(){
        Realm realm = Realm.getDefaultInstance();
        Language language = realm.where(Language.class).findFirst();
        RealmResults<Language> results = realm.where(Language.class).findAll().sort("level", Sort.DESCENDING);
        if (results.size() > 0)
            language = results.first();
        MutableLiveData<Language> data = new MutableLiveData<>();
        data.setValue(language);
        realm.close();
        return data;
    }

}
