package com.example.eauction.Validations;

import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.VipPhoneNumber;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class TelemetryValidationTest extends TestCase
{
    @Test
    public void testCarPlateValidation()
    {
        //Given
        TelemetryValidation Validator = new TelemetryValidation();

        //When
        CarPlate DummyCarPlate = new CarPlate();
        DummyCarPlate.setPlateCode("50");
        DummyCarPlate.setEmirate("Abu Dhabi");
        DummyCarPlate.setPlateNumber("123");

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = Validator.CarPlateValidation(DummyCarPlate).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testCarValidation()
    {
        //Given
        TelemetryValidation Validator = new TelemetryValidation();

        //When
        Car DummyCar = new Car();
        DummyCar.setModel("Niro");
        DummyCar.setMileage(51);
        DummyCar.setName("KIA");
        DummyCar.setHorsePower(139);

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = Validator.CarValidation(DummyCar).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testLandmarkValidation()
    {
        //Given
        TelemetryValidation Validator = new TelemetryValidation();

        //When
        Landmark DummyLandmark = new Landmark();
        DummyLandmark.setName("Burj Alarab");
        DummyLandmark.setArea(234);
        DummyLandmark.setLocation("Jumirah-Dubai");
        DummyLandmark.setType("Hotel And Resort");

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = Validator.LandmarkValidation(DummyLandmark).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testVipPhoneNumberValidation()
    {
        //Given
        TelemetryValidation Validator = new TelemetryValidation();

        //When
        VipPhoneNumber DummyVipPhone = new VipPhoneNumber();
        DummyVipPhone.setPhoneNumber("050-123-5678");
        DummyVipPhone.setCompany("Etisalat");

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = Validator.VipPhoneNumberValidation(DummyVipPhone).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }

    @Test
    public void testGeneralValidation()
    {
        //Given
        TelemetryValidation Validator = new TelemetryValidation();

        //When
        General DummyGeneral = new General();
        DummyGeneral.setName("Nintendo 2DS XL");

        //Actual: The actual value is the result from the validation and should be true.
        boolean ActualValue = Validator.GeneralValidation(DummyGeneral).isSuccess();

        //If the actualValue is true, then it passed the test, otherwise it will throw an exception.
        Assert.assertEquals(true, ActualValue);
    }
}