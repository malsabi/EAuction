package com.example.eauction.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Service extends Telemetry
{
    private String name;
    private ArrayList<ServiceComment> serviceComments;

    //region Constructors
    public Service()
    {
        super();
        name = "";
    }

    public Service(String name)
    {
        super();
        this.name = name;
    }
    //endregion
    //region Setters
    public void setName(String name)
    {
        this.name = name;
    }
    public void setServiceComments(ArrayList<ServiceComment> serviceComments) { this.serviceComments = serviceComments; }
    //endregion
    //region Getters
    public String getName()
    {
        return name;
    }
    public ArrayList<ServiceComment> getServiceComments() {return serviceComments; }
    //endregion

    public boolean IsEqual(Service S)
    {
        return name.equals(S.getName()) && getDetails().equals(S.getDetails());
    }

    @NonNull
    @Override
    public String toString()
    {
        return (name + getDetails()).toUpperCase();
    }
}