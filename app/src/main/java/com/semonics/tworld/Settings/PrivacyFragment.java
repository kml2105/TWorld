package com.semonics.tworld.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.semonics.tworld.R;
import com.semonics.tworld.Settings.BlockAccount.BlockedAccListFragment;
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
import static com.semonics.tworld.WebService.WSParams.SERVICE_ACCOUNT_SETTINGS;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_MSG;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

public class PrivacyFragment extends Fragment implements View.OnClickListener {

    View view;
    boolean videoDownload;
    SessionManager sessionManager;

    public PrivacyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_privacy, container, false);
        sessionManager = TWorld.getInstance().getSession();
        view.findViewById(R.id.video_comment).setOnClickListener(this);
        Switch downloadSwitch = view.findViewById(R.id.video_download_switch);
        downloadSwitch.setChecked(sessionManager.getBoolean(PREF_DOWNLOAD_VIDEO));
        downloadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    videoDownload = true;
                } else {
                    videoDownload = false;
                }
                apiCall();
                Snackbar.make(buttonView, "Switch state checked " + isChecked, Snackbar.LENGTH_LONG)
                        .setAction("ACTION", null).show();
            }
        });
        view.findViewById(R.id.video_mention).setOnClickListener(this);
        view.findViewById(R.id.Goback).setOnClickListener(this);
        view.findViewById(R.id.bloc_account).setOnClickListener(this);
        return view;
    }

    private void apiCall() {
        new BaseAPIService(getActivity(), SERVICE_ACCOUNT_SETTINGS, RequestParams.accountSettingApiCall( sessionManager.getInt(PREF_COMMENT),sessionManager.getBoolean(PREF_COMMENT_NOTI),sessionManager.getBoolean(PREF_DIRECT_MSG_NOTI),
                sessionManager.getInt(PREF_DIRECT_MSG),sessionManager.getBoolean(PREF_DOWNLOAD_VIDEO),sessionManager.getBoolean(PREF_FOLLOWERS_VIDEO_NOTI),
                sessionManager.getBoolean(PREF_LIKES_NOTI),sessionManager.getBoolean(PREF_MENTION_NOTI),sessionManager.getBoolean(PREF_NEW_FOLLOWERS_VIDEO_NOTI),
                sessionManager.getInt(PREF_PRIVATE_ACC),sessionManager.getBoolean(PREF_SAVE_LOGIN_INFO),sessionManager.getInt(PREF_LIKES)), true, responseListener, "POST", true);
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

    public void navigateToComment() {
        CommentFragment commentFragment = new CommentFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_privacy
                , commentFragment).commit();
    }

    public void navigateToMention() {
        TagsFragment tagsFragment = new TagsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_privacy, tagsFragment).commit();
    }

    public void navigateToBlockedAccList() {
        BlockedAccListFragment blockedAccListFragment = new BlockedAccListFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_privacy, blockedAccListFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_comment:
                navigateToComment();
                break;

            case R.id.video_mention:
                navigateToMention();
                break;

            case R.id.Goback:
                getActivity().onBackPressed();
                break;

            case R.id.bloc_account:
                navigateToBlockedAccList();
                break;
        }

    }
}