package com.example.eauction.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eauction.R;
import com.royrodriguez.transitionbutton.TransitionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsFragment extends Fragment {

    @BindView(R.id.EtFullName)
    EditText EtFullName;

    @BindView(R.id.EtEmail)
    EditText EtEmail;

    @BindView(R.id.EtPhoneNumber)
    EditText EtPhoneNumber;

    @BindView(R.id.EtMessageBox)
    EditText EtMessageBox;

    @BindView(R.id.BtnSendMessage)
    TransitionButton BtnSendMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactus,container,false);
        ButterKnife.bind(this,view);


        return view;
    }
}
