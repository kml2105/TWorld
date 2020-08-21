package com.semonics.tworld.WebService;


import android.content.Context;
import android.content.SharedPreferences;

import static com.semonics.tworld.WebService.WSParams.IS_LOGIN;

public class SessionManager {     /*Shared Preferences*/
    SharedPreferences pref;     /*Editor for Shared preferences*/
    SharedPreferences.Editor editor;     /*Context*/
    Context _context;     /*Shared pref mode*/
    int PRIVATE_MODE = 0;     /*Sharedpref file name*/
    public static final String PREF_NAME = "tworld";
    public static final String PREF_TOKEN = "token";
    public static final String PREF_IS_LOGIN = "isLogIn";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_BASE_URL = "base_url";
    public static final String PREF_COMMENT = "comment";
    public static final String PREF_COMMENT_NOTI = "comment_noti";
    public static final String PREF_DIRECT_MSG = "direct_msg";
    public static final String PREF_DIRECT_MSG_NOTI = "direct_msg_noti";
    public static final String PREF_DOWNLOAD_VIDEO = "download_video";
    public static final String PREF_FOLLOWERS_VIDEO_NOTI = "followers_video_noti";
    public static final String PREF_NEW_FOLLOWERS_VIDEO_NOTI = "new_followers_video_noti";
    public static final String PREF_LIKES = "likes";
    public static final String PREF_MENTION = "mentions";
    public static final String PREF_LIKES_NOTI = "likes_noti";
    public static final String PREF_MENTION_NOTI = "mention_noti";
    public static final String PREF_SAVE_LOGIN_INFO = "save_login_info";
    public static final String PREF_LIKE_COUNT = "like_count";
    public static final String PREF_SEARCH_KEYWORD = "keyword";
    public static final String PREF_PRIVATE_ACC= "private_acc";
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

    public boolean getBoolean(final String key) {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getBoolean(key, false);
    }

    public void setBoolean(final String key, final boolean value) {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /*Method to clear the login data of the application.*/
    public void clearAllData() {
        editor.remove(PREF_IS_LOGIN);
        editor.remove(PREF_TOKEN);
        editor.remove(PREF_NAME);
        editor.remove(PREF_LIKES);
        editor.remove(PREF_SEARCH_KEYWORD);
        editor.remove(PREF_LIKE_COUNT);
        editor.remove(PREF_SAVE_LOGIN_INFO);
        editor.remove(PREF_MENTION_NOTI);
        editor.remove(PREF_LIKES_NOTI);
        editor.remove(PREF_NEW_FOLLOWERS_VIDEO_NOTI);
        editor.remove(PREF_USER_ID);
        editor.remove(PREF_FOLLOWERS_VIDEO_NOTI);
        editor.remove(PREF_NEW_FOLLOWERS_VIDEO_NOTI);
        editor.remove(PREF_COMMENT);
        editor.apply();
        TWorld.getInstance().getSession().setBoolean(PREF_IS_LOGIN, false);
        //_context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        // _context.finish();
    }


    /**
     * Quick check for login
     **/
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}

