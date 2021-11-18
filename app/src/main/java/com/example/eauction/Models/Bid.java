package com.example.eauction.Models;

public class Bid
{
    private String UserId;
    private double currentBid;

    public Bid()
    {
        UserId = null;
        currentBid = 0;
    }

    public Bid(String userId, double currentBid)
    {
        UserId = userId;
        this.currentBid = currentBid;
    }

    public String getUserId()
    {
        return UserId;
    }

    public void setUserId(String userId)
    {
        UserId = userId;
    }

    public double getCurrentBid()
    {
        return currentBid;
    }

    public void setCurrentBid(double currentBid)
    {
        this.currentBid = currentBid;
    }
}