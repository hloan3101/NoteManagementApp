package com.example.notemanagerapp.sharedpreferences;

import android.content.Context;

public class DataLocalManager {

    private static final String PREF_FIRST_INSTALL = "PREF_FIRST_INSTALL";
    private static final String CHECK_LOGOUT = "CHECK_LOGOUT";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init (Context context){
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance(){
        if (instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setFirstInstalled (boolean isFirst){
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(PREF_FIRST_INSTALL, isFirst);
    }

    public static boolean getFirstInstalled (){
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(PREF_FIRST_INSTALL);
    }

    public static void setCheckLogout (boolean isLogout){
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(CHECK_LOGOUT, isLogout);
    }

    public static boolean getCheckLogout (){
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(CHECK_LOGOUT);
    }

    public static void setEmail (boolean email){
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(EMAIL, email);
    }

    public static String getEmail (){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(EMAIL);
    }

    public static void setPassword (boolean password){
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(PASSWORD, password);
    }

    public static String getPassword (){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(PASSWORD);
    }


}
