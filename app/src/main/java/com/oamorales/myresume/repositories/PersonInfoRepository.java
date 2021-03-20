package com.oamorales.myresume.repositories;

import androidx.lifecycle.MutableLiveData;

import com.oamorales.myresume.models.PersonInfo;

import io.realm.Realm;

public class PersonInfoRepository {

    private static PersonInfoRepository instance;

    public static PersonInfoRepository getInstance(){
        if (instance==null)
            instance = new PersonInfoRepository();
        return instance;
    }

    public MutableLiveData<PersonInfo> getPersonInfo(){
        Realm realm = Realm.getDefaultInstance();
        PersonInfo info = realm.where(PersonInfo.class).findFirst();
        MutableLiveData<PersonInfo> personInfo = new MutableLiveData<>();
        personInfo.setValue(info);
        realm.close();
        return personInfo;
    }

    public void savePersonInfo(PersonInfo person){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PersonInfo info = realm.where(PersonInfo.class).findFirst();
        if (info != null)
            info.deleteFromRealm();
        realm.copyToRealm(person);
        realm.commitTransaction();
        realm.close();
    }

}
