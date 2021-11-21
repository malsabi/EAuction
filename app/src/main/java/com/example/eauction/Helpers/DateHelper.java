package com.example.eauction.Helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper
{
    public static String GetCurrentDateTime()
    {
        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate now = LocalDate.now();
        return DateFormatter.format(now);
    }

    public static LocalDate ParseDateTime(String Date)
    {
        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(Date, DateFormatter);
    }

    public static boolean IsDateValid(String Date)
    {
        try
        {
            DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(Date, DateFormatter);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}