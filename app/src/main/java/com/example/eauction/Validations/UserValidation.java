package com.example.eauction.Validations;

import android.util.Log;
import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation
{
    //region "Registration/Signing In Validation"
    public ValidationResult ValidateRegister(User UserObj)
    {
        ValidationResult Result = new ValidationResult();
        Result.setSuccess(true);
        Result.setMessage("Valid data");

        if (!FirstNameValidation(UserObj.getFirstName()).isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle("EtFirstNameSignup");
            Result.setMessage("First name is invalid");
        }
        else if (!LastNameValidation(UserObj.getLastName()).isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle("EtLastNameSignup");
            Result.setMessage("Last name is invalid");
        }
        else if (!EmailValidation(UserObj.getEmail()).isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle("EtEmailSignup");
            Result.setMessage("Email is invalid");
        }
        else if (!PhoneNumberValidation(UserObj.getPhoneNumber()).isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle("EtPhoneNumberSignup");
            Result.setMessage("Phone number is invalid");
        }
        else if (!PasswordValidation(UserObj.getPassword()).isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle("EtPasswordSignup");
            Result.setMessage("Password is invalid");
        }
        else if (!DateValidation(UserObj.getDate()).isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle("EtDOBSignup");
            Result.setMessage("Date is invalid");
        }
        else if (!IdValidation(UserObj.getId()).isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle("EtIdSignup");
            Result.setMessage("Id is invalid");
        }
        return Result;
    }
    public ValidationResult ValidateSignIn(SignIn SignInObj)
    {
        ValidationResult Result = new ValidationResult();
        Result.setSuccess(true);
        Result.setMessage("Valid data");
        if (EmailValidation(SignInObj.getEmail()).isSuccess() && PasswordValidation(SignInObj.getPassword()).isSuccess())
        {
            Result = new ValidationResult("", "Email and Password are valid", true);
        }
        else if (!EmailValidation(SignInObj.getEmail()).isSuccess())
        {
            Result = new ValidationResult("EtEmailLogin", "Email is invalid", false);
        }
        else if (!PasswordValidation(SignInObj.getPassword()).isSuccess())
        {
            Result = new ValidationResult("EtPasswordLogin", "Password is invalid", false);
        }
        return Result;
    }
    //endregion
    //region "Validation Functions
    public ValidationResult FirstNameValidation(String FirstName)
    {
        ValidationResult Result = new ValidationResult();
        Result.setSuccess(true);
        Result.setMessage("First name is valid");
        if (FirstName.length() <= 2)
        {
            Result.setSuccess(false);
            Result.setMessage("First name should be at least 3 or more letters");
        }
        else if (FirstName.length() > 30)
        {
            Result.setSuccess(false);
            Result.setMessage("First name should be less than 15 letters");
        }
        else
        {
            for (int i = 0; i < FirstName.length(); i++)
            {
                if (!IsLetter(FirstName.charAt(i)))
                {
                    Result.setSuccess(false);
                    Result.setMessage("First name should only contain alphabetical letters");
                }
            }
        }
        return Result;
    }
    public ValidationResult LastNameValidation(String LastName)
    {
        ValidationResult Result = new ValidationResult();
        Result.setSuccess(true);
        Result.setMessage("First name is valid");
        if (LastName.length() <= 2)
        {
            Result.setSuccess(false);
            Result.setMessage("First name should be at least 3 or more letters");
        }
        else if (LastName.length() > 15)
        {
            Result.setSuccess(false);
            Result.setMessage("First name should be less than 15 letters");
        }
        else
        {
            for (int i = 0; i < LastName.length(); i++)
            {
                if (!IsLetter(LastName.charAt(i)))
                {
                    Result.setSuccess(false);
                    Result.setMessage("Last name should only contain alphabetical letters");
                }
            }
        }
        return Result;
    }
    public ValidationResult EmailValidation(String Email)
    {
        ValidationResult Result = new ValidationResult();
        if (IsValidEmailAddress(Email))
        {
            Result.setSuccess(true);
            Result.setMessage("Valid email address");
        }
        else
        {
            Result.setSuccess(false);
            Result.setMessage("Invalid email address");
        }
        return Result;
    }
    public ValidationResult PhoneNumberValidation(String PhoneNumber)
    {
        ValidationResult Result = new ValidationResult();
        Result.setSuccess(true);
        Result.setMessage("Valid phone number");
        if (PhoneNumber.length() != 12)
        {
            Result.setSuccess(false);
            Result.setMessage("Phone number should contain 9 digits");
        }
        else
        {
            for (int i = 0; i < PhoneNumber.length(); i++)
            {
                if (!IsDigit(PhoneNumber.charAt(i)) && PhoneNumber.charAt(i) != '-')
                {
                    Result.setSuccess(false);
                    Result.setMessage("Phone number should contain only digits");
                    break;
                }
            }
        }
        return Result;
    }
    public ValidationResult PasswordValidation(String Password)
    {
        ValidationResult Result = new ValidationResult();
        Result.setSuccess(true);
        Result.setMessage("Valid password");

        if (Password.length() > 40)
        {
            Result.setSuccess(false);
            Result.setMessage("Password should not exceed 20 characters");
        }
        else if (Password.length() < 5)
        {
            Result.setSuccess(false);
            Result.setMessage("Password should contain at least 6 or more characters");
        }
        return  Result;
    }
    public ValidationResult DateValidation(String Date)
    {
        ValidationResult Result = new ValidationResult();
        if (IsValidDate(Date))
        {
            Result.setSuccess(true);
            Result.setMessage("Date is valid");
        }
        else
        {
            Result.setSuccess(false);
            Result.setMessage("Date is invalid");
        }
        return Result;
    }
    public ValidationResult GenderValidation(String Gender)
    {
        ValidationResult Result = new ValidationResult();
        if (Gender.equals("Female") || Gender.equals("Male"))
        {
            Result.setSuccess(true);
            Result.setMessage("Gender is valid");
        }
        else
        {
            Result.setSuccess(false);
            Result.setMessage("Gender is invalid");
        }
        return Result;
    }
    public ValidationResult IdValidation(String Id) //Update it to UAE ID Format.
    {
        ValidationResult Result = new ValidationResult();
        Result.setSuccess(true);
        Result.setMessage("Valid Id");
        if (Id.length() != 18)
        {
            Result.setSuccess(false);
            Result.setMessage("The Id contains invalid format or missing digits.");
        }
        else
        {
            String[] Digits = Id.split("-");
            if (Digits.length != 4)
            {
                Result.setSuccess(false);
                Result.setMessage("The Id contains invalid format.");
            }
            else
            {
                for (int i = 0; i < 4; i++)
                {
                    for (int j = 0; j < Digits[i].length(); j++)
                    {
                        if (!IsDigit(Digits[i].charAt(j)))
                        {
                            Result.setSuccess(false);
                            Result.setMessage("The Id contains non digit characters.");
                            break;
                        }
                    }
                }
            }
        }
        return Result;
    }
    //endregion
    //region "Private Methods"
    private boolean IsDigit(char ch)
    {
        return (ch >= '0' && ch <= '9');
    }
    private boolean IsLetter(char ch)
    {
        return  (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }
    public boolean IsValidEmailAddress(String emailAddress)
    {
        if (emailAddress.length() > 100)
        {
            return false;
        }
        else
        {
            Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
            Matcher regMatcher = regexPattern.matcher(emailAddress);
            if(regMatcher.matches())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    private boolean IsValidDate(String date)
    {
        if (!date.trim().equals(""))
        {
            SimpleDateFormat SDFormat = new SimpleDateFormat("mm/dd/yyyy");
            SDFormat.setLenient(false);
            try
            {
                SDFormat.parse(date);
                return true;
            }
            catch (ParseException e)
            {
                Log.d("DATE", e.getMessage());
                return false;
            }
        }
        return false;
    }
    //endregion
}