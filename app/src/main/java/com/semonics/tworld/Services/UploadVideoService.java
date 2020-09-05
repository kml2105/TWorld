package com.semonics.tworld.Services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;

import com.semonics.tworld.Main_Menu.MainMenuActivity;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.semonics.tworld.WebService.WSParams.SERVICE_UPLOAD_DOC;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_CODE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_MSG;


public class UploadVideoService extends IntentService {

    public static final String EXTRA_VIDEO_PATH = "VIDEO_PATH";
    private String fileName;
    private String videoFile;
    private String to;
    private String chatId;
    String description;

    private String serverVideoId;
    private File dataFile;
    private Bitmap thumbnailBitmap;
    private byte[] data;

    private int count;

    private boolean fromDiscover;
    private static final float maxImageSize = 500;


    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public UploadVideoService getService() {
            return UploadVideoService.this;
        }
    }

    public UploadVideoService() {
        super("UploadVideoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            count = 0;
            videoFile = intent.getStringExtra("uri");
            description = intent.getStringExtra("desc");
            fromDiscover = true;
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

            postVideoUrl();
        }
    }


    private void postVideoUrl() {
        try {
            dataFile = new File(videoFile);
            fileName = dataFile.getName();

            Map<String,RequestBody> map = new HashMap<String, RequestBody>() {};
            RequestBody descriptionReq = RequestBody.create(
                    MediaType.parse("text/plain"),
                    description);
            RequestBody locationReq = RequestBody.create(
                    MediaType.parse("text/plain"),
                    description);
            RequestBody captionReq = RequestBody.create(
                    MediaType.parse("text/plain"),
                    description);
            RequestBody hashTagReq = RequestBody.create(
                    MediaType.parse("text/plain"),
                    description);
            RequestBody peopleReq = RequestBody.create(
                    MediaType.parse("text/plain"),
                    description);
            RequestBody musicIdReq = RequestBody.create(
                    MediaType.parse("text/plain"),
                    description);
            map.put("location",locationReq);
            map.put("hashtag",hashTagReq);
            map.put("caption",descriptionReq);
            map.put("people",peopleReq);
            map.put("musicId",musicIdReq);
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("video/*"), dataFile);
            // Create MultipartBody.Part using file request-body,file name and part name
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", dataFile.getName(), fileReqBody);
//            Utility.printRequestLog(videoJson.toString());
            new BaseAPIService(this, SERVICE_UPLOAD_DOC,part,responseListener,map);

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
                 String msg = jsonObject.getString(WS_KEY_MSG);
                if (code == 200) {
                    Utils.methodToast(getApplicationContext(),msg);
                    Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(getApplicationContext(), error);
        }
    };


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




}
