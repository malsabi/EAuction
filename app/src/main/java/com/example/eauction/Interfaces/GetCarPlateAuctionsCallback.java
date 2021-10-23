package com.example.eauction.Interfaces;

import com.example.eauction.Models.CarPlate;
import java.util.ArrayList;

public interface GetCarPlateAuctionsCallback
{
    void onCallback(ArrayList<CarPlate> Result);
}
