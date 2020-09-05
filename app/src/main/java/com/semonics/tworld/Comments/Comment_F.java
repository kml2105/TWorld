package com.semonics.tworld.Comments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.semonics.tworld.Home.HomeModel;
import com.semonics.tworld.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.semonics.tworld.Model.UserDetails;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.API_CallBack;
import com.semonics.tworld.SimpleClasses.Fragment_Data_Send;
import com.semonics.tworld.SimpleClasses.Functions;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.SimpleClasses.Variables;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_ALL_COMMENT_LIST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_SEND_COMMENT;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_CODE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

/**
 * A simple {@link Fragment} subclass.
 */
public class Comment_F extends RootFragment {

    View view;
    Context context;

    RecyclerView recyclerView;

    Comments_Adapter adapter;

    ArrayList<Comment_Get_Set> data_list;

    String video_id;
    String user_id;

    EditText message_edit;
    ImageButton send_btn;
    ProgressBar send_progress;

    TextView comment_count_txt;

    FrameLayout comment_screen;

    public static int comment_count = 0;

    public Comment_F() {

    }

    Fragment_Data_Send fragment_data_send;

    @SuppressLint("ValidFragment")
    public Comment_F(int count, Fragment_Data_Send fragment_data_send) {
        comment_count = count;
        this.fragment_data_send = fragment_data_send;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        context = getContext();


        comment_screen = view.findViewById(R.id.comment_screen);
        comment_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }
        });

        view.findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            video_id = bundle.getString("video_id");
            user_id = bundle.getString("user_id");
        }


        comment_count_txt = view.findViewById(R.id.comment_count);

        recyclerView = view.findViewById(R.id.recylerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);


        data_list = new ArrayList<>();
        adapter = new Comments_Adapter(context, data_list, new Comments_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, Comment_Get_Set item, View view) {


            }
        });

        recyclerView.setAdapter(adapter);


        message_edit = view.findViewById(R.id.message_edit);


        send_progress = view.findViewById(R.id.send_progress);
        send_btn = view.findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = message_edit.getText().toString();
                if (!message.trim().isEmpty()) {
                    apiCallForSendComment();
                }

            }
        });

        apiCall();

        return view;
    }


    @Override
    public void onDetach() {
        Functions.hideSoftKeyboard(getActivity());
        super.onDetach();
    }

    // this funtion will get all the comments against post
    public void apiCall() {
        try {
            message_edit.setText(null);
            new BaseAPIService(getApplicationContext(), SERVICE_GET_ALL_COMMENT_LIST + video_id, null, true, responseListener, METHOD_GET, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                data_list.clear();

                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);
                JSONObject object = jsonObject.getJSONObject(WS_KEY_OBJ);
                if (code == 200) {
                    JSONArray commentArray = object.getJSONArray("comment");
                    for(int i =0;i<commentArray.length();i++){
                        JSONObject commentObject = commentArray.optJSONObject(i);
                        Comment_Get_Set comment_get_set = new Comment_Get_Set();
                        comment_get_set.comment = commentObject.optString("comment");
                        JSONObject user_info = commentObject.optJSONObject("userDetails");
                        if (user_info != null) {
                            UserDetails userDetails = new UserDetails();
                            userDetails.firstName = user_info.optString("firstName");
                            userDetails.lastName = user_info.optString("lastName");
                            userDetails.profilePic = user_info.optString("profilePic");
                            comment_get_set.userDetails = userDetails;
                        }
                        data_list.add(comment_get_set);
                    }
                    comment_count_txt.setText(data_list.size() + " comments");
                    adapter.notifyDataSetChanged();
                } else {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(getContext(), error);
        }
    };

    private void apiCallForSendComment() {
        try {
            send_progress.setVisibility(View.VISIBLE);
            send_btn.setVisibility(View.GONE);
            new BaseAPIService(getApplicationContext(), SERVICE_SEND_COMMENT + video_id, RequestParams.addComment(message_edit.getText().toString()), true, responseListenerForSendComment, METHOD_POST, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListenerForSendComment = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {

                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);
                if (code == 200) {
                    send_progress.setVisibility(View.GONE);
                    send_btn.setVisibility(View.VISIBLE);
                    apiCall();
                   /* ArrayList<Comment_Get_Set> arrayList1 = data_list;
                    for (Comment_Get_Set item : arrayList1) {
                        Comment_Get_Set item1 = new Comment_Get_Set();
                        item1.comment = message_edit.getText().toString();
                        data_list.add(0, item1);
                        comment_count++;
                        comment_count_txt.setText(comment_count + " comments");
                        if (fragment_data_send != null)
                            fragment_data_send.onDataSent("" + comment_count);
                    }
                    adapter.notifyDataSetChanged();
                    send_progress.setVisibility(View.GONE);
                    send_btn.setVisibility(View.VISIBLE);*/
                } else {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(getContext(), error);
        }
    };

    // this function will call an api to upload your comment
    public void Send_Comments(String video_id, final String comment) {

        Functions.Call_Api_For_Send_Comment(getActivity(), video_id, comment, new API_CallBack() {
            @Override
            public void ArrayData(ArrayList arrayList) {

                ArrayList<Comment_Get_Set> arrayList1 = arrayList;
                for (Comment_Get_Set item : arrayList1) {
                    data_list.add(0, item);
                    comment_count++;

                    SendPushNotification(getActivity(), user_id, comment);

                    comment_count_txt.setText(comment_count + " comments");

                    if (fragment_data_send != null)
                        fragment_data_send.onDataSent("" + comment_count);

                }
                adapter.notifyDataSetChanged();
                send_progress.setVisibility(View.GONE);
                send_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSuccess(String responce) {

            }

            @Override
            public void OnFail(String responce) {

            }
        });

    }


    public void SendPushNotification(Activity activity, String user_id, String comment) {

        JSONObject notimap = new JSONObject();
        try {
            notimap.put("title", Variables.sharedPreferences.getString(Variables.u_name, "") + " Comment on your video");
            notimap.put("message", comment);
            notimap.put("icon", Variables.sharedPreferences.getString(Variables.u_pic, ""));
            notimap.put("senderid", Variables.sharedPreferences.getString(Variables.u_id, ""));
            notimap.put("receiverid", user_id);
            notimap.put("action_type", "comment");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ApiRequest.Call_Api(context,Variables.sendPushNotification,notimap,null);

    }


}
