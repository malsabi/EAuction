package com.example.eauction.DataBase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eauction.Cryptograpgy.FirestoreEncoder;
import com.example.eauction.Cryptograpgy.Hashing;
import com.example.eauction.Interfaces.GetCarAuctionsCallback;
import com.example.eauction.Interfaces.GetCarPlateAuctionsCallback;
import com.example.eauction.Interfaces.GetGeneralAuctionsCallback;
import com.example.eauction.Interfaces.GetLandmarkAuctionsCallback;
import com.example.eauction.Interfaces.GetServiceAuctionsCallback;
import com.example.eauction.Interfaces.GetUserTelemetryCallback;
import com.example.eauction.Interfaces.GetVIPPhoneAuctionsCallback;
import com.example.eauction.Interfaces.SetUserTelemetryCallback;
import com.example.eauction.Interfaces.RegisterUserCallback;
import com.example.eauction.Interfaces.SignInUserCallback;
import com.example.eauction.Interfaces.SignOutUserCallback;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.Validations.UserValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FireStoreManager extends FireStoreHelpers
{
    private final FirebaseFirestore DB;

    public FireStoreManager()
    {
        DB = FirebaseFirestore.getInstance();
        SetFirebaseFirestore(DB);
    }

    public void RegisterUser(final RegisterUserCallback RegisterCallback, User UserObj)
    {
        FireStoreResult Result = new FireStoreResult();
        UserValidation Validation = new UserValidation();
        ValidationResult UserValidationResult = Validation.ValidateRegister(UserObj);
        if (!UserValidationResult.isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle(UserValidationResult.getTitle());
            Result.setMessage(UserValidationResult.getMessage());
            RegisterCallback.onCallback(Result);
        }
        else
        {
            UserObj.setEmail(FirestoreEncoder.EncodeForFirebaseKey(Hashing.SHA256(UserObj.getEmail())));
            UserObj.setPassword(FirestoreEncoder.EncodeForFirebaseKey(Hashing.SHA256(UserObj.getPassword())));
            Log.d("TAG", "Attempting to make set request");
            DB.collection("USERS").document(UserObj.getEmail()).set(UserObj)
            .addOnSuccessListener(d ->
            {
                Log.d("TAG", "Successfully added the item");
                Result.setSuccess(true);
                Result.setTitle("Success");
                Result.setMessage("User successfully registered");
                RegisterCallback.onCallback(Result);
            })
            .addOnFailureListener(e ->
            {
                Log.d("TAG", "failed to add the item");
                Result.setSuccess(false);
                Result.setTitle("Error");
                Result.setMessage(e.getMessage());
                RegisterCallback.onCallback(Result);
            });
        }
    }

    public void SignInUser(final SignInUserCallback SignInCallback, SignIn SignInObj)
    {
        FireStoreResult Result = new FireStoreResult();
        UserValidation Validation = new UserValidation();

        ValidationResult UserValidationResult = Validation.ValidateSignIn(SignInObj);

        if (!UserValidationResult.isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle(UserValidationResult.getTitle());
            Result.setMessage(UserValidationResult.getMessage());
            SignInCallback.onCallback(Result, null);
        }
        else
        {
            Log.d("TAG", "Attempting to make get request");
            SignInObj.setEmail(FirestoreEncoder.EncodeForFirebaseKey(Hashing.SHA256(SignInObj.getEmail())));
            SignInObj.setPassword(FirestoreEncoder.EncodeForFirebaseKey(Hashing.SHA256(SignInObj.getPassword())));
            DB.collection("USERS").document(SignInObj.getEmail()).get()
            .addOnSuccessListener(d ->
            {
                User UserObj = null;
                if (d.exists())
                {
                    UserObj = d.toObject(User.class);
                    if (UserObj != null)
                    {
                        if (UserObj.getIsActive().equals("Online"))
                        {
                            Log.d("TAG", "This account is already signed in");
                            Result.setSuccess(false);
                            Result.setTitle("Exception");
                            Result.setMessage("failed to sign in, because this account is already signed in");
                            SignInCallback.onCallback(Result, null);
                        }
                        else
                        {
                            if (UserObj.getPassword().equals(SignInObj.getPassword()))
                            {
                                Log.d("TAG", "Received all documents: " + UserObj.getSsn());
                                User FinalUserObj = UserObj;
                                SetUserIsActive(IsOperatingSuccess ->
                                {
                                    Log.d("TAG", "SetUserIsActive");
                                    if (!IsOperatingSuccess)
                                    {
                                        Result.setSuccess(false);
                                        Result.setTitle("Exception");
                                        Result.setMessage("Failed to set the user activity");
                                        SignInCallback.onCallback(Result, null);
                                    }
                                    else
                                    {
                                        Result.setSuccess(true);
                                        Result.setTitle("Success");
                                        Result.setMessage("User successfully logged in");
                                        SignInCallback.onCallback(Result, FinalUserObj);
                                    }
                                }, "Online", SignInObj.getEmail());
                            }
                            else
                            {
                                Log.d("TAG", "failed to sign in, make sure you typed the correct password!");
                                Result.setSuccess(false);
                                Result.setTitle("Exception");
                                Result.setMessage("failed to sign in, make sure you typed the correct password!");
                                SignInCallback.onCallback(Result, null);
                            }
                        }
                    }
                }
                else
                {
                    Log.d("TAG", "failed to sign in, make sure you are registered!");
                    Result.setSuccess(false);
                    Result.setTitle("Exception");
                    Result.setMessage("failed to sign in, make sure you are registered!");
                    SignInCallback.onCallback(Result, null);
                }

            })
            .addOnFailureListener(e ->
            {
                Log.d("TAG", "failed to sign in, make sure you are registered!");
                Result.setSuccess(false);
                Result.setTitle("Exception");
                Result.setMessage(e.getMessage());
                SignInCallback.onCallback(Result, null);
            });
        }
    }

    public void SignOut(final SignOutUserCallback SignOutCallback, User UserObj)
    {
        FireStoreResult Result = new FireStoreResult();
        SetUserIsActive(IsOperationSuccess ->
        {
            if (!IsOperationSuccess)
            {
                Result.setSuccess(false);
                Result.setTitle("Exception");
                Result.setMessage("Failed to sign out");
            }
            else
            {
                Result.setSuccess(true);
                Result.setMessage("Successfully signed out");
            }
            SignOutCallback.onCallback(Result);
        }, "Offline", UserObj.getEmail());
    }

    public void AddCarPlateAuction(SetUserTelemetryCallback AddCarPlateCallback, CarPlate CarPlateTelemetry)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("CARPLATE");
        //Atomically add a new region to the "regions" array field.
        CarPlateDocument.update("CarPlateTelemetries", FieldValue.arrayUnion(CarPlateTelemetry))
        .addOnSuccessListener(d ->
        {
            AddCarPlateCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddCarPlateCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void GetCarPlateAuctions(GetCarPlateAuctionsCallback Callback)
    {
        ArrayList<CarPlate> CarPlates = new ArrayList<>();
        DocumentReference documentReference = DB.collection("AUCTIONS").document("CARPLATE");
        documentReference.get().addOnSuccessListener(documentSnapshot ->
        {
            if (documentSnapshot.exists())
            {
                ArrayList<?> CarPlateTelemetries = (ArrayList<?>)documentSnapshot.get("CarPlateTelemetries");
                Gson gson = new Gson();
                for (int i = 0; i < (CarPlateTelemetries != null ? CarPlateTelemetries.size() : 0); i++)
                {
                    JsonElement jsonElement = gson.toJsonTree(CarPlateTelemetries.get(i));
                    CarPlate CarPlate = gson.fromJson(jsonElement, CarPlate.class);
                    CarPlates.add(CarPlate);
                    Log.d("UserObjCarPlate", "CarPlates: " + CarPlate.getPlateNumber());
                }
                Callback.onCallback(CarPlates);
            }
        });
    }

    public void AddCarAuction(SetUserTelemetryCallback AddCarCallback, Car CarTelemetry)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("CAR");
        //Atomically add a new region to the "regions" array field.
        CarPlateDocument.update("CarTelemetries", FieldValue.arrayUnion(CarTelemetry))
        .addOnSuccessListener(d ->
        {
            AddCarCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddCarCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void GetCarAuctions(GetCarAuctionsCallback Callback)
    {
        ArrayList<Car> Cars = new ArrayList<>();
        DocumentReference documentReference = DB.collection("AUCTIONS").document("CAR");
        documentReference.get().addOnSuccessListener(documentSnapshot ->
        {
            if (documentSnapshot.exists())
            {
                ArrayList<?> CarTelemetries = (ArrayList<?>)documentSnapshot.get("CarTelemetries");
                Gson gson = new Gson();
                for (int i = 0; i < (CarTelemetries != null ? CarTelemetries.size() : 0); i++)
                {
                    if (!CarTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(CarTelemetries.get(i));
                        Car Car = gson.fromJson(jsonElement, Car.class);
                        Cars.add(Car);
                        Log.d("UserObjCar", "CarModel: " + Car.getModel());
                    }
                }
                Callback.onCallback(Cars);
            }
        });
    }

    public void AddLandmarkAuction(SetUserTelemetryCallback AddLandmarkCallback, Landmark LandmarkTelemetry)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("LANDMARK");
        //Atomically add a new region to the "regions" array field.
        CarPlateDocument.update("LandmarkTelemetries", FieldValue.arrayUnion(LandmarkTelemetry))
        .addOnSuccessListener(d ->
        {
            AddLandmarkCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddLandmarkCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void GetLandmarkAuctions(GetLandmarkAuctionsCallback Callback)
    {
        ArrayList<Landmark> Landmarks = new ArrayList<>();
        DocumentReference documentReference = DB.collection("AUCTIONS").document("LANDMARK");
        documentReference.get().addOnSuccessListener(documentSnapshot ->
        {
            if (documentSnapshot.exists())
            {
                ArrayList<?> LandmarkTelemetries = (ArrayList<?>)documentSnapshot.get("LandmarkTelemetries");
                Gson gson = new Gson();
                for (int i = 0; i < (LandmarkTelemetries != null ? LandmarkTelemetries.size() : 0); i++)
                {
                    JsonElement jsonElement = gson.toJsonTree(LandmarkTelemetries.get(i));
                    Landmark Landmark = gson.fromJson(jsonElement, Landmark.class);
                    Landmarks.add(Landmark);
                    Log.d("UserObjCar", "LandmarkType: " + Landmark.getType());
                }
                Callback.onCallback(Landmarks);
            }
        });
    }

    public void AddVIPPhoneNumberAuction(SetUserTelemetryCallback AddVIPPhoneNumberCallback, VipPhoneNumber VipPhoneNumberTelemetry)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("VIPPHONE");
        //Atomically add a new region to the "regions" array field.
        CarPlateDocument.update("VIPPhoneTelemetries", FieldValue.arrayUnion(VipPhoneNumberTelemetry))
        .addOnSuccessListener(d ->
        {
            AddVIPPhoneNumberCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddVIPPhoneNumberCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void GetVIPPHoneNumberAuctions(GetVIPPhoneAuctionsCallback Callback)
    {
        ArrayList<VipPhoneNumber> VIPPhoneNumbers = new ArrayList<>();
        DocumentReference documentReference = DB.collection("AUCTIONS").document("VIPPHONE");
        documentReference.get().addOnSuccessListener(documentSnapshot ->
        {
            if (documentSnapshot.exists())
            {
                ArrayList<?> VIPPhoneTelemetries = (ArrayList<?>)documentSnapshot.get("VIPPhoneTelemetries");
                Gson gson = new Gson();
                for (int i = 0; i < (VIPPhoneTelemetries != null ? VIPPhoneTelemetries.size() : 0); i++)
                {
                    JsonElement jsonElement = gson.toJsonTree(VIPPhoneTelemetries.get(i));
                    VipPhoneNumber VipPhoneNumber = gson.fromJson(jsonElement, VipPhoneNumber.class);
                    VIPPhoneNumbers.add(VipPhoneNumber);
                    Log.d("UserObjCar", "PhoneNumber: " + VipPhoneNumber.getPhoneNumber());
                }
                Callback.onCallback(VIPPhoneNumbers);
            }
        });
    }

    public void AddGeneralAuction(SetUserTelemetryCallback AddGeneralCallback, General GeneralTelemetry)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("GENERAL");
        //Atomically add a new region to the "regions" array field.
        CarPlateDocument.update("GeneralTelemetries", FieldValue.arrayUnion(GeneralTelemetry))
        .addOnSuccessListener(d ->
        {
            AddGeneralCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddGeneralCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void GetGeneralAuctions(GetGeneralAuctionsCallback Callback)
    {
        ArrayList<General> Generals = new ArrayList<>();
        DocumentReference documentReference = DB.collection("AUCTIONS").document("GENERAL");
        documentReference.get().addOnSuccessListener(documentSnapshot ->
        {
            if (documentSnapshot.exists())
            {
                ArrayList<?> GeneralTelemetries = (ArrayList<?>)documentSnapshot.get("GeneralTelemetries");
                Gson gson = new Gson();
                for (int i = 0; i < (GeneralTelemetries != null ? GeneralTelemetries.size() : 0); i++)
                {
                    JsonElement jsonElement = gson.toJsonTree(GeneralTelemetries.get(i));
                    General General = gson.fromJson(jsonElement, General.class);
                    Generals.add(General);
                    Log.d("UserObjCar", "General Name: " + General.getName());
                }
                Callback.onCallback(Generals);
            }
        });
    }

    public void AddServiceAuction(SetUserTelemetryCallback AddServiceCallback, Service ServiceTelemetry)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("SERVICE");
        //Atomically add a new region to the "regions" array field.
        CarPlateDocument.update("ServiceTelemetries", FieldValue.arrayUnion(ServiceTelemetry))
        .addOnSuccessListener(d ->
        {
            AddServiceCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddServiceCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }

    public void GetServiceAuctions(GetServiceAuctionsCallback Callback)
    {
        ArrayList<Service> Services = new ArrayList<>();
        DocumentReference documentReference = DB.collection("AUCTIONS").document("SERVICE");
        documentReference.get().addOnSuccessListener(documentSnapshot ->
        {
            if (documentSnapshot.exists())
            {
                ArrayList<?> ServiceTelemetries = (ArrayList<?>)documentSnapshot.get("ServiceTelemetries");
                Gson gson = new Gson();
                for (int i = 0; i < (ServiceTelemetries != null ? ServiceTelemetries.size() : 0); i++)
                {
                    JsonElement jsonElement = gson.toJsonTree(ServiceTelemetries.get(i));
                    Service Service = gson.fromJson(jsonElement, Service.class);
                    Services.add(Service);
                    Log.d("UserObjCar", "Service Name: " + Service.getName());
                }
                Callback.onCallback(Services);
            }
        });
    }
}