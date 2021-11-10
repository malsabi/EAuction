package com.example.eauction;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

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
public class App extends Application implements LifecycleObserver
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
        Log.d("App", "OnCreate Called");
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded()
    {
        Log.d("App", "App in background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded()
    {
        Log.d("App", "App in foreground");
    }

    public FireStoreManager GetFireStoreInstance()
    {
        return FireStore;
    }
}