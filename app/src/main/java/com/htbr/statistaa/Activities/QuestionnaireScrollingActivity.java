package com.htbr.statistaa.Activities;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.htbr.statistaa.Adapters.QuestionAdapter;
import com.htbr.statistaa.Classes.Questionnaire;
import com.htbr.statistaa.R;

public class QuestionnaireScrollingActivity extends AppCompatActivity {

    private static final String TAG = "QuestionnaireScrollingA";

    Questionnaire questionnaire;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout  = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        questionnaire = (Questionnaire) getIntent().getSerializableExtra("Questionnaire");

        collapsingToolbarLayout.setTitle(questionnaire.getName());





        Log.d(TAG, questionnaire.getQuestions().toString());






        recyclerView = (RecyclerView) findViewById(R.id.question_recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new QuestionAdapter(questionnaire.getQuestions().toArray(new String[0]));
        recyclerView.setAdapter(mAdapter);




    }

}
