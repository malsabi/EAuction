package com.example.eauction.Fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.eauction.App;
import com.example.eauction.DummyData.DummyData;
import com.example.eauction.Enums.StatusEnum;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.R;
import com.example.eauction.Utilities.PreferenceUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuctionsFragment extends Fragment
{
    @BindView(R.id.RvMyAuctionsTelemetry)
    public RecyclerView RvMyAuctionsTelemetry;

    private TelemetryAdapter RecyclerViewAdapter;
    private RecyclerView.LayoutManager LayoutManager;

    private App AppInstance;
    private User UserObj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_auctions,container,false);
        ButterKnife.bind(this,view);

        AppInstance = (App) this.requireActivity().getApplication();
        GetUserInformation();

        return view;
    }

    private void GetUserInformation()
    {
        if (PreferenceUtils.getEmail(this.getContext()) != null && PreferenceUtils.getPassword(this.getContext()) != null)
        {
            Log.d("AuctionsFragment", "GetUserInformation Email: " + PreferenceUtils.getEmail(this.getContext()));
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                this.UserObj = UserObj;
                Log.d("AuctionsFragment", "GetUserInformation User Id: " + this.UserObj.getId());

                ArrayList<Telemetry> UserTelemetries = Merge(UserObj.getOwnedCarPlateTelemetry(), UserObj.getOwnedCarTelemetry(), UserObj.getOwnedLandmarkTelemetry(), UserObj.getOwnedVipPhoneTelemetry(), UserObj.getOwnedGeneralTelemetry());

                LayoutManager = new LinearLayoutManager(getContext());
                RecyclerViewAdapter = new TelemetryAdapter(UserTelemetries);
                RvMyAuctionsTelemetry.setLayoutManager(LayoutManager);
                RvMyAuctionsTelemetry.setAdapter(RecyclerViewAdapter);

            }, PreferenceUtils.getEmail(this.getContext()));
        }
    }

    @SafeVarargs
    private ArrayList<Telemetry> Merge(@NonNull ArrayList<? extends Telemetry>...args)
    {
        ArrayList<Telemetry> returnTelemetries = new ArrayList<>();
        for (ArrayList<? extends Telemetry> arg: args)
        {
            if(arg != null && !arg.isEmpty())
            {
                for (int i = 0; i < arg.size(); i++)
                {
                    if (arg.get(i).getStatus().equals(StatusEnum.Auctioned))
                    {
                        returnTelemetries.add(arg.get(i));
                    }
                }
            }
        }
        return returnTelemetries;
    }
}