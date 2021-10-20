package com.example.eauction.Cryptograpgy;
import android.annotation.SuppressLint;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES
{
    private static final String AlgorithmType = "AES";
    private static final byte[] PrivateKey = new byte[] { 'E', 'A', 'u', 'c', 't', 'i', 'o', 'n', '2', '0', '2', '1'};

    public static String Encrypt(String plainText) throws Exception
    {
        Key key = GenerateKey();
        @SuppressLint("GetInstance") Cipher CipherObj = Cipher.getInstance(AlgorithmType);
        CipherObj.init(Cipher.ENCRYPT_MODE, key);
        return BytesToBase64(CipherObj.doFinal(plainText.getBytes()));
    }

    public static String Decrypt(String encryptedText) throws Exception
    {
        Key key = GenerateKey();
        @SuppressLint("GetInstance") Cipher CipherObj = Cipher.getInstance(AlgorithmType);
        CipherObj.init(Cipher.DECRYPT_MODE, key);
        return new String(CipherObj.doFinal(Base64ToBytes(encryptedText)));
    }

    public static String BytesToBase64(byte[] Input)
    {
        return new String(Base64.getEncoder().encode(Input));
    }

    public static byte[] Base64ToBytes(String Input)
    {
        return Base64.getDecoder().decode(Input);
    }

    private static Key GenerateKey()
    {
        return new SecretKeySpec(PrivateKey, AlgorithmType);
    }
}