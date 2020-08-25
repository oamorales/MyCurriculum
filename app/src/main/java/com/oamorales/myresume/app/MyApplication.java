package com.oamorales.myresume.app;

import android.app.Application;

import com.oamorales.myresume.models.Degree;
import com.oamorales.myresume.models.WorkExp;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application {

    public static AtomicInteger degreeId = new AtomicInteger();
    public static AtomicInteger workExpId = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        configureRealm();
        Realm realm = Realm.getDefaultInstance();
        degreeId = getIdByTable(realm, Degree.class);
        workExpId = getIdByTable(realm, WorkExp.class);
        realm.close();
    }

    private void configureRealm(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("myrealm")
                //Elimina la BD si requiere migración; solo para utilizarlo en fase de desarrollo
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size()>0)?new AtomicInteger(results.max("id").intValue()):new AtomicInteger();
    }
}
