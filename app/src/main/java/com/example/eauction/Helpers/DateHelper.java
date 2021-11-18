package com.example.eauction.Helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper
{
    public static String GetCurrentDateTime()
    {
        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return DateFormatter.format(now);
    }

    public static LocalDateTime ParseDateTime(String Date)
    {
        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return LocalDateTime.parse(Date, DateFormatter);
    }

    public static boolean IsDateValid(String Date)
    {
        try
        {
            DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime.parse(Date, DateFormatter);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}