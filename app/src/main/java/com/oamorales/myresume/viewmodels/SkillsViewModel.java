package com.oamorales.myresume.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oamorales.myresume.models.Skill;
import com.oamorales.myresume.repositories.SkillsRepository;

import io.realm.RealmObject;
import io.realm.RealmResults;

public class SkillsViewModel extends ViewModel {

    private MutableLiveData<RealmResults<Skill>> skillList;
    private MutableLiveData<Skill> skill;
    private SkillsRepository repository;

    public void init(){
        if (skillList != null)
            return;
        repository = SkillsRepository.getInstance();
        skillList = repository.getSkillsList();
    }

    public LiveData<RealmResults<Skill>> getSkillsList(){
        return skillList;
    }

    public LiveData<Skill> getSkill(String name){
        setSkill(name);
        return this.skill;
    }

    private void setSkill(String name){
        skill = repository.getSkill(name);
    }

    public void saveSkill(RealmObject object){
        repository.insert(object);
    }

    public void removeSkill(RealmObject object){
        repository.remove(object);
    }

    public void updateSkill(RealmObject object, String newValue){
        repository.remove(object);
        Skill skill = new Skill(newValue);
        repository.insert(skill);
    }
}
