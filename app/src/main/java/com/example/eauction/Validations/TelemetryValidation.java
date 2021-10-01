package com.example.eauction.Validations;
import com.example.eauction.Models.*;

public class TelemetryValidation
{
    public ValidationResult CarPlateValidation(CarPlate CarPlateObj)
    {
        ValidationResult Result = new ValidationResult();
        if (Integer.parseInt(CarPlateObj.getPlateCode())< 0 || Integer.parseInt(CarPlateObj.getPlateCode()) > 5)
        {
            Result.setTitle("PlateCodeText");
            Result.setMessage("PlateCode should be between 1 and 5");
            Result.setSuccess(false);
        }
        return Result;
    }
    public ValidationResult CarValidation(Car CarObj)
    {
        return new ValidationResult();
    }
    public ValidationResult LandmarkValidation(Landmark LandmarkObj)
    {
        return new ValidationResult();
    }
    public ValidationResult VipPhoneNumberValidation(VipPhoneNumber VipPhoneNumberObj)
    {
        return new ValidationResult();
    }
    public ValidationResult GeneralValidation(General GeneralObj)
    {
        return new ValidationResult();
    }
}