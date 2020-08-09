package com.semonics.tiktik.Settings;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semonics.tiktik.Accounts.LoginActivity;
import com.semonics.tiktik.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.semonics.tiktik.R;
import com.semonics.tiktik.WebService.TicTic;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends RootFragment implements View.OnClickListener {

    View view;
    Context context;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_setting, container, false);
        view.findViewById(R.id.Goback).setOnClickListener(this);
        view.findViewById(R.id.setting_ll_logout).setOnClickListener(this);
        view.findViewById(R.id.setting_follow_and_invite_frnd).setOnClickListener(this);
        view.findViewById(R.id.setting_ll_privacy).setOnClickListener(this);
        view.findViewById(R.id.setting_ll_account).setOnClickListener(this);
        return view;
    }

    public  void navigateToFollowFriend(){
        FollowInviteFriendFragment followInviteFriendFragment = new FollowInviteFriendFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_setting, followInviteFriendFragment).commit();
    }

    public  void navigateToPrivacy() {
        PrivacyFragment privacyFragment = new PrivacyFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_setting, privacyFragment).commit();
    }

    public  void navigateToAccount() {
        AccountFragment accountFragment = new AccountFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_setting, accountFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Goback:
                getActivity().onBackPressed();
                break;

            case R.id.setting_ll_logout:
                TicTic.getInstance().getSession().clearAllData();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                getActivity().finish();
                break;

            case R.id.setting_follow_and_invite_frnd:
                navigateToFollowFriend();
                break;

            case R.id.setting_ll_privacy:
                navigateToPrivacy();
                break;

            case R.id.setting_ll_account:
                navigateToAccount();
                break;
        }
    }
}
