package com.example.eauction;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.eauction.DataBase.FireStoreManager;
import com.example.eauction.Interfaces.IsUserSessionAddedCallback;
import com.example.eauction.Interfaces.IsUserSessionValidCallback;
import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import com.example.eauction.Session.SessionManagement;
import com.example.eauction.Utilities.PreferenceUtils;
import com.example.eauction.Validations.UserValidation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Executor;

//Global Access Variable that can be accessed through all activities
//Which allows us to access database and other services.
public class App extends Application implements LifecycleObserver
{
    //region Fields
    private FireStoreManager FireStore;
    private SessionManagement SessionManagement;
    //endregion

    //region Getters
    public FireStoreManager GetFireStoreInstance()
    {
        return FireStore;
    }
    public SessionManagement GetSessionManagement()
    {
        return SessionManagement;
    }
    //endregion

    public App()
    {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("App", "OnCreate method start");

        FirebaseApp.initializeApp(this);
        FireStore = new FireStoreManager();
        SessionManagement = new SessionManagement(FireStore);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded()
    {
        Log.d("App", "App in foreground");
        String UserId = PreferenceUtils.getEmail(this);
        if (UserId != null)
        {
            Log.d("App", "UserId: " + UserId);
            SessionManagement.IsSessionAdded(IsUserSessionAdded ->
            {
                if (IsUserSessionAdded)
                {
                    Log.d("App", "onAppForegrounded: User Session is added");
                    SessionManagement.IsSessionValid(Result ->
                    {
                        if (!Result)
                        {
                            Log.d("App", "onAppForegrounded: Session is invalid, time-out");
                            SignOutUser();
                        }
                        else
                        {
                            Log.d("App", "onAppForegrounded: Session is valid");
                            SessionManagement.UpdateSession(UserId, "N/A");
                        }
                    }, UserId);
                }
                else
                {
                    Log.d("App", "onAppForegrounded: User Session is not added");
                }
            }, UserId);
        }
        else
        {
            Log.d("App", "UserId: null");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded()
    {
        Log.d("App", "App in background");

        String UserId = PreferenceUtils.getEmail(this);

        if (UserId != null)
        {
            Log.d("App", "UserId: " + UserId);
            SessionManagement.IsSessionAdded(IsUserSessionAdded ->
            {
                if (IsUserSessionAdded)
                {
                    Log.d("App", "onAppBackgrounded: User Session is added");
                    SessionManagement.UpdateSession(UserId, LocalDateTime.now().toString());
                }
                else
                {
                    Log.d("App", "onAppBackgrounded: User Session is not added");
                }
            }, UserId);
        }
        else
        {
            Log.d("App", "UserId: is null");
        }
    }

    private void SignOutUser()
    {
        String UserId = PreferenceUtils.getEmail(this);
        GetSessionManagement().DeleteSession(UserId);
        GetFireStoreInstance().SignOut(Result ->
        {
            if (Result.isSuccess())
            {
                PreferenceUtils.saveEmail(null, this);
                PreferenceUtils.savePassword(null, this);
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, "Session finished, please re login again", Toast.LENGTH_SHORT).show();
            }
        }, UserId);
    }
}