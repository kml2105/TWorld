package com.semonics.tiktik.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.semonics.tiktik.R;
import com.semonics.tiktik.WebService.BaseAPIService;
import com.semonics.tiktik.WebService.RequestParams;
import com.semonics.tiktik.WebService.ResponseListener;
import com.semonics.tiktik.WebService.SessionManager;
import com.semonics.tiktik.WebService.TicTic;

import org.json.JSONObject;

import static com.googlecode.mp4parser.h264.Debug.print;
import static com.semonics.tiktik.SimpleClasses.Utils.methodToast;
import static com.semonics.tiktik.WebService.SessionManager.PREF_COMMENT;
import static com.semonics.tiktik.WebService.SessionManager.PREF_COMMENT_NOTI;
import static com.semonics.tiktik.WebService.SessionManager.PREF_DIRECT_MSG;
import static com.semonics.tiktik.WebService.SessionManager.PREF_DIRECT_MSG_NOTI;
import static com.semonics.tiktik.WebService.SessionManager.PREF_DOWNLOAD_VIDEO;
import static com.semonics.tiktik.WebService.SessionManager.PREF_FOLLOWERS_VIDEO_NOTI;
import static com.semonics.tiktik.WebService.SessionManager.PREF_LIKES;
import static com.semonics.tiktik.WebService.SessionManager.PREF_LIKES_NOTI;
import static com.semonics.tiktik.WebService.SessionManager.PREF_MENTION_NOTI;
import static com.semonics.tiktik.WebService.SessionManager.PREF_NEW_FOLLOWERS_VIDEO_NOTI;
import static com.semonics.tiktik.WebService.SessionManager.PREF_PRIVATE_ACC;
import static com.semonics.tiktik.WebService.SessionManager.PREF_SAVE_LOGIN_INFO;
import static com.semonics.tiktik.WebService.WSParams.SERVICE_ACCOUNT_SETTINGS;
import static com.semonics.tiktik.WebService.WSParams.WS_KEY_MSG;

public class CommentFragment extends Fragment implements View.OnClickListener {
    View view;
    int comment;
    SessionManager sessionManager;
    private RadioGroup radioGroup;
    private ProgressBar progressBar;
    private RadioButton radioButton;
    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment2, container, false);
       radioGroup =  view.findViewById(R.id.radioGroup);
        sessionManager = TicTic.getInstance().getSession();
        progressBar = view.findViewById(R.id.fragment_comment2_pb);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                      switch (checkedId) {
                                                          case R.id.rbEveryone:
                                                              comment = 0;
                                                              sessionManager.putInt(PREF_COMMENT,comment);
                                                              apiCall();
                                                              // do operations specific to this selection
                                                              break;
                                                          case R.id.rbPplFollowYou:
                                                              // do operations specific to this selection
                                                              comment = 2;
                                                              sessionManager.putInt(PREF_COMMENT,comment);
                                                              apiCall();
                                                              break;
                                                          case R.id.rbNoOne:
                                                              // do operations specific to this selection
                                                              comment = 1;
                                                              sessionManager.putInt(PREF_COMMENT,comment);
                                                              apiCall();
                                                              break;
                                                      }
                                                  }
                                              });
        view.findViewById(R.id.Goback).setOnClickListener(this);
        return view;

    }

    private void apiCall() {
        progressBar.setVisibility(View.VISIBLE);
        new BaseAPIService(getActivity(), SERVICE_ACCOUNT_SETTINGS, RequestParams.accountSettingApiCall( sessionManager.getInt(PREF_COMMENT),sessionManager.getBoolean(PREF_COMMENT_NOTI),sessionManager.getBoolean(PREF_DIRECT_MSG_NOTI),
                sessionManager.getInt(PREF_DIRECT_MSG),sessionManager.getBoolean(PREF_DOWNLOAD_VIDEO),sessionManager.getBoolean(PREF_FOLLOWERS_VIDEO_NOTI),
                sessionManager.getBoolean(PREF_LIKES_NOTI),sessionManager.getBoolean(PREF_MENTION_NOTI),sessionManager.getBoolean(PREF_NEW_FOLLOWERS_VIDEO_NOTI),
                sessionManager.getInt(PREF_PRIVATE_ACC),sessionManager.getBoolean(PREF_SAVE_LOGIN_INFO),sessionManager.getInt(PREF_LIKES)), false, responseListener, "POST", true);
    }
    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(res);
                String msg = jsonObject.getString(WS_KEY_MSG);
                print(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            progressBar.setVisibility(View.GONE);
            methodToast(getContext(), error);
        }
    };
    @Override
    public void onClick(View view) {
        switch ((view.getId())) {
            case R.id.Goback:
                getActivity().onBackPressed();
                break;
        }
    }
}