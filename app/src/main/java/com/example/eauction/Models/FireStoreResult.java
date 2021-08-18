package com.example.eauction.Models;

public class FireStoreResult
{
    private String Title;
    private String Message;
    private boolean IsSuccess;
    //region Constructors
    public FireStoreResult()
    {
        Title = "";
        Message = "";
        IsSuccess = false;
    }
    public FireStoreResult(String title, String message, boolean isSuccess)
    {
        Title = title;
        Message = message;
        IsSuccess = isSuccess;
    }
    //endregion
    //region Setters
    public void setTitle(String title)
    {
        Title = title;
    }
    public void setMessage(String message)
    {
        Message = message;
    }
    public void setSuccess(boolean success)
    {
        IsSuccess = success;
    }
    //endregion
    //region Getters
    public String getTitle()
    {
        return Title;
    }
    public String getMessage()
    {
        return Message;
    }
    public boolean isSuccess()
    {
        return IsSuccess;
    }
    //endregion
}
