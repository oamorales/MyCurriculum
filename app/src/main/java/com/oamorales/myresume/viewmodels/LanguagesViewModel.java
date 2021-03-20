package com.oamorales.myresume.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oamorales.myresume.models.Language;
import com.oamorales.myresume.repositories.LanguagesRepository;

import io.realm.RealmObject;
import io.realm.RealmResults;

public class LanguagesViewModel extends ViewModel {

    private MutableLiveData<RealmResults<Language>> languages;
    private MutableLiveData<Language> language;
    private LanguagesRepository repository;

    public void init(){
        if (languages != null)
            return;
        repository = LanguagesRepository.getInstance();
        languages = repository.getLanguagesList();
    }

    public LiveData<RealmResults<Language>> getLanguageList(){
        return languages;
    }

    public LiveData<Language> getLanguage(String language){
        setLanguage(language);
        return this.language;
    }

    private void setLanguage(String language) {
        this.language = repository.getLanguage(language);
    }

    public void saveLanguage(RealmObject object, Context context){
        repository.insert(object, context);
    }

    public void deleteLanguage(RealmObject object, Context context){
        repository.remove(object, context);
    }

    public boolean exist(String language){
        MutableLiveData<Language> data = repository.getLanguage(language);
        Language l = data.getValue();
        return l == null;
    }

}
