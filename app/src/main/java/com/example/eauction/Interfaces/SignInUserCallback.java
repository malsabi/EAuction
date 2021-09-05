package com.example.eauction.Interfaces;

import com.example.eauction.Models.FireStoreResult;
import com.example.eauction.Models.User;

public interface SignInUserCallback
{
    void onCallback(FireStoreResult Result, User UserObj);
}