package com.example.cityworld.App;

import android.app.Application;

import com.example.cityworld.Models.City;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApp extends Application {
    public static AtomicInteger CityId = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        setUpRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        CityId = getIdByTable(realm, City.class);

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build());

        realm.close();
    }

    public void setUpRealmConfig(){
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyObject){
        RealmResults<T> results = realm.where(anyObject).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
