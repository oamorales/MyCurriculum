package com.oamorales.myresume.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.repositories.DegreesRepository;

import io.realm.RealmObject;
import io.realm.RealmResults;

public class DegreesViewModel extends ViewModel {

    private MutableLiveData<RealmResults<Degree>> degrees;
    private DegreesRepository repository;
    private MutableLiveData<Degree> degreeMutableLiveData;

    public void init(){
        if (degrees != null){
            return;
        }
        repository = DegreesRepository.getInstance();
        degrees = repository.getDegrees();
    }

    public LiveData<RealmResults<Degree>> getDegreesList(){
        return degrees;
    }

    public LiveData<Degree> getDegree(int id) {
        setDegree(id);
        return degreeMutableLiveData;
    }

    private void setDegree(int id) {
        this.degreeMutableLiveData = repository.getDegree(id);
    }

    public void createDegree(Degree degree, Context context){
        repository.insert(degree, context);
    }

    public void updateDegree(Degree newDegree, int id, Context context){
        repository.updateDegree(newDegree, id, context);
    }

    public void removeDegree(RealmObject object){
        repository.removeDegree(object);
    }
}
