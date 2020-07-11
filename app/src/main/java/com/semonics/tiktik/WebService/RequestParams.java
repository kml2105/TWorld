package com.semonics.tiktik.WebService;

import com.semonics.tiktik.SimpleClasses.WSParams;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

import static com.semonics.tiktik.SimpleClasses.Utils.showLog;
import static com.semonics.tiktik.SimpleClasses.WSParams.WS_KEY_DATA_PARSE_TYPE;
import static com.semonics.tiktik.WebService.BaseAPIService.TAG_REQUEST;

public class RequestParams {

    public static RequestBody getLogin(String user_name, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WSParams.WS_USER_NAME, user_name);
            jsonObject.put(WSParams.WS_PASSWORD, password);
           // jsonObject.put(WS_KEY_DEVICE_ID, deviceId);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public static RequestBody createUser(String user_name, int user_type, String password, int userId, boolean isEdit) {
        try {
            JSONObject jsonObject = new JSONObject();
            if (isEdit) {
                jsonObject.put(WS_KEY_USER_ID, userId);
            } else {
                jsonObject.put(WS_KEY_PASSWORD, password);
            }
            jsonObject.put(WS_KEY_USER_NAME, user_name);
            jsonObject.put(WS_KEY_TYPE, user_type);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*public static RequestBody changePassword(int user_id, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WS_KEY_USER_ID, user_id);
            jsonObject.put(WS_KEY_NEW_PW, password);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*public static RequestBody createAppointment(int userId, String mobile_number, String name, int isNewCase, int caseId, String date) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WS_KEY_USER_ID, userId);
            jsonObject.put(WS_KEY_DATE, date);
            jsonObject.put(WS_KEY_MOBILE_NO, mobile_number);
            jsonObject.put(WS_KEY_NAME, name);
            jsonObject.put(WS_KEY_IS_NEW_CASE, isNewCase);
            jsonObject.put(WS_KEY_CASE_ID, caseId);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }*/

   /* public static RequestBody doLogOut(int user_id, String deviceId) {
        try {
            JSONObject jsonObject = new JSONObject();
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }*/

   /* public static RequestBody changePasswordForDrawer(int user_id, String oldPassword, String newPassword) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WS_KEY_USER_ID, user_id);
            jsonObject.put(WS_KEY_NEW_PW, newPassword);
            jsonObject.put(WS_KEY_OLD_PW, oldPassword);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*public static RequestBody changePasswordForUser(String newPassword) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WS_KEY_NEW_PW, newPassword);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }*/

}