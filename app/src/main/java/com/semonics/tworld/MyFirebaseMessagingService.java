package com.semonics.tworld;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.semonics.MyNotificationManager;
import com.semonics.tworld.WebService.TWorld;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "notification";
    private final String TAG_PROFILE = "Personal Info";
    private final String TAG_LAND_LIST = "Land Listing";
    private final String TAG_CROP_LIST = "Crop Listing";
    private final String TAG_LAND_INFO = "Land info";
    private final String TAG_CROP_INFO = "Crop info";
    private final String TAG_ORDER_PLACED = "My Orders";
    private final String TAG_ORDER_APPROVED = "Order Approved";
    private final String TAG_ORDER_PAYMENT_DONE = "Order Payment Done";
    private final String TAG_ORDER_DISPATCHED = "Order Dispatched";
    private final String TAG_ORDER_DELIVERED = "Order Delivered";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (TWorld.getInstance().getSession().isLoggedIn() &&
                remoteMessage.getData().size() > 0) {

            Log.e(TAG, " Payload: " + remoteMessage.getData().toString());
            Map<String, String> map = remoteMessage.getData();
            String message = map.get("message");
            String type = map.get("type");
            String id;
            /*Gets the id from message otherwise pass zero*/
            if (map.containsKey("id")) {
                id = map.get("id");
            } else {
                id = "0";
            }
            sendPushNotification(message, type, Integer.parseInt(id));
        }
    }

    /*This method will display the notification*/
    /*We are passing the JSONObject that is received from*/
    /*Firebase cloud messaging*/
    private void sendPushNotification(String message, String tag, int id) {
        MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
        Intent intent = new Intent();
        switch (tag) {
        /*    case TAG_PROFILE:
                intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                intent.putExtra(Constant.INTENT_REDIRECT_POSITION, MainMenuActivity.PERSONAL_INFO);
                new GetData().execute();
                break;

            case TAG_LAND_LIST:
                intent = new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra(Constant.INTENT_REDIRECT_POSITION, DrawerActivity.LAND_LIST);
                break;

            case TAG_CROP_LIST:
                intent = new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra(Constant.INTENT_REDIRECT_POSITION, DrawerActivity.CROP_LIST);
                break;

            case TAG_LAND_INFO:
                intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(Constant.INTENT_TITLE, getResources().getString(R.string.title_land_info));
                intent.putExtra(Constant.INTENT_ID, id);
                break;

            case TAG_CROP_INFO:
                intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(Constant.INTENT_TITLE, getResources().getString(R.string.title_crop_info));
                intent.putExtra(Constant.INTENT_ID, id);
                break;

            case TAG_ORDER_PLACED:
                intent = new Intent(getApplicationContext(), DrawerActivity.class);
                break;*/

            case TAG_ORDER_APPROVED:
                intent = new Intent();
                break;

            case TAG_ORDER_PAYMENT_DONE:
                intent = new Intent();
                break;

            case TAG_ORDER_DISPATCHED: {
                intent = new Intent();
                break;
            }
            case TAG_ORDER_DELIVERED:
                intent = new Intent();
                break;
        }
        mNotificationManager.showSmallNotification(message, intent);
    }

    /**//*
    private class GetData extends AsyncTask<Void, Void, Void> {
        String response = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(WSConstants.PARAMS_AUTHKEY, Utils.getSharedPreferences().getAuthKey());
                jsonObject.put(WSConstants.PARAMS_FARMER_ID, Utils.getSharedPreferences().getUserId());
                String request = jsonObject.toString();
                WSWebserviceData wsWebserviceData = new WSWebserviceData(getApplicationContext());
                response = wsWebserviceData.executeWebservice(request, WSConstants.URL.API_FARMER_PROFILE);
                Utils.showLog("response", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response.length() != 0) {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject objectData = responseObject.getJSONObject(WSConstants.PARAMS_DATA);
                    String profileData = objectData.toString();
                    DigiAgriApp.getInstance().getSharedPreferences().putString(DigiAgriAppSharedPref.PREF_PROFILE_DATA, profileData);
                    Intent broadcastIntent = new Intent(Constant.UPDATE_HEADER);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
}
