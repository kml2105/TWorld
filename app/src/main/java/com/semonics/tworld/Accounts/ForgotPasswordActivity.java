package com.semonics.tworld.Accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.semonics.tworld.Main_Menu.MainMenuActivity;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONObject;

import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_FORGOT_PW;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etEmail = findViewById(R.id.activity_forgot_pw_et_email);
        Button btnNext = findViewById(R.id.activity_forgot_pw_btn_next);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_forgot_pw_btn_next:
                if (etEmail.getText().toString().isEmpty()) {
                    Utils.methodToast(ForgotPasswordActivity.this, "Please enter email/ Phone no.");
                }else{
                    apiCall();
                }
                break;
        }
    }

    public void apiCall() {
        try {
            new BaseAPIService(this, SERVICE_FORGOT_PW, RequestParams.getForgotPw(etEmail.getText().toString().trim()), false, responseListener, METHOD_POST, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                JSONObject jsonObject1 = jsonObject.getJSONObject(WS_KEY_OBJ);
                Intent i = new Intent(ForgotPasswordActivity.this, MainMenuActivity.class);
                if (getIntent().getExtras() != null) {
                    i.putExtras(getIntent().getExtras());
                    setIntent(null);
                }
                startActivity(i);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(ForgotPasswordActivity.this, error);
        }
    };
}