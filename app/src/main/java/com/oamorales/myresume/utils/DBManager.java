package com.oamorales.myresume.utils;

import android.content.Context;
import android.widget.Toast;

import com.oamorales.myresume.models.Degree;

import io.realm.Realm;

public abstract class DBManager {

    public static void insert(Degree degree, Context context){
        Realm realm = Realm.getDefaultInstance();
        try{
            realm.beginTransaction();
            realm.copyToRealm(degree);
            realm.commitTransaction();
            realm.close();
        }catch (Error error){
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            realm.close();
        }
    }

    public static void update(Degree degree, Context context){
        Realm realm = Realm.getDefaultInstance();
        try{
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(degree);
            realm.commitTransaction();
            realm.close();
        }catch (Error error){
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            realm.close();
        }
    }

    public static void delete(Degree degree, Context context){
        Realm realm = Realm.getDefaultInstance();
        try{
            realm.beginTransaction();
            degree.deleteFromRealm();
            realm.commitTransaction();
            realm.close();
        }catch (Error error){
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            realm.close();
        }
    }

}
