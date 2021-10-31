package com.example.eauction.Cryptograpgy;

import android.util.Log;
import com.example.eauction.DataBase.FireStoreManager;
import com.example.eauction.Interfaces.GetFieldUserCallback;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESProvider
{
    //region Fields
    private final FireStoreManager FireStore;
    private final String AlgorithmType = "AES/CBC/PKCS5Padding";
    private final String IVSalt = "J@NcRfUjXn2r5u8x";
    //endregion

    public AESProvider(FireStoreManager FireStore)
    {
        this.FireStore = FireStore;
    }

    //region Public Methods
    //region Create Public and Private Keys
    public String CreatePrivateKey()
    {
        String PublicKey = "SgVkYp3s6v9y$B&E)H@McQfThWmZq4t7";
        String PrivateKey = "F)J@McQfTjWnZr4u7x!A%D*G-KaPdRgU";
        String EncryptedPrivateKey = AESEncrypt(PrivateKey, PublicKey.getBytes());
        Log.d("AESProvider", "CreatePrivateKey Encrypted Private Key: " + EncryptedPrivateKey);
        return EncryptedPrivateKey;
    }
    public String CreatePublicKey()
    {
        String PublicKey = "SgVkYp3s6v9y$B&E)H@McQfThWmZq4t7";
        byte[] EncryptedPrivateKey = Arrays.copyOfRange(CreatePrivateKey().getBytes(), 0, 32);
        Log.d("AESProvider", "CreatePublicKey Encrypted Private Key: " + new String(EncryptedPrivateKey));
        String EncryptedPublicKey = AESEncrypt(PublicKey, EncryptedPrivateKey);
        Log.d("AESProvider", "CreatePublicKey Encrypted Public Key: " + EncryptedPublicKey);
        return EncryptedPublicKey;
    }
    //endregion
    //region Encrypt/Decrypt Methods
    public String Encrypt(String Input)
    {
        return AESEncrypt(Input, GetPrivateKey());
    }
    public String Decrypt(String EncryptedInput)
    {
        return AESDecrypt(EncryptedInput, GetPrivateKey());
    }
    //endregion
    //region Base64 Extensions.
    public String BytesToBase64(byte[] Input)
    {
        return new String(Base64.getEncoder().encode(Input));
    }
    public byte[] Base64ToBytes(String Input)
    {
        return Base64.getDecoder().decode(Input);
    }
    //endregion
    //endregion
    //region Private Methods
    //region Getters for Private and Public Keys
    public byte[] GetPrivateKey()
    {
        Log.d("AESProvider", "GetPrivateKey Method Started");
        String EncryptedPrivateKey = FireStore.GetPrivateKey();
        Log.d("AESProvider", "GetPrivateKey Encrypted Private Key: " + EncryptedPrivateKey);

        byte[] PublicKey = GetPublicKey(EncryptedPrivateKey);
        Log.d("AESProvider", "GetPrivateKey Public Key: " + new String(PublicKey));
        return AESDecrypt(EncryptedPrivateKey, PublicKey).getBytes();
    }

    private byte[] GetPublicKey(String EncryptedPrivateKey)
    {
        Log.d("AESProvider", "GetPublicKey Method Started");
        String EncryptedPublicKey = FireStore.GetPublicKey();
        Log.d("AESProvider", "GetPublicKey Encrypted Public Key: " + EncryptedPublicKey);
        byte[] Key = Arrays.copyOfRange(EncryptedPrivateKey.getBytes(), 0, 32);
        Log.d("AESProvider", "GetPublicKey Encrypted Private Key: " + new String(Key));
        return AESDecrypt(EncryptedPublicKey, Key).getBytes();
    }
    //endregion
    //region AESProvider
    private String AESEncrypt(String Input, byte[] Key)
    {
        return BytesToBase64(Processor(Cipher.ENCRYPT_MODE, Input.getBytes(), Key));
    }
    public String AESDecrypt(String Input, byte[] Key)
    {
        return new String(Processor(Cipher.DECRYPT_MODE, Base64ToBytes(Input), Key));
    }
    private byte[] Processor(int Mode, byte[] Input, byte[] Key)
    {
        Log.d("AESProvider", "Input Length: " + Input.length + " Key Length: " + Key.length);
        SecretKeySpec SecretKey = new SecretKeySpec(Key, "AES");
        IvParameterSpec param = new IvParameterSpec(IVSalt.getBytes());
        try
        {
            Cipher CipherObj = Cipher.getInstance(AlgorithmType);
            CipherObj.init(Mode, SecretKey, param);
            return CipherObj.doFinal(Input);
        }
        catch (Exception e)
        {
            Log.d("AESProvider", "Processor Failure: " + e.getMessage());
            return null;
        }
    }
    //endregion
    //endregion
}