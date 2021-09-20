package com.example.eauction.DataBase;

import android.util.Log;

import com.example.eauction.Interfaces.GetFieldUserCallback;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Interfaces.SetUserIsActiveCallback;
import com.example.eauction.Interfaces.SetUserOwnedTelemetryCallback;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

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
        DB.collection("USERS").document(Email).update("active", IsActive)
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
                    Result = UserObj.getActive();
                }
            }
            GetFieldCallback.onCallback(Result);
        })
        .addOnFailureListener(d ->
        {
            GetFieldCallback.onCallback(null);
        });
    }

    public void SetUserOwnedTelemetry(final SetUserOwnedTelemetryCallback Callback, List<Telemetry> OwnedTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedTelemetry", OwnedTelemetry)
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