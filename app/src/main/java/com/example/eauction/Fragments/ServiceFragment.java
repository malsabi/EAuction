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

public class ServiceFragment extends Fragment
{
    @BindView(R.id.EtServiceName)
    public EditText ServiceNameEditText;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_service,container,false);
        ButterKnife.bind(this, view);
        return view;
    }
}