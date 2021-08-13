package com.example.eauction.Models;

public class CarPlate extends Telemetry
{
    private int plateNumber;
    private int plateCode;
    private String emirate;

    //region Constructors
    public CarPlate()
    {
        super();
        plateNumber = -1;
        plateCode = -1;
        emirate = "";
    }
    public CarPlate(int plateNumber, int plateCode, String emirate)
    {
        super();
        this.plateNumber = plateNumber;
        this.plateCode = plateCode;
        this.emirate = emirate;
    }
    //endregion
    //region Setters
    public void setPlateNumber(int plateNumber)
    {
        this.plateNumber = plateNumber;
    }
    public void setPlateCode(int plateCode)
    {
        this.plateCode = plateCode;
    }
    public void setEmirate(String emirate)
    {
        this.emirate = emirate;
    }
    //endregion
    //region Getters
    public int getPlateNumber()
    {
        return plateNumber;
    }
    public int getPlateCode()
    {
        return plateCode;
    }
    public String getEmirate()
    {
        return emirate;
    }
    //endregion
}
