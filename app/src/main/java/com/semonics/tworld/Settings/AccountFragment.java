package com.semonics.tworld.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.semonics.tworld.R;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;
import com.semonics.tworld.WebService.SessionManager;
import com.semonics.tworld.WebService.TWorld;

import org.json.JSONObject;

import static com.googlecode.mp4parser.h264.Debug.print;
import static com.semonics.tworld.SimpleClasses.Utils.methodToast;
import static com.semonics.tworld.WebService.SessionManager.PREF_COMMENT;
import static com.semonics.tworld.WebService.SessionManager.PREF_COMMENT_NOTI;
import static com.semonics.tworld.WebService.SessionManager.PREF_DIRECT_MSG;
import static com.semonics.tworld.WebService.SessionManager.PREF_DIRECT_MSG_NOTI;
import static com.semonics.tworld.WebService.SessionManager.PREF_DOWNLOAD_VIDEO;
import static com.semonics.tworld.WebService.SessionManager.PREF_FOLLOWERS_VIDEO_NOTI;
import static com.semonics.tworld.WebService.SessionManager.PREF_LIKES;
import static com.semonics.tworld.WebService.SessionManager.PREF_LIKES_NOTI;
import static com.semonics.tworld.WebService.SessionManager.PREF_MENTION_NOTI;
import static com.semonics.tworld.WebService.SessionManager.PREF_NEW_FOLLOWERS_VIDEO_NOTI;
import static com.semonics.tworld.WebService.SessionManager.PREF_PRIVATE_ACC;
import static com.semonics.tworld.WebService.SessionManager.PREF_SAVE_LOGIN_INFO;
import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_ACCOUNT_SETTINGS;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_MSG;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

public class AccountFragment extends Fragment {
    private int privateAcc;
    View view;
    private SessionManager sessionManager;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        Switch downloadSwitch = view.findViewById(R.id.video_download_switch);
        sessionManager = TWorld.getInstance().getSession();
        if(sessionManager.getInt(PREF_PRIVATE_ACC)==0){
            downloadSwitch.setChecked(true);
        }else{
            downloadSwitch.setChecked(false);
        }
        downloadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    privateAcc = 0;
                } else {
                    privateAcc = 1;
                }
                sessionManager.putInt(PREF_PRIVATE_ACC, privateAcc);
                apiCall();
                Snackbar.make(buttonView, "Switch state checked " + isChecked, Snackbar.LENGTH_LONG)
                        .setAction("ACTION", null).show();
            }
        });
        view.findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    private void apiCall() {
        new BaseAPIService(getActivity(), SERVICE_ACCOUNT_SETTINGS, RequestParams.accountSettingApiCall(sessionManager.getInt(PREF_COMMENT), sessionManager.getBoolean(PREF_COMMENT_NOTI), sessionManager.getBoolean(PREF_DIRECT_MSG_NOTI),
                sessionManager.getInt(PREF_DIRECT_MSG), sessionManager.getBoolean(PREF_DOWNLOAD_VIDEO), sessionManager.getBoolean(PREF_FOLLOWERS_VIDEO_NOTI),
                sessionManager.getBoolean(PREF_LIKES_NOTI), sessionManager.getBoolean(PREF_MENTION_NOTI), sessionManager.getBoolean(PREF_NEW_FOLLOWERS_VIDEO_NOTI),
                privateAcc, sessionManager.getBoolean(PREF_SAVE_LOGIN_INFO), sessionManager.getInt(PREF_LIKES)), true, responseListener, METHOD_POST, true);
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject(WS_KEY_OBJ);
                sessionManager.putInt(PREF_COMMENT, jsonObject1.getInt("commentVideoPrivacy"));
                sessionManager.setBoolean(PREF_COMMENT_NOTI, jsonObject1.getBoolean("comments"));
                sessionManager.setBoolean(PREF_DIRECT_MSG_NOTI, jsonObject1.getBoolean("directMessage"));
                sessionManager.putInt(PREF_DIRECT_MSG, jsonObject1.getInt("directMessagePrivacy"));
                sessionManager.setBoolean(PREF_DOWNLOAD_VIDEO, jsonObject1.getBoolean("downloadVideoPrivacy"));
                sessionManager.setBoolean(PREF_FOLLOWERS_VIDEO_NOTI, jsonObject1.getBoolean("followersVideo"));
                sessionManager.setBoolean(PREF_LIKES_NOTI, jsonObject1.getBoolean("likes"));
                sessionManager.setBoolean(PREF_MENTION_NOTI, jsonObject1.getBoolean("mentions"));
                sessionManager.setBoolean(PREF_NEW_FOLLOWERS_VIDEO_NOTI, jsonObject1.getBoolean("followersVideo"));
                sessionManager.putInt(PREF_PRIVATE_ACC, jsonObject1.getInt("reactToVideoPrivacy"));
                sessionManager.setBoolean(PREF_SAVE_LOGIN_INFO, jsonObject1.getBoolean("saveLoginInfo"));
                sessionManager.putInt(PREF_LIKES, jsonObject1.getInt("viewLikedVideosPrivacy"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            methodToast(getContext(), error);
        }
    };

}