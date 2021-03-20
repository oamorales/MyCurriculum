package com.oamorales.myresume.repositories;

import androidx.lifecycle.MutableLiveData;

import com.oamorales.myresume.models.Skill;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

public class SkillsRepository {

    private static SkillsRepository instance;
    private RealmResults<Skill> dataSet;

    public static SkillsRepository getInstance(){
        if (instance == null)
            instance = new SkillsRepository();
        return instance;
    }

    public MutableLiveData<RealmResults<Skill>> getSkillsList(){
        loadSkills();
        MutableLiveData<RealmResults<Skill>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void loadSkills(){
        Realm realm = Realm.getDefaultInstance();
        dataSet = realm.where(Skill.class).findAllAsync().sort("skill", Sort.ASCENDING);
        realm.close();
    }

    public MutableLiveData<Skill> getSkill(String name){
        Realm realm = Realm.getDefaultInstance();
        Skill s = realm.where(Skill.class).equalTo("skill", name).findFirst();
        MutableLiveData<Skill> data = new MutableLiveData<>();
        data.setValue(s);
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

}
