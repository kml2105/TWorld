package com.semonics.tiktik.Profile.UserVideos;


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
import com.semonics.tiktik.SimpleClasses.Variables;
import com.semonics.tiktik.WatchVideos.WatchVideos_F;
import com.semonics.tiktik.WebService.BaseAPIService;
import com.semonics.tiktik.WebService.ResponseListener;
import com.semonics.tiktik.WebService.SessionManager;
import com.semonics.tiktik.WebService.TicTic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.semonics.tiktik.WebService.WSParams.METHOD_GET;
import static com.semonics.tiktik.WebService.WSParams.SERVICE_ALL_VIDEO;
import static com.semonics.tiktik.WebService.WSParams.WS_KEY_OBJ;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserVideo_F extends Fragment {

    public RecyclerView recyclerView;
    ArrayList<HomeModel> data_list;
    MyVideos_Adapter adapter;
    View view;
    Context context;
    String user_id;
    SessionManager sessionManager;
    RelativeLayout no_data_layout;
    public static int myvideo_count = 0;

    public UserVideo_F() {

    }

    public void apiCall() {
        try {
            new BaseAPIService(context, SERVICE_ALL_VIDEO, null, true, responseListener, METHOD_GET, true);
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
                JSONObject jsonObject1 = jsonObject.getJSONObject(WS_KEY_OBJ);
                JSONArray jsonArray = jsonObject1.getJSONArray("documents");
                if(jsonArray.length()>=1){
                    myvideo_count = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject itemdata = jsonArray.optJSONObject(i);
                        HomeModel item = new HomeModel();
                        item.id = itemdata.optString("_id");
                        item.docName = itemdata.optString("docName");
                        JSONObject user_info = itemdata.optJSONObject("userDetails");
                        UserDetails userDetails = new UserDetails();
                        userDetails.firstName = user_info.optString("firstName");
                        userDetails.lastName = user_info.optString("lastName");
                        userDetails.profilePic = user_info.optString("profilePic");
                        item.userDetails = userDetails;

                        JSONObject sound_data = itemdata.optJSONObject("music");
                        if(sound_data!=null){
                            Music musicModel = new Music();
                            musicModel.id = sound_data.optString("_id");
                            musicModel.musicName = sound_data.optString("musicName");
                            musicModel.thumb = sound_data.optString("thumb");
                            item.music = musicModel;
                        }

                        item.likeCount = itemdata.optInt("likeCount");
                        item.thumb = itemdata.optString("thumb");
                        item.viewerCount = itemdata.optInt("viewerCount");
                        item.commentCount = itemdata.optInt("commentCount");
                        JSONArray hashTagArray = itemdata.getJSONArray("hashTag");
                        String[] hashtags = new String[hashTagArray.length()];
                        for (int a = 0; a < hashTagArray.length(); a++) {
                            hashtags[a]= hashTagArray.getString(a);
                        }
                        List<String> hashTagList = Arrays.asList(hashtags);
                        item.hashTag = hashTagList;
                        item.location = itemdata.optString("location");
                        item.docName =/*Variables.base_url+*/itemdata.optString("docName");
                        item.caption = itemdata.optString("caption");
                        item.createdDate = itemdata.optInt("createdDate");
                        data_list.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }else {
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
        view = inflater.inflate(R.layout.fragment_user_video, container, false);

        context = getContext();

        recyclerView = view.findViewById(R.id.recylerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        data_list = new ArrayList<>();
        adapter = new MyVideos_Adapter(context, data_list, new MyVideos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, HomeModel item, View view) {
                OpenWatchVideo(postion,data_list);
            }
        });

        recyclerView.setAdapter(adapter);

        no_data_layout = view.findViewById(R.id.no_data_layout);
        sessionManager = TicTic.getInstance().getSession();
        //apiCall();

        return view;

    }

    Boolean isVisibleToUser = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (view != null && isVisibleToUser) {
       apiCall();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if ((view != null && isVisibleToUser) && (!data_list.isEmpty() && !is_api_run)) {
           apiCall();
        }
    }


    Boolean is_api_run = false;

    //this will get the all videos data of user and then parse the data
    private void Call_Api_For_get_Allvideos() {
        is_api_run = true;
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("my_fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

   /*     ApiRequest.Call_Api(context, Variables.showMyAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                is_api_run=false;
                //Parse_data(resp);
            }
        });*/


    }

/*    public void Parse_data(String responce){

        data_list.clear();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                JSONObject data=msgArray.getJSONObject(0);
                JSONObject user_info=data.optJSONObject("user_info");



                JSONArray user_videos=data.getJSONArray("user_videos");
                if(!user_videos.toString().equals("["+"0"+"]")){

                    no_data_layout.setVisibility(View.GONE);

                    for (int i=0;i<user_videos.length();i++) {
                        JSONObject itemdata = user_videos.optJSONObject(i);

                        Home_Get_Set item=new Home_Get_Set();
                        item.fb_id=user_id;

                        item.first_name=user_info.optString("first_name");
                        item.last_name=user_info.optString("last_name");
                        item.profile_pic=user_info.optString("profile_pic");

                        Log.d("resp", item.fb_id+" "+item.first_name);

                        JSONObject count=itemdata.optJSONObject("count");
                        item.like_count=count.optString("like_count");
                        item.video_comment_count=count.optString("video_comment_count");
                        item.views=count.optString("view");

                        JSONObject sound_data=itemdata.optJSONObject("sound");
                        item.sound_id=sound_data.optString("id");
                        item.sound_name=sound_data.optString("sound_name");
                        item.sound_pic=sound_data.optString("thum");


                        item.video_id=itemdata.optString("id");
                        item.liked=itemdata.optString("liked");
                        item.gif=Variables.base_url+itemdata.optString("gif");
                        item.video_url=Variables.base_url+itemdata.optString("video");
                        item.thum=Variables.base_url+itemdata.optString("thum");
                        item.created_date=itemdata.optString("created");

                        item.video_description=itemdata.optString("description");


                        data_list.add(item);
                    }

                    myvideo_count=data_list.size();

                }else {
                    no_data_layout.setVisibility(View.VISIBLE);
                }




                adapter.notifyDataSetChanged();

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/


    private void OpenWatchVideo(int postion,ArrayList<HomeModel> list) {
        Intent intent = new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", list);
        intent.putExtra("position", postion);
        startActivity(intent);
    }
}
