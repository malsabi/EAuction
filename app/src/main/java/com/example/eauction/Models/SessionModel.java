package com.example.eauction.Models;

public class SessionModel
{
    //region Fields
    private String UserId;
    private String IsActive;
    private String LastSeen;
    //endregion

    //region Constructors
    public SessionModel()
    {
        UserId = null;
        IsActive = null;
        LastSeen = null;
    }
    public SessionModel(String userId, String isActive, String lastSeen)
    {
        UserId = userId;
        IsActive = isActive;
        LastSeen = lastSeen;
    }
    //endregion

    //region Getters/Setters
    public String getUserId()
    {
        return UserId;
    }
    public void setUserId(String userId)
    {
        UserId = userId;
    }
    public String getIsActive()
    {
        return IsActive;
    }
    public void setIsActive(String isActive)
    {
        IsActive = isActive;
    }
    public String getLastSeen()
    {
        return LastSeen;
    }
    public void setLastSeen(String lastSeen)
    {
        LastSeen = lastSeen;
    }
    //endregion
}