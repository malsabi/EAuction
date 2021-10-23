package com.example.eauction.Interfaces;

import com.example.eauction.Models.VipPhoneNumber;
import java.util.ArrayList;

public interface GetVIPPhoneAuctionsCallback
{
    void onCallback(ArrayList<VipPhoneNumber> Result);
}