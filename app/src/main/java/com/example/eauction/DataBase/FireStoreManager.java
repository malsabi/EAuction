package com.example.eauction.DataBase;

import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eauction.Cryptograpgy.AESProvider;
import com.example.eauction.Cryptograpgy.FirestoreEncoder;
import com.example.eauction.Cryptograpgy.Hashing;
import com.example.eauction.Interfaces.AddUserSessionCallback;
import com.example.eauction.Interfaces.DeleteUserAuctionCallback;
import com.example.eauction.Interfaces.DeleteUserSessionCallback;
import com.example.eauction.Interfaces.GetCarAuctionsCallback;
import com.example.eauction.Interfaces.GetCarPlateAuctionsCallback;
import com.example.eauction.Interfaces.GetGeneralAuctionsCallback;
import com.example.eauction.Interfaces.GetLandmarkAuctionsCallback;
import com.example.eauction.Interfaces.GetServiceAuctionsCallback;
import com.example.eauction.Interfaces.GetUserInformationCallback;
import com.example.eauction.Interfaces.GetUserSessionCallback;
import com.example.eauction.Interfaces.GetVIPPhoneAuctionsCallback;
import com.example.eauction.Interfaces.IsUserRegisteredCallback;
import com.example.eauction.Interfaces.IsUserSessionAddedCallback;
import com.example.eauction.Interfaces.SetUserIsActiveCallback;
import com.example.eauction.Interfaces.SetUserTelemetryCallback;
import com.example.eauction.Interfaces.RegisterUserCallback;
import com.example.eauction.Interfaces.SignInUserCallback;
import com.example.eauction.Interfaces.SignOutUserCallback;
import com.example.eauction.Interfaces.UpdateCarAuctionsCallback;
import com.example.eauction.Interfaces.UpdateCarPlateAuctionsCallback;
import com.example.eauction.Interfaces.UpdateLandmarkAuctionsCallback;
import com.example.eauction.Interfaces.UpdateServiceAuctionCallback;
import com.example.eauction.Interfaces.UpdateUserPasswordCallback;
import com.example.eauction.Interfaces.UpdateUserSessionCallback;
import com.example.eauction.Interfaces.UpdateVIPPHoneNumberCallback;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.SessionModel;
import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.Utilities.SyncFireStore;
import com.example.eauction.Validations.UserValidation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.security.auth.callback.Callback;

public class FireStoreManager extends FireStoreHelpers
{
    private final FirebaseFirestore DB;
    private final AESProvider AesProvider;

    public FireStoreManager()
    {
        DB = FirebaseFirestore.getInstance();
        SetFirebaseFirestore(DB);
        AesProvider = new AESProvider(this);
        //SetCompanyAccount();
        //SetCompanyPassword();
        //SetPrivateKey(AesProvider.CreatePrivateKey());
        //SetPublicKey(AesProvider.CreatePublicKey());
        //AesProvider.GetPrivateKey();
    }

    //region Getters
    public AESProvider GetAesProvider()
    {
        return AesProvider;
    }
    //endregion

    //region User
    public void IsUserRegistered(IsUserRegisteredCallback IsRegisteredCallback, String Email, boolean EncryptEmail)
    {
        if (EncryptEmail && Email.length() != 0)
        {
            Email = FirestoreEncoder.EncodeForFirebaseKey(AesProvider.Encrypt(Email));
        }
        DB.collection("USERS").document(Email).get()
        .addOnSuccessListener(d ->
        {
            IsRegisteredCallback.onCallback(d.exists());
        })
        .addOnFailureListener(d -> IsRegisteredCallback.onCallback(false));
    }

    public void UpdateUserPassword(UpdateUserPasswordCallback UpdatePasswordCallback, String Email, String NewPassword, boolean EncryptEmail)
    {
        if (EncryptEmail && Email.length() != 0)
        {
            Email = FirestoreEncoder.EncodeForFirebaseKey(AesProvider.Encrypt(Email));
        }
        NewPassword = FirestoreEncoder.EncodeForFirebaseKey(Objects.requireNonNull(Hashing.SHA256(NewPassword)));
        DB.collection("USERS").document(Email).update("password", NewPassword)
        .addOnSuccessListener(d -> UpdatePasswordCallback.onCallback(true))
        .addOnFailureListener(d -> UpdatePasswordCallback.onCallback(false));
    }
    //endregion

