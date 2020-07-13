package com.semonics.tiktik.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.semonics.tiktik.Accounts.LoginActivity;
import com.semonics.tiktik.SimpleClasses.SessionManager;
import com.semonics.tiktik.SimpleClasses.TicTic;
import com.semonics.tiktik.SimpleClasses.Utils;
import com.semonics.tiktik.SimpleClasses.WSParams;

import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.semonics.tiktik.SimpleClasses.Utils.dismissProgressDialog;
import static com.semonics.tiktik.SimpleClasses.Utils.methodToast;
import static com.semonics.tiktik.SimpleClasses.Utils.showLog;
import static com.semonics.tiktik.SimpleClasses.Utils.showProgressDialog;
import static com.semonics.tiktik.SimpleClasses.WSParams.WS_KEY_CODE;


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

    /**
     * This is the constructor of the class      *      * @param context          of the class      * @param serviceName      name of the service      * @param body             request body of data      * @param responseListener is the listener of response      * @param isShowProgress   decides whether show progress bar or not
     */
    public BaseAPIService(final Context context, String serviceName, RequestBody body, ResponseListener responseListener, boolean isShowProgress) {
        this.context = context;
        this.responseListener = responseListener;
        this.isShowProgress = isShowProgress;
        if (Utils.isNetworkAvailable(this.context)) {
            if (this.isShowProgress) {
                showProgressDialog(this.context);
            }
            if (body == null) {
                processRequest(serviceName);
            } else {
                processRequest(serviceName, body);
            }
        } else {

//            Utils.showDialogWithOption(context, R.mipmap.ic_launcher, context.getResources().getString(R.string.internet_connection_error), "", context.getResources().getString(R.string.ok), "", new DialogYesNoListener() {
//                @Override
//                public void onClickYes() {
//
//                }
//
//                @Override
//                public void onClickNo() {
//
//                }
//            });
        }
    }

    private void processRequest(String serviceName, RequestBody body) {
        showLog(TAG_URL, TicTic.getInstance().getSession().getString(SessionManager.PREF_BASE_URL) + "/" + serviceName);

        RetrofitBuilder.getWebService(TicTic.getInstance().getSession().getString(SessionManager.PREF_BASE_URL)).doReqeust(serviceName, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dismissProgressDialog();
                try {
                    if (response.code() == 200) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        int code = jsonObject.getInt(WS_KEY_CODE);
                        if (code == 401) {
                            TicTic.getInstance().getSession().setBoolean(SessionManager.PREF_IS_LOGIN, false);
                            TicTic.getInstance().getSession().clearAllData();
                            Intent i = new Intent(context, LoginActivity.class);
                            context.startActivity(i);
                        }else if(code ==200){
                            responseListener.onSuccess(res);
                        }
                        showLog(TAG_RESPONSE, res);
                    } else {
                        responseListener.onFailure(response.code() + "");
//                        Utils.showDialogWithOption(context, R.mipmap.ic_launcher, context.getResources().getString(R.string.internet_connection_error), "", context.getResources().getString(R.string.ok), "", new DialogYesNoListener() {
//                            @Override
//                            public void onClickYes() {
//
//                            }
//
//                            @Override
//                            public void onClickNo() {
//
//                            }
//                        });
                    }
                } catch (Exception e) {
                    showLog(TAG_EXCEPTION,e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                try {
                    showLog(TAG_EXCEPTION, t.getMessage());
                    responseListener.onFailure(t.getMessage());
                } catch (Exception e) {
                    showLog(TAG_EXCEPTION,e.toString());
                }
            }
        });
    }

    private void processRequest(String serviceName) {
        showLog(TAG_URL, TicTic.getInstance().getSession().getString(SessionManager.PREF_BASE_URL) + "/" + serviceName);
        RetrofitBuilder.getWebService().doRequestWithoutBody(serviceName).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dismissProgressDialog();
                try {
                    if (response.code() == 200) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        int code = jsonObject.getInt("API_CODE");
                        if (code == 401) {
                            TicTic.getInstance().getSession().setBoolean(SessionManager.PREF_IS_LOGIN, false);
                            TicTic.getInstance().getSession().clearAllData();
                            Intent i = new Intent(context, LoginActivity.class);
                            context.startActivity(i);
                        }
                        showLog(TAG_RESPONSE, res);
                        responseListener.onSuccess(res);
                    } else {
                        methodToast(context, "No Internet Connection!!");

//                        Utils.showDialogWithOption(context, R.mipmap.ic_launcher, context.getResources().getString(R.string.internet_connection_error), "", context.getResources().getString(R.string.ok), "", new DialogYesNoListener() {
//                            @Override
//                            public void onClickYes() {
//
//                            }
//
//                            @Override
//                            public void onClickNo() {
//
//                            }
//                        });
                    }
                } catch (Exception e) {
                    showLog(TAG_EXCEPTION,e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               dismissProgressDialog();
                try {
                    showLog(TAG_EXCEPTION, t.getMessage());
                    responseListener.onFailure(t.getMessage());
                } catch (Exception e) {
                    showLog(TAG_EXCEPTION,e.toString());
                }
            }
        });
    }




}



