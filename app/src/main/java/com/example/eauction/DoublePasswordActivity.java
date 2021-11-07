package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.example.eauction.Interfaces.UpdateUserPasswordCallback;
import com.royrodriguez.transitionbutton.TransitionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoublePasswordActivity extends AppCompatActivity
{
    @BindView(R.id.EtResetPassword)
    EditText PasswordEditText;

    @BindView(R.id.EtResetPasswordAgain)
    EditText PasswordAgainEditText;

    @BindView(R.id.ConfirmPasswordBtn)
    TransitionButton ConfirmPasswordButton;

    private String Email;
    private App AppInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_password);
        ButterKnife.bind(this);

        AppInstance = (App)getApplication();

        Intent VerifyCodeIntent = getIntent();

        Email = VerifyCodeIntent.getStringExtra("Email");

        ConfirmPasswordButton.setOnClickListener(view -> HandleConfirmPassword(Email, PasswordEditText.getText().toString(), PasswordAgainEditText.getText().toString()));
    }

    private void HandleConfirmPassword(String Email, String Password, String PasswordAgain)
    {
        ConfirmPasswordButton.startAnimation();
        if (Password == null || Password.length() == 0)
        {
            PasswordEditText.setError("Password cannot be empty, please insert a valid password");
            ConfirmPasswordButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }
        else if (PasswordAgain == null || PasswordAgain.length() == 0)
        {
            PasswordAgainEditText.setError("Password cannot be empty, please insert a valid password");
            ConfirmPasswordButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
        }
        else
        {
            if (Password.equals(PasswordAgain))
            {
                AppInstance.GetFireStoreInstance().UpdateUserPassword(IsPasswordUpdated ->
                {
                    if (IsPasswordUpdated)
                    {
                        ConfirmPasswordButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, null);
                        startActivity(new Intent(DoublePasswordActivity.this, LoginActivity.class));
                        Toast.makeText(DoublePasswordActivity.this, "Password Updated Successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(DoublePasswordActivity.this, "Could not update the password.", Toast.LENGTH_SHORT).show();
                        ConfirmPasswordButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    }
                }, Email, Password, true);
            }
            else
            {
                PasswordAgainEditText.setError("Password is not same, make sure you to enter the same password");
                ConfirmPasswordButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            }
        }
    }
}