package com.example.eauction.Interfaces;

import com.example.eauction.Models.Car;
import java.util.ArrayList;

public interface GetCarAuctionsCallback
{
    void onCallback(ArrayList<Car> Result);
}