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
import com.example.eauction.Interfaces.SetUserTelemetryCallback;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.FireStoreResult;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
            Log.d("UserObjProperties", "MSG Fragment: " + PreferenceUtils.getEmail(this.getContext()));
            AppInstance.GetFireStoreInstance().GetUserInformation(UserObj ->
            {
                this.UserObj = UserObj;
                Log.d("UserObjProperties", "SSN Fragment: " + this.UserObj.getSsn());

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
                        //TODO Apply the logic you need for the baseprice then update object and set it as auctioned
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
        Log.d("UserObjProperties", "Type Name: " + TelemetryType);
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
                                Log.d("UserObjProperties", "Successfully added the auction into the global auction collection");
                            }
                            else
                            {
                                Log.d("UserObjProperties", "Failed to add the auction into the global auction collection");
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
                        Log.d("UserObjProperties", "SetUserInformation Updated successfully");
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
                    }
                }, UserObj);
            }
            break;
        }
    }

    private void HandleUserTelemetry(Telemetry Telemetry, String Type)
    {
        if (Type.equals("CarPlate"))
        {
            ArrayList<CarPlate> CarPlateTelemetries = UserObj.getOwnedCarPlateTelemetry();
            CarPlate CarPlateItem = (CarPlate)Telemetry;
            for (CarPlate CarPlateObj : UserObj.getOwnedCarPlateTelemetry())
            {
                if (CarPlateObj.IsEqual(CarPlateItem))
                {
                    CarPlateTelemetries.remove(CarPlateObj);
                    CarPlateTelemetries.add(CarPlateItem);
                    break;
                }
            }
            UserObj.setOwnedCarPlateTelemetry(CarPlateTelemetries);
        }
        else if (Type.equals("Car"))
        {
            ArrayList<Car> CarTelemetries = UserObj.getOwnedCarTelemetry();
            Car CarItem = (Car)Telemetry;
            for (Car CarObj : UserObj.getOwnedCarTelemetry())
            {
                if (CarObj.IsEqual(CarItem))
                {
                    CarTelemetries.remove(CarObj);
                    CarTelemetries.add(CarItem);
                    break;
                }
            }
            UserObj.setOwnedCarTelemetry(CarTelemetries);
        }
        else if (Type.equals("Landmark"))
        {
            ArrayList<Landmark> LandmarkTelemetries = UserObj.getOwnedLandmarkTelemetry();
            Landmark LandmarkItem = (Landmark)Telemetry;
            for (Landmark LandmarkObj : UserObj.getOwnedLandmarkTelemetry())
            {
                if (LandmarkObj.IsEqual(LandmarkItem))
                {
                    LandmarkTelemetries.remove(LandmarkObj);
                    LandmarkTelemetries.add(LandmarkItem);
                    break;
                }
            }
            UserObj.setOwnedLandmarkTelemetry(LandmarkTelemetries);
        }
        else if (Type.equals("VipPhoneNumber"))
        {
            ArrayList<VipPhoneNumber> VipPhoneNumberTelemetries = UserObj.getOwnedVipPhoneTelemetry();
            VipPhoneNumber VipPhoneItem = (VipPhoneNumber)Telemetry;
            for (VipPhoneNumber VipPhoneObj : UserObj.getOwnedVipPhoneTelemetry())
            {
                if (VipPhoneObj.IsEqual(VipPhoneItem))
                {
                    VipPhoneNumberTelemetries.remove(VipPhoneObj);
                    VipPhoneNumberTelemetries.add(VipPhoneItem);
                    break;
                }
            }
            UserObj.setOwnedVipPhoneTelemetry(VipPhoneNumberTelemetries);
        }
        else if (Type.equals("General"))
        {
            ArrayList<General> GeneralTelemetries = UserObj.getOwnedGeneralTelemetry();
            General GeneralItem = (General)Telemetry;
            for (General GeneralObj : UserObj.getOwnedGeneralTelemetry())
            {
                if (GeneralObj.IsEqual(GeneralItem))
                {
                    GeneralTelemetries.remove(GeneralObj);
                    GeneralTelemetries.add(GeneralItem);
                    break;
                }
            }
            UserObj.setOwnedGeneralTelemetry(GeneralTelemetries);
        }
        else if (Type.equals("Service"))
        {
            ArrayList<Service> ServiceTelemetries = UserObj.getOwnedServiceTelemetry();
            Service ServiceItem = (Service)Telemetry;
            for (Service ServiceObj : UserObj.getOwnedServiceTelemetry())
            {
                if (ServiceObj.IsEqual(ServiceItem))
                {
                    ServiceTelemetries.remove(ServiceObj);
                    ServiceTelemetries.add(ServiceItem);
                    break;
                }
            }
            UserObj.setOwnedServiceTelemetry(ServiceTelemetries);
        }
    }
}