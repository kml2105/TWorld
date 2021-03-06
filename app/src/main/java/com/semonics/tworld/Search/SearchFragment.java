package com.semonics.tworld.Search;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.semonics.tworld.Home.HomeModel;
import com.semonics.tworld.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.semonics.tworld.Model.Music;
import com.semonics.tworld.Model.UserDetails;
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
import java.util.Iterator;

import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.SERVICE_SEARCH_ALL;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends RootFragment {

    View view;
    Context context;

    RecyclerView recyclerView;
    EditText search_edit;
    SessionManager sessionManager;
    private LinearLayout llVideos;
    private LinearLayout llMusic;
    private LinearLayout llHashTag;
    private LinearLayout llUsers;

    SwipeRefreshLayout swiperefresh;

    public SearchFragment() {
        // Required empty public constructor
    }

    ArrayList<String> list = new ArrayList<>();
    ArrayList<SearchModel> datalist;
    ArrayList<SearchUserModel> userList;
    SearchAdapter adapter;
    SearchUserAdapter searchUserAdapter;
    ArrayList<HomeModel> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getContext();
        datalist = new ArrayList<>();
        userList = new ArrayList<>();
        arrayList = new ArrayList<>();
        sessionManager = TWorld.getInstance().getSession();
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new SearchAdapter(context, datalist, new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<HomeModel> datalist, int postion) {
                OpenWatchVideo(postion, datalist);
            }
        });
        recyclerView.setAdapter(adapter);

        search_edit = view.findViewById(R.id.search_edit);
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                swiperefresh.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = search_edit.getText().toString();
                if (adapter != null)
                   searchApi();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        });
        swiperefresh = view.findViewById(R.id.swiperefresh);
        llVideos = view.findViewById(R.id.fragment_search_ll_video);
        llVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchVideoFragment TargetFragment=new SearchVideoFragment(1);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content,TargetFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        llMusic = view.findViewById(R.id.fragment_search_ll_music);
        llMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchVideoFragment TargetFragment=new SearchVideoFragment(2);
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content,TargetFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        llHashTag = view.findViewById(R.id.fragment_search_ll_hashtag);
        llUsers = view.findViewById(R.id.fragment_search_ll_user);
        llUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchUserFragment TargetFragment=new SearchUserFragment();
                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content,TargetFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        swiperefresh.setColorSchemeResources(R.color.black);
        search_edit.setText("");
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //searchApi();
            }
        });
        //searchApi();
        return view;
    }

    public void searchApi() {
        sessionManager.putString(SessionManager.PREF_SEARCH_KEYWORD, search_edit.getText().toString());
        try {
            new BaseAPIService(context, SERVICE_SEARCH_ALL, null, true, responseListener, METHOD_GET, true);
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
                JSONObject hashTags = jsonObject1.getJSONObject("hashTags");
                JSONArray documentArray = jsonObject1.optJSONArray("documents");
                JSONArray musicArray = jsonObject1.optJSONArray("musics");
                JSONArray userArray = jsonObject1.optJSONArray("users");
                if (documentArray.length() >= 1) {
                    SearchModel searchModel = new SearchModel();
                    searchModel.title = "Videos";
                    ArrayList<HomeModel> arrayList = new ArrayList<>();
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

                        arrayList.add(item);
                    }
                    searchModel.arrayList = arrayList;
                    datalist.add(searchModel);
                }
                if (musicArray.length() >= 1) {
                    SearchModel searchModel = new SearchModel();
                    searchModel.title = "Music";
                    ArrayList<HomeModel> arrayList = new ArrayList<>();
                    for (int i = 0; i < musicArray.length(); i++) {
                        JSONObject object = musicArray.optJSONObject(i);
                        HomeModel item = new HomeModel();
                        JSONObject user_info = object.optJSONObject("userDetails");
                        if (user_info != null) {
                            UserDetails userDetails = new UserDetails();
                            userDetails.firstName = user_info.optString("firstName");
                            userDetails.lastName = user_info.optString("lastName");
                            userDetails.profilePic = user_info.optString("profilePic");
                            item.userDetails = userDetails;
                        }
                        JSONObject sound_data = object.optJSONObject("musics");
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
                        arrayList.add(item);
                    }
                    searchModel.arrayList = arrayList;
                    datalist.add(searchModel);

                }
                if (hashTags != null) {
                    SearchModel searchModel = new SearchModel();
                    Iterator iterator = hashTags.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        searchModel.title = "#" + key;
                        JSONArray arr = hashTags.getJSONArray(key);

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject object = arr.optJSONObject(i);
                            HomeModel item = new HomeModel();
                            JSONObject user_info = object.optJSONObject("userDetails");
                            if (user_info != null) {
                                UserDetails userDetails = new UserDetails();
                                userDetails.firstName = user_info.optString("firstName");
                                userDetails.lastName = user_info.optString("lastName");
                                userDetails.profilePic = user_info.optString("profilePic");
                                item.userDetails = userDetails;
                            }
                            JSONObject sound_data = object.optJSONObject("musics");
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

                            arrayList.add(item);

                        }
                        searchModel.arrayList = arrayList;
                        datalist.add(searchModel);
                        adapter.notifyDataSetChanged();
                    }
                }

                if (userArray.length() >= 1) {
                        for (int i = 0; i <userArray.length();i++) {
                            JSONObject object = userArray.optJSONObject(i);
                            SearchUserModel searchModel = new SearchUserModel();
                            searchModel.id = object.getString("_id");
                            searchModel.username = object.getString("username");
                            searchModel.firstName = object.getString("firstName");
                            searchModel.lastName = object.getString("lastName");
                            //    searchModelet.p = object.getString("profilePic");
                            searchModel.status = object.getInt("status");
                            userList.add(searchModel);
                            searchUserAdapter.notifyItemInserted(i);
                        }
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

    // When you click on any Video a new activity is open which will play the Clicked video
    private void OpenWatchVideo(int postion, ArrayList<HomeModel> data_list) {

        Intent intent = new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", data_list);
        intent.putExtra("position", postion);
        startActivity(intent);
    }
}
