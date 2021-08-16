package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity
{

    @BindView(R.id.EtPhoneNumberSignup)
    EditText PhoneNumberEt;

    @BindView(R.id.EtDOBSignup)
    EditText EtDOBSignup;

    @BindView(R.id.EtSSNSignup)
    EditText EtSSNSignup;

    String lastChar = " ";
    String lastCharSSN = " ";
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        //region PhoneNumber TextWatcher
        PhoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                int digits = PhoneNumberEt.getText().toString().length();
                if (digits > 1)
                {
                    lastChar = PhoneNumberEt.getText().toString().substring(digits-1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int digits = PhoneNumberEt.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastChar.equals("-"))
                {
                    if (digits == 3 || digits == 7)
                    {
                        PhoneNumberEt.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        //endregion

        //region DatePicker Event DOB
        myCalendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        EtDOBSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatePickerDialog dp = new DatePickerDialog(SignUpActivity.this, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
                dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.WHITE);
                dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
            }
        });
        //endregion

        //region Textwatcher for the SSN 784-1998-XXXXXXXX
        EtSSNSignup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                int digits = EtSSNSignup.getText().toString().length();
                if (digits > 1)
                {
                    lastCharSSN = EtSSNSignup.getText().toString().substring(digits - 1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int digits = EtSSNSignup.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!lastCharSSN.equals("-"))
                {
                    if (digits == 3 || digits == 8|| digits == 16)
                    {
                        EtSSNSignup.append("-");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        //endregion
    }

    private void updateLabel()
    {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        EtDOBSignup.setText(sdf.format(myCalendar.getTime()));
    }
}

