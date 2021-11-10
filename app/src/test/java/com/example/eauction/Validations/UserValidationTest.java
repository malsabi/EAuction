package com.example.eauction.Validations;

import com.example.eauction.Models.SignIn;
import com.example.eauction.Models.User;
import org.junit.Assert;
import org.junit.Test;

public class UserValidationTest
{
    @Test
    public void testValidateRegister()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        User DummyUser = new User();
        DummyUser.setFirstName("Shouq");
        DummyUser.setLastName("Aisha");
        DummyUser.setEmail("ShouqAisha@gmail.com");
        DummyUser.setPhoneNumber("050-458-5334");
        DummyUser.setPassword("123qwe321");
        DummyUser.setDate("12/07/1999");
        DummyUser.setGender("Male");
        DummyUser.setId("000-1111-1234567-0");

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.ValidateRegister(DummyUser).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testValidateSignIn()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        SignIn DummySignIn = new SignIn();
        DummySignIn.setEmail("ShouqAisha123@gmail.com");
        DummySignIn.setPassword("123qwe321!@");

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.ValidateSignIn(DummySignIn).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testFirstNameValidation()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        String FirstName = "Amna";

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.FirstNameValidation(FirstName).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testLastNameValidation()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        String LastName = "Aisha";

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.LastNameValidation(LastName).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testEmailValidation()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        String Email = "Aisha@outlook.com";

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.EmailValidation(Email).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testPhoneNumberValidation()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        String PhoneNumber = "050-123-4567";

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.PhoneNumberValidation(PhoneNumber).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testPasswordValidation()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        String Password = "kali_linux123";

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.PasswordValidation(Password).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testDateValidation()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        String Date = "19/09/1998";

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.DateValidation(Date).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testGenderValidation()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        String Gender = "Female";

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.GenderValidation(Gender).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testIdValidation()
    {
        //Given
        UserValidation UserValidator = new UserValidation();

        //When
        String Id = "000-0000-0000111-1";

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = UserValidator.IdValidation(Id).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }
}