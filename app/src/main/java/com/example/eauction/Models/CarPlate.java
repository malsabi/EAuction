package com.example.eauction.Models;

public class CarPlate extends Telemetry
{
    private String plateNumber;
    private String plateCode;
    private String emirate;

    //region Constructors
    public CarPlate()
    {
        super();
        plateNumber = "";
        plateCode = "";
        emirate = "";
    }
    public CarPlate(String plateNumber, String plateCode, String emirate)
    {
        super();
        this.plateNumber = plateNumber;
        this.plateCode = plateCode;
        this.emirate = emirate;
    }
    //endregion
    //region Setters
    public void setPlateNumber(String plateNumber)
    {
        this.plateNumber = plateNumber;
    }
    public void setPlateCode(String plateCode)
    {
        this.plateCode = plateCode;
    }
    public void setEmirate(String emirate)
    {
        this.emirate = emirate;
    }
    //endregion
    //region Getters
    public String getPlateNumber()
    {
        return plateNumber;
    }
    public String getPlateCode()
    {
        return plateCode;
    }
    public String getEmirate()
    {
        return emirate;
    }
    //endregion

    public boolean IsEqual(CarPlate CP)
    {
        return plateNumber.equals(CP.getPlateNumber()) && plateCode.equals(CP.getPlateCode()) && emirate.equals(CP.getEmirate()) && getDetails().equals(CP.getDetails());
    }
}
