package com.semonics.tworld.Settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semonics.tworld.R;

public class FollowInviteFriendFragment extends Fragment implements View.OnClickListener {
    View view;

    public static FollowInviteFriendFragment newInstance() {
        return new FollowInviteFriendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.follow_invite_friend_fragment, container, false);
        view.findViewById(R.id.invite_friends_by_email).setOnClickListener(this);
        view.findViewById(R.id.invite_friends_by_sms).setOnClickListener(this);
        view.findViewById(R.id.invite_friends_via).setOnClickListener(this);
        view.findViewById(R.id.invite_friends_by_wapp).setOnClickListener(this);
        view.findViewById(R.id.back).setOnClickListener(this);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                getActivity().onBackPressed();
                break;

            case R.id.invite_friends_via:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case  R.id.invite_friends_by_sms:
                Uri uri = Uri.parse("smsto:");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "Here you can set the SMS text to be sent");
                startActivity(it);
                break;

            case  R.id.invite_friends_by_email:
                Intent sendIntent1 = new Intent();
                sendIntent1.setAction(Intent.ACTION_SEND);
                sendIntent1.putExtra(Intent.EXTRA_TEXT, "https://www.google.com/");
                sendIntent1.setType("text/plain");
                startActivity(sendIntent1);
                break;

            case R.id.invite_friends_by_wapp:
                break;
        }
    }
}