package com.example.eauction.Models;

import androidx.annotation.NonNull;

public class Landmark extends Telemetry
{
    private String type;
    private String location;
    private String name;
    private int area;

    //region Constructors
    public Landmark()
    {
        super();
        type = "";
        location = "";
        name = "";
        area = 0;
    }

    public Landmark(String type, String location, String name, int area)
    {
        super();
        this.type = type;
        this.location = location;
        this.name = name;
        this.area = area;
    }
    //endregion
    //region Setters
    public void setType(String type)
    {
        this.type = type;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setArea(int area)
    {
        this.area = area;
    }
    //endregion
    //region Getters
    public String getType()
    {
        return type;
    }
    public String getLocation()
    {
        return location;
    }
    public String getName()
    {
        return name;
    }
    public int getArea()
    {
        return area;
    }
    //endregion

    public boolean IsEqual(Landmark LM)
    {
        return type.equals(LM.getType()) && location.equals(LM.getLocation()) && name.equals(LM.getName()) && area == LM.getArea() && getDetails().equals(LM.getDetails());
    }

    @NonNull
    @Override
    public String toString()
    {
        return (type + location + name + area + getDetails()).toUpperCase();
    }
}
