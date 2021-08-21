package com.example.eauction.Validations;

import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;

public class UserValidation
{
    public ValidationResult ValidateٌRegister(User UserObj)
    {
        return new ValidationResult("", "", true);
    }

    public ValidationResult ValidateSignIn(SignIn SignInObj)
    {
        ValidationResult Result;
        if (EmailValidation(SignInObj.getEmail()).isSuccess() && PasswordValidation(SignInObj.getPassword()).isSuccess())
        {
            Result = new ValidationResult("", "Email and Password are valid", true);
        }
        else if (!EmailValidation(SignInObj.getEmail()).isSuccess())
        {
            Result = new ValidationResult("EtEmailLogin", "Email is invalid", false);
        }
        else
        {
            Result = new ValidationResult("EtPasswordLogin", "Password is invalid", false);
        }
        Result.setSuccess(true);
        return Result;
    }

    public ValidationResult NameValidation(String FirstName, String LastName)
    {
        return new ValidationResult();
    }
    public ValidationResult EmailValidation(String Email)
    {
        return new ValidationResult();
    }
    public ValidationResult PhoneNumberValidation(String PhoneNumber)
    {
        return new ValidationResult();
    }
    public ValidationResult PasswordValidation(String Password)
    {
        return new ValidationResult();
    }
    public ValidationResult DateValidation(String Date)
    {
        return new ValidationResult();
    }
    public ValidationResult GenderValidation(String Gender)
    {
        return new ValidationResult();
    }
    public ValidationResult SSNValidation(String SSN)
    {
        return new ValidationResult();
    }
}