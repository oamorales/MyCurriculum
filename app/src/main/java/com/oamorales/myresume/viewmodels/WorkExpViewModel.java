package com.oamorales.myresume.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.repositories.WorkExpRepository;

import io.realm.RealmObject;
import io.realm.RealmResults;

public class WorkExpViewModel extends ViewModel {

    private MutableLiveData<RealmResults<WorkExp>> list;
    private MutableLiveData<WorkExp> workExp;
    private WorkExpRepository repository;

    public void init(){
        if (list != null)
            return;
        repository = WorkExpRepository.getInstance();
        list = repository.getWorkExpList();
    }

    public LiveData<RealmResults<WorkExp>> getWorkExpList(){
        return list;
    }

    public LiveData<WorkExp> getWorkExp(int id){
        loadWorkExp(id);
        return this.workExp;
    }

    private void loadWorkExp(int id){
        workExp = repository.getWorkExp(id);
    }

    public void createWorkExp(RealmObject object){
        repository.insert(object);
    }

    public void removeWorkExp(RealmObject object){
        repository.remove(object);
    }

    public void updateWorkExp(WorkExp newWorkExp, int id){
        repository.update(newWorkExp, id);
    }

}
