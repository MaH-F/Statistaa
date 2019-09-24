package com.htbr.statistaa.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.htbr.statistaa.Activities.ExerciseScrollingActivity;
import com.htbr.statistaa.Adapters.ArchiveRecyclerAdapter;
import com.htbr.statistaa.Adapters.ExerciseAdapter;
import com.htbr.statistaa.Classes.Exercise;
import com.htbr.statistaa.Classes.FileWriter;
import com.htbr.statistaa.Classes.UserHandler;
import com.htbr.statistaa.Interfaces.OnExerciseSelectedListener;
import com.htbr.statistaa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements OnExerciseSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    static final String TAG = "RecViewFragment";


    List<Exercise> exerciseList;

    List<Exercise> exerciseListGroup1;
    List<Exercise> exerciseListGroup2;
    List<Exercise> exerciseListGroup3;

    int mNum;

    public static RecyclerViewFragment newInstance(int num){
        RecyclerViewFragment f = new RecyclerViewFragment();



        //Supply num input as an argument
        Bundle args = new Bundle();
        args.putInt("num", num);



        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mNum = getArguments() != null ? getArguments().getInt("num") : 1;



        exerciseListGroup1 = new ArrayList<Exercise>();
        exerciseListGroup2 = new ArrayList<Exercise>();
        exerciseListGroup3 = new ArrayList<Exercise>();

        //make dataset
        //TODO: really every time ??

        try {

            JSONArray mySelectetExerciseJSONArray = new JSONArray(FileWriter.readFile(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid() + getString(R.string.mySelectedExerciseJSON)));



            JSONObject exerciseJSON;
            JSONObject detailsJSON;
            for (int i = 0; i < mySelectetExerciseJSONArray.length(); i++){
                final Exercise exercise;
                exerciseJSON = (JSONObject) mySelectetExerciseJSONArray.get(i);
                detailsJSON = (JSONObject) exerciseJSON.get("exercise");
                //TODO: if a string is in the json that is not a file, what to do?
                String exerciseFileName = detailsJSON.getString(getString(R.string.jsonparam_exercise_id));





                // if file does not exist
                if  ( FileWriter.exists(getContext(),exerciseFileName) == 0 ){
                    Log.e(TAG, "File " + exerciseFileName + " does not exist!");


                }
                else {
                    //file exists
                    exercise = (Exercise) FileWriter.readObjectFromFile(getContext(), exerciseFileName);


                    switch ((int) detailsJSON.get("group")){
                        case 0:
                            exerciseListGroup1.add(exercise);
                            break;
                        case 1:
                            exerciseListGroup2.add(exercise);
                            break;
                        case 2:
                            exerciseListGroup3.add(exercise);
                            break;
                    }
                }









            }



        } catch (JSONException e) {
            e.printStackTrace();
        }




        switch (mNum){
            case 0:
                exerciseList = exerciseListGroup1;
                break;
            case 1:
                exerciseList = exerciseListGroup2;
                break;
            case 2:
                exerciseList = exerciseListGroup3;
                break;
        }







    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_list, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.archiveRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //specify an adpater
        recyclerViewAdapter = new ArchiveRecyclerAdapter(exerciseList, this);






        recyclerView.setAdapter(recyclerViewAdapter);



    }


    @Override
    public void onExerciseSelected(DocumentSnapshot exercise) {
        //do nothing
    }

    @Override
    public void onExerciseSelected(Exercise exercise) {
        Log.d(TAG, "Exercise " + exercise.getId() + " selected");


        Intent intent = new Intent(getContext(), ExerciseScrollingActivity.class);
        intent.putExtra("Exercise" , (Serializable) exercise);
        intent.putExtra("CallingActivity", "RecyclerViewFragment");
        startActivity(intent);

    }
}
