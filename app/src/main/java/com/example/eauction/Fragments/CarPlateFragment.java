package com.example.eauction.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.eauction.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CarPlateFragment extends Fragment {

    @BindView(R.id.SpinnerCountry)
    MaterialSpinner SpinnerCountry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carplate,container,false);
        ButterKnife.bind(this, view);
        String[] ITEMS = {"Abu Dhabi","Ajman", "Dubai","Fujairah","Ras Al Khaimah","Sharjah","Umm Al Quwain"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerCountry.setAdapter(adapter);
        SpinnerCountry.setSelection(0); //Default Position

        return view;
    }

}
