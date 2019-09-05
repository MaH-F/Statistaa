package com.htbr.statistaa.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.htbr.statistaa.R;
import com.htbr.statistaa.Classes.Questionnaire;
import com.htbr.statistaa.Adapters.QuestionnaireAdapter;
import com.htbr.statistaa.Classes.UserHandler;

import java.io.Serializable;

public class QuestionnairesRecyclerActivity extends AppCompatActivity implements View.OnClickListener, QuestionnaireAdapter.OnQuestionnaireSelectedListener {
    private RecyclerView mQuestionnaireRecycler;
    private QuestionnaireAdapter mAdapter;


    private static final String TAG = "QuestionnaireRecycler";

    private FirebaseFirestore mFirestore;


    private Query mQuery;
    private int LIMIT = 50;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnairerecycler);

        mQuestionnaireRecycler = findViewById(R.id.my_questionnairerecycler_view);
        //TODO do we need this??
        //emptyView = findViewById(R.id.view_empty);



    }


    @Override
    public void onStart() {
        super.onStart();
        initFirestore();
        initRecyclerView();





        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }


    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();




        //get user group id
        long group = UserHandler.getUsergroup(this, FirebaseAuth.getInstance().getCurrentUser());;




        // Get the 50 highest rated restaurants
        //TODO generate GroupID
        mQuery = mFirestore.collection("Questtionnaires")
                .orderBy("number", Query.Direction.DESCENDING)
                .limit(LIMIT);



        mAdapter = new QuestionnaireAdapter(mQuery, this);



        mQuestionnaireRecycler.setAdapter(mAdapter);
    }



    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new QuestionnaireAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mQuestionnaireRecycler.setVisibility(View.GONE);
                    //emptyView.setVisibility(View.VISIBLE);
                } else {
                    mQuestionnaireRecycler.setVisibility(View.VISIBLE);
                    //emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mQuestionnaireRecycler.setLayoutManager(new LinearLayoutManager(this));
        mQuestionnaireRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onQuestionnaireSelected(DocumentSnapshot questionnaireSnapshot) {

        Questionnaire questionnaire = questionnaireSnapshot.toObject(Questionnaire.class);
        questionnaire.setId(questionnaireSnapshot.getId());
        Log.d(TAG, "Questionnaire id: " + questionnaire.getId() + " selected");


        Intent intent = new Intent(this, QuestionnaireScrollingActivity.class);
        intent.putExtra("Questionnaire", (Serializable) questionnaire);
        startActivity(intent);



    }
}