package com.semonics.tworld.Accounts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.chaos.view.PinView;
import com.semonics.tworld.R;
import com.semonics.tworld.SimpleClasses.Utils;
import com.semonics.tworld.WebService.BaseAPIService;
import com.semonics.tworld.WebService.RequestParams;
import com.semonics.tworld.WebService.ResponseListener;

import org.json.JSONObject;

import static com.semonics.tworld.WebService.WSParams.METHOD_POST;
import static com.semonics.tworld.WebService.WSParams.SERVICE_ADD_USER;
import static com.semonics.tworld.WebService.WSParams.SERVICE_VERIFY_OTP;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_CODE;
import static com.semonics.tworld.WebService.WSParams.WS_KEY_OBJ;

public class OTPActivity extends AppCompatActivity {
    PinView pinView;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        pinView = findViewById(R.id.pinView);
        Intent intent = getIntent();
         email = intent.getExtras().getString("email");

    }

    public void apiCall() {
        try {
            new BaseAPIService(OTPActivity.this, SERVICE_VERIFY_OTP+pinView.getText().toString().trim(), RequestParams.getForgotPw(email), false, responseListener, METHOD_POST, true);
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
                    Intent i = new Intent(OTPActivity.this, ChangePasswordActivity.class);
                    if (getIntent().getExtras() != null) {
                        i.putExtras(getIntent().getExtras());
                        i.putExtra("email",email);
                        setIntent(null);
                    }
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String error) {
            Utils.methodToast(OTPActivity.this, error);
        }
    };
}