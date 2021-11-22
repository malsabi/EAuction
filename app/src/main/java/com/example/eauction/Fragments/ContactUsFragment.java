package com.example.eauction.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eauction.App;
import com.example.eauction.Helpers.ContactUsHelper;
import com.example.eauction.MainActivity;
import com.example.eauction.R;
import com.royrodriguez.transitionbutton.TransitionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsFragment extends Fragment
{
    @BindView(R.id.EtFullName)
    EditText FullNameEditText;

    @BindView(R.id.EtEmail)
    EditText EmailEditText;

    @BindView(R.id.EtPhoneNumber)
    EditText PhoneNumberEditText;

    @BindView(R.id.EtMessageBox)
    EditText MessageBoxEditText;

    @BindView(R.id.BtnSendMessage)
    TransitionButton SendMessageButton;

    private App AppInstance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_contactus,container,false);
        ButterKnife.bind(this,view);

        AppInstance = (App) this.requireActivity().getApplication();

        SendMessageButton.setOnClickListener(View ->
        {
            String FullName = FullNameEditText.getText().toString();
            String Email = EmailEditText.getText().toString();
            String PhoneNumber = PhoneNumberEditText.getText().toString();
            String MessageBox = MessageBoxEditText.getText().toString();
            HandleSendMessage(FullName, Email, PhoneNumber, MessageBox);
        });

        return view;
    }

    private void HandleSendMessage(String FullName, String EmailAddress, String PhoneNumber, String MessageBox)
    {
        SendMessageButton.startAnimation();
        String CompanyEmail = AppInstance.GetFireStoreInstance().GetCompanyAccount();
        String CompanyPassword = AppInstance.GetFireStoreInstance().GetCompanyPassword();
        String Title = "Contact Us Message";
        String Message = String.format("USER MESSAGE\n\nFull Name: %s\nPhone Number: %s\nEmail Address: %s\nMessage: %s", FullName, PhoneNumber, EmailAddress, MessageBox);
        ContactUsHelper.SendMessage(CompanyEmail, CompanyPassword, EmailAddress, Title, Message);
        Toast.makeText(this.requireActivity(), "Message Sent successfully.", Toast.LENGTH_SHORT).show();
        SendMessageButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND,null);
        startActivity(new Intent(this.requireActivity(), MainActivity.class));
    }
}