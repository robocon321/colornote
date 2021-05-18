package com.example.colornote.util;

import android.content.SharedPreferences;

public class Settings {
    SharedPreferences sharedPreferences;
    private static Settings settings = new Settings();
    public static Settings getInstance(){
        return settings;
    }

    private Settings(){}

    public void setSharedPreferences(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
}
