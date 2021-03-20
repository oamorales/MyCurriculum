package com.oamorales.myresume.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oamorales.myresume.models.PersonInfo;
import com.oamorales.myresume.repositories.PersonInfoRepository;

public class PersonInfoViewModel extends ViewModel {

    private MutableLiveData<PersonInfo> personInfo;
    private PersonInfoRepository repository;

    public void init(){
        if (personInfo != null)
            return;
        repository = PersonInfoRepository.getInstance();
        personInfo = repository.getPersonInfo();
    }

    public LiveData<PersonInfo> getPersonInfo(){
        setPersonInfo();
        return this.personInfo;
    }

    private void setPersonInfo(){
        this.personInfo = repository.getPersonInfo();
    }

    public void savePersonInfo(PersonInfo person){
        repository.savePersonInfo(person);
    }
}
