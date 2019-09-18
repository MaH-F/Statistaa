package com.htbr.statistaa.Classes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.htbr.statistaa.R;



public class UserHandler {
    private static final String TAG = "UserHandler";


    public static long getUsergroup(Activity c, FirebaseUser firebaseUser)  {


        SharedPreferences sharedPref = c.getSharedPreferences(c.getString(R.string.usergroup_sp_file_key),Context.MODE_PRIVATE);
        int defaultValue = 0;
        long group = sharedPref.getLong(firebaseUser.getUid(), defaultValue);
        return group;

    }
    public static void setUsergroup(Activity c, final FirebaseUser firebaseUser){

        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();



        final String uid = firebaseUser.getUid();

        final long[] group = {0};

        final Activity context = c;


        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.get("group"));
                        group[0] = (long) document.get("group");

                        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.usergroup_sp_file_key),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong(uid, group[0]);
                        editor.commit();
                        //editor.apply();
                        Log.d(TAG," SharedPreferences commit was successfull");
                    } else {
                        Log.d(TAG, "No such document");

                        // write -1 to file
                        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.usergroup_sp_file_key),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong(uid, -1);
                        editor.commit();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });










    }



}
