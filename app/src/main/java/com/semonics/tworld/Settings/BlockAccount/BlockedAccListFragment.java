package com.semonics.tworld.Settings.BlockAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.semonics.tworld.Model.BlockAccountModel;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.SERVICE_BLOCK_ACC_LIST;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_CODE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

public class BlockedAccListFragment extends Fragment {
    ArrayList<BlockAccountModel> blockList;
    BlockAdapter adapter;
    private RecyclerView recyclerView;
    View view;
    private LinearLayout llNoDataFound,llMainLayout;

    public BlockedAccListFragment() {
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
        view = inflater.inflate(R.layout.fragment_blocked_acc_list, container, false);
        recyclerView = view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        llNoDataFound = view.findViewById(R.id.no_data_layout);
        llMainLayout = view.findViewById(R.id.ll_main_layout);
        blockList = new ArrayList<>();
        apiCall();
        view.findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    public void apiCall() {
        try {
            new BaseAPIService(getApplicationContext(), SERVICE_BLOCK_ACC_LIST, null,true, responseListener,METHOD_GET, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {

                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);
                if(code==200){
                    llNoDataFound.setVisibility(View.GONE);
                    llMainLayout.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = jsonObject.getJSONArray(WS_KEY_OBJ);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject profile_data = jsonArray.optJSONObject(i);
                        BlockAccountModel item = new BlockAccountModel();
                        item.userID = profile_data.optString("userId");
                        item.first_name = profile_data.optString("firstName");
                        item.last_name = profile_data.optString("lastName");
                        item.username = profile_data.optString("userName");
                        item.profile_pic = profile_data.optString("profilePic");
                        blockList.add(item);
                        adapter.notifyItemInserted(i);
                    }
                }else{
                    llNoDataFound.setVisibility(View.VISIBLE);
                    llMainLayout.setVisibility(View.GONE                                                );
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