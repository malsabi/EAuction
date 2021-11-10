package com.example.eauction.Validations;

import com.example.eauction.Models.*;

public class TelemetryValidation
{
    //Codes used in car plates for each emirate used in Validation
    private final String[] AbuDhabiCodes = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "17", "50"};
    private final String[] AjmanCodes = {"A", "B", "C", "D", "E"};
    private final String[] DubaiCodes = {"A", "AA", "B", "N", "X", "J"};
    private final String[] FujairahCodes = {"A", "B", "C", "D", "E", "F", "G", "K", "M", "P", "R", "S", "T"};
    private final String[] RasAlKhaimahCodes = {"A", "B", "C", "D", "I", "K", "M", "N", "S", "V", "Y"};
    private final String[] SharjahCodes = {"1", "2", "3"};
    private final String[] UmmAlQuwainCodes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "X"};

    //Codes used in VIP Phone Number validation
    private final String[] EtisalatCodes = {"050", "054", "056"};
    private final String[] DuCodes =  {"055", "052", "058"};

    public ValidationResult CarPlateValidation(CarPlate CarPlateObj)
    {
        ValidationResult Result = new ValidationResult();
        Result.setMessage("Valid Data");
        Result.setSuccess(true);

        String Code = CarPlateObj.getPlateCode();
        String Plate = CarPlateObj.getPlateNumber();
        String Emirate = CarPlateObj.getEmirate();
        switch (Emirate)
        {
            case "Abu Dhabi":
            {
                if (!IsPlateNumberValid(Plate))
                {
                    Result.setTitle("EtCarPlateNumber");
                    Result.setMessage("Please make sure to enter a valid plate number");
                    Result.setSuccess(false);
                }
                else if (!IsCodeValid("Abu Dhabi", Code))
                {
                    Result.setTitle("EtCarPlateCode");
                    Result.setMessage("Please make sure to enter a valid code");
                    Result.setSuccess(false);
                }
            }
            break;
            case "Ajman":
            {
                if (!IsPlateNumberValid(Plate))
                {
                    Result.setTitle("EtCarPlateNumber");
                    Result.setMessage("Please make sure to enter a valid plate number");
                    Result.setSuccess(false);
                }
                else if (!IsCodeValid("Ajman", Code))
                {
                    Result.setTitle("EtCarPlateCode");
                    Result.setMessage("Please make sure to enter a valid code");
                    Result.setSuccess(false);
                }
            }
            break;
            case "Dubai":
            {
                if (!IsPlateNumberValid(Plate))
                {
                    Result.setTitle("EtCarPlateNumber");
                    Result.setMessage("Please make sure to enter a valid plate number");
                    Result.setSuccess(false);
                }
                else if (!IsCodeValid("Dubai", Code))
                {
                    Result.setTitle("EtCarPlateCode");
                    Result.setMessage("Please make sure to enter a valid code");
                    Result.setSuccess(false);
                }
            }
            break;
            case "Fujairah":
            {
                if (!IsPlateNumberValid(Plate))
                {
                    Result.setTitle("EtCarPlateNumber");
                    Result.setMessage("Please make sure to enter a valid plate number");
                    Result.setSuccess(false);
                }
                else if (!IsCodeValid("Fujairah", Code))
                {
                    Result.setTitle("EtCarPlateCode");
                    Result.setMessage("Please make sure to enter a valid code");
                    Result.setSuccess(false);
                }
            }
            break;
            case "Ras Al Khaimah":
            {
                if (!IsPlateNumberValid(Plate))
                {
                    Result.setTitle("EtCarPlateNumber");
                    Result.setMessage("Please make sure to enter a valid plate number");
                    Result.setSuccess(false);
                }
                else if (!IsCodeValid("Ras Al Khaimah", Code))
                {
                    Result.setTitle("EtCarPlateCode");
                    Result.setMessage("Please make sure to enter a valid code");
                    Result.setSuccess(false);
                }
            }
            break;
            case "Sharjah":
            {
                if (!IsPlateNumberValid(Plate))
                {
                    Result.setTitle("EtCarPlateNumber");
                    Result.setMessage("Please make sure to enter a valid plate number");
                    Result.setSuccess(false);
                }
                else if (!IsCodeValid("Sharjah", Code))
                {
                    Result.setTitle("EtCarPlateCode");
                    Result.setMessage("Please make sure to enter a valid code");
                    Result.setSuccess(false);
                }
            }
            break;
            case "Umm Al Quwain":
            {
                if (!IsPlateNumberValid(Plate))
                {
                    Result.setTitle("EtCarPlateNumber");
                    Result.setMessage("Please make sure to enter a valid plate number");
                    Result.setSuccess(false);
                }
                else if (!IsCodeValid("Umm Al Quwain", Code))
                {
                    Result.setTitle("EtCarPlateCode");
                    Result.setMessage("Please make sure to enter a valid code");
                    Result.setSuccess(false);
                }
            }
            break;
            default:
            {
                Result.setMessage("Invalid Data");
                Result.setSuccess(false);
            }
        }
        return Result;
    }
    public ValidationResult CarValidation(Car CarObj)
    {

        ValidationResult Result = new ValidationResult();

        Result.setMessage("Valid Data");
        Result.setSuccess(true);

        String Model = CarObj.getModel();
        int Mileage = CarObj.getMileage();
        String Name = CarObj.getName();
        int HorsePower = CarObj.getHorsePower();

        if (Model.length() == 0 || Model.length() > 20)
        {
            Result.setTitle("EtCarModel");
            Result.setMessage("The car model is invalid");
            Result.setSuccess(false);
        }
        else if (Mileage < 0 || Mileage > 500000)
        {
            Result.setTitle("EtMileage");
            Result.setMessage("The Mileage range should between from 0 to 500,000");
            Result.setSuccess(false);
        }
        else if (Name.length() == 0 || Name.length() > 20)
        {
            Result.setTitle("EtCarName");
            Result.setMessage("The car name is invalid");
            Result.setSuccess(false);
        }
        else if (HorsePower < 50 || HorsePower > 5000)
        {
            Result.setTitle("EtHorsePower");
            Result.setMessage("The Horsepower should be between 50 and 5000");
            Result.setSuccess(false);
        }
        return Result;
    }
    public ValidationResult LandmarkValidation(Landmark LandmarkObj)
    {
        ValidationResult Result = new ValidationResult();
        Result.setMessage("Valid Data");
        Result.setSuccess(true);

        String Type = LandmarkObj.getType();
        String Location = LandmarkObj.getLocation();
        String Name = LandmarkObj.getName();
        int Area = LandmarkObj.getArea();

        if (Type.length() < 4 || Type.length() >= 50)
        {
            Result.setTitle("EtLandmarkType");
            Result.setMessage("Type should be in the range of 4 to 20");
            Result.setSuccess(false);
        }
        else if (Location.length() < 4 || Location.length() >= 50)
        {

            Result.setTitle("EtLandmarkLocation");
            Result.setMessage("Location should be in the range of 4 to 20");
            Result.setSuccess(false);
        }
        else if (Name.length() < 4 || Name.length() >= 50)
        {
            Result.setTitle("EtLandmarkName");
            Result.setMessage("Name should be in the range of 4 to 20");
            Result.setSuccess(false);
        }
        else if (Area <= 0 || Area > 1000)
        {
            //
            Result.setTitle("EtLandmarkArea");
            Result.setMessage("Area should be in the range of 0 to 1000");
            Result.setSuccess(false);
        }
        return Result;
    }
    public ValidationResult VipPhoneNumberValidation(VipPhoneNumber VipPhoneNumberObj)
    {
        ValidationResult Result = new ValidationResult();
        Result.setMessage("Valid Data");
        Result.setSuccess(true);

        String PhoneNumber = VipPhoneNumberObj.getPhoneNumber();
        String CompanyName = VipPhoneNumberObj.getCompany();

        if (PhoneNumber.length() != 12 || !PhoneNumber.contains("-"))
        {
            Result.setTitle("EtPhoneNumberTel");
            Result.setMessage("Please enter a valid phone number");
            Result.setSuccess(false);
        }
        else
        {
            String[] PARTS = PhoneNumber.split("-");
            if (PARTS.length != 3)
            {
                Result.setTitle("EtPhoneNumberTel");
                Result.setMessage("Please enter a full valid phone number");
                Result.setSuccess(false);
            }
            else
            {
                String Code = PARTS[0];
                String Number = PARTS[1] + PARTS[2];
                if (Code.length() != 3 || Number.length() != 7)
                {
                    Result.setTitle("EtPhoneNumberTel");
                    Result.setMessage("Please enter a correct UAE format phone number");
                    Result.setSuccess(false);
                }
                else
                {
                    switch (CompanyName)
                    {
                        case "Etisalat":
                        {
                            boolean IsFound = false;
                            for (String EtisalatCode : EtisalatCodes)
                            {
                                if (EtisalatCode.equals(Code))
                                {
                                    IsFound = true;
                                    break;
                                }
                            }
                            if (!IsFound)
                            {
                                Result.setTitle("EtPhoneNumberTel");
                                Result.setMessage("Please enter a correct etisalat code");
                                Result.setSuccess(false);
                            }
                        }
                        break;
                        case "Du":
                        {
                            boolean IsFound = false;
                            for (String DuCode : DuCodes)
                            {
                                if (DuCode.equals(Code))
                                {
                                    IsFound = true;
                                    break;
                                }
                            }
                            if (!IsFound)
                            {
                                Result.setTitle("EtPhoneNumberTel");
                                Result.setMessage("Please enter a correct du code");
                                Result.setSuccess(false);
                            }
                        }
                        break;
                        default:
                            Result.setMessage("Missing Company Name");
                            Result.setSuccess(false);
                    }
                    for (int i = 0; i < Number.length(); i++)
                    {
                        if (!IsDigit(Number.charAt(i)))
                        {
                            Result.setTitle("EtPhoneNumberTel");
                            Result.setMessage("Please enter a correct UAE format phone number");
                            Result.setSuccess(false);
                        }
                    }
                }
            }
        }
        return Result;
    }
    public ValidationResult GeneralValidation(General GeneralObj)
    {
        ValidationResult Result = new ValidationResult();
        Result.setMessage("Valid Data");
        Result.setSuccess(true);

        String Name = GeneralObj.getName();

        if (Name.length() == 0 || Name.length() > 50)
        {
            Result.setTitle("");
            Result.setMessage("Name object should be between 0 and 50");
            Result.setSuccess(false);
        }

        return Result;
    }

    //region Helpers
    private boolean IsCodeValid(String Emirate, String Code)
    {
        switch (Emirate)
        {
            case "Abu Dhabi":
            {
                boolean IsFound = false;
                if (Code.length() == 0 || Code.length() > 2)
                {
                    return false;
                }
                else
                {
                    for (String abuDhabiCode : AbuDhabiCodes)
                    {
                        if (abuDhabiCode.equals(Code))
                        {
                            IsFound = true;
                            break;
                        }
                    }
                }
                return IsFound;
            }
            case "Ajman":
            {
                boolean IsFound = false;
                if (Code.length() != 1)
                {
                    return false;
                }
                else
                {
                    for (String ajmanCode : AjmanCodes)
                    {
                        if (ajmanCode.equals(Code))
                        {
                            IsFound = true;
                            break;
                        }
                    }
                }
                return IsFound;
            }
            case "Dubai":
            {
                boolean IsFound = false;
                if (Code.length() == 0 || Code.length() > 2)
                {
                    return false;
                }
                else
                {
                    for (String dubaiCode : DubaiCodes)
                    {
                        if (dubaiCode.equals(Code))
                        {
                            IsFound = true;
                            break;
                        }
                    }
                }
                return IsFound;
            }
            case "Fujairah":
            {
                boolean IsFound = false;
                if (Code.length() != 1)
                {
                    return false;
                }
                else
                {
                    for (String fujairahCode : FujairahCodes)
                    {
                        if (fujairahCode.equals(Code))
                        {
                            IsFound = true;
                            break;
                        }
                    }
                }
                return IsFound;
            }
            case "Ras Al Khaimah":
            {
                boolean IsFound = false;
                if (Code.length() != 1)
                {
                    return false;
                }
                else
                {
                    for (String rasAlKhaimahCode : RasAlKhaimahCodes)
                    {
                        if (rasAlKhaimahCode.equals(Code))
                        {
                            IsFound = true;
                            break;
                        }
                    }
                }
                return IsFound;
            }
            case "Sharjah":
            {
                boolean IsFound = false;
                if (Code.length() != 1)
                {
                    return false;
                }
                else
                {
                    for (String sharjahCode : SharjahCodes)
                    {
                        if (sharjahCode.equals(Code))
                        {
                            IsFound = true;
                            break;
                        }
                    }
                }
                return IsFound;
            }
            case "Umm Al Quwain":
            {
                boolean IsFound = false;
                if (Code.length() != 1)
                {
                    return false;
                }
                else
                {
                    for (String ummAlQuwainCode : UmmAlQuwainCodes)
                    {
                        if (ummAlQuwainCode.equals(Code))
                        {
                            IsFound = true;
                            break;
                        }
                    }
                }
                return IsFound;
            }
            default:
                return false;
        }
    }
    private boolean IsPlateNumberValid(String PlateNumber)
    {
        if (PlateNumber.length() == 0 || PlateNumber.length() > 5)
        {
            return false;
        }
        return IsAllDigits(PlateNumber);
    }
    private boolean IsDigit(char c)
    {
        return (c >= '0' && c <= '9');
    }
    private  boolean IsAllDigits(String Input)
    {
        for (int i = 0; i < Input.length(); i++)
        {
            if (!IsDigit(Input.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
    //endregion
}