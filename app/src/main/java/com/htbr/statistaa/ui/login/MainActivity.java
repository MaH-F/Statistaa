package com.htbr.statistaa.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.htbr.statistaa.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // go to RootActivity if user is registered
        if (user != null){
            startActivity(new Intent(this,RootActivity.class));
            //finish this activity
            finish();
        }


        setContentView(R.layout.activity_main);


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();

                // Log and toast
               // String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, token);
                Toast.makeText(MainActivity.this, "registered", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.gotologin).setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        ;
        if (i == R.id.gotologin) {

            if(user == null) {

                startActivity(new Intent(this, LoginActivity.class));
            }
            else {
                startActivity(new Intent(this,RootActivity.class));
            }
        }
    }



}
