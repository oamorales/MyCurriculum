package com.oamorales.myresume.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.oamorales.myresume.models.Language;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

public class LanguagesRepository {

    private static LanguagesRepository instance;
    private RealmResults<Language> dataSet;

    public static LanguagesRepository getInstance(){
        if (instance==null)
            instance = new LanguagesRepository();
        return instance;
    }

    public MutableLiveData<RealmResults<Language>> getLanguagesList(){
        loadLanguages();
        MutableLiveData<RealmResults<Language>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void loadLanguages() {
        try(Realm realm = Realm.getDefaultInstance()){
            dataSet = realm.where(Language.class).findAll().sort("level", Sort.DESCENDING);
        }
    }

    public MutableLiveData<Language> getLanguage(String language){
        Realm realm = Realm.getDefaultInstance();
        Language l = realm.where(Language.class).equalTo("language", language).findFirst();
        MutableLiveData<Language> data = new MutableLiveData<>();
        data.setValue(l);
        realm.close();
        return data;
    }

    /** Insert an object */
    public void insert(RealmObject realmObject, Context context){
        Realm realm = Realm.getDefaultInstance();
        try{
            realm.beginTransaction();
            realm.copyToRealm(realmObject);
            realm.commitTransaction();
            realm.close();
        }catch (Error error){
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            realm.close();
        }
    }

    public void remove(RealmObject realmObject, Context context){
        Realm realm = Realm.getDefaultInstance();
        try{
            realm.beginTransaction();
            realmObject.deleteFromRealm();
            realm.commitTransaction();
            realm.close();
        }catch (Error error){
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            realm.close();
        }
    }

}
