package com.example.eauction.DataBase;

import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.User;
import java.util.List;

public class FireStoreManager
{
    public FireStoreManager()
    {
    }
    public FireStoreResult RegisterUser(User UserObj)
    {
        return new FireStoreResult();
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
    public FireStoreResult SignInUser(SignIn SignInObj)
    {
        return new FireStoreResult();
    }
    public FireStoreResult SignOut(User UserObj)
    {
        return new FireStoreResult();
    }
}