package com.example.eauction.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eauction.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

public class VIPNumberFragment extends Fragment
{

    @BindView(R.id.SpinnerPhoneCompany)
    public MaterialSpinner SpinnerPhoneCompany;

    @BindView(R.id.EtPhoneNumberTel)
    public EditText PhoneNumber;

    private String LastChar = " ";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_vipnumber,container,false);
        ButterKnife.bind(this, view);
        String[] ITEMS = {"Etisalat","Du"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerPhoneCompany.setAdapter(adapter);
        SpinnerPhoneCompany.setSelection(0); //Default Position

        //region PhoneNumber TextWatcher
        PhoneNumber.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                int digits = PhoneNumber.getText().toString().length();
                if (digits > 1)
                {
                    LastChar = PhoneNumber.getText().toString().substring(digits-1);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int digits = PhoneNumber.getText().toString().length();
                if (!LastChar.equals("-"))
                {
                    if (digits == 3 || digits == 7)
                    {
                        PhoneNumber.append("-");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        //endregion
        return view;
    }
}