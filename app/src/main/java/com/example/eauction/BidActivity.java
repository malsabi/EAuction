package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Interfaces.SetUserInformationCallback;
import com.example.eauction.Interfaces.UpdateCarPlateAuctionsCallback;
import com.example.eauction.Models.Bid;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.User;
import com.example.eauction.Models.VipPhoneNumber;
import com.royrodriguez.transitionbutton.TransitionButton;

import java.security.acl.Owner;

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

        MakeBidButton.setOnClickListener(view -> HandleBid(Double.parseDouble(extras.get("Current Bid").toString())));
    }

    @SuppressLint("SetTextI18n")
    private void AddTableRow(String label, String content, TableLayout TableContent){
        String Label = "<b><u>" + label+":" + "</u></b> ";
        TextView labelTV = new TextView(TableContent.getContext());
        labelTV.setText(Html.fromHtml(Label));
        labelTV.setTextSize(20);
        labelTV.setPadding(8,0,0,8);
        labelTV.setTextColor(Color.GRAY);

        TextView ContentTV = new TextView(TableContent.getContext());
        ContentTV.setTextSize(20);
        ContentTV.setPadding(8,0,0,8);
        ContentTV.setTextColor(Color.BLACK);
        ContentTV.setText(content + " AED");
        if(label.equals("Current Bid"))
            ContentTV.setId(BID_LABEL_ID);


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

    @SuppressLint("SetTextI18n")
    private void HandleBid(double CurrentBidValue)
    {
        //Validation Phase.
        double UserBidValue = Integer.parseInt(CurrentBidEditText.getText().toString());
        if (UserBidValue <= CurrentBidValue)
        {
            Toast.makeText(this, "Invalid bid amount inserted.", Toast.LENGTH_SHORT).show();
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
                        else
                        {
                            //Update the current bid in the user auction.
                            CarPlateAuction.setCurrentBid(UserBidValue);
                            CarPlateAuction.getBids().add(UserBid);
                            AuctionOwnerUserObj.getOwnedCarPlateTelemetry().set(Index, CarPlateAuction);
                            //Update Owner User Information
                            AppInstance.GetFireStoreInstance().SetUserInformation(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the current bid.", Toast.LENGTH_SHORT).show();
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
                        else
                        {
                            //Update the current bid in the user auction.
                            CarAuction.setCurrentBid(UserBidValue);
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
                        else
                        {
                            //Update the current bid in the user auction.
                            LandmarkAuction.setCurrentBid(UserBidValue);
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
                        else
                        {
                            //Update the current bid in the user auction.
                            VipPhoneNumberAuction.setCurrentBid(UserBidValue);
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
                        else
                        {
                            //Update the current bid in the user auction.
                            GeneralAuction.setCurrentBid(UserBidValue);
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
}