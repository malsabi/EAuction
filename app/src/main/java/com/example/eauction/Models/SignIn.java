package com.example.eauction.Models;

public class SignIn
{
    private String email;
    private String password;

    //region Constructors
    public SignIn()
    {
        email = "";
        password = "";
    }
    public SignIn(String email, String password)
    {
        this.email = email;
        this.password = password;
    }
    //endregion
    //region Setters
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    //endregion
    //region Getters
    public String getEmail()
    {
        return email;
    }
    public String getPassword()
    {
        return password;
    }
    //endregion
}