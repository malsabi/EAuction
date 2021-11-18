package com.example.eauction.Session;

import android.util.Log;
import com.example.eauction.DataBase.FireStoreManager;
import com.example.eauction.Interfaces.IsUserSessionAddedCallback;
import com.example.eauction.Interfaces.IsUserSessionValidCallback;
import com.example.eauction.Models.SessionModel;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SessionManagement
{
    //region Constants
    //Represents FireStore Database access
    private final FireStoreManager FireStore;
    //Represents 1 hour ie: 3600 seconds for a session to remain alive.
    private final int MaximumSessionTimeout = 60;
    //Represents when was the last time the user was active on the application.
    private LocalDateTime LastSeen;
    //endregion

    //region Getters/Setters
    public LocalDateTime GetLastSeen()
    {
        return LastSeen;
    }
    public void SetLastSeen(LocalDateTime LastSeen)
    {
        this.LastSeen = LastSeen;
    }
    //endregion

    //region Constructors
    public SessionManagement(FireStoreManager FireStore)
    {
        this.FireStore = FireStore;
    }
    //endregion

    //region Private Methods
    //endregion

    //region Public Methods
    //Adds the session when the user signs in.
    public void AddSession(SessionModel Session)
    {
        IsSessionAdded(IsUserSessionAdded ->
        {
            if (!IsUserSessionAdded)
            {
                Log.d("SessionManagement", "AddSession - IsSessionAdded: false.");
                FireStore.AddUserSession(Result ->
                {
                    if (Result)
                    {
                        Log.d("SessionManagement", "AddSession - AddUserSession: Added session successfully.");
                    }
                    else
                    {
                        Log.d("SessionManagement", "AddSession - AddUserSession: Failed to add session.");
                    }
                }, Session);
            }
            else
            {
                Log.d("SessionManagement", "AddSession - IsSessionAdded: true.");
            }
        }, Session.getUserId());
    }
    //Updates the session in the DB when the user resumes the application.
    public void UpdateSession(String UserId, String LastSeen)
    {
       IsSessionAdded(IsUserSessionAdded ->
       {
           if (IsUserSessionAdded)
           {
               Log.d("SessionManagement", "UpdateSession - IsSessionAdded: true.");
               FireStore.UpdateUserSession(Result ->
               {
                   if (Result)
                   {
                       Log.d("SessionManagement", "UpdateSession - UpdateUserSession: Updated session successfully.");
                   }
                   else
                   {
                       Log.d("SessionManagement", "UpdateSession - UpdateUserSession: Failed to update session.");
                   }
               }, UserId, LastSeen);
           }
           else
           {
               Log.d("SessionManagement", "UpdateSession - IsSessionAdded: false.");
           }
       }, UserId);
    }
    //Deletes the session when the user signs out.
    public void DeleteSession(String UserId)
    {
        IsSessionAdded(IsUserSessionAdded ->
        {
            if (IsUserSessionAdded)
            {
                Log.d("SessionManagement", "RemoveSession - IsSessionAdded: true.");
                FireStore.DeleteUserSession(Result ->
                {
                    if (Result)
                    {
                        Log.d("SessionManagement", "DeleteSession - DeleteUserSession: Deleted session successfully.");
                    }
                    else
                    {
                        Log.d("SessionManagement", "DeleteSession - DeleteUserSession: Failed to delete session.");
                    }
                }, UserId);
            }
            else
            {
                Log.d("SessionManagement", "RemoveSession - IsSessionAdded: false.");
            }
        }, UserId);
    }
    //Checks if the session is added in the DB or not.
    public void IsSessionAdded(final IsUserSessionAddedCallback IsSessionAddedCallback, String UserId)
    {
        FireStore.IsUserSessionAdded(IsSessionAddedCallback, UserId);
    }
    //Checks if the session reached the time out or not.
    public void IsSessionValid(final IsUserSessionValidCallback IsSessionValidCallback, String UserId)
    {
        FireStore.GetUserSession(Session ->
        {
            if (Session.getLastSeen().equals("N/A"))
            {
                IsSessionValidCallback.onCallback(true);
            }
            else
            {
                LocalDateTime CurrentTimeStamp = LocalDateTime.now();
                long TotalMinutes = ChronoUnit.MINUTES.between(LocalDateTime.parse(Session.getLastSeen()), CurrentTimeStamp);
                Log.d("SessionManagement", "IsSessionValid - GetUserSession: LastSeen: " + Session.getLastSeen() + ", Now: " + CurrentTimeStamp.toString() + ", TotalMinutes: " + TotalMinutes);
                if (TotalMinutes >= MaximumSessionTimeout)
                {
                    IsSessionValidCallback.onCallback(false);
                }
                else
                {
                    IsSessionValidCallback.onCallback(true);
                }
            }
        }, UserId);
    }
    //endregion
}