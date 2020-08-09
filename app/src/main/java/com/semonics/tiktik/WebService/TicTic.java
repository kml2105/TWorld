package com.semonics.tiktik.WebService;

import android.app.Application;

import com.google.firebase.FirebaseApp;



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
      //  Fabric.with(this, new Crashlytics());
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
