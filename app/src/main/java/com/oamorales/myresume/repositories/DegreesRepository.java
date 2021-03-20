package com.oamorales.myresume.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.oamorales.myresume.models.Degree;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

public class DegreesRepository {

    private static DegreesRepository instance;
    private RealmResults<Degree> dataSet;

    /** Singleton */
    public static DegreesRepository getInstance(){
        if (instance == null){
            instance = new DegreesRepository();
        }
        return instance;
    }

    public MutableLiveData<RealmResults<Degree>> getDegrees(){
        loadDegrees();
        MutableLiveData<RealmResults<Degree>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void loadDegrees(){
        try(Realm realm = Realm.getDefaultInstance()){
            dataSet = realm.where(Degree.class).findAll().sort("yearEnd", Sort.DESCENDING);
        }
    }

    public MutableLiveData<Degree> getDegree(int id){
        MutableLiveData<Degree> mutableLiveData = new MutableLiveData<>();
        Realm realm = Realm.getDefaultInstance();
        Degree degree = realm.where(Degree.class).equalTo("id", id).findFirst();
        mutableLiveData.setValue(degree);
        realm.close();
        return mutableLiveData;
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

    /** Update Degree Object */
    public void updateDegree(Degree newDegree, int id, Context context){
        Realm realm = Realm.getDefaultInstance();
        Degree degree = realm.where(Degree.class).equalTo("id", id).findFirst();
        try{
            realm.beginTransaction();
            assert degree != null;
            degree.setDegreeTittle(newDegree.getDegreeTittle());
            degree.setUniversity(newDegree.getUniversity());
            degree.setDiscipline(newDegree.getDiscipline());
            degree.setStartDate(newDegree.getStartDate());
            degree.setEndDate(newDegree.getEndDate());
            degree.setYearBegin(newDegree.getYearBegin());
            degree.setYearEnd(newDegree.getYearEnd());
            degree.setGradeAverage(newDegree.getGradeAverage());
            realm.copyToRealmOrUpdate(degree);
            realm.commitTransaction();
            realm.close();
        }catch (Error error){
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            realm.close();
        }
    }

    public void removeDegree(RealmObject object){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        object.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    /** Provide an ID to get an object from DB */
    /*public static <T extends RealmObject> RealmObject getObjectById(Class<T> anyClass, int id){
        Realm realm = Realm.getDefaultInstance();
        RealmObject object = realm.where(anyClass).equalTo("id", id).findFirst();
        realm.close();
        return object;
    }*/

}
