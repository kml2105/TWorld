package com.semonics.tworld.Notifications;


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

import com.semonics.tworld.Inbox.Inbox_F;
import com.semonics.tworld.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;
import com.semonics.tworld.WebService.SessionManager;
import com.semonics.tworld.WebService.TWorld;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_NEW_NOTI;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_PENDING_REQ_COUNT;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_SEEN_NOTI;
import static com.semonics.tworld.WebService.WSParams.SERVICE_READ_NOTI;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_CODE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;*/

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification_F extends RootFragment {

    View view;
    Context context;

    Notification_Adapter adapterForNew,adapterForAll;
    RecyclerView recyclerViewAll, recyclerViewNew;
    RelativeLayout llNoDataLayout;
    TextView tvReqCount;
    LinearLayout llPendingReq;
    SessionManager sessionManager;
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

        adapterForNew = new Notification_Adapter(context, datalist, new Notification_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Notification_Get_Set item) {
                apiCallForReadNotification(datalist.get(postion).id);
            }
        }
        );
        adapterForAll = new Notification_Adapter(context, datalist, new Notification_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Notification_Get_Set item) {
                apiCallForReadNotification(datalist.get(postion).id);
            }
        }
        );
        sessionManager = TWorld.getInstance().getSession();
        recyclerViewNew.setAdapter(adapterForNew);
        recyclerViewAll.setAdapter(adapterForAll);

        // apiCall();

        llNoDataLayout = view.findViewById(R.id.no_data_layout);
        if (sessionManager.getInt(SessionManager.PREF_PRIVATE_ACC) == 0) {
            llPendingReq.setVisibility(View.VISIBLE);
        } else {
            llPendingReq.setVisibility(View.GONE);
        }
        llPendingReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvReqCount.getText() != "0") {
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
//        transaction.replace(R.id.MainMenuFragment, inbox_f).commit();

    }

    public void apiCall() {
        try {
            new BaseAPIService(context, SERVICE_GET_PENDING_REQ_COUNT, null, true, responseListener, METHOD_GET, false);
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
                if (code == 200) {
                    String count = jsonObject.getString(WS_KEY_OBJ);
                    tvReqCount.setText(count);
                } else {
                    tvReqCount.setText("0");
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

    public void apiCallForReadNotification(String id) {
        try {
            new BaseAPIService(context, SERVICE_READ_NOTI + id, RequestParams.likeVideo(), true, responseListenerForReadNotification, METHOD_POST, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListenerForReadNotification = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(context, error);
        }
    };


    public void apiCallForAllUnseenNoti() {
        try {
            if (sessionManager.getInt(SessionManager.PREF_PRIVATE_ACC) == 0) {
                llPendingReq.setVisibility(View.VISIBLE);
            } else {
                llPendingReq.setVisibility(View.GONE);
            }
            new BaseAPIService(context, SERVICE_GET_NEW_NOTI, null, true, responseListenerForAllUnseenNoti, METHOD_GET, false);
            apiCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListenerForAllUnseenNoti = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray(WS_KEY_OBJ);
                    Notification_Get_Set notification_get_set = new Notification_Get_Set();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        notification_get_set.first_name = object.getString("name");
                        notification_get_set.profile_pic = object.getString("profilePic");
                        notification_get_set.id = object.getString("_id");
                        notification_get_set.title = object.getString("title");
                        notification_get_set.followStatus = object.getInt("status");
                        datalist.add(notification_get_set);
                    }

                    adapterForNew.notifyDataSetChanged();
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


    public void apiCallForAllSeenNoti() {
        try {
            new BaseAPIService(context, SERVICE_GET_SEEN_NOTI, null, true, responseListenerForAllseenNoti, METHOD_GET, false);
            apiCallForAllUnseenNoti();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListenerForAllseenNoti = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);
                if (code == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray(WS_KEY_OBJ);
                    Notification_Get_Set notification_get_set = new Notification_Get_Set();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        notification_get_set.first_name = object.getString("name");
                        notification_get_set.profile_pic = object.getString("profilePic");
                        notification_get_set.id = object.getString("_id");
                        notification_get_set.title = object.getString("title");
                        notification_get_set.followStatus = object.getInt("status");
                        datalist.add(notification_get_set);
                    }

                    adapterForAll.notifyDataSetChanged();
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
}
