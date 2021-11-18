package com.example.eauction.Models;

import com.example.eauction.Enums.StatusEnum;

import java.util.ArrayList;

public class Telemetry
{
    private StatusEnum status;
    private double currentBid;
    private double basePrice;
    private String auctionStart;
    private String auctionEnd;
    private String details;
    private String id;
    private String ownerId;
    private String image;
    private ArrayList<Bid> bids;

    //region Constructor
    public Telemetry()
    {
        status = StatusEnum.None;
        currentBid = 0;
        basePrice = 0;
        auctionStart = "";
        auctionEnd = "";
        details = "";
        id = "";
        image = "";
        bids = null;
    }
    //endregion
    //region Setters
    public void setStatus(StatusEnum status)
    {
        this.status = status;
    }
    public void setCurrentBid(double currentBid)
    {
        this.currentBid = currentBid;
    }
    public void setBasePrice(double basePrice)
    {
        this.basePrice = basePrice;
    }
    public void setAuctionStart(String auctionStart)
    {
        this.auctionStart = auctionStart;
    }
    public void setAuctionEnd(String auctionEnd)
    {
        this.auctionEnd = auctionEnd;
    }
    public void setDetails(String details)
    {
        this.details = details;
    }
    public void setId(String id) {this.id = id;}
    public void setOwnerId(String ownerId) {this.ownerId = ownerId;}
    public void setImage(String image) {this.image = image; }
    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }
    //endregion
    //region Getters
    public StatusEnum getStatus()
    {
        return status;
    }
    public double getCurrentBid()
    {
        return currentBid;
    }
    public double getBasePrice()
    {
        return basePrice;
    }
    public String getAuctionStart()
    {
        return auctionStart;
    }
    public String getAuctionEnd()
    {
        return auctionEnd;
    }
    public String getDetails()
    {
        return details;
    }
    public String getID() {
        return id;
    }
    public String getOwnerId() {return ownerId;}
    public String getImage() {return  image; }
    public ArrayList<Bid> getBids() {
        return bids;
    }
    //endregion
}