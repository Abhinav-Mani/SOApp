package com.maxpetroleum.soapp.Activity;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class SOApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
