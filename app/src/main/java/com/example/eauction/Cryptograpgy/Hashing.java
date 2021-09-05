package com.example.eauction.Cryptograpgy;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hashing
{
    public static String SHA256(String Input)
    {
        try
        {
            MessageDigest Digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = Digest.digest(Input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }
        catch (NoSuchAlgorithmException ex)
        {
            return null;
        }
    }
}