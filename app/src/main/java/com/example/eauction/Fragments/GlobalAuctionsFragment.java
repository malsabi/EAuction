package com.example.eauction.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class GlobalAuctionsFragment extends Fragment {

    @BindView(R.id.RvAucitonedGlobalProperties)
    public RecyclerView RvAucitonedGlobalProperties;

    private TelemetryAdapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_globalauctions,container,false);
        ButterKnife.bind(this,view);
        String auctionNeeded = this.getArguments().getString("AuctionNeeded");
        PopulateCards(auctionNeeded);

        return view;
    }

    private void PopulateCards(String auctionNeeded){
        if(auctionNeeded.equals("CarAuction")){
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show(); //Todo Sabi populate cards with auctioned cars

            layoutManager = new LinearLayoutManager(getContext());
            ArrayList<Telemetry> userTelemertries = DummyData.GetDummyItems();
            rvAdapter = new TelemetryAdapter(userTelemertries);
            RvAucitonedGlobalProperties.setLayoutManager(layoutManager);
            RvAucitonedGlobalProperties.setAdapter(rvAdapter);
        }
        else if(auctionNeeded.equals("LandmarkAuction")){
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();//Todo Sabi populate cards with auctioned landmarks
        }
        else if(auctionNeeded.equals("VipAuction")){
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();//Todo Sabi populate cards with auctioned Vip
        }
        else if(auctionNeeded.equals("PlateCodeAuction")){
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();//Todo Sabi populate cards with auctioned PlateCode
        }
        else if(auctionNeeded.equals("GeneralAuction")){
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();//Todo Sabi populate cards with auctioned General
        }
        else if(auctionNeeded.equals("ServiceAuction")){
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();//Todo Sabi populate cards with auctioned Service
        }
    }
}
