package com.htbr.statistaa.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htbr.statistaa.R;

public class TermsActivity extends AppCompatActivity {

    public static String TAG = "TermsActvty";

    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);


        user = FirebaseAuth.getInstance().getCurrentUser();

        final CheckBox checkBox = (CheckBox) findViewById(R.id.termsAcceptCheckBox);
        final Button goOnButton = (Button) findViewById(R.id.GoOnButton);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(user.getUid()+"_terms", Context.MODE_PRIVATE);
        boolean accepted = sharedPreferences.getBoolean("accepted", false);


        if (accepted){
            checkBox.setChecked(true);
            goOnButton.setVisibility(View.VISIBLE);
        }




        String htmlString = getString(R.string.termsHTMLS);

        WebView webView = (WebView) findViewById(R.id.termsWebView);
        webView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(user.getUid()+"_terms", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (isChecked){

                    //continue
                    Toast.makeText(getApplicationContext(), getString(R.string.accepted), Toast.LENGTH_SHORT).show();

                    goOnButton.setVisibility(View.VISIBLE);



                    editor.putBoolean("accepted", true);
                    editor.apply();
                }

                else {



                    editor.putBoolean("accepted", false);
                    editor.apply();

                    Log.e(TAG, "Not accepted.");
                    finish();
                }
            }
        });


        goOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RootActivity.class));
            }
        });
    }
}
