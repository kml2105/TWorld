package com.semonics.tworld.WebService;

public class WSParams {
    /*response kString WS_KEY_TOKEN = "token"; */

    /*api method*/
    public static String METHOD_POST = "POST";
    public static String METHOD_GET = "GET";
    public static String METHOD_PUT = "PUT";
    public static String METHOD_DELETE = "DELETE";

    /*req key paraey params*/
    public static String BASE_URL = "http://18.188.113.192:80/";
    public static String WS_KEY_CODE = "res_code";
    public static String WS_KEY_MSG = "res_message";
    public static String WS_KEY_OBJ = "res_object";
    public static String WS_HAS_TAG = "hashTag";
    public static String WS_USER_NAME = "username";
    public static String WS_KEY_BEARER = "Bearer ";
    public static String WS_PASSWORD = "password";
    public static String WS_DEVICE_ID = "deviceId";
    public static String WS_FILE = "file";
    public static String WS_LOCATION = "location";
    public static String WS_CAPTION = "caption";
    public static String WS_MUSIC_ID = "musicId";
    public static String WS_TAG_PEOPLE = "tagPeople";
    public static final String WS_KEY_DATA_PARSE_TYPE = "application/json; charset=utf-8";
    public static String WS_KEY_TOKEN = "authToken";
    public static String WS_KEY_COMMENT_VIDEO_PRIVACY = "commentVideoPrivacy";
    public static String WS_COMMENTS = "comments";
    public static String WS_DIRECT_MSG = "directMessage";
    public static String WS_DIRECT_MSG_PRIVACY = "directMessagePrivacy";
    public static String WS_DOWNLOAD_VIDEO_PRIVACY = "downloadVideoPrivacy";
    public static String WS_DUE_VIDEO_PRIVACY = "duetWithVideoPrivacy";
    public static String WS_FOLLOWERS_VIDEO = "followersVideo";
    public static String WS_LIKES = "likes";
    public static String WS_MENTIONS = "mentions";
    public static String WS_REACT_TO_VIDEO_PRIVACY = "reactToVideoPrivacy";
    public static String WS_NEW_FOLLOWERS = "newFollowers";
    public static String WS_VIEW_LIKED_VIDEOS_PRIVACY = "viewLikedVideosPrivacy";
    public static String WS_SAVE_LOGIN_INFO = "saveLoginInfo";
    public static String WS_KEYWORD = "keyword";
    public static String WS_FOLLOW = "follow";
    public static String WS_EMAIL_OR_MOBILE = "emailOrMobile";

    /*pref string*/
    public static String IS_LOGIN = "isLogin";

    /*service name*/
    public static String MODULE_DOC = "document/";
    public static String MODULE_COMMENT = "comment/";
    public static String MODULE_FOLLOW = "follow/";
    public static String MODULE_USER = "user/";
    public static String MODULE_LIKE = "like/";
    public static String MODULE_ALL = "all/";
    public static String MODULE_NOTIFICATION = "notification/";
    public static String MODULE_COUNT = "count/";
    public static String MODULE_SEEN = "seen/";
    public static String MODULE_FORGOT_PASSWORD = "ForgotPassword/";
    public static String MODULE_REQUEST_COUNT = "requestCount/";
    public static String SERVICE_UPLOAD_DOC = "document";
    public static String SERVICE_AUTHENTICATE = "authenticate";
    public static String SERVICE_ADD_USER = "addUser";
    public static String SERVICE_GET_DOC_FOR_YOU = MODULE_DOC + "forYou";
    public static String SERVICE_ACCOUNT_SETTINGS = "user/accountSettings";
    public static String SERVICE_GET_USER = "user";
    public static String SERVICE_BLOCK_ACC_LIST = MODULE_FOLLOW + "blockAccountList";
    public static String SERVICE_ALL_LIKED_VIDEO = MODULE_DOC + MODULE_LIKE + MODULE_ALL;
    public static String SERVICE_ALL_VIDEO = MODULE_DOC + SERVICE_GET_USER;
    public static String SERVICE_LIKE_VIDEO = MODULE_DOC + MODULE_LIKE;
    public static String SERVICE_SEARCH_ALL = "search";
    public static String SERVICE_GET_MENTIONED_LIST = MODULE_USER + "mentioned";
    public static String SERVICE_GET_FOLLOWING_LIST = MODULE_FOLLOW + "following";
    public static String SERVICE_GET_FOLLOWERS_LIST = MODULE_FOLLOW + "followers";
    public static String SERVICE_GET_PENDING_REQ = MODULE_FOLLOW + "pendingRequest";
    public static String SERVICE_GET_PENDING_REQ_COUNT = MODULE_FOLLOW + MODULE_REQUEST_COUNT + "user";
    public static String SERVICE_GET_ALL_NOTI_COUNT = MODULE_NOTIFICATION + "count";
    public static String SERVICE_GET_NEW_NOTI = MODULE_NOTIFICATION + MODULE_SEEN + "0";
    public static String SERVICE_GET_SEEN_NOTI = MODULE_NOTIFICATION + MODULE_SEEN + "1";
    public static String SERVICE_READ_NOTI = SERVICE_GET_SEEN_NOTI + "/";
    public static String SERVICE_UNFOLLOW = "unfollow";
    public static String SERVICE_FORGOT_PW = MODULE_USER + "forgotPassword";
    public static String SERVICE_SEARCH_VIDEO = MODULE_DOC + SERVICE_SEARCH_ALL;
    public static String SERVICE_SEARCH_MUSIC = MODULE_DOC + "musicSearch";
    public static String SERVICE_SEARCH_USER = SERVICE_GET_USER + "/" + SERVICE_SEARCH_ALL;
    public static String SERVICE_GET_ALL_COMMENT_LIST = MODULE_COMMENT + MODULE_DOC;
    public static String SERVICE_VERIFY_OTP = MODULE_USER + MODULE_FORGOT_PASSWORD;
    public static String SERVICE_UPDATE_PASSWORD = MODULE_USER + "password";
    public static String SERVICE_ADD_DEVICE = "device";
    public static String SERVICE_UPLOAD_PROFILE_PIC = MODULE_USER + "profile";
    public static String SERVICE_SEND_COMMENT = MODULE_COMMENT;
}

