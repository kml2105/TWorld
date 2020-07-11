package com.semonics.tiktik.Accounts;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.semonics.tiktik.Comments.Comment_Get_Set;
import com.semonics.tiktik.Main_Menu.MainMenuActivity;
import com.semonics.tiktik.R;
import com.semonics.tiktik.SimpleClasses.API_CallBack;
import com.semonics.tiktik.SimpleClasses.ApiRequest;
import com.semonics.tiktik.SimpleClasses.Callback;
import com.semonics.tiktik.SimpleClasses.SessionManager;
import com.semonics.tiktik.SimpleClasses.TicTic;
import com.semonics.tiktik.SimpleClasses.Variables;
import com.semonics.tiktik.SimpleClasses.WSParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.semonics.tiktik.SimpleClasses.SessionManager.PREF_TOKEN;
import static com.semonics.tiktik.SimpleClasses.WSParams.WS_KEY_TOKEN;
import static com.semonics.tiktik.SimpleClasses.WSParams.WS_PASSWORD;
import static com.semonics.tiktik.SimpleClasses.WSParams.WS_USER_NAME;

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
                logInAPICall(this);
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

    public void logInAPICall(final Activity activity) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put(WS_USER_NAME, etUserName.getText().toString().trim());
            parameters.put(WS_PASSWORD,etPassword.getText().toString().trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity, Variables.logInAPI, parameters, new Callback() {
            @Override
            public void Responce(String resp) {

                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject response=new JSONObject(resp);
                    String token = response.getString(WS_KEY_TOKEN);
                    Log.e("token:",token);
                    sessionManager.putString(PREF_TOKEN,token);
                    Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                    if(getIntent().getExtras()!=null) {
                        i.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    finish();
                  /*  String code=response.optString("code");
                    if(code.equals("200")){


                    }else {
                        Toast.makeText(activity, ""+response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}