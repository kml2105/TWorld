package com.semonics.tworld.WebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.semonics.tworld.Accounts.LoginActivity;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.semonics.tworld.SimpleClasses.Utils.dismissProgressDialog;
import static com.semonics.tworld.SimpleClasses.Utils.getDeviceId;
import static com.semonics.tworld.SimpleClasses.Utils.methodToast;
import static com.semonics.tworld.SimpleClasses.Utils.showLog;
import static com.semonics.tworld.SimpleClasses.Utils.showProgressDialog;
import static com.semonics.tworld.WebService.WSParams.SERVICE_ALL_LIKED_VIDEO;
import static com.semonics.tworld.WebService.WSParams.SERVICE_ALL_VIDEO;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_DOC_FOR_YOU;
import static com.semonics.tworld.WebService.WSParams.SERVICE_GET_MENTIONED_LIST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_SEARCH_ALL;
import static com.semonics.tworld.WebService.WSParams.SERVICE_SEARCH_MUSIC;
import static com.semonics.tworld.WebService.WSParams.SERVICE_SEARCH_VIDEO;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_BEARER;


public class BaseAPIService {
    private Context context;
    private ResponseListener responseListener; /*Custom interface for listening reponse success or failure.*/
    private boolean isShowProgress = false; /*This variable is for deciding dynamically that you want to show dialog or not.*/
    ProgressDialog pd;
    public static final String TAG_EXCEPTION = "Exception";
    public static final String TAG_ERROR = "Error";
    public static final String TAG_SUCCESS = "Success";
    public static final String TAG_FAILURE = "Failure";
    public static final String TAG_LOG = "LOG";
    public static final String TAG_REQUEST = "Request";
    public static final String TAG_URL = "Url";
    public static final String TAG_RESPONSE = "Response";
    SessionManager sessionManager;

    /**
     * This is the constructor of the class      *      * @param context          of the class      * @param serviceName      name of the service      * @param body             request body of data      * @param responseListener is the listener of response      * @param isShowProgress   decides whether show progress bar or not
     */
    public BaseAPIService(final Context context, String module, RequestBody requestBody, boolean isHeader, ResponseListener responseListener, String apiMethodType, boolean isShowProgress) {
        this.context = context;
        sessionManager = TWorld.getInstance().getSession();
        this.responseListener = responseListener;
        this.isShowProgress = isShowProgress;
        if (Utils.isNetworkAvailable(this.context)) {
            showLog("URL", module);
            if (isShowProgress) {
                showProgressDialog(context);
            }
            processRequest(module, requestBody, isHeader, context, apiMethodType);
        } else {
            responseListener.onFailure(context.getResources().getString(R.string.no_internet_connection));
        }
    }

    public BaseAPIService(final Context context, String module, MultipartBody.Part multipartBody, HashMap<String, RequestBody> requestBody, ResponseListener responseListener) {
        this.context = context;
        sessionManager = TWorld.getInstance().getSession();
        this.responseListener = responseListener;
        if (Utils.isNetworkAvailable(this.context)) {
            showLog("URL", module);
            if (isShowProgress) {
                showProgressDialog(context);
            }
            processMultiPartRequest(module, multipartBody, requestBody);
        } else {
            responseListener.onFailure(context.getResources().getString(R.string.no_internet_connection));
        }
    }

    private void processRequest(final String module, final RequestBody requestBody, final boolean isHeader, final Context context, final String apiMethodType) {
//        showLog(TAG_REQUEST, TAG_URL + serviceName);
        String token = "";
        token = WS_KEY_BEARER + sessionManager.getString(SessionManager.PREF_TOKEN);
        showLog("token req", token);
        Call<ResponseBody> call = null;
        switch (apiMethodType) {
            case "POST":
                if (isHeader) {
                    showLog("token", token);
                    call = RetrofitBuilder.getWebService().doRequestPostWithHeader(token, module, requestBody);
                } else {
                    String deviceId = getDeviceId(context);
                    call = RetrofitBuilder.getWebService().doRequestForAuthentication(module, requestBody,deviceId);
                }
                break;
            case "GET":
                if (module.equals(SERVICE_GET_DOC_FOR_YOU) || module.contains(SERVICE_ALL_VIDEO) || module.contains(SERVICE_ALL_LIKED_VIDEO)|| module.contains(SERVICE_GET_MENTIONED_LIST)) {
                    call = RetrofitBuilder.getWebService().doRequestGetWithPagination(token, module, 0, 10);
                } else if (module.contains(SERVICE_SEARCH_VIDEO) || module.contains(SERVICE_SEARCH_ALL)||module.contains(SERVICE_SEARCH_MUSIC)) {
                    String keyword = sessionManager.getString(SessionManager.PREF_SEARCH_KEYWORD);
                    call = RetrofitBuilder.getWebService().doRequestGetForSearchWithPagination(token, module, keyword, 0, 10);
                } else {
                    call = RetrofitBuilder.getWebService().doRequestGet(token, module);
                }
                break;
            case "DELETE":
                call = RetrofitBuilder.getWebService().doRequestDelete(token, module);
                break;
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    dismissProgressDialog();
                    if (response.code() == 200) {
                        String res = response.body().string();
                        responseListener.onSuccess(res);
                        showLog(TAG_RESPONSE, res);
                    } else if (response.code() == 401) {
                        TWorld.getInstance().getSession().setBoolean(SessionManager.PREF_IS_LOGIN, false);
                        TWorld.getInstance().getSession().clearAllData();
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);
                    } else if (response.code() == 500) {
                        methodToast(context, "Internal Server error.");
                    }
                } catch (Exception e) {
                    showLog(TAG_EXCEPTION, e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                try {
                    showLog(TAG_EXCEPTION, t.getMessage());
                    responseListener.onFailure(t.getMessage());
                } catch (Exception e) {
                    showLog(TAG_EXCEPTION, e.toString());
                }
            }
        });
    }


    private void processMultiPartRequest(final String module, MultipartBody.Part multipartBody, HashMap<String, RequestBody> requestBody) {
        String token = "";
        Call<ResponseBody> call = null;
        token = WS_KEY_BEARER + sessionManager.getString(SessionManager.PREF_TOKEN);
        showLog("token", token);
        call = RetrofitBuilder.getWebService().postVideoThumbnail(token, module, requestBody, multipartBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dismissProgressDialog();
                try {
                    if (response.code() == 200) {
                        String res = response.body().string();
                        responseListener.onSuccess(res);
                        showLog("Response:", res);
                    } else if (response.code() == 401) {
                        TWorld.getInstance().getSession().setBoolean(SessionManager.PREF_IS_LOGIN, false);
                        TWorld.getInstance().getSession().clearAllData();
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);
                    } else {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            String userMessage = jsonObject.getString("message");
                            Utils.methodToast(context, userMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    showLog(TAG_EXCEPTION, e.toString());
                    ;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                try {
                    showLog(TAG_EXCEPTION, t.getMessage());
                    responseListener.onFailure(t.getMessage());
                } catch (Exception e) {
//                    showLogException(e);
                }
            }
        });
    }


}



