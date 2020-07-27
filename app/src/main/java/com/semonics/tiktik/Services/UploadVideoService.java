package com.semonics.tiktik.Services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.downloader.Constants;
import com.facebook.internal.Utility;
import com.semonics.tiktik.WebService.ResponseListener;
import com.semonics.tiktik.WebService.TicTic;
import com.semonics.tiktik.WebService.WSParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;
import static java.sql.Types.TIME;


public class UploadVideoService extends IntentService implements ResponseListener {

    public static final String EXTRA_VIDEO_PATH = "VIDEO_PATH";
    private String fileName;
    private String videoFile;
    private String to;
    private String chatId;

    private String serverVideoId;
    //    private DiscoverVO mDiscoverVO;
//    private VideoDataSource mDataSource;
    private File dataFile;
    private Bitmap thumbnailBitmap;
    private byte[] data;

    private int cVideoId = -1;
    private int count;
    private int minAge, maxAge;
    private int MAX_TRY = 0;

    private boolean fromDiscover;
    private static final float maxImageSize = 500;

    public static boolean isVideoUploading;


    public UploadVideoService() {
        super("UploadVideoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            count = 0;
            videoFile = intent.getStringExtra(EXTRA_VIDEO_PATH);
            fromDiscover = true;
            MAX_TRY = 0;
            // Check if intent has video id, if yes upload profile video else consider as reaction or reply.
//            if (intent.hasExtra("cVideoId")) {
//                fromDiscover = false;
//                cVideoId = intent.getIntExtra("cVideoId", -1);
//
////                mDataSource = new VideoDataSource(this);
////                openDatabase();
//            }

          //  dbHelper = new QueryDataHelper(UploadVideoService.this);

            if(intent.hasExtra("serverVideoId")){
                fromDiscover = false;
                serverVideoId = intent.getStringExtra("serverVideoId");
            }

            uploadVideo();
        }
    }




