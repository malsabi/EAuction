package com.example.eauction.DataBase;

import android.util.Log;

import com.example.eauction.Interfaces.GetFieldUserCallback;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Interfaces.SetUserIsActiveCallback;
import com.example.eauction.Models.User;
import com.google.firebase.firestore.FirebaseFirestore;

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
                GetInformationCallback.onCallback(null);
            }
        })
        .addOnFailureListener(d ->
        {
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
            Log.d("FAILED", d.getMessage());
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
}