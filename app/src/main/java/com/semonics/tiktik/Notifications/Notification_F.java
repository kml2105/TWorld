package com.semonics.tiktik.Notifications;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.semonics.tiktik.Inbox.Inbox_F;
import com.semonics.tiktik.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.semonics.tiktik.R;
import com.semonics.tiktik.SimpleClasses.Utils;
import com.semonics.tiktik.WebService.BaseAPIService;
import com.semonics.tiktik.WebService.ResponseListener;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.semonics.tiktik.WebService.WSParams.METHOD_GET;
import static com.semonics.tiktik.WebService.WSParams.SERVICE_GET_PENDING_REQ_COUNT;
import static com.semonics.tiktik.WebService.WSParams.WS_KEY_OBJ;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;*/

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification_F extends RootFragment {

    View view;
    Context context;

    Notification_Adapter adapter;
    RecyclerView recyclerViewAll, recyclerViewNew;
    RelativeLayout llNoDataLayout;
    TextView tvReqCount;
    LinearLayout llPendingReq;

    ArrayList<Notification_Get_Set> datalist;


    public Notification_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        context = getContext();


        datalist = new ArrayList<>();

        tvReqCount = view.findViewById(R.id.req_count_txt);
        recyclerViewNew = (RecyclerView) view.findViewById(R.id.fragment_inbox_rv_new);
        recyclerViewAll = (RecyclerView) view.findViewById(R.id.fragment_inbox_rv_all);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        recyclerViewNew.setLayoutManager(layoutManager);
        recyclerViewNew.setHasFixedSize(true);
        recyclerViewAll.setLayoutManager(layoutManager1);
        recyclerViewAll.setHasFixedSize(true);
        llPendingReq = view.findViewById(R.id.ll_pending_requests);

        adapter = new Notification_Adapter(context, datalist, new Notification_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Notification_Get_Set item) {

            }
        }
        );

        recyclerViewNew.setAdapter(adapter);
        recyclerViewAll.setAdapter(adapter);

       // apiCall();

        llNoDataLayout = view.findViewById(R.id.no_data_layout);
        if(tvReqCount.getText()!="0"){
           llPendingReq.setVisibility(View.VISIBLE);
        }else{
            llPendingReq.setVisibility(View.GONE);
        }
        llPendingReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvReqCount.getText()!="0"){
                    navigateToPendingReqPage();
                }
            }
        });

        return view;
    }

    public void navigateToPendingReqPage() {
        PendingRequestFragment pendingRequestFragment = new PendingRequestFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_noti, pendingRequestFragment).commit();
    }

    //AdView adView;
    @Override
    public void onStart() {
        super.onStart();
       /* adView = view.findViewById(R.id.bannerad);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
    }

    private void Open_inbox_F() {

        Inbox_F inbox_f = new Inbox_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, inbox_f).commit();

    }

    public void apiCall() {
        try {
            new BaseAPIService(context, SERVICE_GET_PENDING_REQ_COUNT, null, false, responseListener, METHOD_GET, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                String count = jsonObject.getString(WS_KEY_OBJ);
                tvReqCount.setText(count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(context, error);
        }
    };
}
