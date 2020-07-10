package com.semonics.tiktik.Accounts;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.semonics.tiktik.Main_Menu.MainMenuActivity;
import com.semonics.tiktik.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btn_log_in);
        tvSignUp = findViewById(R.id.tv_sign_up);
        tvSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_log_in:
                Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                if(getIntent().getExtras()!=null) {
                    i.putExtras(getIntent().getExtras());
                    setIntent(null);
                }
                startActivity(i);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                finish();
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
}