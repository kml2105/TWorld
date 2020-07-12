package com.semonics.tiktik.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semonics.tiktik.R;

public class PrivacyFragment extends Fragment implements View.OnClickListener {

    View view;

    public PrivacyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_privacy, container, false);
        view.findViewById(R.id.video_comment).setOnClickListener(this);
        view.findViewById(R.id.video_mention).setOnClickListener(this);
        view.findViewById(R.id.Goback).setOnClickListener(this);
        return view;
    }


    public  void navigateToComment(){
        CommentFragment commentFragment = new CommentFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        View view=getActivity().findViewById(R.id.MainMenuFragment);
        if(view!=null)
            transaction.replace(R.id.MainMenuFragment, commentFragment).commit();
        else
            transaction.replace(R.id.Profile_F, commentFragment).commit();
    }

    public  void navigateToMention(){
        TagsFragment tagsFragment = new TagsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        View view=getActivity().findViewById(R.id.MainMenuFragment);
        if(view!=null)
            transaction.replace(R.id.MainMenuFragment, tagsFragment).commit();
        else
            transaction.replace(R.id.Profile_F, tagsFragment).commit();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.video_comment:
                navigateToComment();
                break;

            case R.id.video_mention:
                navigateToMention();
                break;

            case R.id.Goback:
                getActivity().onBackPressed();
                break;
        }

    }
}