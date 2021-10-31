package com.example.eauction.Utilities;

import android.os.AsyncTask;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutionException;

public class SyncFireStore extends AsyncTask<Object, Void, Object>
{
    public Object GetField(String Collection, String Document, String Field, FirebaseFirestore DB)
    {
        try
        {
            return this.execute(Collection, Document, Field, DB).get();
        }
        catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected Object doInBackground(Object[] objects)
    {
        FirebaseFirestore DB = (FirebaseFirestore)objects[3];
        Task<DocumentSnapshot> DocumentSnapShot = DB.collection((String) objects[0]).document((String) objects[1]).get();
        try
        {
            DocumentSnapshot documentSnapshot = Tasks.await(DocumentSnapShot);
            return documentSnapshot.get((String)objects[2]);
        }
        catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
