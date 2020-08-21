package com.semonics.tworld.Notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.semonics.tworld.Model.BlockAccountModel;
import com.semonics.tworld.R;
import com.semonics.tworld.Settings.BlockAccount.BlockAdapter;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_PENDING_REQ;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

public class PendingRequestFragment extends Fragment {
    BlockAdapter adapter;
    RecyclerView recyclerView;
    View view;
    ArrayList<BlockAccountModel> dataList;
    private RelativeLayout noDataLayout;

    public PendingRequestFragment() {
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
        view = inflater.inflate(R.layout.fragment_pending_request, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_pending_req_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        dataList = new ArrayList<>();
        noDataLayout = view.findViewById(R.id.no_data_layout);
        apiCall();
        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }


    private void apiCall(){
        try {
            new BaseAPIService(getApplicationContext(), SERVICE_GET_PENDING_REQ, null,true, responseListener,METHOD_GET, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                dataList.clear();
                JSONObject jsonObject = new JSONObject(res);
                JSONArray jsonArray = jsonObject.getJSONArray(WS_KEY_OBJ);
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject profile_data = jsonArray.optJSONObject(i);
                    BlockAccountModel item=new BlockAccountModel();
                    item.userID=profile_data.optString("userId");
                    item.first_name=profile_data.optString("firstName");
                    item.last_name=profile_data.optString("lastName");
                    item.username=profile_data.optString("userName");
                    item.profile_pic=profile_data.optString("profilePic");
                    dataList.add(item);
                    adapter.notifyItemInserted(i);
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