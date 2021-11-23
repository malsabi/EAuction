package com.example.eauction;

import static android.widget.TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;
import static com.example.eauction.BidActivity.BID_LABEL_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eauction.Adapters.CommentAdapter;
import com.example.eauction.Adapters.TelemetryMyPropertiesAdapter;
import com.example.eauction.DummyData.DummyData;
import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Models.Bid;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.ServiceComment;
import com.example.eauction.Models.VipPhoneNumber;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.royrodriguez.transitionbutton.TransitionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BidServiceActivity extends AppCompatActivity
{

    @BindView(R.id.TableServiceContent)
    public TableLayout TableServiceContent;

    @BindView(R.id.TelServiceIV)
    public ImageView TelServiceIV;

    @BindView(R.id.MakeBidServiceBtn)
    public TransitionButton MakeBidServiceBtn;

    @BindView(R.id.ServiceComments)
    public TextView ServiceComments;

    private CommentAdapter CommentsAdapter;
    private RecyclerView.LayoutManager LayoutManager;
    private Dialog ServiceDialog;

    private App AppInstance;
    private String AuctionOwnerUserId = "";
    private String TelemetryType = "";
    private String UserId = "";
    private String TelemetryId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_service);
        ButterKnife.bind(this);

        AppInstance = (App)getApplication();

        Bundle extras = getIntent().getExtras();
        for (String key : extras.keySet())
        {
            if (key.equals("ID") || key.equals("Picture") || key.equals("Details") || key.equals("AuctionOwnerUserId") || key.equals("UserId"))
                continue;
            AddTableRow(key, extras.get(key).toString(), TableServiceContent);
        }
        AddTableRow("Details", extras.get("Details").toString(), TableServiceContent);

        if(!extras.get("Picture").equals(""))
        {
            Bitmap img = TelemetryHelper.Base64ToImage(extras.get("Picture").toString());
            TelServiceIV.setImageBitmap(img);
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

        HandleListeners(TelemetryType);

        ServiceComments.setOnClickListener((v) ->
        {
            Toast.makeText(this, "Please wait, loading service comments.", Toast.LENGTH_SHORT).show();
            AppInstance.GetFireStoreInstance().GetUserInformation(AuctionOwnerUserObj ->
            {
                if (AuctionOwnerUserObj != null)
                {
                    Log.d("BidServiceActivity", "OnCreate:: GetUserInformation: Auction Owner UserObj Id: " + AuctionOwnerUserObj.getEmail());
                    //region "Dialog Settings"
                    final Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_comments);
                    RecyclerView RvComments = dialog.findViewById(R.id.RvComments);
                    LayoutManager = new LinearLayoutManager(this);
                    int Index = TelemetryHelper.GetTelemetryIndex(AuctionOwnerUserObj.getOwnedServiceTelemetry(), TelemetryId);
                    Service ServiceAuction = AuctionOwnerUserObj.getOwnedServiceTelemetry().get(Index);
                    if (ServiceAuction.getServiceComments() == null)
                    {
                        CommentsAdapter = new CommentAdapter(new ArrayList<>());
                        Toast.makeText(this, "There is no service comments found.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        CommentsAdapter = new CommentAdapter(ServiceAuction.getServiceComments());
                        Toast.makeText(this, "Service comments loaded successfully.", Toast.LENGTH_SHORT).show();
                    }
                    RvComments.setLayoutManager(LayoutManager);
                    RvComments.setAdapter(CommentsAdapter);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                    //endregion
                }
                else
                {
                    Log.d("BidServiceActivity", "OnCreate:: GetUserInformation: null");
                    Toast.makeText(this, "There is no service comments found.", Toast.LENGTH_SHORT).show();
                }
            }, AuctionOwnerUserId);
        });



        MakeBidServiceBtn.setOnClickListener((view ->
        {
            //region Dialog Settings
            ServiceDialog = new Dialog(this);
            ServiceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            ServiceDialog.setCancelable(true);
            ServiceDialog.setContentView(R.layout.dialog_makebid);
            //region MakeEvent
            EditText ServiceBidEditText = ServiceDialog.findViewById(R.id.EtServiceBidDialog);
            EditText ServiceCommentEditText = ServiceDialog.findViewById(R.id.EtServiceCommentDialog);
            TransitionButton BtnSubmitServiceBid = ServiceDialog.findViewById(R.id.BtnSubmitServiceBid);

            //endregion
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(ServiceDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            ServiceDialog.show();
            ServiceDialog.getWindow().setAttributes(lp);
            //endregion

            BtnSubmitServiceBid.setOnClickListener(view1 -> HandleBid(Double.parseDouble(extras.get("Base Price").toString()), ServiceBidEditText.getText().toString(), ServiceCommentEditText.getText().toString()));
        }));
    }

    //region Content Handling
    @SuppressLint("SetTextI18n")
    private void AddTableRow(String label, String content, TableLayout TableContent)
    {
        String Label = "<b><u>" + label  + ":" + "</u></b> ";
        TextView labelTV = new TextView(TableContent.getContext());
        labelTV.setText(Html.fromHtml(Label));
        labelTV.setTextSize(20);
        labelTV.setPadding(8,0,0,8);
        labelTV.setTextColor(Color.GRAY);

        TextView ContentTV = new TextView(TableContent.getContext());
        ContentTV.setTextColor(Color.BLACK);

        if(label.equals("Base Price")){
            ContentTV.setTextSize(20);
            ContentTV.setText(content + " AED");
        }
        else if(label.equals("Details"))
        {

            ContentTV.setPadding(0,0,8,0);
            ContentTV.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            //ContentTV.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            ContentTV.setText(content);
            ContentTV.setGravity(Gravity.CENTER);
            //ContentTV.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        }
        else{
            ContentTV.setTextSize(20);
            ContentTV.setPadding(8,0,0,8);
            ContentTV.setText(content);
        }
        if(label.equals("Current Bid"))
        {
            ContentTV.setText(content + " AED");
            ContentTV.setVisibility(View.GONE); //Hiding value and label
            labelTV.setVisibility(View.GONE);
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
            Log.d("BidServiceActivity", "HandleListeners:: Starting");
            if (TelemetryType.equals("Service"))
            {
                AppInstance.GetFireStoreInstance().GetFirebaseFirestore().collection("AUCTIONS").document("SERVICE")
                .addSnapshotListener((documentSnapshot, error) ->
                {
                    if (documentSnapshot != null && documentSnapshot.exists())
                    {
                        boolean IsFound = false;
                        ArrayList<?> ServiceTelemetries = (ArrayList<?>)documentSnapshot.get("ServiceTelemetries");
                        Gson gson = new Gson();
                        for (int i = 0; i < (ServiceTelemetries != null ? ServiceTelemetries.size() : 0); i++)
                        {
                            JsonElement jsonElement = gson.toJsonTree(ServiceTelemetries.get(i));
                            Service Service = gson.fromJson(jsonElement, Service.class);
                            if (Service.getID().equals(TelemetryId))
                            {
                                Log.d("BidServiceActivity", "HandleListeners:: Required Service Item is found, Current Bid: " + Service.getCurrentBid());
                                EditContent(String.valueOf(Service.getCurrentBid()));
                                IsFound = true;
                                break;
                            }
                            Log.d("BidServiceActivity", "HandleListeners:: Updated Service Received: " + Service.getName());
                        }
                        if (!IsFound)
                        {
                            Log.d("BidServiceActivity", "HandleListeners:: BidServiceActivity will be closed");
                            Toast.makeText(this, "Service auction session is finished", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    }
                    else
                    {
                        Log.d("BidServiceActivity", "HandleListeners:: Service: document does not exists");
                    }
                });
                Log.d("BidServiceActivity", "HandleListeners:: Service Listener added successfully.");
            }
            else
            {
                Toast.makeText(this, "No service telemetry is found", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private void HandleBid(double BasePriceValue, String BidDialogEditText, String UserCommentContent)
    {
        //Validation Phase.
        double UserBidValue = Double.parseDouble(BidDialogEditText);
        if (UserBidValue >= BasePriceValue)
        {
            Toast.makeText(this, "Current bid value should be less than the base price.", Toast.LENGTH_SHORT).show();
        }
        else if (TelemetryType.equals("") || AuctionOwnerUserId.equals(""))
        {
            Toast.makeText(this, "No service telemetry is found", Toast.LENGTH_SHORT).show();
        }
        else  //Processing Phase.
        {

            if (TelemetryType.equals("Service"))
            {
                //Create the Service-Comment Object
                ServiceComment ServiceCommentObj = new ServiceComment();

                //Extract Owner User Information.
                AppInstance.GetFireStoreInstance().GetUserInformation(AuctionOwnerUserObj ->
                {
                    int Index = TelemetryHelper.GetTelemetryIndex(AuctionOwnerUserObj.getOwnedServiceTelemetry(), TelemetryId);
                    Service ServiceAuction = AuctionOwnerUserObj.getOwnedServiceTelemetry().get(Index);

                    if (UserBidValue >= ServiceAuction.getCurrentBid() && ServiceAuction.getCurrentBid() != 0.0)
                    {
                        Toast.makeText(this, "Invalid Bid. Please add a lower bid value.", Toast.LENGTH_SHORT).show();
                        //Update the current bid in the view
                        EditContent(String.valueOf(ServiceAuction.getCurrentBid()));
                    }
                    else if (UserBidValue >= GetContentBid() && GetContentBid() != 0.0)
                    {
                        Toast.makeText(this, "Invalid Bid. Please add a lower bid value.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Extract Service Provider Information.
                        AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
                        {
                            //Update the Service-Comment Object
                            ServiceCommentObj.setName(UserObj.getFirstName() + " " + UserObj.getLastName());
                            ServiceCommentObj.setEmail(AppInstance.GetFireStoreInstance().DecryptField(UserObj.getEmail()));
                            ServiceCommentObj.setPhoneNumber(UserObj.getPhoneNumber());
                            ServiceCommentObj.setComment(UserCommentContent);
                            ServiceCommentObj.setCost(String.valueOf(UserBidValue));

                            //Update the Service Auction CurrentBid Value
                            ServiceAuction.setCurrentBid(UserBidValue);

                            //Initialize Service Comments arrayList Object (First ServiceProvider).
                            if (ServiceAuction.getServiceComments() == null)
                            {
                                ServiceAuction.setServiceComments(new ArrayList<>());
                            }
                            //Add the service comment object.
                            ServiceAuction.getServiceComments().add(ServiceCommentObj);

                            //Update the owned service telemetry arrayObject.
                            AuctionOwnerUserObj.getOwnedServiceTelemetry().set(Index, ServiceAuction);

                            //region "Update Owner User Information"
                            AppInstance.GetFireStoreInstance().SetUserInformation(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set the user information.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //Update the current bid in the view
                                    EditContent(String.valueOf(ServiceAuction.getCurrentBid()));
                                    //Notify the user that the auction succeeded.
                                    Toast.makeText(this, "Your bid " + ServiceAuction.getCurrentBid() + " was successfully added.", Toast.LENGTH_SHORT).show();
                                }
                            }, AuctionOwnerUserObj);
                            //endregion

                            //region "Update Global Auctions"
                            AppInstance.GetFireStoreInstance().UpdateServiceAuctions(Result ->
                            {
                                if (!Result)
                                {
                                    Toast.makeText(this, "Failed to set service auctions.", Toast.LENGTH_SHORT).show();
                                }
                            }, ServiceAuction);
                            //endregion

                            //Close the Service Bid Dialog.
                            if (ServiceDialog != null)
                            {
                                ServiceDialog.cancel();
                            }
                        }, UserId);
                    }
                }, AuctionOwnerUserId);
            }
            else
            {
                Toast.makeText(this, "No service telemetry is found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //endregion
}