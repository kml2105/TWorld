package com.semonics.tworld.Settings;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.semonics.tworld.Accounts.LoginActivity;
import com.semonics.tworld.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.semonics.tworld.R;
import com.semonics.tworld.WebService.TWorld;


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
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        view.findViewById(R.id.Goback).setOnClickListener(this);
        view.findViewById(R.id.setting_ll_logout).setOnClickListener(this);
        view.findViewById(R.id.setting_follow_and_invite_frnd).setOnClickListener(this);
        view.findViewById(R.id.setting_ll_privacy).setOnClickListener(this);
        view.findViewById(R.id.setting_ll_account).setOnClickListener(this);
        return view;
    }

    public void navigateToFollowFriend() {
        FollowInviteFriendFragment followInviteFriendFragment = new FollowInviteFriendFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_setting, followInviteFriendFragment).commit();
    }

    public void navigateToPrivacy() {
        PrivacyFragment privacyFragment = new PrivacyFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_setting, privacyFragment).commit();
    }

    public void navigateToAccount() {
        AccountFragment accountFragment = new AccountFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_setting, accountFragment).commit();
    }

    public void logOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure you want to Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //upload image
                dialog.dismiss();
                logOut();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //take photo
                dialog.dismiss();
            }
        });
        AlertDialog alert11 = builder.create();
        alert11.show();

        Button btnCancel = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        btnCancel.setTextColor(Color.WHITE);

        Button btnYes = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        btnYes.setTextColor(Color.BLUE);
        alert11.show();
    }

    public void logOut() {
        TWorld.getInstance().getSession().clearAllData();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Goback:
                getActivity().onBackPressed();
                break;

            case R.id.setting_ll_logout:
                logOutDialog();
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
