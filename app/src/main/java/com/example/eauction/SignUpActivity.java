package com.example.eauction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
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

    @BindView(R.id.EtPasswordSignupReapeat)
    EditText PasswordRepeatEditText;

    @BindView(R.id.EtDOBSignup)
    EditText DayOfBirthEditText;

    @BindView(R.id.MaleRadioBtn)
    RadioButton MaleRadioButton;

    @BindView(R.id.FemaleRadioBtn)
    RadioButton FemaleRadioButton;

    @BindView(R.id.EtIdSignup)
    EditText IdEditText;

    @BindView(R.id.BtnSignup)
    TransitionButton SignUpButton;
    //endregion

    private String LastChar = " ";
    private String LastCharId = " ";
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
                Log.d("SignUpActivity","OnTextChanged Text Length: " + digits);
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
            dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.PrimaryRedColor));
            dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.PrimaryRedColor));
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
        });
        //endregion
        //region Id TextWatcher
        IdEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                int digits = IdEditText.getText().toString().length();
                if (digits > 1)
                {
                    LastCharId = IdEditText.getText().toString().substring(digits - 1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int characters = IdEditText.getText().toString().length();
                if (!LastCharId.equals("-"))
                {
                    if (characters == 3 || characters == 8 || characters == 16)
                    {
                        IdEditText.append("-");
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
        TakeImageFromGallery();
        //TODO Image should uploaded of the user
    }
    private void HandleSignUpUser()
    {
        SignUpButton.startAnimation();
        if (PasswordRepeatEditText.getText().toString().equals(PasswordEditText.getText().toString()))
        {
            User UserObj = new User();
            UserObj.setFirstName(FirstNameEditText.getText().toString());
            UserObj.setLastName(LastNameEditText.getText().toString());
            UserObj.setPhoneNumber(PhoneNumberEditText.getText().toString());
            UserObj.setPassword(PasswordEditText.getText().toString());
            UserObj.setEmail(EmailEditText.getText().toString());
            UserObj.setDate(DayOfBirthEditText.getText().toString());
            UserObj.setGender(MaleRadioButton.isChecked() ? "Male" : "Female");
            UserObj.setId(IdEditText.getText().toString());
            UserObj.setIsActive("Offline");

            AppInstance.GetFireStoreInstance().RegisterUser(Result ->
            {
                if (Result.isSuccess())
                {
                    Toast.makeText(SignUpActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                }
                else
                {
                    int resID = getResources().getIdentifier(Result.getTitle(), "id", getPackageName());
                    if (resID != 0)
                    {
                        EditText InvalidControl = findViewById(resID);
                        InvalidControl.setError(Result.getMessage());
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("SignUpActivity", "HandleSignUpUser Finished from registerUser");
                SignUpButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            }, UserObj);
        }
        else
        {
            PasswordRepeatEditText.setError("Password is not same, make sure you to enter the same password");
            SignUpButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }
    }
    private void TakeImageFromGallery()
    {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                assert data != null;
                Uri selectedImageUri = data.getData();
                ProfileImage.setImageURI(selectedImageUri);
            }
        }
    }
}