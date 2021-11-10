package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eauction.Helpers.ResetPasswordHelper;
import com.github.gongw.VerifyCodeView;
import com.royrodriguez.transitionbutton.TransitionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyCodeActivity extends AppCompatActivity
{
    @BindView(R.id.EtOTPCode)
    VerifyCodeView OTPCodeEditText;

    @BindView(R.id.VerifyCodeBtn)
    TransitionButton VerifyCodeButton;

    @BindView(R.id.EtTimer)
    TextView EtTimer;

    private String OTPCode;
    private String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        ButterKnife.bind(this);

        App appInstance = (App) getApplication();

        Intent ResetPasswordIntent = getIntent();
        OTPCode = ResetPasswordIntent.getStringExtra("Code");
        Email = ResetPasswordIntent.getStringExtra("Email");

        //Send an email.
        String CompanyAccount = appInstance.GetFireStoreInstance().GetCompanyAccount();
        String CompanyPassword = appInstance.GetFireStoreInstance().GetCompanyPassword();

        Log.d("VerifyCodeActivity", "Company Account: " + CompanyAccount + " Company Password: " + CompanyPassword);

        ResetPasswordHelper.SendMessage(CompanyAccount, CompanyPassword, Email, "OTP CODE", "Your OTP Code is: " + OTPCode);

        VerifyCodeButton.setOnClickListener(view -> HandleVerifyCode(OTPCodeEditText.getVcText().toString()));

        startTimer();
    }

    private void HandleVerifyCode(String UserCode)
    {
        VerifyCodeButton.startAnimation();
        if (UserCode == null || UserCode.length() == 0)
        {
            Toast.makeText(this, "OTP Code cannot be empty, please insert a valid OTP Code.", Toast.LENGTH_SHORT).show();
            VerifyCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }
        else if (OTPCode == null || OTPCode.length() == 0)
        {
            Toast.makeText(VerifyCodeActivity.this, "Session is invalid, Please try again", Toast.LENGTH_SHORT).show();
            VerifyCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            finish();
        }
        else
        {
            if (OTPCode.equals(UserCode))
            {
                Intent DoublePasswordIntent = new Intent(VerifyCodeActivity.this, DoublePasswordActivity.class);
                DoublePasswordIntent.putExtra("Email", Email);
                startActivity(DoublePasswordIntent);
                Toast.makeText(VerifyCodeActivity.this, "OTP Success.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(this, "Invalid Code, please insert a valid OTP Code.", Toast.LENGTH_SHORT).show();
                VerifyCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            }
        }
    }

    private void startTimer()
    {
        new CountDownTimer(20000, 1000)
        {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished)
            {
                String RemainingSeconds = String.valueOf(millisUntilFinished / 1000);
                EtTimer.setText("Code expires in " + RemainingSeconds + " seconds");
            }
            public void onFinish()
            {
                OTPCode = null;
                finish();
                Toast.makeText(VerifyCodeActivity.this, "Session is finished, Please try again", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
}