package com.semonics.tiktik.Accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semonics.tiktik.Main_Menu.MainMenuActivity;
import com.semonics.tiktik.Model.CountryModel;
import com.semonics.tiktik.R;
import com.semonics.tiktik.SimpleClasses.SessionManager;
import com.semonics.tiktik.SimpleClasses.TicTic;
import com.semonics.tiktik.SimpleClasses.Utils;
import com.semonics.tiktik.WebService.BaseAPIService;
import com.semonics.tiktik.WebService.RequestParams;
import com.semonics.tiktik.WebService.ResponseListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.semonics.tiktik.SimpleClasses.SessionManager.PREF_IS_LOGIN;
import static com.semonics.tiktik.SimpleClasses.SessionManager.PREF_TOKEN;
import static com.semonics.tiktik.SimpleClasses.Utils.methodToast;
import static com.semonics.tiktik.SimpleClasses.WSParams.WS_KEY_TOKEN;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvLogIn,tvCountryName;
    private ImageView ivCountryImg;
    private LinearLayout llCountry;
    private EditText etUserName,etPw;
    private SessionManager sessionManager;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        tvLogIn = findViewById(R.id.tv_log_in);
        llCountry = findViewById(R.id.activity_sign_up_ll_country);
        tvCountryName = findViewById(R.id.activity_sign_up_tv_country_name);
        ivCountryImg = findViewById(R.id.activity_sign_up_iv_country);
        etUserName=findViewById(R.id.activity_sign_up_email);
        etPw=findViewById(R.id.activity_sign_up_password);
        sessionManager = TicTic.getInstance().getSession();
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);
        tvLogIn.setOnClickListener(this);
        llCountry.setOnClickListener(this);

    }

    private void showAlertDialog() {
        // Prepare grid view
        GridView gridView = new GridView(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final List<CountryModel> mList = new ArrayList<CountryModel>();
        CountryModel countryModel = new CountryModel();
        countryModel.setCountryName("India");
        countryModel.setCountryImg("https://mk0businessofem29abh.kinstacdn.com/wp-content/uploads/2020/03/photo-earth.jpg");
        mList.add(countryModel);


        gridView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList));
        gridView.setNumColumns(5);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // do something here
                tvCountryName.setText(mList.get(position).getCountryName());
                Picasso.with(SignUpActivity.this).load(mList.get(position).getCountryImg()).resize(400,400).centerCrop().into(ivCountryImg);

            }
        });

        // Set grid view to alertDialog

        builder.setView(gridView);
        builder.setTitle("Select Country");
        builder.show();
    }

    private void validation(){
        if(etUserName.getText().toString().isEmpty()){
            methodToast(SignUpActivity.this,"Please enter user name.");
        }else if(etPw.getText().toString().isEmpty()){
            methodToast(SignUpActivity.this,"Please enter password.");
        }else{
            apiCall();
        }
    }
    public void apiCall() {
        try {
            new BaseAPIService(SignUpActivity.this, "addUser", RequestParams.getLogin(etUserName.getText().toString().trim(), etPw.getText().toString().trim()), responseListener, true);
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
                Intent i = new Intent(SignUpActivity.this, MainMenuActivity.class);
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
            methodToast(SignUpActivity.this, "error");
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_log_in:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                if(getIntent().getExtras()!=null) {
                    intent.putExtras(getIntent().getExtras());
                    setIntent(null);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                finish();
                break;
            case R.id.activity_sign_up_ll_country:
                showAlertDialog();
                break;
            case R.id.btn_sign_up:
                validation();
                break;
        }
    }
}
