package com.oamorales.myresume.repositories;

import androidx.lifecycle.MutableLiveData;

import com.oamorales.myresume.models.WorkExp;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

public class WorkExpRepository {

    private static WorkExpRepository instance;
    private RealmResults<WorkExp> dataSet;

    public static WorkExpRepository getInstance(){
        if (instance == null)
            instance = new WorkExpRepository();
        return instance;
    }

    public MutableLiveData<RealmResults<WorkExp>> getWorkExpList(){
        loadList();
        MutableLiveData<RealmResults<WorkExp>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void loadList(){
        Realm realm = Realm.getDefaultInstance();
        dataSet = realm.where(WorkExp.class).findAll().sort("endDate", Sort.DESCENDING);
        realm.close();
    }

    public MutableLiveData<WorkExp> getWorkExp(int id){
        Realm realm = Realm.getDefaultInstance();
        WorkExp workExp = realm.where(WorkExp.class).equalTo("id", id).findFirst();
        MutableLiveData<WorkExp> data = new MutableLiveData<>();
        data.setValue(workExp);
        realm.close();
        return data;
    }

    public void insert(RealmObject object){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
        realm.close();
    }

    public void remove(RealmObject object){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        object.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public void update(WorkExp newWorkExp, int id){
        Realm realm = Realm.getDefaultInstance();
        WorkExp workExp = realm.where(WorkExp.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        assert workExp != null;
        workExp.setCompanyName(newWorkExp.getCompanyName());
        workExp.setPosition(newWorkExp.getPosition());
        workExp.setPositionDesc(newWorkExp.getPositionDesc());
        workExp.setStartDate(newWorkExp.getStartDate());
        workExp.setEndDate(newWorkExp.getEndDate());
        workExp.setStartYear(newWorkExp.getStartYear());
        workExp.setEndYear(newWorkExp.getEndYear());
        realm.copyToRealmOrUpdate(workExp);
        realm.commitTransaction();
        realm.close();
    }

}
