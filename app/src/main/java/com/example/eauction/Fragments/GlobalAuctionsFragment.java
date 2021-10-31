package com.example.eauction.Fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.eauction.App;
import com.example.eauction.DummyData.DummyData;
import com.example.eauction.Enums.StatusEnum;
import com.example.eauction.Interfaces.GetCarPlateAuctionsCallback;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.R;
import com.example.eauction.Utilities.PreferenceUtils;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlobalAuctionsFragment extends Fragment {

    @BindView(R.id.RvAucitonedGlobalProperties)
    public RecyclerView RvAuctionedGlobalProperties;

    private TelemetryAdapter RecyclerViewAdapter;
    private RecyclerView.LayoutManager LayoutManager;
    private App AppInstance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_globalauctions,container,false);
        ButterKnife.bind(this,view);
        AppInstance = (App) this.requireActivity().getApplication();

        String auctionNeeded = this.requireArguments().getString("AuctionNeeded");
        PopulateCards(auctionNeeded);

        return view;
    }

    private void PopulateCards(String auctionNeeded)
    {
        if(auctionNeeded.equals("PlateCodeAuction"))
        {
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();
            AppInstance.GetFireStoreInstance().GetCarPlateAuctions(Result ->
            {

                if (Result != null)
                {
                    LayoutManager = new LinearLayoutManager(getContext());
                    RecyclerViewAdapter = new TelemetryAdapter(Merge(Result));
                    RvAuctionedGlobalProperties.setLayoutManager(LayoutManager);
                    RvAuctionedGlobalProperties.setAdapter(RecyclerViewAdapter);
                }
            });
        }
        else if(auctionNeeded.equals("CarAuction"))
        {
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();
            AppInstance.GetFireStoreInstance().GetCarAuctions(Result ->
            {
                if (Result != null)
                {
                    LayoutManager = new LinearLayoutManager(getContext());
                    RecyclerViewAdapter = new TelemetryAdapter(Merge(Result));
                    RvAuctionedGlobalProperties.setLayoutManager(LayoutManager);
                    RvAuctionedGlobalProperties.setAdapter(RecyclerViewAdapter);
                }
            });
        }
        else if(auctionNeeded.equals("LandmarkAuction"))
        {
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();
            AppInstance.GetFireStoreInstance().GetLandmarkAuctions(Result ->
            {
                if (Result != null)
                {
                    LayoutManager = new LinearLayoutManager(getContext());
                    RecyclerViewAdapter = new TelemetryAdapter(Merge(Result));
                    RvAuctionedGlobalProperties.setLayoutManager(LayoutManager);
                    RvAuctionedGlobalProperties.setAdapter(RecyclerViewAdapter);
                }
            });
        }
        else if(auctionNeeded.equals("VipAuction"))
        {
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();
            AppInstance.GetFireStoreInstance().GetVIPPHoneNumberAuctions(Result ->
            {
                if (Result != null)
                {
                    LayoutManager = new LinearLayoutManager(getContext());
                    RecyclerViewAdapter = new TelemetryAdapter(Merge(Result));
                    RvAuctionedGlobalProperties.setLayoutManager(LayoutManager);
                    RvAuctionedGlobalProperties.setAdapter(RecyclerViewAdapter);
                }
            });
        }
        else if(auctionNeeded.equals("GeneralAuction"))
        {
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();
            AppInstance.GetFireStoreInstance().GetGeneralAuctions(Result ->
            {
                if (Result != null)
                {
                    LayoutManager = new LinearLayoutManager(getContext());
                    RecyclerViewAdapter = new TelemetryAdapter(Merge(Result));
                    RvAuctionedGlobalProperties.setLayoutManager(LayoutManager);
                    RvAuctionedGlobalProperties.setAdapter(RecyclerViewAdapter);
                }
            });
        }
        else if(auctionNeeded.equals("ServiceAuction"))
        {
            Toast.makeText(getContext(), auctionNeeded, Toast.LENGTH_SHORT).show();
            AppInstance.GetFireStoreInstance().GetServiceAuctions(Result ->
            {
                if (Result != null)
                {
                    LayoutManager = new LinearLayoutManager(getContext());
                    RecyclerViewAdapter = new TelemetryAdapter(Merge(Result));
                    RvAuctionedGlobalProperties.setLayoutManager(LayoutManager);
                    RvAuctionedGlobalProperties.setAdapter(RecyclerViewAdapter);
                }
            });
        }
    }

    private ArrayList<Telemetry> Merge(@NonNull ArrayList<? extends Telemetry> arg)
    {
        String Email = "";
        if (PreferenceUtils.getEmail(this.getContext()) != null)
        {
            Email = PreferenceUtils.getEmail(this.getContext());
        }
        ArrayList<Telemetry> returnTelemetries = new ArrayList<>();
        if(!arg.isEmpty())
        {
            for (int i = 0; i < arg.size(); i++)
            {
                if (!arg.get(i).getOwnerId().equals(Email))
                {
                    returnTelemetries.add(arg.get(i));
                }
            }
        }
        return returnTelemetries;
    }
}