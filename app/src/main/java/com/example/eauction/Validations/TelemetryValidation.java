package com.example.eauction.Validations;
import com.example.eauction.Models.*;

public class TelemetryValidation
{
    //Codes used in car plates for each emirate used in Validation
    private final String[] AbuDhabiCodes = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "17", "50"};
    private final String[] AjmanCodes = {"A", "B", "C", "D", "E"};
    private final String[] DubaiCodes = {"A", "AA"};
    private final String[] FujairahCodes = {"A", "B", "C", "D", "E", "F", "G", "K", "M", "P", "R", "S", "T"};
    private final String[] RasAlKhaimahCodes = {"A", "C", "D", "I", "K", "M", "N", "S", "V", "Y"};
    private final String[] SharjahCodes = {"1", "2", "3"};
    private final String[] UmmAlQuwainCodes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "X"};

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
                else if (!IsCodeValid("Ras Al Khaima", Code))
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