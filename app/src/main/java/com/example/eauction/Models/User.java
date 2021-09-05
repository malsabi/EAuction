package com.example.eauction.Models;

import java.util.List;

public class User
{
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String date;
    private String gender;
    private String nationality;
    private String ssn;
    private String profilePicture;
    private List<Telemetry> ownedTelemetry;
    private String isActive;

    //region Constructors
    public User()
    {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phoneNumber = "";
        this.password = "";
        this.date = "";
        this.gender = "";
        this.nationality = "";
        this.ssn = "";
        this.profilePicture = "";
        this.ownedTelemetry = null;
        this.isActive = "Offline";
    }

    public User(String firstName, String lastName, String email, String phoneNumber, String password, String date, String gender, String nationality, String ssn, String profilePicture, List<Telemetry> ownedTelemetry, String isActive)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.date = date;
        this.gender = gender;
        this.nationality = nationality;
        this.ssn = ssn;
        this.profilePicture = profilePicture;
        this.ownedTelemetry = ownedTelemetry;
        this.isActive = isActive;
    }
    //endregion
    //region Setters
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }
    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }
    public void setSsn(String ssn)
    {
        this.ssn = ssn;
    }
    public void setProfilePicture(String profilePicture)
    {
        this.profilePicture = profilePicture;
    }
    public void setOwnedTelemetry(List<Telemetry> ownedTelemetry)
    {
        this.ownedTelemetry = ownedTelemetry;
    }
    public void setActive(String active)
    {
        isActive = active;
    }
    //endregion
    //region Getters
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getEmail()
    {
        return email;
    }
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public String getPassword()
    {
        return password;
    }
    public String getDate()
    {
        return date;
    }
    public String getGender()
    {
        return gender;
    }
    public String getNationality()
    {
        return nationality;
    }
    public String getSsn()
    {
        return ssn;
    }
    public String getProfilePicture()
    {
        return profilePicture;
    }
    public List<Telemetry> getOwnedTelemetry()
    {
        return ownedTelemetry;
    }
    public String getActive()
    {
        return isActive;
    }
    //endregion
}