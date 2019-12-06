package com.htbr.statistaa.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htbr.statistaa.R;

public class ChangePasswordActivity extends AppCompatActivity {


    private static final String TAG = "ChPwdActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chpassword);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        final EditText a = (EditText) findViewById(R.id.newPassword1);
        final EditText b = (EditText) findViewById(R.id.newPassword2);


        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();


        Button button = (Button) findViewById(R.id.button_chpw_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNewPassword = a.getText().toString();
                String secondNewPassword = b.getText().toString();

                if(firstNewPassword.equals(secondNewPassword)){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String newPassword = firstNewPassword;

                    if (user != null){
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                        }
                                    }
                                });
                    }

                }
            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
