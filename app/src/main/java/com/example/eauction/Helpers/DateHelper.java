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
}