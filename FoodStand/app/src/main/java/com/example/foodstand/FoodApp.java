package com.example.foodstand;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class FoodApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
