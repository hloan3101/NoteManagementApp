package com.example.notemanagerapp;

import android.app.Application;

import com.example.notemanagerapp.sharedpreferences.DataLocalManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
