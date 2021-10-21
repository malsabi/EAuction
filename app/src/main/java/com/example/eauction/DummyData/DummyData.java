package com.example.eauction.DummyData;

import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.VipPhoneNumber;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static ArrayList<Telemetry> GetDummyItems(){

        ArrayList<Telemetry> Telemetries = new ArrayList<>();

        CarPlate carPlate = new CarPlate();
        carPlate.setPlateCode("AUH");
        carPlate.setPlateNumber("12345");
        carPlate.setEmirate("Abu Dhabi");
        carPlate.setDetails("Details of Telemetry");
        carPlate.setBasePrice(3200);
        carPlate.setCurrentBid(3200);

        Car car = new Car();
        car.setName("Honda");
        car.setModel("Civic");
        car.setMileage(60000);
        car.setDetails("First Owner");
        car.setBasePrice(2000);
        car.setCurrentBid(2000);

        Landmark landmark = new Landmark();
        landmark.setName("Pharaoh Tower");
        landmark.setLocation("Abu Dhabi, Tourist Club");
        landmark.setType("Tower");
        landmark.setArea(500);
        landmark.setDetails("The tower is 50 years old");
        landmark.setBasePrice(1500);
        landmark.setCurrentBid(1500);

        VipPhoneNumber vipPhoneNumber = new VipPhoneNumber();
        vipPhoneNumber.setPhoneNumber("0504444442");
        vipPhoneNumber.setCompany("Etisalat");
        vipPhoneNumber.setDetails("No Details");
        vipPhoneNumber.setBasePrice(800);
        vipPhoneNumber.setCurrentBid(800);

        General item = new General();
        item.setName("Diamond Pen");
        item.setDetails("Gold pen with diamonds");
        item.setBasePrice(450);
        item.setCurrentBid(450);

        Telemetries.add(carPlate);
        Telemetries.add(car);
        Telemetries.add(landmark);
        Telemetries.add(vipPhoneNumber);
        Telemetries.add(item);

        return Telemetries;
    }

}
