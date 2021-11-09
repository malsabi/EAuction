package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.QuickContactBadge;
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

    private App AppInstance;
    private String OTPCode;
    private String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        ButterKnife.bind(this);

        AppInstance = (App)getApplication();

        Intent ResetPasswordIntent = getIntent();
        OTPCode = ResetPasswordIntent.getStringExtra("Code");
        Email = ResetPasswordIntent.getStringExtra("Email");

        //Send an email.
        String CompanyAccount = AppInstance.GetFireStoreInstance().GetCompanyAccount();
        String CompanyPassword = AppInstance.GetFireStoreInstance().GetCompanyPassword();

        Log.d("VerifyCodeActivity", "Company Account: " + CompanyAccount + " Company Password: " + CompanyPassword);

        ResetPasswordHelper.SendMessage(CompanyAccount, CompanyPassword, Email, "OTP CODE", "Your OTP Code is: " + OTPCode);

        VerifyCodeButton.setOnClickListener(view -> HandleVerifyCode(OTPCodeEditText.getVcText().toString()));
        startTimer(60000, 1000);
    }

    private void HandleVerifyCode(String UserCode)
    {
        VerifyCodeButton.startAnimation();
        if (UserCode == null || UserCode.length() == 0)
        {
            //OTPCodeEditText.setError("OTP Code cannot be empty, please insert a valid OTP Code.");
            Toast.makeText(this, "OTP Code cannot be empty, please insert a valid OTP Code.", Toast.LENGTH_SHORT).show();
            VerifyCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
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
                //OTPCodeEditText.setError("Invalid Code, please insert a valid OTP Code.");
                Toast.makeText(this, "Invalid Code, please insert a valid OTP Code.", Toast.LENGTH_SHORT).show();
                VerifyCodeButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            }
        }
    }
    //Helper Timer Button
    private void startTimer(final long finish, long tick) {
        CountDownTimer t;
        t = new CountDownTimer(finish, tick) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                String remainedSecs = String.valueOf(millisUntilFinished / 1000);
                EtTimer.setText("Code expires in "+remainedSecs+" seconds");
            }

            public void onFinish() { //TODO This is the event for the timer
                Toast.makeText(VerifyCodeActivity.this, "Timer is over", Toast.LENGTH_SHORT).show();
                cancel();
            }
        }.start();
    }
}