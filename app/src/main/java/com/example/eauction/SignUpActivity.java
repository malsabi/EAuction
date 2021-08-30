package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.eauction.Interfaces.RegisterUserCallback;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.User;
import com.royrodriguez.transitionbutton.TransitionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity
{
    //region Binding Controls
    @BindView(R.id.ProfileImage)
    ImageView ProfileImage;

    @BindView(R.id.EtFirstNameSignup)
    EditText FirstNameEditText;

    @BindView(R.id.EtLastNameSignup)
    EditText LastNameEditText;

    @BindView(R.id.EtEmailSignup)
    EditText EmailEditText;

    @BindView(R.id.EtPhoneNumberSignup)
    EditText PhoneNumberEditText;

    @BindView(R.id.EtPasswordSignup)
    EditText PasswordEditText;

    @BindView(R.id.EtDOBSignup)
    EditText DayOfBirthEditText;

    @BindView(R.id.MaleRadioBtn)
    RadioButton MaleRadioButton;

    @BindView(R.id.FemaleRadioBtn)
    RadioButton FemaleRadioButton;

    @BindView(R.id.EtSSNSignup)
    EditText SSNEditText;

    @BindView(R.id.BtnSignup)
    TransitionButton SignUpButton;
    //endregion

    private String LastChar = " ";
    private String LastCharSSN = " ";
    private Calendar MyCalendar = null;
    private App AppInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        AppInstance = (App)getApplication();

        //region PhoneNumber TextWatcher
        PhoneNumberEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                int digits = PhoneNumberEditText.getText().toString().length();
                if (digits > 1)
                {
                    LastChar = PhoneNumberEditText.getText().toString().substring(digits-1);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int digits = PhoneNumberEditText.getText().toString().length();
                Log.d("LENGTH",""+digits);
                if (!LastChar.equals("-"))
                {
                    if (digits == 3 || digits == 7)
                    {
                        PhoneNumberEditText.append("-");
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
        MyCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) ->
        {
            MyCalendar.set(Calendar.YEAR, year);
            MyCalendar.set(Calendar.MONTH, monthOfYear);
            MyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            UpdateLabel();
        };
        DayOfBirthEditText.setOnClickListener(v ->
        {
            DatePickerDialog dp = new DatePickerDialog(SignUpActivity.this, R.style.DialogTheme, date, MyCalendar.get(Calendar.YEAR), MyCalendar.get(Calendar.MONTH), MyCalendar.get(Calendar.DAY_OF_MONTH));
            dp.show();
            dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.WHITE);
            dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
        });
        //endregion
        //region SSN TextWatcher
        SSNEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                int digits = SSNEditText.getText().toString().length();
                if (digits > 1)
                {
                    LastCharSSN = SSNEditText.getText().toString().substring(digits - 1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int characters = SSNEditText.getText().toString().length();
                if (!LastCharSSN.equals("-"))
                {
                    if (characters == 3 || characters == 6)
                    {
                        SSNEditText.append("-");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        //endregion

        ProfileImage.setOnClickListener(v ->
        {
            HandleUserImage();
        });
        SignUpButton.setOnClickListener(v ->
        {
            HandleSignUpUser();
        });
    }
    private void UpdateLabel()
    {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DayOfBirthEditText.setText(sdf.format(MyCalendar.getTime()));
    }
    private void HandleUserImage()
    {
    }
    private void HandleSignUpUser()
    {
        SignUpButton.startAnimation();

        User UserObj = new User();
        UserObj.setFirstName(FirstNameEditText.getText().toString());
        UserObj.setLastName(LastNameEditText.getText().toString());
        UserObj.setPhoneNumber(PhoneNumberEditText.getText().toString());
        UserObj.setPassword(PasswordEditText.getText().toString());
        UserObj.setEmail(EmailEditText.getText().toString());
        UserObj.setDate(DayOfBirthEditText.getText().toString());
        UserObj.setGender(MaleRadioButton.isChecked() ? "Male" : "Female");
        UserObj.setSsn(SSNEditText.getText().toString());

        AppInstance.GetFireStoreInstance().RegisterUser(new RegisterUserCallback()
        {
            @Override
            public void onCallback(FireStoreResult Result)
            {
                if (Result.isSuccess()) {
                    Toast.makeText(SignUpActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                }
                else
                    {
                    int resID = getResources().getIdentifier(Result.getTitle(), "id", getPackageName());
                    if (resID != 0) {
                        EditText InvalidControl = findViewById(resID);
                        InvalidControl.setError(Result.getMessage());
                    } else
                        {
                        Toast.makeText(SignUpActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("TAG", "Finished from registerUser");
                SignUpButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            }
        }, UserObj);
    }
}