package com.example.eauction.DataBase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eauction.Cryptograpgy.AESProvider;
import com.example.eauction.Cryptograpgy.FirestoreEncoder;
import com.example.eauction.Interfaces.GetFieldUserCallback;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Interfaces.IsUserRegisteredCallback;
import com.example.eauction.Interfaces.SetUserInformationCallback;
import com.example.eauction.Interfaces.SetUserIsActiveCallback;
import com.example.eauction.Interfaces.SetUserTelemetryCallback;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.User;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.Utilities.SyncFireStore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

    //region User Helpers
    public void GetUserInformation(final GetUserInformationCallback GetInformationCallback, String Email)
    {
        DB.collection("USERS").document(Email).get()
        .addOnSuccessListener(d ->
        {
            if (d.exists())
            {
                User UserObj = d.toObject(User.class);
                assert UserObj != null;
                Log.d("FireStore", "GetUserInformation Success User Id: " + UserObj.getId());
                GetInformationCallback.onCallback(UserObj);
            }
            else
            {
                Log.d("FireStore", "GetUserInformation Failed user does not exists");
                GetInformationCallback.onCallback(null);
            }
        })
        .addOnFailureListener(d ->
        {
            Log.d("FireStore", "GetUserInformation Error: " + d.getMessage());
            GetInformationCallback.onCallback(null);
        });
    }
    public void SetUserInformation(final SetUserInformationCallback SetInformationCallback, User UserObj)
    {
        DB.collection("USERS").document(UserObj.getEmail()).set(UserObj)
       .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetUserInformation Success");
            SetInformationCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            Log.d("FireStore", "SetUserInformation Failed");
            SetInformationCallback.onCallback(false);
        });
    }
    public void SetUserIsActive(final SetUserIsActiveCallback IsActiveCallback, String IsActive, String Email)
    {
        DB.collection("USERS").document(Email).update("isActive", IsActive)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetUserIsActive Success");
            IsActiveCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            Log.d("FireStore", "SetUserIsActive Failed: " + d.getMessage());
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
                    Log.d("FireStore", "GetUserIsActive Success User Id: " + UserObj.getId());
                    Result = UserObj.getIsActive();
                }
            }
            GetFieldCallback.onCallback(Result);
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "GetUserIsActive Failed: " + e.getMessage());
            GetFieldCallback.onCallback(null);
        });
    }
    public void SetUserImage(String Email, String Image)
    {

        DB.collection("USERS").document(Email).update("profilePicture", Image)
        .addOnSuccessListener(u ->
        {
            Log.d("FireStore", "SetUserImage Success");
        }).addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetUserImage Failed: " + e.getMessage());
        });
    }
    //endregion

    //region Telemetry Helpers
    public void SetCarTelemetry(final SetUserTelemetryCallback Callback, ArrayList<Car> CarTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedCarTelemetry", CarTelemetry)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetCarTelemetry Succeeded");
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetCarTelemetry Failed: " + e.getMessage());
            Callback.onCallback(new FireStoreResult("", e.getMessage(), false));
        });
    }
    public void SetCarPlateTelemetry(final SetUserTelemetryCallback Callback, ArrayList<CarPlate> CarPlateTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedCarPlateTelemetry", CarPlateTelemetry)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetCarPlateTelemetry Succeeded");
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetCarPlateTelemetry Failed: " + e.getMessage());
            Callback.onCallback(new FireStoreResult("", e.getMessage(), false));
        });
    }
    public void SetVipPhoneTelemetry(final SetUserTelemetryCallback Callback, ArrayList<VipPhoneNumber> VipPhoneTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedVipPhoneTelemetry", VipPhoneTelemetry)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetVipPhoneTelemetry Succeeded");
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetVipPhoneTelemetry Failed: " + e.getMessage());
            Callback.onCallback(new FireStoreResult("", e.getMessage(), false));
        });
    }
    public void SetLandmarkTelemetry(final SetUserTelemetryCallback Callback, ArrayList<Landmark> LandmarkTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedLandmarkTelemetry", LandmarkTelemetry)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetLandmarkTelemetry Succeeded");
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetLandmarkTelemetry Failed: " + e.getMessage());
            Callback.onCallback(new FireStoreResult("", e.getMessage(), false));
        });
    }
    public void SetGeneralTelemetry(final SetUserTelemetryCallback Callback, ArrayList<General> GeneralTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedGeneralTelemetry", GeneralTelemetry)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetGeneralTelemetry Succeeded");
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetGeneralTelemetry Failed: " + e.getMessage());
            Callback.onCallback(new FireStoreResult("", e.getMessage(), false));
        });
    }
    public void SetServiceTelemetry(final SetUserTelemetryCallback Callback, ArrayList<Service> ServiceTelemetry, String Email)
    {
        DB.collection("USERS").document(Email).update("ownedServiceTelemetry", ServiceTelemetry)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetServiceTelemetry Succeeded");
            Callback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetServiceTelemetry Failed: " + e.getMessage());
            Callback.onCallback(new FireStoreResult("", e.getMessage(), false));
        });
    }
    //endregion

    //region PrivateKey Helpers
    public void SetPrivateKey(String PrivateKey)
    {
        Log.d("FireStore", "SetPrivateKey Method Started");
        Map<String, Object> HashMap = new HashMap<>();
        HashMap.put("Key", PrivateKey);

        DB.collection("SECURITY").document("Key1").set(HashMap)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetPrivateKey Method Finished: Success");
        }).addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetPrivateKey Method Error: " + e.getMessage());
        });
    }
    public void SetPublicKey(String PublicKey)
    {
        Log.d("FireStore", "SetPublicKey Method Started");

        Map<String, Object> HashMap = new HashMap<>();
        HashMap.put("Key", PublicKey);

        DB.collection("SECURITY").document("Key2").set(HashMap)
        .addOnSuccessListener(d ->
        {
            Log.d("FireStore", "SetPublicKey Method Finished: Success");
        }).addOnFailureListener(e ->
        {
            Log.d("FireStore", "SetPublicKey Method Error: " + e.getMessage());
        });
    }
    public String GetPrivateKey()
    {
        Log.d("FireStore", "GetPrivateKey Method Started");
        String EncryptedPrivateKey = (String)new SyncFireStore().GetField("SECURITY", "Key1", "Key", DB);
        Log.d("FireStore", "GetPrivateKey Method Finished, EncryptedPrivateKey: " + EncryptedPrivateKey);
        return EncryptedPrivateKey;
    }

    public String GetPublicKey()
    {
        Log.d("FireStore", "GetPublicKey Method Started");
        String EncryptedPublicKey = (String)new SyncFireStore().GetField("SECURITY", "Key2", "Key", DB);
        Log.d("FireStore", "GetPublicKey Method Finished, EncryptedPublicKey: " + EncryptedPublicKey);
        return EncryptedPublicKey;
    }
    //endregion
}