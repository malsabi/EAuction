package com.example.eauction.Helpers;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Base64;
import android.util.Log;

import com.example.eauction.Cryptograpgy.Hashing;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.VipPhoneNumber;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TelemetryHelper
{
    public static String ImageToBase64(String filePath)
    {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try
        {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }
        catch (Exception e)
        {
            encodeString = "";
        }
        return encodeString;
    }

    public static String ImageToBase64(Drawable ImgDrawable)
    {
        try
        {
            Bitmap image = ((BitmapDrawable)ImgDrawable).getBitmap();
            if (image == null)
            {
                return "";
            }
            else
            {
                ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
                return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
            }
        }
        catch (Exception ex)
        {
            return "";
        }
    }

    public static Bitmap Base64ToImage(String input)
    {
        byte[] decodedBytes = Base64.decode(input,0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String GetTelemetryType(Telemetry Telemetry)
    {
        if (Telemetry instanceof CarPlate)
        {
            return "CarPlate";
        }
        else if (Telemetry instanceof Car)
        {
            return "Car";
        }
        else if (Telemetry instanceof Landmark)
        {
            return "Landmark";
        }
        else if (Telemetry instanceof VipPhoneNumber)
        {
            return "VipPhoneNumber";
        }
        else if (Telemetry instanceof General)
        {
            return "General";
        }
        else if (Telemetry instanceof Service)
        {
            return "Service";
        }
        else
        {
            return "N/A";
        }
    }

    public static String GetTelemetryHash(String TelemetryPropertyValues)
    {
        return Hashing.SHA256(TelemetryPropertyValues);
    }

    public static Boolean IsTelemetryAdded(ArrayList<? extends Telemetry> Telemetries, Telemetry NewTelemetry)
    {
        for (Telemetry Telemetry : Telemetries)
        {
            if (Telemetry.getID().equals(NewTelemetry.getID()))
            {
                return true;
            }
        }
        return false;
    }

    public static int GetTelemetryIndex(ArrayList<? extends  Telemetry> Telemetries, String ID)
    {
        int Index = -1;
        for (Telemetry Telemetry : Telemetries)
        {
            if (Telemetry.getID().equals(ID))
            {
                Index = Telemetries.indexOf(Telemetry);
            }
        }
        return Index;
    }

    public static void SendServiceInformation(String CompanyEmail, String CompanyPassword, String UserEmail, String Title, String Msg)
    {
        Thread thread = new Thread(() ->
        {
            try
            {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(CompanyEmail, CompanyPassword);
                    }
                });
                try
                {
                    MimeMessage message = new MimeMessage(session);
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(UserEmail));
                    message.setSubject(Title);
                    message.setText(Msg);
                    Transport.send(message);
                    Log.d("TelemetryHelper", "Message sent successfully");
                }
                catch (MessagingException e)
                {
                    Log.d("TelemetryHelper", e.getMessage());
                }
            }
            catch (Exception e)
            {
                Log.d("TelemetryHelper", e.getMessage());
            }
        });
        thread.start();
    }
}