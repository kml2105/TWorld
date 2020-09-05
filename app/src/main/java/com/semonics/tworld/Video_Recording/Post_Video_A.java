package com.semonics.tworld.Video_Recording;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.semonics.tworld.Main_Menu.MainMenuActivity;
import com.semonics.tworld.R;
import com.semonics.tworld.Services.ServiceCallback;
import com.semonics.tworld.Services.UploadVideoService;
import com.semonics.tworld.SimpleClasses.Functions;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.SimpleClasses.Variables;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.semonics.tworld.WebService.WSParams.SERVICE_UPLOAD_DOC;

public class Post_Video_A extends AppCompatActivity implements ServiceCallback {

    ImageView video_thumbnail;
    private String compressVideoFilePath;
    String video_path;
    private static final float maxImageSize = 500;
    ProgressDialog progressDialog;

    ServiceCallback serviceCallback;

    int AUTOCOMPLETE_REQUEST_CODE = 0;
    LocalBroadcastManager lbm;
    EditText description_edit;
    TextView tvEventLocation;
    int videoDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_video);

        video_path = Variables.output_filter_file;
        video_thumbnail = findViewById(R.id.video_thumbnail);
        tvEventLocation = findViewById(R.id.tv_select_place);
        lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("FFMPEG_COMPLETE"));
        /*tvEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvEventLocation.showDropDown();
            }
         });*/
        description_edit=findViewById(R.id.description_edit);

        // this will get the thumbnail of video and show them in imageview
        Bitmap bmThumbnail;
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(video_path,
                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

        if (bmThumbnail != null) {
            video_thumbnail.setImageBitmap(bmThumbnail);
        } else {
        }




        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);




      findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){
            onBackPressed();
        }
    });


     findViewById(R.id.post_btn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){

            progressDialog.show();
            Start_Service();

        }
    });
}

    /**
     * @return video thumbnail byte array to upload on server.
     */
    private byte[] getBytes() {
        Bitmap mBitmap = ThumbnailUtils.createVideoThumbnail(compressVideoFilePath, MediaStore.Images.Thumbnails.MINI_KIND);
        ByteArrayOutputStream mStream = new ByteArrayOutputStream();

        float ratio = Math.min(maxImageSize / mBitmap.getWidth(), maxImageSize / mBitmap.getHeight());
        int width = Math.round(ratio * mBitmap.getWidth());
        int height = Math.round(ratio * mBitmap.getHeight());

        Bitmap thumbnailBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
        thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 70, mStream);
        mBitmap.recycle();
        return mStream.toByteArray();
    }
    private void callWSForPreUploadS3() {
        try {
            File fileToPass = null;
            RequestBody requestFile;
            MultipartBody.Part multipartBody = null;
            try {
                fileToPass = new File(compressVideoFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String,String> map = new HashMap<String, String>() {};
            map.put("location","");
            map.put("hashtag","");
            map.put("caption",description_edit.getText().toString());
            map.put("People","");

//            Utility.printRequestLog(videoJson.toString());
          //   new BaseAPIService(this, SERVICE_UPLOAD_DOC,fileToPass,responseListener,map);
            //, , this,true,this, "POST",true,true
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                // int code = jsonObject.getInt(API_CODE);
                // String msg = jsonObject.getString(API_MSG);
                //if (code == 200) {
               /* } else {
                    methodToast(context, msg);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(Post_Video_A.this, error);
        }
    };

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                callWSForPreUploadS3();
            }
        }
    };
    public void autoCompletePlace(View view) {

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyD1LEUfuKnF30HtUZ7H-b7_mFw4-qH1DBI");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ID, Place.Field.PHONE_NUMBER, Place.Field.RATING, Place.Field.WEBSITE_URI);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place != null) {
                    tvEventLocation.setText(place.getName());
                }

                Log.i("TAG", "Place: " + place.getAddress() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

// this will start the service for uploading the video into database
    public void Start_Service(){

        serviceCallback=this;

        /*PublicVideoVO publicVideoVO = s3UploadFailedList.get(i);
                Intent serviceIntent = new Intent(FeedActivity.this, UploadVideoService.class);
                serviceIntent.putExtra("serverVideoId", publicVideoVO.getVideoId());
                serviceIntent.putExtra(UploadVideoService.EXTRA_VIDEO_PATH, publicVideoVO.getLocalVideoPath());
                startService(serviceIntent);*/
        UploadVideoService mService = new UploadVideoService();
        if (!Functions.isMyServiceRunning(this,mService.getClass())) {
            Intent mServiceIntent = new Intent(this.getApplicationContext(), mService.getClass());
            mServiceIntent.setAction("startservice");
            mServiceIntent.putExtra("uri",""+ Uri.fromFile(new File(video_path)));
            mServiceIntent.putExtra("desc",""+description_edit.getText().toString());
            mServiceIntent.putExtra("duration",videoDuration);
            mServiceIntent.putExtra(UploadVideoService.EXTRA_VIDEO_PATH,  Uri.fromFile(new File(video_path)));
            startService(mServiceIntent);

            Intent intent = new Intent(this, UploadVideoService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        }
        else {
            Toast.makeText(this, "Please wait video already in uploading progress", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        Stop_Service();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }




    // when the video is uploading successfully it will restart the appliaction
    @Override
    public void ShowResponce(final String responce) {

        Toast.makeText(Post_Video_A.this, responce, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();


        if(responce.equalsIgnoreCase("Your Video is uploaded Successfully")) {


            startActivity(new Intent(Post_Video_A.this, MainMenuActivity.class));
            finishAffinity();

        }
    }


    // this is importance for binding the service to the activity
    UploadVideoService mService;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

           UploadVideoService.LocalBinder binder = (UploadVideoService.LocalBinder) service;
            mService = binder.getService();

           // mService.setCallbacks(Post_Video_A.this);



        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // this function will stop the the ruuning service
    public void Stop_Service(){

        serviceCallback=this;

        UploadVideoService mService = new UploadVideoService();

        if (Functions.isMyServiceRunning(this,mService.getClass())) {
            Intent mServiceIntent = new Intent(this.getApplicationContext(), mService.getClass());
            mServiceIntent.setAction("stopservice");
            startService(mServiceIntent);
        }

    }



}
