package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.royrodriguez.transitionbutton.TransitionButton;

public class LoginActivity extends AppCompatActivity
{
    private TransitionButton transitionButton;
    private EditText EmailEditText;
    private EditText PasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailEditText = findViewById(R.id.EtEmailLogin);
        PasswordEditText = findViewById(R.id.EtPasswordLogin);

        transitionButton = findViewById(R.id.transition_button);
        transitionButton.setOnClickListener(view ->
                OnSignInClick()
        );
    }

    private void OnSignInClick()
    {
        transitionButton.startAnimation();
        String Email = EmailEditText.getText().toString();
        String Password = PasswordEditText.getText().toString();

        transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener()
        {
            @Override
            public void onAnimationStopEnd()
            {

            }
        });
    }
}