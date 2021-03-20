package com.oamorales.myresume.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.models.PersonInfo;
import com.oamorales.myresume.models.WorkExp;
import com.oamorales.myresume.repositories.GeneralDataRepository;

public class GeneralDataViewModel extends ViewModel {

    private GeneralDataRepository repository;
    private MutableLiveData<PersonInfo> personInfo;
    private MutableLiveData<Degree> degree;
    private MutableLiveData<WorkExp> workExp;
    private MutableLiveData<Language> language;
    private MutableLiveData<String> imgPath = new MutableLiveData<>();

    public void init(){
        repository = GeneralDataRepository.getInstance();
    }

    private void setDegree(){
        degree = repository.getDegree();
    }

    public LiveData<Degree> getDegree(){
        setDegree();
        return degree;
    }

    private void setPersonInfo(){
        personInfo = repository.getPersonInfo();
    }

    public LiveData<PersonInfo> getPersonInfo(){
        setPersonInfo();
        return personInfo;
    }

    private void setWorkExp(){
        workExp = repository.getWorkExp();
    }

    public LiveData<WorkExp> getWorkExp(){
        setWorkExp();
        return workExp;
    }

    private void setLanguage(){
        language = repository.getLanguage();
    }

    public LiveData<Language> getLanguage(){
        setLanguage();
        return language;
    }

    public LiveData<String> getImgPath(){
        /*if (imgPath==null){
            setImgPath("");
        }*/
        return imgPath;
    }

    public void setImgPath(String imgPath){
        this.imgPath.setValue(imgPath);
    }

}
