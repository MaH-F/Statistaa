package com.htbr.statistaa.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htbr.statistaa.R;




public class MyProfileActivity extends AppCompatActivity {

    private static final String TAG = "MyProfileActivity";

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();


        TextView userTextView = findViewById(R.id.userTextView);
        userTextView.setText(user.getEmail());

    }
}
