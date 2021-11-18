package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eauction.Interfaces.SetUserIsActiveCallback;
import com.example.eauction.Interfaces.SignInUserCallback;
import com.example.eauction.Models.SessionModel;
import com.example.eauction.Models.SignIn;
import com.example.eauction.Utilities.PreferenceUtils;
import com.royrodriguez.transitionbutton.TransitionButton;

import java.time.LocalDateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity
{
    @BindView(R.id.LoginButton)
    TransitionButton SignInButton;
    @BindView(R.id.EtEmailLogin)
    EditText EmailEditText;
    @BindView(R.id.EtPasswordLogin)
    EditText PasswordEditText;
    @BindView(R.id.TvSignUp)
    TextView signUpTextView;
    @BindView(R.id.TvResetPassword)
    TextView TvResetPassword;

    private App AppInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AppInstance = (App)getApplication();

        CheckIsUserSignedIn();

        SignInButton.setOnClickListener(v ->
        {
            OnSignInClick();
        });
        signUpTextView.setOnClickListener(v ->
        {
            OnSignUpClick();
        });
        TvResetPassword.setOnClickListener(v -> {
            startActivity(new Intent(this,ResetPasswordActivity.class));
        });
    }

    private void CheckIsUserSignedIn()
    {
        if (PreferenceUtils.getEmail(this) != null && PreferenceUtils.getPassword(this) != null)
        {
            AppInstance.GetTelemetryMonitor().StartMonitor(PreferenceUtils.getEmail(this));
            Intent IntentObj = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(IntentObj);
            finish();
        }
    }

    private void OnSignInClick()
    {
        SignInButton.startAnimation();

        String Email = EmailEditText.getText().toString();
        String Password = PasswordEditText.getText().toString();

        SignIn SignInObj = new SignIn(Email, Password);
        Log.d("LoginActivity", "OnSignInClick Starting SignIn function");

        AppInstance.GetFireStoreInstance().SignInUser((Result, UserObj) ->
        {
            if (Result.isSuccess())
            {
                Toast.makeText(LoginActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();
                AppInstance.GetTelemetryMonitor().StartMonitor(UserObj.getEmail());
                AddUserSession(UserObj.getEmail(), UserObj.getIsActive());
                PreferenceUtils.saveEmail(UserObj.getEmail(), this);
                PreferenceUtils.savePassword(UserObj.getPassword(), this);
                Intent I = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(I);
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
                    Toast.makeText(LoginActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            Log.d("LoginActivity", "OnSignInClick Finished from SignIn Function");
            SignInButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }, SignInObj);
    }

    private void OnSignUpClick()
    {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    private void AddUserSession(String UserId, String IsActive)
    {
        SessionModel Session = new SessionModel();
        Session.setUserId(UserId);
        Session.setIsActive(IsActive);
        Session.setLastSeen("N/A");
        AppInstance.GetSessionManagement().AddSession(Session);
    }
}