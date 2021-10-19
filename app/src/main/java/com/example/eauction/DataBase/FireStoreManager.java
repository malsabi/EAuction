package com.example.eauction.DataBase;

import android.util.Log;

import com.example.eauction.Cryptograpgy.FirestoreEncoder;
import com.example.eauction.Cryptograpgy.Hashing;
import com.example.eauction.Interfaces.SetUserTelemetryCallback;
import com.example.eauction.Interfaces.RegisterUserCallback;
import com.example.eauction.Interfaces.SignInUserCallback;
import com.example.eauction.Interfaces.SignOutUserCallback;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Validations.UserValidation;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

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

    public void AddAuction(SetUserTelemetryCallback AddAuctionCallback, Telemetry TelemetryObj, User UserObj)
    {
        FireStoreResult Result = new FireStoreResult();
    }

    public List<Telemetry> GetActiveAuctions()
    {
        return null;
    }
    public FireStoreResult UpdateAuction(Telemetry TelemetryObj)
    {
        return new FireStoreResult();
    }
}