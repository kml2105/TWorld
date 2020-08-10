package com.semonics.tiktik.Search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semonics.tiktik.Home.HomeModel;
import com.semonics.tiktik.Model.Music;
import com.semonics.tiktik.Model.UserDetails;
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

import static com.semonics.tiktik.WebService.WSParams.METHOD_GET;
import static com.semonics.tiktik.WebService.WSParams.SERVICE_SEARCH_MUSIC;
import static com.semonics.tiktik.WebService.WSParams.SERVICE_SEARCH_VIDEO;
import static com.semonics.tiktik.WebService.WSParams.WS_KEY_OBJ;

public class SearchVideoFragment extends Fragment {

    RecyclerView recyclerView;
    View view;
    SearchVideoAdapter adapter;
    ArrayList<HomeModel> datalist;
    EditText search_edit;
    SessionManager sessionManager;
    RelativeLayout llNoData;
    ImageView imgBackBtn;
    TextView toolbarTitle;
    int pos;

    public SearchVideoFragment(int p) {
        // Required empty public constructor
        this.pos = p;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_video, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);
        datalist = new ArrayList<>();
        imgBackBtn = view.findViewById(R.id.back_btn);
        toolbarTitle = view.findViewById(R.id.title_txt);
        if (pos == 1) {
            toolbarTitle.setText("Videos");
        } else {
            toolbarTitle.setText("Musics");
        }
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        sessionManager = TicTic.getInstance().getSession();
        adapter = new SearchVideoAdapter(getContext(), datalist, new SearchVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<HomeModel> datalist, int postion) {
                OpenWatchVideo(postion, datalist);
            }
        });
        recyclerView.setAdapter(adapter);
        search_edit = view.findViewById(R.id.search_edit_video);
        llNoData = view.findViewById(R.id.no_data_layout);
        searchVideoApi();
        return view;
    }


    public void searchVideoApi() {
        sessionManager.putString(SessionManager.PREF_SEARCH_KEYWORD, search_edit.getText().toString());
        try {
            new BaseAPIService(getContext(), pos == 1 ? SERVICE_SEARCH_VIDEO : SERVICE_SEARCH_MUSIC, null, false, responseListener, METHOD_GET, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                datalist.clear();
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject(WS_KEY_OBJ);
                    JSONArray  documentArray = jsonObject1.optJSONArray("documents");
                if (documentArray.length() >= 1) {
                    llNoData.setVisibility(View.GONE);
                    for (int i = 0; i < documentArray.length(); i++) {
                        JSONObject object = documentArray.optJSONObject(i);
                        HomeModel item = new HomeModel();
                        JSONObject user_info = object.optJSONObject("userDetails");
                        if (user_info != null) {
                            UserDetails userDetails = new UserDetails();
                            userDetails.firstName = user_info.optString("firstName");
                            userDetails.lastName = user_info.optString("lastName");
                            userDetails.profilePic = user_info.optString("profilePic");
                            item.userDetails = userDetails;
                        }
                        JSONObject sound_data = object.optJSONObject("music");
                        if (sound_data != null) {
                            Music music = new Music();
                            music.id = sound_data.optString("_id");
                            music.musicName = sound_data.optString("musicName");
                            music.thumb = sound_data.optString("thumb");
                            item.music = music;
                        }

                        item.id = object.optString("_id");
                        item.like = object.getInt("like");
                        item.likeCount = object.optInt("likeCount");
                        item.commentCount = object.getInt("like");
                        item.docName = object.optString("docName");
                        item.createdDate = object.optInt("createdDate");
                        item.caption = object.optString("caption");
                        item.thumb = object.optString("thumb");

                        datalist.add(item);
                    }
                } else {
                    llNoData.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(getContext(), error);
        }
    };

    // When you click on any Video a new activity is open which will play the Clicked video
    private void OpenWatchVideo(int postion, ArrayList<HomeModel> data_list) {

        Intent intent = new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", data_list);
        intent.putExtra("position", postion);
        startActivity(intent);
    }
}