    /**
     * Upload video to AS3 by using {@link TransferUtility}
     */
    private void uploadVideo() {


        dataFile = new File(videoFile);
        fileName = dataFile.getName();
        String BUCKET_NAME = bucketName + "/temp_video";
        try {
            final TransferObserver transferObserver = transferUtility.upload(
                    BUCKET_NAME,     /* The bucket to upload to */
                    fileName,    /* The key for the uploaded object */
                    dataFile        /* The file where the data to upload exists */
            );

            transferObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d("TAG", " State: " + state.name());
                    if (state == TransferState.COMPLETED && count == 0) {
                        MAX_TRY =0;
                        count++;
                        isVideoUploading = false;
                        if(dbHelper != null){
                            // S3 upload successfully
                            dbHelper.updatePublicVideoStatus(serverVideoId, 2);
                        }
                        updateVideoStatusToServer();
                        // postVideoUrl();
//                        getUploadVideoAPICall();
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                }

                @Override
                public void onError(int id, Exception ex) {
                    ex.printStackTrace();
                    if (!fromDiscover) {
//                        uploadFailed(false);
                    } else {
                        if (MAX_TRY == 0) {
                            MAX_TRY++;
                            uploadVideo();
                        }
                    }
                }
            });
        } catch (IllegalStateException e) {
            isVideoUploading = false;
            if (Utility.isAppOpened()) {
                try {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error while uploading video.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IllegalStateException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void openDatabase() {
//        if (!mDataSource.isOpen())
//            mDataSource.open();
    }


    private void postVideoUrl() {
        try {
            data = getBytes();


            JSONObject videoJson = new JSONObject();
            videoJson.put("video_name", fileName);

            String URL =baseur+ Constants.VIDEO_UPLOAD;
            final String token = TicTic.getInstance().getString(TOKEN);
            Utility.printURLLog("POST "+URL);
            VolleyMultipartRequest mRequest = new VolleyMultipartRequest(POST, URL, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    Utility.printResponseLog(resultResponse);
                    try {
//                        AppsFlyerLib.getInstance().trackEvent(getApplicationContext(), "Public_Video_Upload", null);
                        JSONObject responseJson = new JSONObject(resultResponse);
                        JSONObject dataJson = responseJson.getJSONObject(Constants.JSON_DATA);
                        String tempVideoId = dataJson.getString("video_id");
                        String videoPath = dataJson.getString("video_path");
                        String thumbnailUrl = dataJson.getString("video_thumbnail");

//                        openDatabase();
                        // Insert video details on local database.
//                        mDataSource.updatePublicVideo(cVideoId, null, null, tempVideoId, videoPath, 0, 1, thumbnailUrl,0, "");
                        Intent intent = new Intent(Constants.BROADCAST_EVENT);
                        intent.putExtra("video_id", tempVideoId);

                        // Broadcast to ConversationFragment to update public video details.
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        if (thumbnailBitmap !=null)
                            thumbnailBitmap.recycle();
                    } catch (Exception e) {
//                        uploadFailed(false);
                        e.printStackTrace();
                    } finally {
//                        if (mDataSource != null && mDataSource.isOpen())
//                            mDataSource.close();
                        data = null;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.statusCode == 504) {
//                        uploadFailed(true);
                    }
                    error.printStackTrace();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> mHeaders = new HashMap<>();
                    mHeaders.put(Constants.HEADER_TOKEN, token);
                    Utility.printHeaderLog(mHeaders.toString());
                    return mHeaders;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("video_thumbnail", new DataPart(fileName.replace(".mp4", ".jpeg"), data, "image/jpeg"));
                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("video_name", fileName);
                    // Default age range is +-5 from user's birth date.
                    params.put("filter_age_range", minAge + "," + maxAge);
                    params.put("filter_gender_range", "male,female,couple");
                    params.put("filter_distance",Constants.DISTANCE_FILTER+Constants.BLANK_STRING);
                    if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
                        params.put("video_latitude", lat);
                        params.put("video_longitude", lng);
                    } else {
                        params.put("video_latitude", "0");
                        params.put("video_longitude", "0");
                    }
                    Utility.printRequestLog(params.toString());
                    return params;
                }
            };

            mRequest.setRetryPolicy(new DefaultRetryPolicy(
                    Constants.CONNECTION_TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            GenuInApplication.getInstance().getRequestQueue().add(mRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retry to upload video on either AS# or server.
     * @param fromServer -boolean to indicate from where uploading has failed
     */
//    private void uploadFailed(boolean fromServer) {
//
//        openDatabase();
//        if (MAX_TRY == 0) {
//            MAX_TRY ++;
//            mDataSource.updatePublicVideo(cVideoId, null, null, null, null, 0, 2, null, 0, "");
//            if (mDataSource != null && mDataSource.isOpen())
//                mDataSource.close();
//
//            if (Utility.isNetworkAvailable(this)) {
//                if (fromServer) {
//                    postVideoUrl();
//                } else {
//                    uploadVideo();
//                }
//            }
//
//            if (thumbnailBitmap != null)
//                thumbnailBitmap.recycle();
//        } else {
//            MAX_TRY = 0;
//            mDataSource.updatePublicVideo(cVideoId, null, null, null, null, 0, 3, null, 0, "");
//            if (mDataSource != null && mDataSource.isOpen())
//                mDataSource.close();
//        }
//    }

    /**
     * @return video thumbnail byte array to upload on server.
     */
    private byte[] getBytes() {
        Bitmap mBitmap = ThumbnailUtils.createVideoThumbnail(dataFile.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);
        ByteArrayOutputStream mStream = new ByteArrayOutputStream();

        float ratio = Math.min(maxImageSize / mBitmap.getWidth(), maxImageSize / mBitmap.getHeight());
        int width = Math.round( ratio * mBitmap.getWidth());
        int height = Math.round(ratio * mBitmap.getHeight());

        thumbnailBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
        thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 70, mStream);
        mBitmap.recycle();
        return mStream.toByteArray();
    }

    @Override
    public void onSuccess(String response) {
        try{
            JSONObject responseJson = new JSONObject(response);
            JSONObject dataJson = responseJson.getJSONObject(Constants.JSON_DATA);
            String tempVideoId = dataJson.getString("video_id");
            String videoPath = dataJson.getString("video_path");
            String thumbnailUrl = dataJson.getString("video_thumbnail");

//                        openDatabase();
            // Insert video details on local database.
//                        mDataSource.updatePublicVideo(cVideoId, null, null, tempVideoId, videoPath, 0, 1, thumbnailUrl,0, "");
            Intent intent = new Intent(Constants.BROADCAST_EVENT);
            intent.putExtra("video_id", tempVideoId);

            // Broadcast to ConversationFragment to update public video details.
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            if (thumbnailBitmap !=null)
                thumbnailBitmap.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
