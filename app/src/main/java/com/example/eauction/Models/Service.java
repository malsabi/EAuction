package com.example.eauction.Models;

public class Service extends Telemetry
{
    private String name;

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
    //endregion
    //region Getters
    public String getName()
    {
        return name;
    }
    //endregion

    public boolean IsEqual(Service S)
    {
        return name.equals(S.getName()) && getDetails().equals(S.getDetails());
    }
}