package com.semonics.tiktik.SimpleClasses;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.semonics.tiktik.SimpleClasses.WSParams.IS_LOGIN;

public class SessionManager {     /*Shared Preferences*/
    SharedPreferences pref;     /*Editor for Shared preferences*/
    SharedPreferences.Editor editor;     /*Context*/
    Context _context;     /*Shared pref mode*/
    int PRIVATE_MODE = 0;     /*Sharedpref file name*/
    public static final String PREF_NAME = "tworld";
    public static final String PREF_TOKEN = "token";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

   /* public void setRegistrationDetail(String mobileNo, String password, String deviceId) {         *//*Storing login value as TRUE*//*
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(MOBILE_NO, mobileNo);
        editor.putString(PASSWORD, password);
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }  */

    public void putString(final String key, final String value) {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(final String key) {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getString(key, "");
    }

    public int getInt(final String key) {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getInt(key, 0);
    }

    public void putInt(final String key, final int value) {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }





    /**
     * Quick check for login
     **/
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}

