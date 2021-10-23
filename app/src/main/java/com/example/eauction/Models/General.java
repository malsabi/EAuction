package com.example.eauction.Models;

public class General extends Telemetry
{
    private String name;

    //region Constructors
    public General()
    {
        super();
        name = "";
    }

    public General(String name)
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

    public boolean IsEqual(General G)
    {
        return name.equals(G.getName()) && getDetails().equals(G.getDetails());
    }
}
