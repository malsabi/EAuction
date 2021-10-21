package com.example.eauction.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eauction.Adapters.TelemetryAdapter;
import com.example.eauction.DummyData.DummyData;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuctionsFragment extends Fragment {

    @BindView(R.id.RvMyAuctionsTelemetry)
    public RecyclerView RvMyAuctionsTelemetry;

    private TelemetryAdapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_auctions,container,false);
        ButterKnife.bind(this,view);
        layoutManager = new LinearLayoutManager(getContext());
        ArrayList<Telemetry> userTelemertries = DummyData.GetDummyItems(); //Todo Sabi here you should add what the user is auctioning
        rvAdapter = new TelemetryAdapter(userTelemertries);
        RvMyAuctionsTelemetry.setLayoutManager(layoutManager);
        RvMyAuctionsTelemetry.setAdapter(rvAdapter);
        return view;
    }
}
