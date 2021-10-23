package com.example.eauction.Interfaces;

import com.example.eauction.Models.Service;
import java.util.ArrayList;

public interface GetServiceAuctionsCallback
{
    void onCallback(ArrayList<Service> Result);
}