package com.example.eauction.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.eauction.BidActivity;
import com.example.eauction.Models.Bid;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.R;
import com.example.eauction.Utilities.PreferenceUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlobalAuctionsFragment extends Fragment {

    @BindView(R.id.RvAucitonedGlobalProperties)
    public RecyclerView RvAuctionedGlobalProperties;

    private TelemetryAdapter RecyclerViewAdapter;
    private RecyclerView.LayoutManager LayoutManager;
    private App AppInstance;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view =  inflater.inflate(R.layout.fragment_globalauctions,container,false);
        ButterKnife.bind(this,view);
        AppInstance = (App) this.requireActivity().getApplication();

        String auctionNeeded = this.requireArguments().getString("AuctionNeeded");
        PopulateCards(auctionNeeded);

        //TODO Submit button event should be handeled
        //TODO Validation of the Bid Should be Handeled
        //TODO if you are going to start the animation of the button don't forget to return it back

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
                    ArrayList<Telemetry> MergeList = Merge(Result);
                    RecyclerViewAdapter = new TelemetryAdapter(MergeList);
                    RecyclerViewAdapter.setOnItemClickListener(new TelemetryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            CarPlate generalItem =  (CarPlate) MergeList.get(position);
                            Bundle extras = new Bundle();
                            extras.putString("Plate Number", generalItem.getPlateNumber());
                            extras.putString("Plate Code", generalItem.getPlateCode());
                            extras.putString("Emirate", generalItem.getEmirate());
                            extras.putString("Current Bid", generalItem.getCurrentBid()+"");
                            extras.putString("Base Price", generalItem.getBasePrice()+"");
                            extras.putString("Auction Start",generalItem.getAuctionStart());
                            extras.putString("Auction End",generalItem.getAuctionEnd());
                            extras.putString("Picture", generalItem.getImage());
                            extras.putString("ID", generalItem.getID());
                            extras.putString("Details", generalItem.getDetails());
                            Intent intent = new Intent(view.getContext(), BidActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
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
                    ArrayList<Telemetry> MergeList = Merge(Result);
                    RecyclerViewAdapter = new TelemetryAdapter(MergeList);
                    RecyclerViewAdapter.setOnItemClickListener(new TelemetryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Car car =  (Car) MergeList.get(position);
                            Bundle extras = new Bundle();
                            extras.putString("Name", car.getName());
                            extras.putString("Current Bid", car.getCurrentBid()+"");
                            extras.putString("Base Price", car.getBasePrice()+"");
                            extras.putString("Auction Start",car.getAuctionStart());
                            extras.putString("Auction End",car.getAuctionEnd());
                            extras.putString("Picture", car.getImage());
                            extras.putString("Mileage", car.getMileage()+"");
                            extras.putString("Model", car.getModel());
                            extras.putString("Power", car.getHorsePower()+"");
                            extras.putString("ID", car.getID());
                            extras.putString("Details", car.getDetails());
                            Intent intent = new Intent(view.getContext(), BidActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
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
                    ArrayList<Telemetry> MergeList = Merge(Result);
                    RecyclerViewAdapter = new TelemetryAdapter(MergeList);
                    RecyclerViewAdapter.setOnItemClickListener(new TelemetryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Landmark landmark =  (Landmark) MergeList.get(position);
                            Bundle extras = new Bundle();
                            extras.putString("Name", landmark.getName());
                            extras.putString("Current Bid", landmark.getCurrentBid()+"");
                            extras.putString("Base Price", landmark.getBasePrice()+"");
                            extras.putString("Auction Start",landmark.getAuctionStart());
                            extras.putString("Auction End",landmark.getAuctionEnd());
                            extras.putString("Picture", landmark.getImage());
                            extras.putString("ID", landmark.getID());
                            extras.putString("Details", landmark.getDetails());
                            extras.putString("Type", landmark.getType());
                            extras.putString("Location", landmark.getLocation());
                            extras.putString("Area", landmark.getArea()+"");
                            Intent intent = new Intent(view.getContext(), BidActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
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
                    ArrayList<Telemetry> MergeList = Merge(Result);
                    RecyclerViewAdapter = new TelemetryAdapter(MergeList);
                    RecyclerViewAdapter.setOnItemClickListener(new TelemetryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            VipPhoneNumber vipPhoneNumber =  (VipPhoneNumber) MergeList.get(position);
                            Bundle extras = new Bundle();
                            extras.putString("Name", vipPhoneNumber.getCompany());
                            extras.putString("Current Bid", vipPhoneNumber.getCurrentBid()+"");
                            extras.putString("Base Price", vipPhoneNumber.getBasePrice()+"");
                            extras.putString("Auction Start",vipPhoneNumber.getAuctionStart());
                            extras.putString("Auction End",vipPhoneNumber.getAuctionEnd());
                            extras.putString("Picture", vipPhoneNumber.getImage());
                            extras.putString("ID", vipPhoneNumber.getID());
                            extras.putString("Details", vipPhoneNumber.getDetails());
                            extras.putString("Phone Number", vipPhoneNumber.getPhoneNumber());
                            Intent intent = new Intent(view.getContext(), BidActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
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
                    ArrayList<Telemetry> MergeList = Merge(Result);
                    RecyclerViewAdapter = new TelemetryAdapter(MergeList);
                    RecyclerViewAdapter.setOnItemClickListener(new TelemetryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            General generalItem =  (General) MergeList.get(position);
                            Bundle extras = new Bundle();
                            extras.putString("Name", generalItem.getName());
                            extras.putString("Current Bid", generalItem.getCurrentBid()+"");
                            extras.putString("Base Price", generalItem.getBasePrice()+"");
                            extras.putString("Auction Start",generalItem.getAuctionStart());
                            extras.putString("Auction End",generalItem.getAuctionEnd());
                            extras.putString("Picture", generalItem.getImage());
                            extras.putString("ID", generalItem.getID());
                            extras.putString("Details", generalItem.getDetails());
                            Intent intent = new Intent(view.getContext(), BidActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
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