    //region Session
    public void AddUserSession(final AddUserSessionCallback AddSessionCallback, SessionModel Session)
    {
        DB.collection("SESSIONS").document(Session.getUserId()).set(Session)
        .addOnSuccessListener(o ->
        {
            AddSessionCallback.onCallback(true);
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "AddUserSession Exception: " + e.getMessage());
            AddSessionCallback.onCallback(false);
        });
    }
    public void DeleteUserSession(final DeleteUserSessionCallback DeleteSessionCallback, String UserId)
    {
        DB.collection("SESSIONS").document(UserId).delete()
        .addOnSuccessListener(o ->
        {
            DeleteSessionCallback.onCallback(true);
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "DeleteUserSession Exception: " + e.getMessage());
            DeleteSessionCallback.onCallback(false);
        });
    }
    public void UpdateUserSession(final UpdateUserSessionCallback UpdateSessionCallback, String UserId, String LastSeen)
    {
        HashMap<String, Object> UpdatedField = new HashMap<>();
        UpdatedField.put("lastSeen", LastSeen.toString());
        DB.collection("SESSIONS").document(UserId).update(UpdatedField)
        .addOnSuccessListener(o ->
        {
            UpdateSessionCallback.onCallback(true);
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "UpdateUserSession Exception: " + e.getMessage());
            UpdateSessionCallback.onCallback(false);
        });
    }
    public void IsUserSessionAdded(final IsUserSessionAddedCallback IsSessionAddedCallback, String UserId)
    {
        DB.collection("SESSIONS").document(UserId).get()
        .addOnSuccessListener(d ->
        {
            IsSessionAddedCallback.onCallback(d.exists());
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "IsUserSessionAdded Exception: " + e.getMessage());
            IsSessionAddedCallback.onCallback(false);
        });
    }
    public void GetUserSession(final GetUserSessionCallback GetSessionCallback, String UserId)
    {
        DB.collection("SESSIONS").document(UserId).get()
        .addOnSuccessListener(d ->
        {
            GetSessionCallback.onCallback(d.toObject(SessionModel.class));
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "GetUserSession Exception: " + e.getMessage());
            GetSessionCallback.onCallback(null);
        });
    }
    //endregion

    //region Identity
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
            UserObj.setId(FirestoreEncoder.EncodeForFirebaseKey(AesProvider.Encrypt(UserObj.getId())));
            UserObj.setEmail(FirestoreEncoder.EncodeForFirebaseKey(AesProvider.Encrypt(UserObj.getEmail())));
            UserObj.setPassword(FirestoreEncoder.EncodeForFirebaseKey(Objects.requireNonNull(Hashing.SHA256(UserObj.getPassword()))));
            Log.d("FireStore", "RegisterUser Starting to register the user into the database");
            DB.collection("USERS").document(UserObj.getEmail()).set(UserObj)
            .addOnSuccessListener(d ->
            {
                Log.d("FireStore", "RegisterUser Successfully added the user");
                Result.setSuccess(true);
                Result.setTitle("Success");
                Result.setMessage("User successfully registered");
                RegisterCallback.onCallback(Result);
            })
            .addOnFailureListener(e ->
            {
                Log.d("FireStore", "RegisterUser failed to add the user: " + e.getMessage());
                Result.setSuccess(false);
                Result.setTitle("Error");
                Result.setMessage(e.getMessage());
                RegisterCallback.onCallback(Result);
            });
        }
    }

    public void SignInUser(final SignInUserCallback SignInCallback, SignIn SignInObj) {
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
            Log.d("FireStore", "SignInUser Attempting to make get request");
            SignInObj.setEmail(FirestoreEncoder.EncodeForFirebaseKey(AesProvider.Encrypt(SignInObj.getEmail())));
            SignInObj.setPassword(FirestoreEncoder.EncodeForFirebaseKey(Objects.requireNonNull(Hashing.SHA256(SignInObj.getPassword()))));
            SetUserIsActive(SetUserIsActiveResult ->
            {
                if (SetUserIsActiveResult)
                {
                    DB.collection("USERS").document(SignInObj.getEmail()).get()
                    .addOnSuccessListener(d ->
                    {
                        if (d.exists())
                        {
                            User UserObj = d.toObject(User.class);
                            if (UserObj != null)
                            {
                                if (UserObj.getIsActive().equals("Online"))
                                {
                                    Log.d("FireStore", "SignInUser This account is already signed in");
                                    Result.setSuccess(false);
                                    Result.setTitle("Exception");
                                    Result.setMessage("failed to sign in, because this account is already signed in");
                                    SignInCallback.onCallback(Result, null);
                                }
                                else
                                {
                                    if (UserObj.getPassword().equals(SignInObj.getPassword()))
                                    {
                                        Log.d("FireStore", "SignInUser Received all documents: " + UserObj.getId());
                                        SetUserIsActive(IsOperatingSuccess ->
                                        {
                                            if (!IsOperatingSuccess)
                                            {
                                                Log.d("FireStore", "SignInUser SetUserIsActive: Failed");
                                                Result.setSuccess(false);
                                                Result.setTitle("Exception");
                                                Result.setMessage("Failed to set the user activity");
                                                SignInCallback.onCallback(Result, null);
                                            }
                                            else
                                            {
                                                Log.d("FireStore", "SignInUser SetUserIsActive: Success");
                                                UserObj.setIsActive("Online");
                                                Result.setSuccess(true);
                                                Result.setTitle("Success");
                                                Result.setMessage("User successfully logged in");
                                                SignInCallback.onCallback(Result, UserObj);
                                            }
                                        }, "Online", SignInObj.getEmail());
                                    }
                                    else
                                    {
                                        Log.d("FireStore", "SignInUser failed to sign in, make sure you typed the correct password!");
                                        Result.setSuccess(false);
                                        Result.setTitle("Exception");
                                        Result.setMessage("failed to sign in, incorrect password.");
                                        SignInCallback.onCallback(Result, null);
                                    }
                                }
                            }
                        } else {
                            Log.d("FireStore", "SignInUser failed to sign in, make sure you are registered!");
                            Result.setSuccess(false);
                            Result.setTitle("Exception");
                            Result.setMessage("failed to sign in, unregistered account.");
                            SignInCallback.onCallback(Result, null);
                        }

                    })
                    .addOnFailureListener(e ->
                    {
                        Log.d("FireStore", "SignInUser failed to sign in, make sure you are connected to the internet: " + e.getMessage());
                        Result.setSuccess(false);
                        Result.setTitle("Exception");
                        Result.setMessage(e.getMessage());
                        SignInCallback.onCallback(Result, null);
                    });
                }
            }, "Offline", SignInObj.getEmail());
        }
    }

    public void SignOut(final SignOutUserCallback SignOutCallback, String UserId)
    {
        FireStoreResult Result = new FireStoreResult();
        SetUserIsActive(IsOperationSuccess ->
        {
            if (!IsOperationSuccess)
            {
                Log.d("FireStore", "SignOut failed to SetUserIsActive to Offline");
                Result.setSuccess(false);
                Result.setTitle("Exception");
                Result.setMessage("Failed to sign out");
            }
            else
            {
                Log.d("FireStore", "SignOut succeeded to SetUserIsActive to Offline");
                Result.setSuccess(true);
                Result.setMessage("Successfully signed out");
            }
            SignOutCallback.onCallback(Result);
        }, "Offline", UserId);
    }
    //endregion

    //region Auctions
    //region "CarPlate Auction"
    public void AddCarPlateAuction(final SetUserTelemetryCallback AddCarPlateCallback, CarPlate CarPlateAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("CARPLATE");
        CarPlateDocument.update("CarPlateTelemetries", FieldValue.arrayUnion(CarPlateAuction))
        .addOnSuccessListener(d ->
        {
            AddCarPlateCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddCarPlateCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }
    public void DeleteCarPlateAuction(DeleteUserAuctionCallback DeleteAuctionCallback, CarPlate CarPlateAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("CARPLATE");
        CarPlateDocument.update("CarPlateTelemetries", FieldValue.arrayRemove(CarPlateAuction))
        .addOnSuccessListener(d ->
        {
            DeleteAuctionCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            DeleteAuctionCallback.onCallback(false);
        });
    }
    public void GetCarPlateAuctions(final GetCarPlateAuctionsCallback Callback)
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
                    Log.d("FireStore", "GetCarPlateAuctions Received: " + CarPlate.getPlateNumber());
                }
                Callback.onCallback(CarPlates);
            }
        });
    }
    public void UpdateCarPlateAuctions(final UpdateCarPlateAuctionsCallback CallBack, CarPlate CarPlateAuction)
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
                    if (CarPlateAuction.getID().equals(CarPlate.getID()))
                    {
                        CarPlates.add(CarPlateAuction);
                    }
                    else
                    {
                        CarPlates.add(CarPlate);
                    }
                    Log.d("FireStore", "GetCarPlateAuctions Received: " + CarPlate.getPlateNumber());
                }
                DB.collection("AUCTIONS").document("CARPLATE").update("CarPlateTelemetries", CarPlates)
                .addOnSuccessListener(unused -> {
                    Log.d("FireStore", "UpdateCarPlateAuctions Updated Successfully");
                    CallBack.onCallback(true);
                })
                .addOnFailureListener(e ->
                {
                    Log.d("FireStore", "UpdateCarPlateAuctions Exception: " + e.getMessage());
                    CallBack.onCallback(false);
                });
            }
            else
            {
                Log.d("FireStore", "UpdateCarPlateAuctions document does not exists");
                CallBack.onCallback(false);
            }
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "UpdateCarPlateAuctions Exception: " + e.getMessage());
            CallBack.onCallback(false);
        });
    }
    //endregion
    //region "Car Auction"
    public void AddCarAuction(final SetUserTelemetryCallback AddCarCallback, Car CarAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("CAR");
        CarPlateDocument.update("CarTelemetries", FieldValue.arrayUnion(CarAuction))
        .addOnSuccessListener(d ->
        {
            AddCarCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddCarCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }
    public void DeleteCarAuction(DeleteUserAuctionCallback DeleteAuctionCallback, Car CarAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("CAR");
        CarPlateDocument.update("CarTelemetries", FieldValue.arrayRemove(CarAuction))
        .addOnSuccessListener(d ->
        {
            DeleteAuctionCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            DeleteAuctionCallback.onCallback(false);
        });
    }
    public void GetCarAuctions(final GetCarAuctionsCallback Callback)
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
                        Log.d("FireStore", "GetCarAuctions Received: " + Car.getModel());
                    }
                }
                Callback.onCallback(Cars);
            }
        });
    }
    public void UpdateCarAuctions(final UpdateCarAuctionsCallback CallBack, Car CarAuction)
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
                        if (CarAuction.getID().equals(Car.getID()))
                        {
                            Cars.add(CarAuction);
                        }
                        else
                        {
                            Cars.add(Car);
                        }
                        Log.d("FireStore", "GetCarAuctions Received: " + Car.getModel());
                    }
                }
                DB.collection("AUCTIONS").document("CAR").update("CarTelemetries", Cars)
                .addOnSuccessListener(unused ->
                {
                    Log.d("FireStore", "UpdateCarAuctions Updated Successfully");
                    CallBack.onCallback(true);
                })
                .addOnFailureListener(e ->
                {
                    Log.d("FireStore", "UpdateCarAuctions Exception: " + e.getMessage());
                    CallBack.onCallback(false);
                });
            }
            else
            {
                Log.d("FireStore", "UpdateCarAuctions document does not exists");
                CallBack.onCallback(false);
            }
        }).addOnFailureListener(e ->
        {
            Log.d("FireStore", "UpdateCarAuctions Exception: " + e.getMessage());
            CallBack.onCallback(false);
        });
    }
    //endregion
    //region "Landmark Auction"
    public void AddLandmarkAuction(final SetUserTelemetryCallback AddLandmarkCallback, Landmark LandmarkAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("LANDMARK");
        //Atomically add a new region to the "regions" array field.
        CarPlateDocument.update("LandmarkTelemetries", FieldValue.arrayUnion(LandmarkAuction))
        .addOnSuccessListener(d ->
        {
            AddLandmarkCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddLandmarkCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }
    public void DeleteLandmarkAuction(DeleteUserAuctionCallback DeleteAuctionCallback, Landmark LandmarkAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("LANDMARK");
        CarPlateDocument.update("LandmarkTelemetries", FieldValue.arrayRemove(LandmarkAuction))
        .addOnSuccessListener(d ->
        {
            DeleteAuctionCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            DeleteAuctionCallback.onCallback(false);
        });
    }
    public void GetLandmarkAuctions(final GetLandmarkAuctionsCallback Callback)
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
                    if (!LandmarkTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(LandmarkTelemetries.get(i));
                        Landmark Landmark = gson.fromJson(jsonElement, Landmark.class);
                        Landmarks.add(Landmark);
                        Log.d("FireStore", "GetLandmarkAuctions Received: " + Landmark.getType());
                    }
                }
                Callback.onCallback(Landmarks);
            }
        });
    }
    public void UpdateLandmarkAuctions(final UpdateLandmarkAuctionsCallback CallBack, Landmark LandmarkAuction)
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
                    if (!LandmarkTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(LandmarkTelemetries.get(i));
                        Landmark Landmark = gson.fromJson(jsonElement, Landmark.class);
                        if (LandmarkAuction.getID().equals(Landmark.getID()))
                        {
                            Landmarks.add(LandmarkAuction);
                        }
                        else
                        {
                            Landmarks.add(Landmark);
                        }
                        Log.d("FireStore", "GetLandmarkAuctions Received: " + Landmark.getType());
                    }
                }
                DB.collection("AUCTIONS").document("LANDMARK").update("LandmarkTelemetries", Landmarks)
                .addOnSuccessListener(unused ->
                {
                    Log.d("FireStore", "UpdateLandmarkAuctions Updated Successfully");
                    CallBack.onCallback(true);
                })
                .addOnFailureListener(e ->
                {
                    Log.d("FireStore", "UpdateLandmarkAuctions Exception: " + e.getMessage());
                    CallBack.onCallback(false);
                });
            }
            else
            {
                Log.d("FireStore", "UpdateLandmarkAuctions document does not exists");
                CallBack.onCallback(false);
            }
        }).addOnFailureListener(e ->
        {
            Log.d("FireStore", "UpdateLandmarkAuctions Exception: " + e.getMessage());
            CallBack.onCallback(false);
        });
    }
    //endregion
    //region "VIPPhoneNumber Auction"
    public void AddVIPPhoneNumberAuction(final SetUserTelemetryCallback AddVIPPhoneNumberCallback, VipPhoneNumber VipPhoneNumberAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("VIPPHONE");
        CarPlateDocument.update("VIPPhoneTelemetries", FieldValue.arrayUnion(VipPhoneNumberAuction))
        .addOnSuccessListener(d ->
        {
            AddVIPPhoneNumberCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddVIPPhoneNumberCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }
    public void DeleteVIPPhoneNumberAuction(final DeleteUserAuctionCallback DeleteAuctionCallback, VipPhoneNumber VipPhoneNumberAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("VIPPHONE");
        CarPlateDocument.update("VIPPhoneTelemetries", FieldValue.arrayRemove(VipPhoneNumberAuction))
        .addOnSuccessListener(d ->
        {
            DeleteAuctionCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            DeleteAuctionCallback.onCallback(false);
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
                    if (!VIPPhoneTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(VIPPhoneTelemetries.get(i));
                        VipPhoneNumber VipPhoneNumber = gson.fromJson(jsonElement, VipPhoneNumber.class);
                        VIPPhoneNumbers.add(VipPhoneNumber);
                        Log.d("FireStore", "GetVIPPHoneNumberAuctions Received: " + VipPhoneNumber.getPhoneNumber());
                    }
                }
                Callback.onCallback(VIPPhoneNumbers);
            }
        });
    }
    public void UpdateVIPPHoneNumberAuctions(final UpdateVIPPHoneNumberCallback CallBack, VipPhoneNumber VipPhoneNumberAuction)
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
                    if (!VIPPhoneTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(VIPPhoneTelemetries.get(i));
                        VipPhoneNumber VipPhoneNumber = gson.fromJson(jsonElement, VipPhoneNumber.class);
                        if (VipPhoneNumberAuction.getID().equals(VipPhoneNumber.getID()))
                        {
                            VIPPhoneNumbers.add(VipPhoneNumberAuction);
                        }
                        else
                        {
                            VIPPhoneNumbers.add(VipPhoneNumber);
                        }
                        Log.d("FireStore", "GetVIPPHoneNumberAuctions Received: " + VipPhoneNumber.getPhoneNumber());
                    }
                }
                DB.collection("AUCTIONS").document("VIPPHONE").update("VIPPhoneTelemetries", VIPPhoneNumbers)
                .addOnSuccessListener(unused ->
                {
                    Log.d("FireStore", "UpdateVIPPHoneNumberAuctions Updated Successfully");
                    CallBack.onCallback(true);
                })
                .addOnFailureListener(e ->
                {
                    Log.d("FireStore", "UpdateVIPPHoneNumberAuctions Exception: " + e.getMessage());
                    CallBack.onCallback(false);
                });
            }
            else
            {
                Log.d("FireStore", "UpdateVIPPHoneNumberAuctions document does not exists");
                CallBack.onCallback(false);
            }
        }).addOnFailureListener(e ->
        {
            Log.d("FireStore", "UpdateVIPPHoneNumberAuctions Exception: " + e.getMessage());
            CallBack.onCallback(false);
        });
    }
    //endregion
    //region "General Auction"
    public void AddGeneralAuction(final SetUserTelemetryCallback AddGeneralCallback, General GeneralAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("GENERAL");
        CarPlateDocument.update("GeneralTelemetries", FieldValue.arrayUnion(GeneralAuction))
        .addOnSuccessListener(d ->
        {
            AddGeneralCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddGeneralCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }
    public void DeleteGeneralAuction(final DeleteUserAuctionCallback DeleteAuctionCallback, General GeneralAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("GENERAL");
        CarPlateDocument.update("GeneralTelemetries", FieldValue.arrayRemove(GeneralAuction))
        .addOnSuccessListener(d ->
        {
            DeleteAuctionCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            DeleteAuctionCallback.onCallback(false);
        });
    }
    public void GetGeneralAuctions(final GetGeneralAuctionsCallback Callback)
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
                    if (!GeneralTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(GeneralTelemetries.get(i));
                        General General = gson.fromJson(jsonElement, General.class);
                        Generals.add(General);
                        Log.d("FireStore", "GetGeneralAuctions Received: " + General.getName());
                    }
                }
                Callback.onCallback(Generals);
            }
        });
    }
    public void UpdateGeneralAuctions(final UpdateVIPPHoneNumberCallback CallBack, General GeneralAuction)
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
                    if (!GeneralTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(GeneralTelemetries.get(i));
                        General General = gson.fromJson(jsonElement, General.class);
                        if (GeneralAuction.getID().equals(General.getID()))
                        {
                            Generals.add(GeneralAuction);
                        }
                        else
                        {
                            Generals.add(General);
                        }
                        Log.d("FireStore", "GetGeneralAuctions Received: " + General.getName());
                    }
                }
                DB.collection("AUCTIONS").document("GENERAL").update("GeneralTelemetries", Generals)
                .addOnSuccessListener(unused ->
                {
                    Log.d("FireStore", "UpdateGeneralAuctions Updated Successfully");
                    CallBack.onCallback(true);
                })
                .addOnFailureListener(e ->
                {
                    Log.d("FireStore", "UpdateGeneralAuctions Exception: " + e.getMessage());
                    CallBack.onCallback(false);
                });
            }
            else
            {
                Log.d("FireStore", "UpdateGeneralAuctions document does not exists");
                CallBack.onCallback(false);
            }
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "UpdateGeneralAuctions Exception: " + e.getMessage());
            CallBack.onCallback(false);
        });
    }
    //endregion
    //region "Service Auction"
    public void AddServiceAuction(final SetUserTelemetryCallback AddServiceCallback, Service ServiceAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("SERVICE");
        CarPlateDocument.update("ServiceTelemetries", FieldValue.arrayUnion(ServiceAuction))
        .addOnSuccessListener(d ->
        {
            AddServiceCallback.onCallback(new FireStoreResult("", "", true));
        })
        .addOnFailureListener(d ->
        {
            AddServiceCallback.onCallback(new FireStoreResult("", d.getMessage(), false));
        });
    }
    public void DeleteServiceAuction(final DeleteUserAuctionCallback DeleteAuctionCallback, Service ServiceAuction)
    {
        DocumentReference CarPlateDocument = DB.collection("AUCTIONS").document("SERVICE");
        CarPlateDocument.update("ServiceTelemetries", FieldValue.arrayRemove(ServiceAuction))
        .addOnSuccessListener(d ->
        {
            DeleteAuctionCallback.onCallback(true);
        })
        .addOnFailureListener(d ->
        {
            DeleteAuctionCallback.onCallback(false);
        });
    }
    public void GetServiceAuctions(final GetServiceAuctionsCallback Callback)
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
                    if (!ServiceTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(ServiceTelemetries.get(i));
                        Service Service = gson.fromJson(jsonElement, Service.class);
                        Services.add(Service);
                        Log.d("FireStore", "GetServiceAuctions Received: " + Service.getName());
                    }
                }
                Callback.onCallback(Services);
            }
        });
    }
    public void UpdateServiceAuctions(final UpdateServiceAuctionCallback CallBack, Service ServiceAuction)
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
                    if (!ServiceTelemetries.get(i).toString().isEmpty())
                    {
                        JsonElement jsonElement = gson.toJsonTree(ServiceTelemetries.get(i));
                        Service Service = gson.fromJson(jsonElement, Service.class);
                        Services.add(Service);
                        Log.d("FireStore", "GetServiceAuctions Received: " + Service.getName());
                    }
                }
                DB.collection("AUCTIONS").document("SERVICE").update("ServiceTelemetries", Services)
                .addOnSuccessListener(unused ->
                {
                    Log.d("FireStore", "UpdateServiceAuctions Updated Successfully");
                    CallBack.onCallback(true);
                })
                .addOnFailureListener(e ->
                {
                    Log.d("FireStore", "UpdateServiceAuctions Exception: " + e.getMessage());
                    CallBack.onCallback(false);
                });
            }
            else
            {
                Log.d("FireStore", "UpdateGeneralAuctions document does not exists");
                CallBack.onCallback(false);
            }
        })
        .addOnFailureListener(e ->
        {
            Log.d("FireStore", "UpdateGeneralAuctions Exception: " + e.getMessage());
            CallBack.onCallback(false);
        });
    }
    //endregion
    //endregion

    //region Listeners
    public void ListenCarAuctions()
    {
    }
    //endregion

    //region Company
    public void SetCompanyAccount()
    {
        String Email = FirestoreEncoder.EncodeForFirebaseKey(AesProvider.Encrypt("eauctionuae@gmail.com"));
        Map<String, Object> HashMap = new HashMap<>();
        HashMap.put("Email", Email);
        DB.collection("COMPANY").document("Email").set(HashMap);
    }

    public void SetCompanyPassword()
    {
        String Password = FirestoreEncoder.EncodeForFirebaseKey(AesProvider.Encrypt("kali_linux"));
        Map<String, Object> HashMap = new HashMap<>();
        HashMap.put("Password", Password);
        DB.collection("COMPANY").document("Password").set(HashMap);
    }

    public String GetCompanyAccount()
    {
        String EncryptedCompanyAccount = (String)new SyncFireStore().GetField("COMPANY", "Email", "Email", DB);
        return AesProvider.Decrypt(FirestoreEncoder.DecodeFromFirebaseKey(EncryptedCompanyAccount));
    }

    public String GetCompanyPassword()
    {
        String EncryptedCompanyPassword = (String)new SyncFireStore().GetField("COMPANY", "Password", "Password", DB);
        return AesProvider.Decrypt(FirestoreEncoder.DecodeFromFirebaseKey(EncryptedCompanyPassword));
    }
    //endregion

    //region Encryption Helpers
    public String EncryptField(String Field)
    {
        return FirestoreEncoder.EncodeForFirebaseKey(AesProvider.Encrypt(Field));
    }
    public String DecryptField(String EncryptedField)
    {
        return (AesProvider.Decrypt(FirestoreEncoder.DecodeFromFirebaseKey(EncryptedField)));
    }
    //endregion
}