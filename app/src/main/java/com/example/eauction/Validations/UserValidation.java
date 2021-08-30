package com.example.eauction.Validations;

import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.User;
import com.example.eauction.Models.ValidationResult;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        else if (!SSNValidation(UserObj.getSsn()).isSuccess())
        {
            Result.setSuccess(false);
            Result.setTitle("EtSSNSignup");
            Result.setMessage("SSN is invalid");
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
        else if (FirstName.length() > 15)
        {
            Result.setSuccess(false);
            Result.setMessage("First name should be less than 15 letters");
        }
        else
        {   //String: array of characters
            //"Mohammed1"
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
        if (IsValidEmailAddress(Email) == true)
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
        //050-123-4567, 050-abcedgf
        if (PhoneNumber.length() != 9)
        {
            Result.setSuccess(false);
            Result.setMessage("Phone number should contain 9 digits");
        }
        else
        {
            for (int i = 0; i < PhoneNumber.length(); i++)
            {
                if (!IsDigit(PhoneNumber.charAt(i)))
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

        if (Password.length() > 20)
        {
            Result.setSuccess(false);
            Result.setMessage("Password should not exceed 20 characters");
        }
        else if (Password.length() <= 5)
        {
            Result.setSuccess(false);
            Result.setMessage("Password should contain at least 6 or more characters");
        }
        return  Result;
    }
    public ValidationResult DateValidation(String Date)
    {
        ValidationResult Result = new ValidationResult();
        if (IsValidDate(Date) == true)
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
    public ValidationResult SSNValidation(String SSN)
    {
        ValidationResult Result = new ValidationResult();
        Result.setSuccess(true);
        Result.setMessage("Valid SSN");
        if (SSN.length() != 10)
        {
            Result.setSuccess(false);
            Result.setMessage("SSN Should contain 8 digits");
        }
        else
        {
            String[] Digits = SSN.split("-");
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < Digits[i].length(); j++)
                {
                    if (!IsDigit(Digits[i].charAt(j))) //123-a2-912
                    {
                        Result.setSuccess(false);
                        Result.setMessage("Invalid SSN");
                    }
                    break;
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
        if (ch >= 'a' && ch <= 'z')
        {
            return true;
        }
        else if (ch >= 'A' && ch <= 'Z')
        {
            return true;
        }
        return false;
    }
    public boolean IsValidEmailAddress(String emailAddress)
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
    private boolean IsValidDate(String date)
    {
        try
        {
            LocalDate.parse(date);
        }
        catch (DateTimeParseException ex)
        {
            return false;
        }
       return true;
    }
    //endregion
}