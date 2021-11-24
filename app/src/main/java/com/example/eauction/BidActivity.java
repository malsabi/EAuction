package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Models.Bid;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.VipPhoneNumber;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.royrodriguez.transitionbutton.TransitionButton;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BidActivity extends AppCompatActivity
{
    public static final int BID_LABEL_ID = 112345672;

    @BindView(R.id.TableContent)
    public TableLayout TableContent;

    @BindView(R.id.TelIV)
    public ImageView TelIV;

    @BindView(R.id.EtCurrentBid)
    public EditText CurrentBidEditText;

    @BindView(R.id.MakeBidBtn)
    public TransitionButton MakeBidButton;

    private App AppInstance;
    private String AuctionOwnerUserId = "";
    private String TelemetryType = "";
    private String UserId = "";
    private String TelemetryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);
        ButterKnife.bind(this);

        AppInstance = (App)getApplication();

        Bundle extras = getIntent().getExtras();
        for (String key : extras.keySet())
        {
            if (key.equals("ID") || key.equals("Picture") || key.equals("Details") || key.equals("AuctionOwnerUserId") || key.equals("UserId"))
                continue;
            AddTableRow(key, extras.get(key).toString(), TableContent);
        }
        AddTableRow("Details", extras.get("Details").toString(), TableContent);

        if(!extras.get("Picture").equals(""))
        {
            Bitmap img = TelemetryHelper.Base64ToImage(extras.get("Picture").toString());
            TelIV.setImageBitmap(img);
        }

        //Extras
        if (!extras.get("AuctionOwnerUserId").equals(""))
        {
            AuctionOwnerUserId = extras.get("AuctionOwnerUserId").toString();
        }
        if (!extras.get("Type").equals(""))
        {
            TelemetryType = extras.get("Type").toString();
        }
        if (!extras.get("UserId").equals(""))
        {
            UserId = extras.get("UserId").toString();
        }
        if (!extras.get("ID").equals(""))
        {
            TelemetryId = extras.get("ID").toString();
        }
        CurrentBidEditText.setText(extras.get("Current Bid").toString());

        MakeBidButton.setOnClickListener(view -> HandleBid(Double.parseDouble(extras.get("Base Price").toString())));

        HandleListeners(TelemetryType);
    }


    //region Content Handling
    @SuppressLint("SetTextI18n")
    private void AddTableRow(String label, String content, TableLayout TableContent){
        String Label = "<b><u>" + label  + ":" + "</u></b> ";
        TextView labelTV = new TextView(TableContent.getContext());
        labelTV.setText(Html.fromHtml(Label));
        labelTV.setTextSize(20);
        labelTV.setPadding(8,0,0,8);
        labelTV.setTextColor(Color.GRAY);

        TextView ContentTV = new TextView(TableContent.getContext());
        ContentTV.setTextSize(20);
        ContentTV.setPadding(8,0,0,8);
        ContentTV.setTextColor(Color.BLACK);

        if(label.equals("Current Bid") || label.equals("Base Price")){
            ContentTV.setText(content + " AED");
        }
        else
        {
            TableContent.setColumnShrinkable(1, true);
            ContentTV.setText(content);
        }


        if(label.equals("Current Bid"))
        {
            ContentTV.setId(BID_LABEL_ID);
        }

        TableRow tableRow = new TableRow(TableContent.getContext());
        tableRow.addView(labelTV);
        tableRow.addView(ContentTV);
        tableRow.setPadding(0,0,0,32);
        TableContent.addView(tableRow);
    }
    @SuppressLint("SetTextI18n")
    private void EditContent(String Content)
    {
        TextView ContentTextView = findViewById(BID_LABEL_ID);
        ContentTextView.setText(Content + " AED");
    }
    public double GetContentBid()
    {
        TextView ContentTextView = findViewById(BID_LABEL_ID);
        String ContentBid = ContentTextView.getText().toString().replace(" AED", "");
        return Double.parseDouble(ContentBid);
    }
    //endregion

    //region Listeners/Bid Handling
    private void HandleListeners(String TelemetryType)
    {
        if (TelemetryType.equals("") || AuctionOwnerUserId.equals(""))
        {
            Toast.makeText(this, "No telemetry is found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d("BidActivity", "HandleListeners:: Starting");
            switch (TelemetryType)
            {
                case "CarPlate":
                {
                    AppInstance.GetFireStoreInstance().GetFirebaseFirestore().collection("AUCTIONS").document("CARPLATE")
                    .addSnapshotListener((documentSnapshot, error) ->
                    {
                        if (documentSnapshot != null && documentSnapshot.exists())
                        {
                            boolean IsFound = false;
                            ArrayList<?> CarPlateTelemetries = (ArrayList<?>)documentSnapshot.get("CarPlateTelemetries");
                            Gson gson = new Gson();
                            for (int i = 0; i < (CarPlateTelemetries != null ? CarPlateTelemetries.size() : 0); i++)
                            {
                                JsonElement jsonElement = gson.toJsonTree(CarPlateTelemetries.get(i));
                                CarPlate CarPlate = gson.fromJson(jsonElement, CarPlate.class);
                                if (CarPlate.getID().equals(TelemetryId))
                                {
                                    Log.d("BidActivity", "HandleListeners:: Required CarPlate Item is found, Current Bid: " + CarPlate.getCurrentBid());
                                    EditContent(String.valueOf(CarPlate.getCurrentBid()));
                                    IsFound = true;
                                    break;
                                }
                                Log.d("BidActivity", "HandleListeners:: Updated CarPlate Received: " + CarPlate.getPlateNumber());
                            }
                            if (!IsFound)
                            {
                                Log.d("BidActivity", "HandleListeners:: BidActivity will be closed");
                                Toast.makeText(this, "Car Plate auction session is finished", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }
                        }
                        else
                        {
                            Log.d("BidActivity", "HandleListeners:: CarPlate: document does not exists");
                        }
                    });
                    Log.d("BidActivity", "HandleListeners:: CarPlate Listener added successfully.");
                }
                break;
                case "Car":
                {
                    AppInstance.GetFireStoreInstance().GetFirebaseFirestore().collection("AUCTIONS").document("CAR")
                    .addSnapshotListener((documentSnapshot, error) ->
                    {
                        if (documentSnapshot != null && documentSnapshot.exists())
                        {
                            boolean IsFound = false;
                            ArrayList<?> CarTelemetries = (ArrayList<?>)documentSnapshot.get("CarTelemetries");
                            Gson gson = new Gson();
                            for (int i = 0; i < (CarTelemetries != null ? CarTelemetries.size() : 0); i++)
                            {
                                if (!CarTelemetries.get(i).toString().isEmpty())
                                {
                                    JsonElement jsonElement = gson.toJsonTree(CarTelemetries.get(i));
                                    Car Car = gson.fromJson(jsonElement, Car.class);
                                    if (Car.getID().equals(TelemetryId))
                                    {
                                        Log.d("BidActivity", "HandleListeners:: Required Car Item is found, Current Bid: " + Car.getCurrentBid());
                                        EditContent(String.valueOf(Car.getCurrentBid()));
                                        IsFound = true;
                                        break;
                                    }
                                    Log.d("BidActivity", "HandleListeners:: Updated Car Received: " + Car.getName());
                                }
                            }
                            if (!IsFound)
                            {
                                Log.d("BidActivity", "HandleListeners:: BidActivity will be closed");
                                Toast.makeText(this, "Car auction session is finished", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }
                        }
                        else
                        {
                            Log.d("BidActivity", "HandleListeners:: Car: document does not exists");
                        }
                    });
                    Log.d("BidActivity", "HandleListeners:: Car Listener added successfully.");
                }
                break;
                case "Landmark":
                {
                    AppInstance.GetFireStoreInstance().GetFirebaseFirestore().collection("AUCTIONS").document("LANDMARK")
                    .addSnapshotListener((documentSnapshot, error) ->
                    {
                        if (documentSnapshot != null && documentSnapshot.exists())
                        {
                            boolean IsFound = false;
                            ArrayList<?> LandmarkTelemetries = (ArrayList<?>)documentSnapshot.get("LandmarkTelemetries");
                            Gson gson = new Gson();
                            for (int i = 0; i < (LandmarkTelemetries != null ? LandmarkTelemetries.size() : 0); i++)
                            {
                                if (!LandmarkTelemetries.get(i).toString().isEmpty())
                                {
                                    JsonElement jsonElement = gson.toJsonTree(LandmarkTelemetries.get(i));
                                    Landmark Landmark = gson.fromJson(jsonElement, Landmark.class);
                                    if (Landmark.getID().equals(TelemetryId))
                                    {
                                        Log.d("BidActivity", "HandleListeners:: Required Landmark Item is found, Current Bid: " + Landmark.getCurrentBid());
                                        EditContent(String.valueOf(Landmark.getCurrentBid()));
                                        IsFound = true;
                                        break;
                                    }
                                    Log.d("BidActivity", "HandleListeners:: Updated Landmark Received: " + Landmark.getName());
                                }
                            }
                            if (!IsFound)
                            {
                                Log.d("BidActivity", "HandleListeners:: BidActivity will be closed");
                                Toast.makeText(this, "Landmark auction session is finished", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }
                        }
                        else
                        {
                            Log.d("BidActivity", "HandleListeners:: Landmark: document does not exists");
                        }
                    });
                    Log.d("BidActivity", "HandleListeners:: Landmark Listener added successfully.");
                }
                break;
                case "VipPhoneNumber":
                {
                    AppInstance.GetFireStoreInstance().GetFirebaseFirestore().collection("AUCTIONS").document("VIPPHONE")
                    .addSnapshotListener((documentSnapshot, error) ->
                    {
                        if (documentSnapshot != null && documentSnapshot.exists())
                        {
                            boolean IsFound = false;
                            ArrayList<?> VIPPhoneTelemetries = (ArrayList<?>)documentSnapshot.get("VIPPhoneTelemetries");
                            Gson gson = new Gson();
                            for (int i = 0; i < (VIPPhoneTelemetries != null ? VIPPhoneTelemetries.size() : 0); i++)
                            {
                                if (!VIPPhoneTelemetries.get(i).toString().isEmpty())
                                {
                                    JsonElement jsonElement = gson.toJsonTree(VIPPhoneTelemetries.get(i));
                                    VipPhoneNumber VipPhoneNumber = gson.fromJson(jsonElement, VipPhoneNumber.class);
                                    if (VipPhoneNumber.getID().equals(TelemetryId))
                                    {
                                        Log.d("BidActivity", "HandleListeners:: Required VipPhoneNumber Item is found, Current Bid: " + VipPhoneNumber.getCurrentBid());
                                        EditContent(String.valueOf(VipPhoneNumber.getCurrentBid()));
                                        IsFound = true;
                                        break;
                                    }
                                    Log.d("BidActivity", "HandleListeners:: Updated VipPhoneNumber Received: " + VipPhoneNumber.getPhoneNumber());
                                }
                            }
                            if (!IsFound)
                            {
                                Log.d("BidActivity", "HandleListeners:: BidActivity will be closed");
                                Toast.makeText(this, "Vip Phone Number auction session is finished", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }
                        }
                        else
                        {
                            Log.d("BidActivity", "HandleListeners:: VipPhoneNumber: document does not exists");
                        }
                    });
                    Log.d("BidActivity", "HandleListeners:: VipPhoneNumber Listener added successfully.");
                }
                break;
                case "General":
                {
                    AppInstance.GetFireStoreInstance().GetFirebaseFirestore().collection("AUCTIONS").document("GENERAL")
                    .addSnapshotListener((documentSnapshot, error) ->
                    {
                        if (documentSnapshot != null && documentSnapshot.exists())
                        {
                            boolean IsFound = false;
                            ArrayList<?> GeneralTelemetries = (ArrayList<?>)documentSnapshot.get("GeneralTelemetries");
                            Gson gson = new Gson();
                            for (int i = 0; i < (GeneralTelemetries != null ? GeneralTelemetries.size() : 0); i++)
                            {
                                if (!GeneralTelemetries.get(i).toString().isEmpty())
                                {
                                    JsonElement jsonElement = gson.toJsonTree(GeneralTelemetries.get(i));
                                    General General = gson.fromJson(jsonElement, General.class);
                                    if (General.getID().equals(TelemetryId))
                                    {
                                        Log.d("BidActivity", "HandleListeners:: Required General Item is found, Current Bid: " + General.getCurrentBid());
                                        EditContent(String.valueOf(General.getCurrentBid()));
                                        IsFound = true;
                                        break;
                                    }
                                    Log.d("BidActivity", "HandleListeners:: Updated Generals Received: " + General.getName());
                                }
                            }
                            if (!IsFound)
                            {
                                Log.d("BidActivity", "HandleListeners:: BidActivity will be closed");
                                Toast.makeText(this, "General auction session is finished", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }
                        }
                        else
                        {
                            Log.d("BidActivity", "HandleListeners:: VipPhoneNumber: document does not exists");
                        }
                    });
                    Log.d("BidActivity", "HandleListeners:: General Listener added successfully.");
                }
                break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void HandleBid(double BasePriceValue)
    {
        //Validation Phase.
        double UserBidValue = Double.parseDouble(CurrentBidEditText.getText().toString());
        if (UserBidValue <= BasePriceValue)
        {
            Toast.makeText(this, "Current bid value should be greater than the base price.", Toast.LENGTH_SHORT).show();
        }
        else if (TelemetryType.equals("") || AuctionOwnerUserId.equals(""))
        {
            Toast.makeText(this, "No telemetry is found", Toast.LENGTH_SHORT).show();
        }
        else  //Processing Phase.
        {
            //Create the bid object
            Bid UserBid = new Bid();
            UserBid.setUserId(UserId);
            UserBid.setCurrentBid(UserBidValue);

            //Extract Owner User Information.
            AppInstance.GetFireStoreInstance().GetUserInformation(AuctionOwnerUserObj ->
            {
                switch (TelemetryType)
                {
                    case "CarPlate":
                    {
                        int Index = TelemetryHelper.GetTelemetryIndex(AuctionOwnerUserObj.getOwnedCarPlateTelemetry(), TelemetryId);
                        CarPlate CarPlateAuction = AuctionOwnerUserObj.getOwnedCarPlateTelemetry().get(Index);

                        if (CarPlateAuction.getCurrentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                            //Update the current bid in the view
                            EditContent(String.valueOf(CarPlateAuction.getCurrentBid()));
                        }
                        else if (GetContentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Update the current bid in the user auction.
                            CarPlateAuction.setCurrentBid(UserBidValue);
                            if (CarPlateAuction.getBids() == null)
                            {
                                CarPlateAuction.setBids(new ArrayList<>());
                            }
                            CarPlateAuction.getBids().add(UserBid);
                            AuctionOwnerUserObj.getOwnedCarPlateTelemetry().set(Index, CarPlateAuction);
                            //Update Owner User Information
                            AppInstance.GetFireStoreInstance().SetUserInformation(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //Update the current bid in the view
                                    EditContent(String.valueOf(CarPlateAuction.getCurrentBid()));
                                }
                            }, AuctionOwnerUserObj);
                            //Update Global Auctions
                            AppInstance.GetFireStoreInstance().UpdateCarPlateAuctions(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, CarPlateAuction);
                        }
                    }
                    break;
                    case "Car":
                    {
                        int Index = TelemetryHelper.GetTelemetryIndex(AuctionOwnerUserObj.getOwnedCarTelemetry(), TelemetryId);
                        Car CarAuction = AuctionOwnerUserObj.getOwnedCarTelemetry().get(Index);

                        if (CarAuction.getCurrentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                            //Update the current bid in the view
                            EditContent(String.valueOf(CarAuction.getCurrentBid()));
                        }
                        else if (GetContentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Update the current bid in the user auction.
                            CarAuction.setCurrentBid(UserBidValue);
                            if (CarAuction.getBids() == null)
                            {
                                CarAuction.setBids(new ArrayList<>());
                            }
                            else
                            {
                                //Update the current bid in the view
                                EditContent(String.valueOf(CarAuction.getCurrentBid()));
                            }
                            CarAuction.getBids().add(UserBid);
                            AuctionOwnerUserObj.getOwnedCarTelemetry().set(Index, CarAuction);
                            //Update Owner User Information
                            AppInstance.GetFireStoreInstance().SetUserInformation(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, AuctionOwnerUserObj);
                            //Update Global Auctions
                            AppInstance.GetFireStoreInstance().UpdateCarAuctions(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, CarAuction);
                        }
                    }
                    break;
                    case "Landmark":
                    {
                        int Index = TelemetryHelper.GetTelemetryIndex(AuctionOwnerUserObj.getOwnedLandmarkTelemetry(), TelemetryId);
                        Landmark LandmarkAuction = AuctionOwnerUserObj.getOwnedLandmarkTelemetry().get(Index);

                        if (LandmarkAuction.getCurrentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                            //Update the current bid in the view
                            EditContent(String.valueOf(LandmarkAuction.getCurrentBid()));
                        }
                        else if (GetContentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Update the current bid in the user auction.
                            LandmarkAuction.setCurrentBid(UserBidValue);
                            if (LandmarkAuction.getBids() == null)
                            {
                                LandmarkAuction.setBids(new ArrayList<>());
                            }
                            else
                            {
                                //Update the current bid in the view
                                EditContent(String.valueOf(LandmarkAuction.getCurrentBid()));
                            }
                            LandmarkAuction.getBids().add(UserBid);
                            AuctionOwnerUserObj.getOwnedLandmarkTelemetry().set(Index, LandmarkAuction);
                            //Update Owner User Information
                            AppInstance.GetFireStoreInstance().SetUserInformation(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, AuctionOwnerUserObj);
                            //Update Global Auctions
                            AppInstance.GetFireStoreInstance().UpdateLandmarkAuctions(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, LandmarkAuction);
                        }
                    }
                    break;
                    case "VipPhoneNumber":
                    {
                        int Index = TelemetryHelper.GetTelemetryIndex(AuctionOwnerUserObj.getOwnedVipPhoneTelemetry(), TelemetryId);
                        VipPhoneNumber VipPhoneNumberAuction = AuctionOwnerUserObj.getOwnedVipPhoneTelemetry().get(Index);

                        if (VipPhoneNumberAuction.getCurrentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                            //Update the current bid in the view
                            EditContent(String.valueOf(VipPhoneNumberAuction.getCurrentBid()));
                        }
                        else if (GetContentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Update the current bid in the user auction.
                            VipPhoneNumberAuction.setCurrentBid(UserBidValue);
                            if (VipPhoneNumberAuction.getBids() == null)
                            {
                                VipPhoneNumberAuction.setBids(new ArrayList<>());
                            }
                            else
                            {
                                //Update the current bid in the view
                                EditContent(String.valueOf(VipPhoneNumberAuction.getCurrentBid()));
                            }
                            VipPhoneNumberAuction.getBids().add(UserBid);
                            AuctionOwnerUserObj.getOwnedVipPhoneTelemetry().set(Index, VipPhoneNumberAuction);
                            //Update Owner User Information
                            AppInstance.GetFireStoreInstance().SetUserInformation(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, AuctionOwnerUserObj);
                            //Update Global Auctions
                            AppInstance.GetFireStoreInstance().UpdateVIPPHoneNumberAuctions(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, VipPhoneNumberAuction);
                        }
                    }
                    break;
                    case "General":
                    {
                        int Index = TelemetryHelper.GetTelemetryIndex(AuctionOwnerUserObj.getOwnedGeneralTelemetry(), TelemetryId);
                        General GeneralAuction = AuctionOwnerUserObj.getOwnedGeneralTelemetry().get(Index);

                        if (GeneralAuction.getCurrentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                            //Update the current bid in the view
                            EditContent(String.valueOf(GeneralAuction.getCurrentBid()));
                        }
                        else if (GetContentBid() >= UserBidValue)
                        {
                            Toast.makeText(this, "Invalid Bid. Please add a higher bid value.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Update the current bid in the user auction.
                            GeneralAuction.setCurrentBid(UserBidValue);
                            if (GeneralAuction.getBids() == null)
                            {
                                GeneralAuction.setBids(new ArrayList<>());
                            }
                            else
                            {
                                //Update the current bid in the view
                                EditContent(String.valueOf(GeneralAuction.getCurrentBid()));
                            }
                            GeneralAuction.getBids().add(UserBid);
                            AuctionOwnerUserObj.getOwnedGeneralTelemetry().set(Index, GeneralAuction);
                            //Update Owner User Information
                            AppInstance.GetFireStoreInstance().SetUserInformation(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, AuctionOwnerUserObj);
                            //Update Global Auctions
                            AppInstance.GetFireStoreInstance().UpdateGeneralAuctions(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
                                }
                            }, GeneralAuction);
                        }
                    }
                    break;
                }
            }, AuctionOwnerUserId);
        }
    }
    //endregion
}