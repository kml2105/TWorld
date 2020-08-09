package com.semonics.tiktik.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.RequestBody;

import static com.semonics.tiktik.SimpleClasses.Utils.showLog;
import static com.semonics.tiktik.WebService.BaseAPIService.TAG_REQUEST;
import static com.semonics.tiktik.WebService.WSParams.WS_COMMENTS;
import static com.semonics.tiktik.WebService.WSParams.WS_DIRECT_MSG;
import static com.semonics.tiktik.WebService.WSParams.WS_DIRECT_MSG_PRIVACY;
import static com.semonics.tiktik.WebService.WSParams.WS_DOWNLOAD_VIDEO_PRIVACY;
import static com.semonics.tiktik.WebService.WSParams.WS_DUE_VIDEO_PRIVACY;
import static com.semonics.tiktik.WebService.WSParams.WS_FOLLOWERS_VIDEO;
import static com.semonics.tiktik.WebService.WSParams.WS_KEY_COMMENT_VIDEO_PRIVACY;
import static com.semonics.tiktik.WebService.WSParams.WS_KEY_DATA_PARSE_TYPE;
import static com.semonics.tiktik.WebService.WSParams.WS_LIKES;
import static com.semonics.tiktik.WebService.WSParams.WS_MENTIONS;
import static com.semonics.tiktik.WebService.WSParams.WS_NEW_FOLLOWERS;
import static com.semonics.tiktik.WebService.WSParams.WS_REACT_TO_VIDEO_PRIVACY;
import static com.semonics.tiktik.WebService.WSParams.WS_SAVE_LOGIN_INFO;
import static com.semonics.tiktik.WebService.WSParams.WS_VIEW_LIKED_VIDEOS_PRIVACY;

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

    public static RequestBody getForgotPw(String email) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WSParams.WS_EMAIL_OR_MOBILE, email);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RequestBody removeFromFollowing(String userId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WSParams.WS_FOLLOW, userId);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RequestBody searchApi(String keyWord) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WSParams.WS_KEYWORD, keyWord);
            // jsonObject.put(WS_KEY_DEVICE_ID, deviceId);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RequestBody likeVideo() {
        JSONObject jsonObject = new JSONObject();
        showLog(TAG_REQUEST, jsonObject.toString());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
        return body;
    }

    public static RequestBody accountSettingApiCall(int comment, boolean commentNoti, boolean directMsg,
                                                    int directMsgNoti, boolean downloadVideoNoti, boolean followersVideoNoti, boolean likesNoti,
                                                    boolean mentionNoti, boolean newFollowersNoti, int reactToVideopPivacy, boolean saveLoginInfo,
                                                    int viewLikedVideoPrivacy) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WS_KEY_COMMENT_VIDEO_PRIVACY, comment);
            jsonObject.put(WS_COMMENTS, commentNoti);
            jsonObject.put(WS_DIRECT_MSG, directMsg);
            jsonObject.put(WS_DIRECT_MSG_PRIVACY, directMsgNoti);
            jsonObject.put(WS_DOWNLOAD_VIDEO_PRIVACY, downloadVideoNoti);
            jsonObject.put(WS_DUE_VIDEO_PRIVACY, 0);
            jsonObject.put(WS_FOLLOWERS_VIDEO, followersVideoNoti);
            jsonObject.put(WS_LIKES, likesNoti);
            jsonObject.put(WS_MENTIONS, mentionNoti);
            jsonObject.put(WS_NEW_FOLLOWERS, newFollowersNoti);
            jsonObject.put(WS_REACT_TO_VIDEO_PRIVACY, reactToVideopPivacy);
            jsonObject.put(WS_SAVE_LOGIN_INFO, saveLoginInfo);
            jsonObject.put(WS_VIEW_LIKED_VIDEOS_PRIVACY, viewLikedVideoPrivacy);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RequestBody addUser(String user_name, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WSParams.WS_USER_NAME, user_name);
            jsonObject.put(WSParams.WS_PASSWORD, password);
            showLog(TAG_REQUEST, jsonObject.toString());
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse(WS_KEY_DATA_PARSE_TYPE), jsonObject.toString());
            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RequestBody addDocument(File file, String location, String hashTag, String caption, String tagPeople, String musicId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WSParams.WS_FILE, file);
            jsonObject.put(WSParams.WS_LOCATION, location);
            jsonObject.put(WSParams.WS_HAS_TAG, hashTag);
            jsonObject.put(WSParams.WS_CAPTION, caption);
            jsonObject.put(WSParams.WS_TAG_PEOPLE, tagPeople);
            jsonObject.put(WSParams.WS_MUSIC_ID, musicId);
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