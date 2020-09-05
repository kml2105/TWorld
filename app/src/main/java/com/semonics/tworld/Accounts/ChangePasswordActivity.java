package com.semonics.tworld.Accounts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.semonics.tworld.Main_Menu.MainMenuActivity;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONObject;

import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_FORGOT_PW;
import static com.semonics.tworld.WebService.WSParams.SERVICE_UPDATE_PASSWORD;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_CODE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

public class ChangePasswordActivity extends AppCompatActivity {
private EditText etPw,etConfirmPw;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        etPw = findViewById(R.id.activity_change_pw_et_pw);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        etConfirmPw = findViewById(R.id.activity_change_pw_et_conf_pw);
        Button btnDone = findViewById(R.id.activity_change_pw_btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    public void validation(){
        try{
            if(etPw.getText().toString().isEmpty()){
                Utils.methodToast(this,"Please enter password.");
            }else if(etConfirmPw.getText().toString().isEmpty()){
                Utils.methodToast(this,"Please enter confirm password.");
            }else if(!etPw.getText().toString().equals(etConfirmPw.getText().toString())){
                Utils.methodToast(this,"Password does not match.");
            }else{
                apiCall();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void apiCall() {
        try {
            new BaseAPIService(this, SERVICE_UPDATE_PASSWORD, RequestParams.updatePassword(email,etConfirmPw.getText().toString().trim()), false, responseListener, METHOD_POST, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onSuccess(String res) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                int code = jsonObject.getInt(WS_KEY_CODE);
                if(code==200){
                    Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(ChangePasswordActivity.this, error);
        }
    };
}