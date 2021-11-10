package com.example.eauction.Helpers;

import android.util.Log;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class ResetPasswordHelper
{
    public static String GetOTPCode(int Size)
    {
        String Sequence = "0123456789";
        Random RND = new Random();
        StringBuilder OTPCode = new StringBuilder(Size);
        for (int i = 0; i < Size; i++)
        {
            OTPCode.append(Sequence.charAt(RND.nextInt(Sequence.length())));
        }
        return OTPCode.toString();
    }

    public static void SendMessage(String CompanyEmail, String CompanyPassword, String UserEmail, String Title, String Msg)
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
                    Log.d("ResetPasswordHelper", "Message sent successfully");
                }
                catch (MessagingException e)
                {
                    Log.d("ResetPasswordHelper", e.getMessage());
                }
            }
            catch (Exception e)
            {
                Log.d("ResetPasswordHelper", e.getMessage());
            }
        });
        thread.start();
    }
}
