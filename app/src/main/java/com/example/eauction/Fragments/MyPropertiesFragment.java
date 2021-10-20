package com.example.eauction.Fragments;

import android.content.Intent;
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
import com.royrodriguez.transitionbutton.TransitionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

public class MyPropertiesFragment extends Fragment {

    @BindView(R.id.RvTelemetry)
    public RecyclerView RvTelemetry;

    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.BtnAddTelemetryInsertActivity)
    TransitionButton BtnAddTelemetryInsertActivity;

    private User UserObj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myproperties,container,false);
        ButterKnife.bind(this,view);
        ArrayList<? extends Telemetry> telemetriesDummy = UserObj.getOwnedCarPlateTelemetry();
        layoutManager = new LinearLayoutManager(getContext());
        rvAdapter = new TelemetryAdapter(Merge(UserObj.getOwnedCarPlateTelemetry(), UserObj.getOwnedCarTelemetry(), UserObj.getOwnedLandmarkTelemetry(), UserObj.getOwnedVipPhoneTelemetry(), UserObj.getOwnedGeneralTelemetry()));
        RvTelemetry.setLayoutManager(layoutManager);
        RvTelemetry.setAdapter(rvAdapter);

        //region EventListener
        BtnAddTelemetryInsertActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),InsertActivity.class));
            }
        });
        //endregion

        return view;
    }
    @SafeVarargs
    private ArrayList<Telemetry> Merge(@NonNull ArrayList<? extends Telemetry>...args)
    {
        ArrayList<Telemetry> returnTelemetries = new ArrayList<>();
        for (ArrayList<? extends Telemetry> arg: args)
        {
            if(arg != null && !arg.isEmpty())
                returnTelemetries.addAll(arg);
        }
        return returnTelemetries;
    }

    public void SetUser(User UserObj)
    {
        this.UserObj = UserObj;
    }
}
