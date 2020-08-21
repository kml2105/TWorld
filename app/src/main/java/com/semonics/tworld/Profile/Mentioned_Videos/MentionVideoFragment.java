package com.semonics.tworld.Profile.Mentioned_Videos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.semonics.tworld.Home.HomeModel;
import com.semonics.tworld.Model.Music;
import com.semonics.tworld.Model.UserDetails;
import com.semonics.tworld.Profile.MyVideos_Adapter;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WatchVideos.WatchVideos_F;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.ResponseListener;
import com.semonics.tworld.WebService.SessionManager;
import com.semonics.tworld.WebService.TWorld;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_MENTIONED_LIST;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentionVideoFragment extends Fragment {

    public RecyclerView recyclerView;
    ArrayList<HomeModel> data_list;
    MyVideos_Adapter adapter;
    View view;
    Context context;
    String user_id;
    SessionManager sessionManager;
    RelativeLayout no_data_layout;
    public static int myvideo_count = 0;

    public MentionVideoFragment() {

    }

    public void apiCall() {
        try {
            new BaseAPIService(context, SERVICE_GET_MENTIONED_LIST, null, true, responseListener, METHOD_GET, true);
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
                if (jsonArray.length() >= 1) {
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
                        if (sound_data != null) {
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
                            hashtags[a] = hashTagArray.getString(a);
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
                OpenWatchVideo(postion, data_list);
            }
        });

        recyclerView.setAdapter(adapter);

        no_data_layout = view.findViewById(R.id.no_data_layout);
        sessionManager = TWorld.getInstance().getSession();

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


    private void OpenWatchVideo(int postion, ArrayList<HomeModel> list) {
        Intent intent = new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", list);
        intent.putExtra("position", postion);
        startActivity(intent);
    }
}
