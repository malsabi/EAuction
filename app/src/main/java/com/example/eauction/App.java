package com.example.eauction;

import android.app.Application;
import android.util.Log;

import com.example.eauction.DataBase.FireStoreManager;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Validations.UserValidation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;

//Global Access Variable that can be accessed through all activities
//Which allows us to access database and other services.
public class App extends Application
{
    private FireStoreManager FireStore;

    public App()
    {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FireStore = new FireStoreManager();
    }

    public FireStoreManager GetFireStoreInstance()
    {
        return FireStore;
    }
}