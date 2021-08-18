package com.example.eauction;

import android.app.Application;

import com.example.eauction.DataBase.FireStoreManager;

//Global Access Variable that can be accessed through all activities
//Which allows us to access database and other services.
public class App extends Application
{
    private FireStoreManager FireStore;

    public App()
    {
        //Initialize FireStore
        //FireStore = new FireStore(Pass FireStore Instance);
    }

    public FireStoreManager GetFireStoreInstance()
    {
        return FireStore;
    }
}