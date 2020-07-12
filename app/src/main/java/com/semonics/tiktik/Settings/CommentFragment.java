package com.semonics.tiktik.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semonics.tiktik.R;

public class CommentFragment extends Fragment implements View.OnClickListener {
    View view;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment2, container, false);
        view.findViewById(R.id.Goback).setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View view) {
switch ((view.getId())){
      case R.id.Goback:
        getActivity().onBackPressed();
        break;
}
    }
}