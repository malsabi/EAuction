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

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarFragment extends Fragment
{
    @BindView(R.id.EtCarModel)
    public EditText ModelEditText;

    @BindView(R.id.EtCarMileage)
    public EditText MileageEditText;

    @BindView(R.id.EtCarName)
    public EditText NameEditText;

    @BindView(R.id.EtCarHorsePower)
    public EditText HorsePowerEditText;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_car,container,false);
        ButterKnife.bind(this, view);
        return view;
    }
}
