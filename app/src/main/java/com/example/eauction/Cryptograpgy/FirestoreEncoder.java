package com.example.eauction.Cryptograpgy;

public class FirestoreEncoder
{
    public static String EncodeForFirebaseKey(String s)
    {
        return s
                .replace("_", "__")
                .replace(".", "_P")
                .replace("$", "_D")
                .replace("#", "_H")
                .replace("[", "_O")
                .replace("]", "_C")
                .replace("/", "_S");
    }

    public static String DecodeFromFirebaseKey(String Encoded)
    {
        Encoded = Encoded.replace("_S", "/");
        return Encoded;
    }
}
