package com.semonics.tiktik.Accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.semonics.tiktik.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvLogIn;
    EditText etBirthday;
    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        tvLogIn = findViewById(R.id.tv_log_in);
        etBirthday = findViewById(R.id.activity_sign_up_et_birthday);
        setDateTimeField();
        tvLogIn.setOnClickListener(this);
        etBirthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

    }
    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                etBirthday.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

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

        }
    }
}
