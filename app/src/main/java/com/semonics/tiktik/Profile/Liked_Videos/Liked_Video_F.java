package com.semonics.tiktik.Profile.Liked_Videos;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.semonics.tiktik.Home.HomeModel;
import com.semonics.tiktik.Model.Music;
import com.semonics.tiktik.Model.UserDetails;
import com.semonics.tiktik.Profile.MyVideos_Adapter;
import com.semonics.tiktik.R;
import com.semonics.tiktik.SimpleClasses.Utils;
import com.semonics.tiktik.WatchVideos.WatchVideos_F;
import com.semonics.tiktik.WebService.BaseAPIService;
import com.semonics.tiktik.WebService.ResponseListener;
import com.semonics.tiktik.WebService.SessionManager;
import com.semonics.tiktik.WebService.TicTic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.semonics.tiktik.WebService.WSParams.METHOD_GET;
import static com.semonics.tiktik.WebService.WSParams.SERVICE_ALL_LIKED_VIDEO;
import static com.semonics.tiktik.WebService.WSParams.WS_KEY_OBJ;

/**
 * A simple {@link Fragment} subclass.
 */
public class Liked_Video_F extends Fragment {

    public static RecyclerView recyclerView;
    ArrayList<HomeModel> data_list;
    MyVideos_Adapter adapter;
    SessionManager sessionManager;
    View view;
    Context context;

    String user_id;
    public static int totalLikedVideo = 0;
    RelativeLayout no_data_layout;

    public Liked_Video_F() {
        // Required empty public constructor
    }

    public void apiCall() {
        try {
            new BaseAPIService(context, SERVICE_ALL_LIKED_VIDEO + sessionManager.getString(SessionManager.PREF_USER_ID), null, false, responseListener, METHOD_GET, true);
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
                JSONArray jsonArray = jsonObject.getJSONArray(WS_KEY_OBJ);
                totalLikedVideo = jsonArray.length();
                if (jsonArray.length() >= 1) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject itemData = jsonArray.optJSONObject(i);
                        HomeModel item = new HomeModel();
                        item.id = itemData.optString("_id");
                        JSONObject user_info = itemData.optJSONObject("userDetails");
                        UserDetails userDetails = new UserDetails();
                        userDetails.firstName = user_info.optString("firstName");
                        userDetails.lastName = user_info.optString("lastName");
                        userDetails.profilePic = user_info.optString("profilePic");

                        item.userDetails = userDetails;

                        JSONObject sound_data = itemData.optJSONObject("music");
                        if (sound_data != null) {
                            Music musicModel = new Music();
                            musicModel.id = sound_data.optString("_id");
                            musicModel.musicName = sound_data.optString("musicName");
                            musicModel.thumb = sound_data.optString("thumb");
                            item.music = musicModel;
                        }

                        item.likeCount = itemData.optInt("likeCount");
                        item.commentCount = itemData.optInt("commentCount");
                        item.viewerCount = itemData.optInt("viewerCount");
                        JSONArray hashTagArray = itemData.getJSONArray("hashTag");
                        String[] hashtags = new String[hashTagArray.length()];
                        for (int a = 0; a < hashTagArray.length(); a++) {
                            hashtags[a] = hashTagArray.getString(a);
                        }
                        List<String> hashtagList = Arrays.asList(hashtags);
                        item.hashTag = hashtagList;
                        item.location = itemData.optString("location");
                        item.docName =/*Variables.base_url+*/itemData.optString("docName");
                        item.caption = itemData.optString("caption");
                        item.thumb = itemData.optString("thumb");
                        item.createdDate = itemData.optInt("createdDate");
                        data_list.add(item);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    no_data_layout.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(context, error);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_likedvideo, container, false);

        context = getContext();

        recyclerView = view.findViewById(R.id.recylerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        sessionManager = TicTic.getInstance().getSession();

        data_list = new ArrayList<>();
        adapter = new MyVideos_Adapter(context, data_list, new MyVideos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, HomeModel item, View view) {

                OpenWatchVideo(postion,data_list);

            }
        });

        recyclerView.setAdapter(adapter);


        no_data_layout = view.findViewById(R.id.no_data_layout);
        //apiCall();

        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (view != null && isVisibleToUser) {
            apiCall();
        }
    }


    private void OpenWatchVideo(int postion,ArrayList<HomeModel> list) {
        Intent intent = new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", list);
        intent.putExtra("position", postion);
        startActivity(intent);
    }


}
