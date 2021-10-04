package com.example.eauction.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eauction.Adapters.TelemetryAdapter;
import com.example.eauction.DummyData.DummyData;
import com.example.eauction.InsertActivity;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

public class MyPropertiesFragment extends Fragment {

    @BindView(R.id.RvTelemetry)
    public RecyclerView RvTelemetry;

    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private User UserObj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myproperties,container,false);
        ButterKnife.bind(this,view);
        ArrayList<? extends Telemetry> telemetriesDummy = UserObj.getOwnedCarPlateTelemetry();
        layoutManager = new LinearLayoutManager(getContext());
        rvAdapter = new TelemetryAdapter((ArrayList<Telemetry>) telemetriesDummy);
        RvTelemetry.setLayoutManager(layoutManager);
        RvTelemetry.setAdapter(rvAdapter);
        return view;
    }

    public void SetUser(User UserObj)
    {
        this.UserObj = UserObj;
    }
}
