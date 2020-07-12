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
import com.semonics.tiktik.SimpleClasses.TicTic;


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
        return view;
    }

    public  void navigateToFollowFriend(){
        FollowInviteFriendFragment followInviteFriendFragment = new FollowInviteFriendFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        View view=getActivity().findViewById(R.id.MainMenuFragment);
        if(view!=null)
            transaction.replace(R.id.MainMenuFragment, followInviteFriendFragment).commit();
        else
            transaction.replace(R.id.Profile_F, followInviteFriendFragment).commit();
    }

    public  void navigateToPrivacy(){
        PrivacyFragment privacyFragment = new PrivacyFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        View view=getActivity().findViewById(R.id.MainMenuFragment);
        if(view!=null)
            transaction.replace(R.id.MainMenuFragment, privacyFragment).commit();
        else
            transaction.replace(R.id.Profile_F, privacyFragment).commit();
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
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                getActivity().finish();
                break;

            case R.id.setting_follow_and_invite_frnd:
                navigateToFollowFriend();
                break;

            case R.id.setting_ll_privacy:
                navigateToPrivacy();
                break;
        }
    }
}
