package com.example.eauction.Helpers;

import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ContactUsHelper
{
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
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(CompanyEmail));
                    message.setSubject(Title);
                    message.setText(Msg);
                    Transport.send(message);
                    Log.d("ContactUsHelper", "Message sent successfully");
                }
                catch (MessagingException e)
                {
                    Log.d("ContactUsHelper", e.getMessage());
                }
            }
            catch (Exception e)
            {
                Log.d("ContactUsHelper", e.getMessage());
            }
        });
        thread.start();
    }
}