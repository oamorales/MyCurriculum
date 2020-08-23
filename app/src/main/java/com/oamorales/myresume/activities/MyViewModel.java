package com.oamorales.myresume.activities;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Uri> imgPath = new MutableLiveData<>();

    public void setImgPath(Uri path){
        imgPath.setValue(path);
    }

    public LiveData<Uri> getImgPath(){
        return imgPath;
    }
}
