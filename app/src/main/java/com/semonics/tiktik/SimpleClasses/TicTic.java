package com.semonics.tiktik.SimpleClasses;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AQEEL on 3/18/2019.
 */

public class TicTic extends Application {

    private static SessionManager sessionManager;
    private static TicTic instance;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());
        instance = this;
    }
    public static TicTic getInstance() {
        return instance;
    }
    public SessionManager getSession() {
        if (sessionManager == null) {
            sessionManager = new SessionManager(instance);
        }
        return sessionManager;
    }


}
