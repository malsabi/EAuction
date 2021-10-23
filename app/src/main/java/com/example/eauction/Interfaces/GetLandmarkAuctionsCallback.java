package com.example.eauction.Interfaces;

import com.example.eauction.Models.Landmark;
import java.util.ArrayList;

public interface GetLandmarkAuctionsCallback
{
    void onCallback(ArrayList<Landmark> Result);
}