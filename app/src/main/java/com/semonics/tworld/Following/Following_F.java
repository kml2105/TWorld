package com.semonics.tworld.Following;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.semonics.tworld.Profile.Profile_F;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Fragment_Callback;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_FOLLOWERS_LIST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_FOLLOWING_LIST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_UNFOLLOW;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

/**
 * A simple {@link Fragment} subclass.
 */
public class Following_F extends Fragment {

    View view;
    Context context;

    String user_id;


    Following_Adapter adapter;
    RecyclerView recyclerView;

    ArrayList<Following_Get_Set> datalist;


    RelativeLayout no_data_layout;


    String following_or_fan = "Followers";

    TextView title_txt;
    int pos;

    public Following_F() {
        // Required empty public constructor
    }


    Fragment_Callback fragment_callback;

    @SuppressLint("ValidFragment")
    public Following_F(Fragment_Callback fragment_callback) {
        this.fragment_callback = fragment_callback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_following, container, false);
        context = getContext();

        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id = bundle.getString("id");
            following_or_fan = bundle.getString("from_where");
        }


        title_txt = view.findViewById(R.id.title_txt);

        datalist = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new Following_Adapter(context, following_or_fan, datalist, new Following_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Following_Get_Set item) {

                switch (view.getId()) {
                    case R.id.action_txt:
                    //    if (user_id.equals(Variables.sharedPreferences.getString(Variables.u_id, "")))
                            Follow_unFollow_User(item, postion);
                        break;

                    case R.id.mainlayout:
                        OpenProfile(item);
                        break;

                }

            }
        }
        );

        recyclerView.setAdapter(adapter);


        no_data_layout = view.findViewById(R.id.no_data_layout);


        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        if (following_or_fan.equals("following")) {
            /* Call_Api_For_get_Allfollowing();*/
            apiCallForFollowing();
            title_txt.setText("Following");
        } else {
            apiCallForFollowers();
            title_txt.setText("Followers");
        }

        return view;
    }


    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_Allfollowing() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


      /*  ApiRequest.Call_Api(context, Variables.get_followings, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_following_data(resp);
            }
        });*/


    }

    public void Parse_following_data(String responce) {

        datalist.clear();

        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");
                for (int i = 0; i < msgArray.length(); i++) {
                    JSONObject profile_data = msgArray.optJSONObject(i);

                    JSONObject follow_Status = profile_data.optJSONObject("follow_Status");

                    Following_Get_Set item = new Following_Get_Set();
                    item.userID = profile_data.optString("fb_id");
                    item.first_name = profile_data.optString("first_name");
                    item.last_name = profile_data.optString("last_name");
                    item.username = profile_data.optString("username");
                    item.profile_pic = profile_data.optString("profile_pic");


                    item.follow = follow_Status.optString("follow");


                    datalist.add(item);
                    adapter.notifyItemInserted(i);
                }

                adapter.notifyDataSetChanged();


                if (datalist.isEmpty()) {
                    no_data_layout.setVisibility(View.VISIBLE);
                } else
                    no_data_layout.setVisibility(View.GONE);

            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //api call for getting following list
    public void apiCallForFollowing() {
        try {
            new BaseAPIService(getApplicationContext(), SERVICE_GET_FOLLOWING_LIST, null, true, responseListenerForFollowing, METHOD_GET, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListenerForFollowing = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONArray jsonArray = jsonObject.getJSONArray(WS_KEY_OBJ);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject profile_data = jsonArray.optJSONObject(i);
                    Following_Get_Set item = new Following_Get_Set();
                    item.userID = profile_data.optString("userId");
                    item.first_name = profile_data.optString("firstName");
                    item.last_name = profile_data.optString("lastName");
                    item.username = profile_data.optString("userName");
                    item.profile_pic = profile_data.optString("profilePic");
                    item.follow = "1";
                    datalist.add(item);
                    adapter.notifyItemInserted(i);
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

    //api call for getting followers list
    public void apiCallForFollowers() {
        try {
            new BaseAPIService(getApplicationContext(), SERVICE_GET_FOLLOWERS_LIST, null, true, responseListenerForFollowers, METHOD_GET, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListenerForFollowers = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONArray jsonArray = jsonObject.getJSONArray(WS_KEY_OBJ);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject profile_data = jsonArray.optJSONObject(i);
                    Following_Get_Set item = new Following_Get_Set();
                    item.userID = profile_data.optString("userId");
                    item.first_name = profile_data.optString("firstName");
                    item.last_name = profile_data.optString("lastName");
                    item.username = profile_data.optString("userName");
                    item.profile_pic = profile_data.optString("profilePic");
                    item.follow = "0";
                    datalist.add(item);
                    adapter.notifyItemInserted(i);
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

    // this will open the profile of user which have uploaded the currenlty running video
    private void OpenProfile(final Following_Get_Set item) {
        Profile_F profile_f = new Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        Bundle args = new Bundle();
        args.putString("user_id", item.userID);
        args.putString("user_name", item.first_name + " " + item.last_name);
        args.putString("user_pic", item.profile_pic);
        profile_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, profile_f).commit();
    }

    //api call for removing from follow list (UnFollow)
    public void apiCallForUnFollow(String id) {
        try {
            new BaseAPIService(getApplicationContext(), SERVICE_UNFOLLOW, RequestParams.removeFromFollowing(id), true, responseListenerForUnFollow, METHOD_POST, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListenerForUnFollow = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
               // JSONObject jsonObject = new JSONObject(res);
             //   String msg = jsonObject.getString(WS_KEY_MSG);
               datalist.remove(pos);
               adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(context, error);
        }
    };

    public void Follow_unFollow_User(final Following_Get_Set item, final int position) {

        final String send_status;
        pos = position;
        if (item.follow.equals("0")) {
            send_status = "1";
        } else {
            send_status = "0";
        }
        apiCallForUnFollow(item.userID);

    }


    @Override
    public void onDetach() {

        if (fragment_callback != null)
            fragment_callback.Responce(new Bundle());

        super.onDetach();
    }

}
