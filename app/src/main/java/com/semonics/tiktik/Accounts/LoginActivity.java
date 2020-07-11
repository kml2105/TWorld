package com.semonics.tiktik.Accounts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.semonics.tiktik.Main_Menu.MainMenuActivity;
import com.semonics.tiktik.R;
import com.semonics.tiktik.SimpleClasses.SessionManager;
import com.semonics.tiktik.SimpleClasses.TicTic;
import com.semonics.tiktik.SimpleClasses.Utils;
import com.semonics.tiktik.WebService.BaseAPIService;
import com.semonics.tiktik.WebService.RequestParams;
import com.semonics.tiktik.WebService.ResponseListener;

import org.json.JSONObject;

import static com.semonics.tiktik.SimpleClasses.SessionManager.PREF_IS_LOGIN;
import static com.semonics.tiktik.SimpleClasses.SessionManager.PREF_TOKEN;
import static com.semonics.tiktik.SimpleClasses.WSParams.WS_KEY_TOKEN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvSignUp;
    private SessionManager sessionManager;
    EditText etUserName,etPassword;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.activity_log_in_btn_log_in);
        tvSignUp = findViewById(R.id.tv_sign_up);
        etUserName = findViewById(R.id.activity_log_in_et_user_name);
        etPassword = findViewById(R.id.activity_log_in_et_password);
        sessionManager = TicTic.getInstance().getSession();
        progressBar=findViewById(R.id.progress_bar);
        tvSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_log_in_btn_log_in:
                apiCall();
                break;

            case R.id.tv_sign_up:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                if(getIntent().getExtras()!=null) {
                    intent.putExtras(getIntent().getExtras());
                    setIntent(null);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
                break;
        }
    }

    public void apiCall() {
        try {
            new BaseAPIService(LoginActivity.this, "authenticate", RequestParams.getLogin(etUserName.getText().toString().trim(), etPassword.getText().toString().trim()), responseListener, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
               // int code = jsonObject.getInt(API_CODE);
               // String msg = jsonObject.getString(API_MSG);
                //if (code == 200) {
                    String token = jsonObject.getString(WS_KEY_TOKEN);
                    Log.e("token:",token);
                    sessionManager.putString(PREF_TOKEN,token);
                    sessionManager.setBoolean(PREF_IS_LOGIN,true);
                    Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                    if(getIntent().getExtras()!=null) {
                        i.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
               /* } else {
                    methodToast(context, msg);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(LoginActivity.this, "error");
        }
    };

}