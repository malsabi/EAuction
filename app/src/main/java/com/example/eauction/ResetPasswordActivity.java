package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.eauction.Helpers.ResetPasswordHelper;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Validations.UserValidation;
import com.royrodriguez.transitionbutton.TransitionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPasswordActivity extends AppCompatActivity
{
    @BindView(R.id.EtEmailReset)
    EditText EmailEditText;

    @BindView(R.id.VerifyCodeBtn)
    TransitionButton SendCodeButton;

    private App AppInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        AppInstance = (App)getApplication();

        SendCodeButton.setOnClickListener(view ->
        {
            HandleResetPassword(EmailEditText.getText().toString());
        });
    }

    private void HandleResetPassword(String Email)
    {
        SendCodeButton.startAnimation();
        UserValidation Validator = new UserValidation();
        if (Email == null || Email.length() == 0)
        {
            SendCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            EmailEditText.setError("Email cannot be empty, please insert your email");
        }
        else
        {
            ValidationResult Result = Validator.EmailValidation(Email);
            if (Result.isSuccess())
            {
                AppInstance.GetFireStoreInstance().IsUserRegistered(IsRegistered ->
                {
                    if (IsRegistered)
                    {
                        SendCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, null);

                        String OTPCode = ResetPasswordHelper.GetOTPCode(6);

                        Intent VerifyCodeIntent = new Intent(ResetPasswordActivity.this, VerifyCodeActivity.class);
                        VerifyCodeIntent.putExtra("Code", OTPCode);
                        VerifyCodeIntent.putExtra("Email", Email);

                        Log.d("ResetPasswordActivity", "Code: " + OTPCode + " Email: " + Email);

                        startActivity(VerifyCodeIntent);
                        finish();
                    }
                    else
                    {
                        SendCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                        EmailEditText.setError("Email is not registered, please insert a registered email address.");
                    }
                }, Email, true);
            }
            else
            {
                SendCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                EmailEditText.setError(Result.getMessage());
            }
        }
    }
}