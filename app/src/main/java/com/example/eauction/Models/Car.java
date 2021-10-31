package com.example.eauction.Models;

import androidx.annotation.NonNull;

public class Car extends Telemetry
{
    private String name;
    private String model;
    private int horsePower;
    private int mileage;

    //region Constructors
    public Car()
    {
        super();
        name = "";
        model = "";
        horsePower = 0;
        mileage = 0;
    }

    public Car(String name, String model, int horsePower, int mileage)
    {
        super();
        this.name = name;
        this.model = model;
        this.horsePower = horsePower;
        this.mileage = mileage;
    }
    //endregion
    //region Setters
    public void setName(String name)
    {
        this.name = name;
    }
    public void setModel(String model)
    {
        this.model = model;
    }
    public void setHorsePower(int horsePower)
    {
        this.horsePower = horsePower;
    }
    public void setMileage(int mileage)
    {
        this.mileage = mileage;
    }
    //endregion
    //region Getters
    public String getName()
    {
        return name;
    }
    public String getModel()
    {
        return model;
    }
    public int getHorsePower()
    {
        return horsePower;
    }
    public int getMileage()
    {
        return mileage;
    }
    //endregion

    public boolean IsEqual(Car C)
    {
        return name.equals(C.getName()) && model.equals(C.getModel()) && horsePower == C.getHorsePower() && mileage == C.getMileage() && getDetails().equals(C.getDetails());
    }

    @NonNull
    @Override
    public String toString()
    {
        return (name + model + horsePower + mileage + getDetails()).toUpperCase();
    }
}