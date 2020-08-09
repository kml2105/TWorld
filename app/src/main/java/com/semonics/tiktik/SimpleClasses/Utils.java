package com.semonics.tiktik.SimpleClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.semonics.tiktik.R;
import com.semonics.tiktik.WebService.SessionManager;
import com.semonics.tiktik.WebService.TicTic;
import com.semonics.tiktik.WebService.WSParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.semonics.tiktik.WebService.BaseAPIService.TAG_EXCEPTION;

public class Utils {
    static ProgressDialog pd;

    /**
     * Method to show anything in log      *      * @param tag     is the representation of exception      * @param message is the exception cause
     */
    public static void showLog(String tag, String message) {
        if (message != null) {
            Log.e(tag, message);
        } else {
            Log.e(tag, "Null value receive");
        }
    }

    /**
     * Method to show anything in log      *      * @param context context of class      * @param text    msg display on toast
     */
    public static String getDeviceId(final Context mContext) {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    /**
     * Method to show anything in log      *      * @param context context of class      * @param text    msg display on toast
     */
    public static void methodToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * This method is for hide the keyboard      *      * @param view     view of class      * @param activity activity of class
     */
    public static void hideSoftKeyboard(View view, Context activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String parseDate(String format, String date) throws Exception {
        java.text.DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date1 = dateFormat.parse(date);
        //19 Jan, 2019
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return spf.format(date1);
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean outcome = false;
        if (context != null) {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            for (NetworkInfo tempNetworkInfo : networkInfos) {
                if (tempNetworkInfo != null && tempNetworkInfo.isConnected()) {
                    outcome = true;
                    break;
                }
            }
        }
        return outcome;
    }


    public static Map<String, String> getHeaders(Context context) {
        Map<String, String> headersMap = new HashMap<>();
        try {
            headersMap.put("Content-Type", "application/json");
            headersMap.put("app-type", "petrocard_pos");
            String authKey = TicTic.getInstance().getSession().getString(SessionManager.PREF_TOKEN);
            if (authKey != null && !authKey.isEmpty()) {
                headersMap.put("Authorization", authKey);
                showLog("auth_key : ", authKey);
            }
            String str = headersMap.toString();
            showLog("Headers : ", str);
        } catch (Exception e) {
            showLog("e", e.toString());
        }
        return headersMap;
    }

    public static void showProgressDialog(Context context) {
        try {
            pd = new ProgressDialog(context, R.style.MyTheme);
            pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pd.setCancelable(false);
            if (!((Activity) context).isFinishing()) {
                pd.show();
            }
        } catch (Exception e) {
            showLog(TAG_EXCEPTION, e.toString());
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
            showLog(TAG_EXCEPTION, e.toString());
        }
    }

}

