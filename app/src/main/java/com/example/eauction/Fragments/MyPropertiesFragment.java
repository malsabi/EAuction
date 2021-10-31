package com.example.eauction.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eauction.Adapters.TelemetryAdapter;
import com.example.eauction.App;
import com.example.eauction.Enums.StatusEnum;
import com.example.eauction.Helpers.DateHelper;
import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.InsertActivity;
import com.example.eauction.Interfaces.SetUserInformationCallback;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.R;
import com.example.eauction.Utilities.PreferenceUtils;
import com.royrodriguez.transitionbutton.TransitionButton;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPropertiesFragment extends Fragment
{
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.RvTelemetry)
    public RecyclerView RecyclerViewTelemetry;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.BtnAddTelemetryInsertActivity)
    TransitionButton BtnAddTelemetryInsertActivity;

    private TelemetryAdapter RecyclerViewAdapter;
    private RecyclerView.LayoutManager LayoutManager;

    private App AppInstance;
    private User UserObj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_myproperties,container,false);
        ButterKnife.bind(this,view);

        AppInstance = (App) this.requireActivity().getApplication();
        GetUserInformation();

        //region Insert-Activity OnClick
        BtnAddTelemetryInsertActivity.setOnClickListener(view1 -> startActivity(new Intent(getContext(), InsertActivity.class)));
        //endregion

        return view;
    }

    private void GetUserInformation()
    {
        if (PreferenceUtils.getEmail(this.getContext()) != null && PreferenceUtils.getPassword(this.getContext()) != null)
        {
            Log.d("MyPropertiesFragment", "GetUserInformation Email: " + PreferenceUtils.getEmail(this.getContext()));
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                this.UserObj = UserObj;
                Log.d("MyPropertiesFragment", "User Id: " + this.UserObj.getId());

                ArrayList<Telemetry> UserTelemetries = Merge(UserObj.getOwnedCarPlateTelemetry(), UserObj.getOwnedCarTelemetry(), UserObj.getOwnedLandmarkTelemetry(), UserObj.getOwnedVipPhoneTelemetry(), UserObj.getOwnedGeneralTelemetry());

                LayoutManager = new LinearLayoutManager(getContext());
                RecyclerViewAdapter = new TelemetryAdapter(UserTelemetries);
                RecyclerViewTelemetry.setLayoutManager(LayoutManager);
                RecyclerViewTelemetry.setAdapter(RecyclerViewAdapter);

                RecyclerViewAdapter.setOnItemClickListener(position ->
                {
                    Telemetry SelectedTelemetryItem = UserTelemetries.get(position); //Item Selected

                    //region Dialog Settings
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.auction_dialog);
                    EditText EtBasePrice = dialog.findViewById(R.id.EtBasePrice);
                    TransitionButton BtnSubmitAuction = dialog.findViewById(R.id.BtnSubmitAuction);
                    dialog.show();
                    //endregion

                    BtnSubmitAuction.setOnClickListener(view ->
                    {
                        //TODO Apply the logic you need for the baseprice then update object and set it as auctioned (done)
                        double UserBasePrice = Double.parseDouble(EtBasePrice.getText().toString());
                        if (UserBasePrice <= 0)
                        {
                            Toast.makeText(getContext(), "Base price cannot be zero or below.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), UserBasePrice + "", Toast.LENGTH_SHORT).show();
                            SelectedTelemetryItem.setBasePrice(UserBasePrice);
                            SelectedTelemetryItem.setAuctionStart(DateHelper.GetCurrentDateTime());
                            SelectedTelemetryItem.setStatus(StatusEnum.Auctioned);
                            HandleTelemetryUpdate(SelectedTelemetryItem);

                        }
                        dialog.hide();
                    });
                });
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
                    if (!arg.get(i).getStatus().equals(StatusEnum.Auctioned))
                    {
                        returnTelemetries.add(arg.get(i));
                    }
                }
            }
        }
        return returnTelemetries;
    }

    private void HandleTelemetryUpdate(Telemetry SelectedTelemetry)
    {
        String TelemetryType = TelemetryHelper.GetTelemetryType(SelectedTelemetry);
        Log.d("MyPropertiesFragment", "HandleTelemetryUpdate Type Name: " + TelemetryType);
        switch (TelemetryType)
        {
            case "CarPlate":
            {
                HandleUserTelemetry(SelectedTelemetry, "CarPlate");
                AppInstance.GetFireStoreInstance().SetUserInformation((SetUserInformationCallback) UserInfoResult ->
                {
                   if (UserInfoResult)
                   {
                        AppInstance.GetFireStoreInstance().AddCarPlateAuction(AddAuctionResult ->
                        {
                            if (AddAuctionResult.isSuccess())
                            {
                                Toast.makeText(getContext(), "Your Telemetry Item is now being auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddCarPlateAuction Successfully added the auction into the global auction collection");
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Failed to set the Telemetry Item to be auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddCarPlateAuction Failed to add the auction into the global auction collection");
                            }
                        }, (CarPlate)SelectedTelemetry);
                   }
                }, UserObj);
            }
            break;
            case "Car":
            {
                HandleUserTelemetry(SelectedTelemetry, "Car");
                AppInstance.GetFireStoreInstance().SetUserInformation((SetUserInformationCallback) Result ->
                {
                    if (Result)
                    {
                        AppInstance.GetFireStoreInstance().AddCarAuction(AddAuctionResult ->
                        {
                            if (AddAuctionResult.isSuccess())
                            {
                                Toast.makeText(getContext(), "Your Telemetry Item is now being auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddCarAuction Successfully added the auction into the global auction collection");
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Failed to set the Telemetry Item to be auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddCarAuction Failed to add the auction into the global auction collection");
                            }
                        }, (Car)SelectedTelemetry);
                    }
                }, UserObj);
            }
            break;
            case "Landmark":
            {
                HandleUserTelemetry(SelectedTelemetry, "Landmark");
                AppInstance.GetFireStoreInstance().SetUserInformation((SetUserInformationCallback) Result ->
                {
                    if (Result)
                    {
                        AppInstance.GetFireStoreInstance().AddLandmarkAuction(AddAuctionResult ->
                        {
                            if (AddAuctionResult.isSuccess())
                            {
                                Toast.makeText(getContext(), "Your Telemetry Item is now being auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddLandmarkAuction Successfully added the auction into the global auction collection");
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Failed to set the Telemetry Item to be auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddLandmarkAuction Failed to add the auction into the global auction collection");
                            }
                        }, (Landmark) SelectedTelemetry);
                    }
                }, UserObj);
            }
            break;
            case "VipPhoneNumber":
            {
                HandleUserTelemetry(SelectedTelemetry, "VipPhoneNumber");
                AppInstance.GetFireStoreInstance().SetUserInformation((SetUserInformationCallback) Result ->
                {
                    if (Result)
                    {
                        AppInstance.GetFireStoreInstance().AddVIPPhoneNumberAuction(AddAuctionResult ->
                        {
                            if (AddAuctionResult.isSuccess())
                            {
                                Toast.makeText(getContext(), "Your Telemetry Item is now being auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddVIPPhoneNumberAuction Successfully added the auction into the global auction collection");
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Failed to set the Telemetry Item to be auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddVIPPhoneNumberAuction Failed to add the auction into the global auction collection");
                            }
                        }, (VipPhoneNumber) SelectedTelemetry);
                    }
                }, UserObj);
            }
            break;
            case "General":
            {
                HandleUserTelemetry(SelectedTelemetry, "General");
                AppInstance.GetFireStoreInstance().SetUserInformation((SetUserInformationCallback) Result ->
                {
                    if (Result)
                    {
                        AppInstance.GetFireStoreInstance().AddGeneralAuction(AddAuctionResult ->
                        {
                            if (AddAuctionResult.isSuccess())
                            {
                                Toast.makeText(getContext(), "Your Telemetry Item is now being auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddGeneralAuction Successfully added the auction into the global auction collection");
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Failed to set the Telemetry Item to be auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddGeneralAuction Failed to add the auction into the global auction collection");
                            }
                        }, (General) SelectedTelemetry);
                    }
                }, UserObj);
            }
            break;
            case "Service":
            {
                HandleUserTelemetry(SelectedTelemetry, "Service");
                AppInstance.GetFireStoreInstance().SetUserInformation((SetUserInformationCallback) Result ->
                {
                    if (Result)
                    {
                        AppInstance.GetFireStoreInstance().AddServiceAuction(AddAuctionResult ->
                        {
                            if (AddAuctionResult.isSuccess())
                            {
                                Toast.makeText(getContext(), "Your Service Item is now being auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddServiceAuction Successfully added the auction into the global auction collection");
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Failed to set the Service Item to be auctioned.", Toast.LENGTH_SHORT).show();
                                Log.d("MyPropertiesFragment", "HandleTelemetryUpdate AddServiceAuction Failed to add the auction into the global auction collection");
                            }
                        }, (Service) SelectedTelemetry);
                    }
                }, UserObj);
            }
            break;
        }
    }

    private void HandleUserTelemetry(Telemetry Telemetry, String Type)
    {
        switch (Type)
        {
            case "CarPlate":
            {
                CarPlate CarPlateItem = (CarPlate) Telemetry;
                int Index = -1;
                for (CarPlate CarPlateObj : UserObj.getOwnedCarPlateTelemetry())
                {
                    if (CarPlateObj.getID().equals(CarPlateItem.getID()))
                    {
                        Index = UserObj.getOwnedCarPlateTelemetry().indexOf(CarPlateObj);
                        break;
                    }
                }
                if (Index != -1)
                {
                    UserObj.getOwnedCarPlateTelemetry().set(Index, CarPlateItem);
                }
                else
                {
                    UserObj.getOwnedCarPlateTelemetry().add(CarPlateItem);
                }
                break;
            }
            case "Car":
            {
                Car CarItem = (Car) Telemetry;
                int Index = -1;
                for (Car CarObj : UserObj.getOwnedCarTelemetry())
                {
                    if (CarObj.getID().equals(CarItem.getID()))
                    {
                        Index = UserObj.getOwnedCarTelemetry().indexOf(CarObj);
                        break;
                    }
                }
                if (Index != -1)
                {
                    UserObj.getOwnedCarTelemetry().set(Index, CarItem);
                }
                else
                {
                    UserObj.getOwnedCarTelemetry().add(CarItem);
                }
                break;
            }
            case "Landmark":
            {
                int Index = -1;
                Landmark LandmarkItem = (Landmark) Telemetry;
                for (Landmark LandmarkObj : UserObj.getOwnedLandmarkTelemetry())
                {
                    if (LandmarkObj.getID().equals(LandmarkItem.getID()))
                    {
                        Index = UserObj.getOwnedLandmarkTelemetry().indexOf(LandmarkObj);
                        break;
                    }
                }
                if (Index != -1)
                {
                    UserObj.getOwnedLandmarkTelemetry().set(Index, LandmarkItem);
                }
                else
                {
                    UserObj.getOwnedLandmarkTelemetry().add(LandmarkItem);
                }
                break;
            }
            case "VipPhoneNumber":
            {
                int Index = -1;
                VipPhoneNumber VipPhoneItem = (VipPhoneNumber) Telemetry;
                for (VipPhoneNumber VipPhoneObj : UserObj.getOwnedVipPhoneTelemetry())
                {
                    if (VipPhoneObj.getID().equals(VipPhoneItem.getID()))
                    {
                        Index = UserObj.getOwnedVipPhoneTelemetry().indexOf(VipPhoneObj);
                        break;
                    }
                }
                if (Index != -1)
                {
                    UserObj.getOwnedVipPhoneTelemetry().set(Index, VipPhoneItem);
                }
                else
                {
                    UserObj.getOwnedVipPhoneTelemetry().add(VipPhoneItem);
                }
                break;
            }
            case "General":
            {
                int Index = -1;
                General GeneralItem = (General) Telemetry;
                for (General GeneralObj : UserObj.getOwnedGeneralTelemetry())
                {
                    if (GeneralObj.getID().equals(GeneralItem.getID()))
                    {
                        Index = UserObj.getOwnedGeneralTelemetry().indexOf(GeneralObj);
                        break;
                    }
                }
                if (Index != -1)
                {
                    UserObj.getOwnedGeneralTelemetry().set(Index, GeneralItem);
                }
                else
                {
                    UserObj.getOwnedGeneralTelemetry().add(GeneralItem);
                }
                break;
            }
            case "Service":
            {
                int Index = -1;
                Service ServiceItem = (Service) Telemetry;
                for (Service ServiceObj : UserObj.getOwnedServiceTelemetry())
                {
                    if (ServiceObj.getID().equals(ServiceItem.getID()))
                    {
                        Index = UserObj.getOwnedServiceTelemetry().indexOf(ServiceObj);
                        break;
                    }
                }
                if (Index != -1)
                {
                    UserObj.getOwnedServiceTelemetry().set(Index, ServiceItem);
                }
                else
                {
                    UserObj.getOwnedServiceTelemetry().add(ServiceItem);
                }
                break;
            }
        }
    }
}