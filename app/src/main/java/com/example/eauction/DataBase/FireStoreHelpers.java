package com.example.eauction.DataBase;

import android.util.Log;
import com.example.eauction.Interfaces.GetFieldUserCallback;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Interfaces.SetUserIsActiveCallback;
import com.example.eauction.Interfaces.SetUserTelemetryCallback;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.User;
import com.example.eauction.Models.VipPhoneNumber;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class FireStoreHelpers
{
    private FirebaseFirestore DB;

    public FireStoreHelpers()
    {
    }

    public void SetFirebaseFirestore(FirebaseFirestore DB)
    {
        this.DB = DB;
    }

    public void GetUserInformation(final GetUserInformationCallback GetInformationCallback, String Email)
    {
        DB.collection("USERS").document(Email).get()
        .addOnSuccessListener(d ->
        {
            if (d.exists())
            {
                User UserObj = d.toObject(User.class);
                GetInformationCallback.onCallback(UserObj);
            }
            else
            {
                Log.d("InsertActivity", "Not Existed");
                GetInformationCallback.onCallback(null);
            }
        })
        .addOnFailureListener(d ->
        {
            Log.d("InsertActivity", "Error: " + d.getMessage());
            GetInformationCallback.onCallback(null);
        });
    }

    public void SetUserIsActive(final SetUserIsActiveCallback IsActiveCallback, String IsActive, String Email)
    {
        DB.collection("USERS").document(Email).update("isActive", IsActive)
        .addOnSuccessListener(d ->
        {
            IsActiveCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            IsActiveCallback.onCallback(false);
        });
    }

    public void GetUserIsActive(final GetFieldUserCallback GetFieldCallback, String Email)
    {
        DB.collection("USERS").document(Email).get()
        .addOnSuccessListener(d ->
        {
            String Result = null;
            if (d.exists())
            {
                User UserObj = d.toObject(User.class);
                if (UserObj != null)
                {
                    Result = UserObj.getIsActive();
                }
            }
            GetFieldCallback.onCallback(Result);
        })
        .addOnFailureListener(d ->
        {
            GetFieldCallback.onCallback(null);
        });
    }
    public void SetCarTelemetry(final SetUserTelemetryCallback Callback, ArrayList<Car> CarTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedCarTelemetry", CarTelemetry)
        .addOnSuccessListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void SetCarPlateTelemetry(final SetUserTelemetryCallback Callback, ArrayList<CarPlate> CarPlateTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedCarPlateTelemetry", CarPlateTelemetry)
        .addOnSuccessListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void SetVipPhoneTelemetry(final SetUserTelemetryCallback Callback, ArrayList<VipPhoneNumber> VipPhoneTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedVipPhoneTelemetry", VipPhoneTelemetry)
        .addOnSuccessListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void SetLandmarkTelemetry(final SetUserTelemetryCallback Callback, ArrayList<Landmark> LandmarkTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedLandmarkTelemetry", LandmarkTelemetry)
        .addOnSuccessListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }
    public void SetGeneralTelemetry(final SetUserTelemetryCallback Callback, ArrayList<General> GeneralTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedGeneralTelemetry", GeneralTelemetry)
        .addOnSuccessListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            Callback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }
}