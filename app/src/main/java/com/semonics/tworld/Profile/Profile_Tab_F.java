package com.semonics.tworld.Profile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.semonics.tworld.Following.Following_F;
import com.semonics.tworld.Main_Menu.MainMenuActivity;
import com.semonics.tworld.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.semonics.tworld.Profile.Liked_Videos.Liked_Video_F;
import com.semonics.tworld.Profile.Mentioned_Videos.MentionVideoFragment;
import com.semonics.tworld.Profile.UserVideos.UserVideo_F;
import com.semonics.tworld.R;
import com.semonics.tworld.See_Full_Image_F;
import com.semonics.tworld.Settings.SettingFragment;
import com.semonics.tworld.SimpleClasses.Fragment_Callback;
import com.semonics.tworld.SimpleClasses.Functions;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.SimpleClasses.Variables;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.ResponseListener;
import com.semonics.tworld.WebService.SessionManager;
import com.semonics.tworld.WebService.TWorld;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.semonics.tworld.WebService.WSParams.METHOD_GET;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_USER;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Tab_F extends RootFragment implements View.OnClickListener {
    View view;
    Context context;
    public TextView username, video_count_txt, txt;
    public ImageView imageView, coverPic;
    public TextView follow_count_txt, fans_count_txt, heart_count_txt;
    private File imageFile;
    ImageView setting_btn;
    SessionManager sessionManager;
    Bundle bundle;

    protected TabLayout tabLayout;

    protected ViewPager pager;

    private ViewPagerAdapter adapter;

    public boolean isdataload = false;


    RelativeLayout tabs_main_layout;

    LinearLayout top_layout;


    public static String pic_url;

    public static final int TAKE_PIC_REQUEST_CODE = 0;
    public static final int CHOOSE_PIC_REQUEST_CODE = 1;
    public static final int MEDIA_TYPE_IMAGE = 2;

    private Uri mMediaUri;
    public LinearLayout create_popup_layout;

    public Profile_Tab_F() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        context = getContext();
        sessionManager = TWorld.getInstance().getSession();
        return init();
    }

    public void apiCall() {
        try {
            new BaseAPIService(context, SERVICE_GET_USER, null, true, responseListener, METHOD_GET, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject(WS_KEY_OBJ);
                username.setText(jsonObject1.getString("username"));
                fans_count_txt.setText(jsonObject1.getString("followersCount"));
                follow_count_txt.setText(jsonObject1.getString("followingCount"));
                video_count_txt.setText(jsonObject1.getString("postCount"));
                JSONObject userProfileObj = jsonObject1.getJSONObject("userProfile");
                Profile_F.pic_url = userProfileObj.optString("profileName");
                Picasso.with(context)
                        .load(Profile_F.pic_url)
                        .placeholder(context.getResources().getDrawable(R.drawable.user_profile))
                        .resize(200, 200).centerCrop().into(imageView);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image:
                method();
                break;

            case R.id.setting_btn:
                //Open_Setting();
                openSetting();
                break;

            case R.id.following_layout:
                Open_Following();
                break;

            case R.id.fans_layout:
                Open_Followers();
                break;
        }
    }

    public void method() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Upload or Take a photo");
        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //upload image
                Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                choosePictureIntent.setType("image/*");
                startActivityForResult(choosePictureIntent, CHOOSE_PIC_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("Take Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //take photo
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                if (mMediaUri == null) {
                    Toast.makeText(getApplicationContext(), "Sorry there was an error! Try again.", Toast.LENGTH_LONG).show();
                } else {
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takePicture, TAKE_PIC_REQUEST_CODE);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PIC_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        } else if (requestCode == TAKE_PIC_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get(MediaStore.EXTRA_OUTPUT);
            imageView.setImageBitmap(photo);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if ((view != null && isVisibleToUser) && !isdataload) {
            if (Variables.sharedPreferences.getBoolean(Variables.islogin, false))
                init();
        }
        if ((view != null && isVisibleToUser) && isdataload) {

            apiCall();

        }

    }

    public void openSetting() {
        SettingFragment settingFragment = new SettingFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        View view = getActivity().findViewById(R.id.MainMenuFragment);
        if (view != null)
            transaction.replace(R.id.MainMenuFragment, settingFragment).commit();
        else
            transaction.replace(R.id.Profile_F, settingFragment).commit();
    }


    public View init() {

        username = view.findViewById(R.id.username);
        imageView = view.findViewById(R.id.user_image);
        imageView.setOnClickListener(this);

        video_count_txt = view.findViewById(R.id.video_count_txt);

        follow_count_txt = view.findViewById(R.id.follow_count_txt);
        fans_count_txt = view.findViewById(R.id.fan_count_txt);
        heart_count_txt = view.findViewById(R.id.heart_count_txt);

        setting_btn = view.findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);

        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        setupTabIcons();


        tabs_main_layout = view.findViewById(R.id.tabs_main_layout);
        top_layout = view.findViewById(R.id.top_layout);


        ViewTreeObserver observer = top_layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int height = top_layout.getMeasuredHeight();

                top_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                ViewTreeObserver observer = tabs_main_layout.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabs_main_layout.getLayoutParams();
                        params.height = (int) (tabs_main_layout.getMeasuredHeight() + height);
                        tabs_main_layout.setLayoutParams(params);
                        tabs_main_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);

                    }
                });

            }
        });


        create_popup_layout = view.findViewById(R.id.create_popup_layout);


        view.findViewById(R.id.following_layout).setOnClickListener(this);
        view.findViewById(R.id.fans_layout).setOnClickListener(this);

        isdataload = true;
        // apiCall();

        // update_profile();

        //Call_Api_For_get_Allvideos();

        return view;
    }


    //inner helper method
    private Uri getOutputMediaFileUri(int mediaTypeImage) {

        if (isExternalStorageAvailable()) {
            //get the URI
            //get external storage dir
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "UPLOADIMAGES");
            //create subdirectore if it does not exist
            if (!mediaStorageDir.exists()) {
                //create dir
                if (!mediaStorageDir.mkdirs()) {

                    return null;
                }
            }
            //create a file name
            //create file
            File mediaFile = null;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaTypeImage == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            }
            //return file uri
            Log.d("UPLOADIMAGE", "FILE: " + Uri.fromFile(mediaFile));

            return Uri.fromFile(mediaFile);
        } else {

            return null;
        }

    }

    //check if external storage is mounted. helper method
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void update_profile() {
        username.setText(Variables.sharedPreferences.getString(Variables.f_name, "") + " " + Variables.sharedPreferences.getString(Variables.l_name, ""));
        pic_url = Variables.sharedPreferences.getString(Variables.u_pic, "null");

        try {
            Picasso.with(context).load(pic_url)
                    .resize(200, 200)
                    .placeholder(R.drawable.user_profile)
                    .centerCrop()
                    .into(imageView);

        } catch (Exception e) {

        }
    }


    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_video_color));
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView2 = view2.findViewById(R.id.image);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_video_gray));
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView3 = view3.findViewById(R.id.image);
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.ic_mention));
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).setCustomView(view3);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:
                        if (UserVideo_F.myvideo_count > 0) {
                            create_popup_layout.setVisibility(View.GONE);
                        } else {
                            create_popup_layout.setVisibility(View.VISIBLE);
                            Animation aniRotate = AnimationUtils.loadAnimation(context, R.anim.up_and_down_animation);
                            create_popup_layout.startAnimation(aniRotate);
                        }
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_video_color));
                        break;
                    case 1:
                        create_popup_layout.clearAnimation();
                        create_popup_layout.setVisibility(View.GONE);
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_video_color));
                        break;
                    case 2:
                        create_popup_layout.clearAnimation();
                        create_popup_layout.setVisibility(View.GONE);
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_mention));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_my_video_gray));
                        break;
                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked_video_gray));
                        break;
                    case 2:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_mention));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                        break;
                }

                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final Resources resources;

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


        public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
            this.resources = resources;
        }

        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new UserVideo_F();
                    break;
                case 1:
                    result = new Liked_Video_F();
                    break;
                case 2:
                    result = new MentionVideoFragment();
                    break;

                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(final int position) {
            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }


        /**
         * Get the Fragment by position
         *
         * @param position tab position of the fragment
         * @return
         */
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }


    }


    //this will get the all videos data of user and then parse the data
    private void Call_Api_For_get_Allvideos() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("my_fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    /*    ApiRequest.Call_Api(context, Variables.showMyAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
            }
        });
