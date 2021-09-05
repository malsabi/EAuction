package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Interfaces.SignInUserCallback;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.User;
import com.example.eauction.Utilities.PreferenceUtils;
import com.google.gson.Gson;
import com.royrodriguez.transitionbutton.TransitionButton;

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
    }

    private void CheckIsUserSignedIn()
    {
        if (PreferenceUtils.getEmail(this) != null && PreferenceUtils.getPassword(this) != null)
        {
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
        Log.d("TAG", "Starting SignIn function");

        AppInstance.GetFireStoreInstance().SignInUser((SignInUserCallback) (Result, UserObj) ->
        {
            if (Result.isSuccess())
            {
                Toast.makeText(LoginActivity.this, Result.getMessage(), Toast.LENGTH_SHORT).show();

                PreferenceUtils.saveEmail(UserObj.getEmail(), this);
                PreferenceUtils.savePassword(UserObj.getPassword(), this);

                Gson gson = new Gson();
                String myJson = gson.toJson(UserObj);
                Log.d("JSON", myJson);
                Intent I = new Intent(LoginActivity.this, MainActivity.class);
                I.putExtra("UserObject", myJson);

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
            Log.d("TAG", "Finished from SignIn Function");
            SignInButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }, SignInObj);
    }

    private void OnSignUpClick()
    {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
}