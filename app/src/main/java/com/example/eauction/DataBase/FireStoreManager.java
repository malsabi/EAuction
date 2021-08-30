package com.example.eauction.DataBase;

import android.app.Activity;
import android.util.Log;

import com.example.eauction.Interfaces.RegisterUserCallback;
import com.example.eauction.Interfaces.SignInUserCallback;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Validations.UserValidation;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class FireStoreManager
{
    private final FirebaseFirestore DB;
    //get, set, update
    public FireStoreManager()
    {
        DB = FirebaseFirestore.getInstance();
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
            SignInCallback.onCallback(Result);
        }
        else
        {
            Log.d("TAG", "Attempting to make get request");
            DB.collection("USERS").document(SignInObj.getEmail()).get()
            .addOnSuccessListener(d ->
            {
                if (d.exists())
                {
                    User UserObj = d.toObject(User.class);
                    assert UserObj != null;
                    if (UserObj.getPassword().equals(SignInObj.getPassword()))
                    {
                        Log.d("TAG", "Received all documents: " + UserObj.getSsn());
                        Result.setSuccess(true);
                        Result.setTitle("Success");
                        Result.setMessage("User successfully logged in");
                        //handle isActive = true
                    }
                    else
                    {
                        Log.d("TAG", "failed to sign in, make sure you typed the correct password!");
                        Result.setSuccess(false);
                        Result.setTitle("Exception");
                        Result.setMessage("failed to sign in, make sure you typed the correct password!");
                    }
                }
                else
                {
                    Log.d("TAG", "failed to sign in, make sure you are registered!");
                    Result.setSuccess(false);
                    Result.setTitle("Exception");
                    Result.setMessage("failed to sign in, make sure you are registered!");
                }
                SignInCallback.onCallback(Result);

            })
            .addOnFailureListener(e ->
            {
                Log.d("TAG", "failed to sign in, make sure you are registered!");
                Result.setSuccess(false);
                Result.setTitle("Exception");
                Result.setMessage(e.getMessage());
                SignInCallback.onCallback(Result);
            });
        }
    }

    public List<Telemetry> GetActiveAuctions()
    {
        return null;
    }
    public FireStoreResult AddAuction(Telemetry TelemetryObj)
    {
        return new FireStoreResult();
    }
    public FireStoreResult UpdateAuction(Telemetry TelemetryObj)
    {
        return new FireStoreResult();
    }
    public FireStoreResult SignOut(User UserObj)
    {
        return new FireStoreResult();
    }
}