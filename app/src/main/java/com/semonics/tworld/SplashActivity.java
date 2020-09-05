package com.semonics.tworld;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.semonics.tworld.Accounts.LoginActivity;
import com.semonics.tworld.Main_Menu.MainMenuActivity;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;
import com.semonics.tworld.WebService.SessionManager;
import com.semonics.tworld.WebService.TWorld;

import org.json.JSONObject;

import okhttp3.RequestBody;

import static com.semonics.tworld.SimpleClasses.Utils.methodToast;
import static com.semonics.tworld.SimpleClasses.Utils.showLog;
import static com.semonics.tworld.WebService.SessionManager.PREF_FCM_TOKEN;
import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_ADD_DEVICE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_CODE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_MSG;

public class SplashActivity extends AppCompatActivity {


    CountDownTimer countDownTimer;
    ProgressBar progressBar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        String token = FirebaseInstanceId.getInstance().getToken();
        progressBar = findViewById(R.id.activity_splash_progress_bar);
        sessionManager = TWorld.getInstance().getSession();
        sessionManager.putString(PREF_FCM_TOKEN, token);
        countDownTimer = new CountDownTimer(2500, 500) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                apiCall();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    public void apiCall() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            new BaseAPIService(this, SERVICE_ADD_DEVICE, RequestParams.likeVideo(), false, responseListener, METHOD_POST, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);
                String msg = jsonObject.getString(WS_KEY_MSG);
                if (code == 200) {
                    if (TWorld.getInstance().getSession().getBoolean(SessionManager.PREF_IS_LOGIN)) {
                        showLog("token:", TWorld.getInstance().getSession().getString(SessionManager.PREF_TOKEN));
                        Intent i = new Intent(SplashActivity.this, MainMenuActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        finish();
                    }
                } else {
                    methodToast(SplashActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            progressBar.setVisibility(View.GONE);
            Utils.methodToast(SplashActivity.this, error);
        }
    };

}