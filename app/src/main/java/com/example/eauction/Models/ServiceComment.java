package com.example.eauction.Models;

public class ServiceComment
{
    private String name;
    private String phoneNumber;
    private String email;
    private String comment;
    private String cost;

    public ServiceComment()
    {
        name = "";
        phoneNumber = "";
        email = "";
        comment = "";
        cost = "";
    }



    public ServiceComment(String name, String phoneNumber, String email, String comment, String cost)
    {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.comment = comment;
        this.cost = cost;
    }


    public void Set(ServiceComment serviceComment)
    {
        name = serviceComment.getName();
        phoneNumber = serviceComment.getPhoneNumber();
        email = serviceComment.getEmail();
        comment = serviceComment.getComment();
        cost = serviceComment.getCost();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
