package com.semonics.tworld.Search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.SERVICE_SEARCH_USER;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

public class SearchUserFragment extends Fragment {
    RecyclerView rvUser;
    View view;
    ImageView imgBackBtn;
    RelativeLayout noDataLayout;
    SessionManager sessionManager;
    EditText etSearch;
    ArrayList<SearchUserModel> userList;
    SearchUserAdapter searchUserAdapter;

    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_user, container, false);
        imgBackBtn = view.findViewById(R.id.back_btn);
        rvUser = view.findViewById(R.id.rv_user);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvUser.setLayoutManager(layoutManager);
        rvUser.setHasFixedSize(true);
        noDataLayout = view.findViewById(R.id.no_data_layout);
        sessionManager = TWorld.getInstance().getSession();
        etSearch = view.findViewById(R.id.search_edit_user);
        userList = new ArrayList<>();
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        searchUserAdapter = new SearchUserAdapter(getContext(), userList, new SearchUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, SearchUserModel item) {
                OpenWatchVideo(postion, userList);
            }
        });
        rvUser.setAdapter(searchUserAdapter);
        searchApi();
        return view;
    }

    public void searchApi() {
        sessionManager.putString(SessionManager.PREF_SEARCH_KEYWORD, etSearch.getText().toString());
        try {
            new BaseAPIService(getContext(), SERVICE_SEARCH_USER, null, true, responseListener, METHOD_GET, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // When you click on any Video a new activity is open which will play the Clicked video
    private void OpenWatchVideo(int postion, ArrayList<SearchUserModel> data_list) {

        Intent intent = new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", data_list);
        intent.putExtra("position", postion);
        startActivity(intent);
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                userList.clear();
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject(WS_KEY_OBJ);
                JSONArray userArray = jsonObject1.optJSONArray("users");
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
                    }
                    searchUserAdapter.notifyDataSetChanged();
                }else{
                    noDataLayout.setVisibility(View.VISIBLE);
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
}