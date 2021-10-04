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

public class LandmarkFragment extends Fragment
{
    @BindView(R.id.EtLandmarkType)
    public EditText TypeEditText;

    @BindView(R.id.EtLandmarkLocation)
    public EditText LocationEditText;

    @BindView(R.id.EtLandmarkName)
    public EditText NameEditText;

    @BindView(R.id.EtLandmarkArea)
    public EditText AreaEditText;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_landmark,container,false);
        ButterKnife.bind(this, view);
        return view;
    }
}