*/


    }

    public void Parse_data(String responce) {


        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");

                JSONObject data = msgArray.getJSONObject(0);
                JSONObject user_info = data.optJSONObject("user_info");
                username.setText(user_info.optString("first_name") + " " + user_info.optString("last_name"));

                Profile_F.pic_url = user_info.optString("profile_pic");
                Picasso.with(context)
                        .load(Profile_F.pic_url)
                        .placeholder(context.getResources().getDrawable(R.drawable.user_profile))
                        .resize(200, 200).centerCrop().into(imageView);

                follow_count_txt.setText(data.optString("total_following"));
                fans_count_txt.setText(data.optString("total_fans"));
                heart_count_txt.setText(data.optString("total_heart"));


                JSONArray user_videos = data.getJSONArray("user_videos");
                if (!user_videos.toString().equals("[" + "0" + "]")) {
                    video_count_txt.setText(user_videos.length() + " Videos");
                    create_popup_layout.setVisibility(View.GONE);

                } else {

                    create_popup_layout.setVisibility(View.VISIBLE);
                    Animation aniRotate = AnimationUtils.loadAnimation(context, R.anim.up_and_down_animation);
                    create_popup_layout.startAnimation(aniRotate);

                }


            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void Open_Setting() {
        Open_menu_tab(setting_btn);
    }


    public void Open_Edit_profile() {
        Edit_Profile_F edit_profile_f = new Edit_Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

                update_profile();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, edit_profile_f).commit();
    }


    //this method will get the big size of profile image.
    public void OpenfullsizeImage(String url) {
        See_Full_Image_F see_image_f = new See_Full_Image_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        Bundle args = new Bundle();
        args.putSerializable("image_url", url);
        see_image_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
    }


    public void Open_menu_tab(View anchor_view) {
        Context wrapper = new ContextThemeWrapper(context, R.style.AlertDialogCustom);
        PopupMenu popup = new PopupMenu(wrapper, anchor_view);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popup.setGravity(Gravity.TOP | Gravity.RIGHT);
        }
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.edit_Profile_id:
                        Open_Edit_profile();
                        break;

                    case R.id.logout_id:
                        Logout();
                        break;
                }
                return true;
            }
        });

    }


    public void Open_Following() {

        Following_F following_f = new Following_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

                apiCall();

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", Variables.sharedPreferences.getString(Variables.u_id, ""));
        args.putString("from_where", "following");
        following_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, following_f).commit();

    }

    public void Open_Followers() {
        Following_F following_f = new Following_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {
                apiCall();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", Variables.sharedPreferences.getString(Variables.u_id, ""));
        args.putString("from_where", "fan");
        following_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, following_f).commit();

    }


    // this will erase all the user info store in locally and logout the user
    public void Logout() {
        /*SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
        editor.putString(Variables.u_id, "");
        editor.putString(Variables.u_name, "");
        editor.putString(Variables.u_pic, "");
        editor.putBoolean(Variables.islogin, false);
        editor.commit();*/
        sessionManager.clearAllData();
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainMenuActivity.class));
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Functions.deleteCache(context);
    }
}
