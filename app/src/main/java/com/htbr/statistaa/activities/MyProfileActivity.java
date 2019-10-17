package com.htbr.statistaa.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();


        TextView userTextView = findViewById(R.id.userTextView);
        userTextView.setText(user.getEmail());

    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            if ( item.getItemId() == android.R.id.home){
                finish();
            }

            return super.onOptionsItemSelected(item);
        }
}
