package com.semonics.tiktik;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.semonics.tiktik.Accounts.LoginActivity;
import com.semonics.tiktik.Main_Menu.MainMenuActivity;
import com.semonics.tiktik.SimpleClasses.SessionManager;
import com.semonics.tiktik.SimpleClasses.TicTic;
import com.semonics.tiktik.SimpleClasses.Variables;

import static com.semonics.tiktik.SimpleClasses.SessionManager.PREF_IS_LOGIN;
import static com.semonics.tiktik.SimpleClasses.Utils.showLog;

public class SplashActivity extends AppCompatActivity {


    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);

        countDownTimer = new CountDownTimer(2500, 500) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                TicTic.getInstance().getSession().setBoolean(PREF_IS_LOGIN,true);
                if(TicTic.getInstance().getSession().getBoolean(SessionManager.PREF_IS_LOGIN)){
                    showLog("token:",TicTic.getInstance().getSession().getString(SessionManager.PREF_TOKEN));
                    Intent i = new Intent(SplashActivity.this, MainMenuActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                }else{
                    Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                }

            }
        }.start();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

}
