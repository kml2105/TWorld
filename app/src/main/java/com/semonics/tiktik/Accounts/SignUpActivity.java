package com.semonics.tiktik.Accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semonics.tiktik.Model.CountryModel;
import com.semonics.tiktik.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvLogIn,tvCountryName;
    private ImageView ivCountryImg;
    private LinearLayout llCountry;
    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        tvLogIn = findViewById(R.id.tv_log_in);
        llCountry = findViewById(R.id.activity_sign_up_ll_country);
        tvCountryName = findViewById(R.id.activity_sign_up_tv_country_name);
        ivCountryImg = findViewById(R.id.activity_sign_up_iv_country);
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
        }
    }
}
