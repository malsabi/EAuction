package com.example.eauction.Helpers;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

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
}