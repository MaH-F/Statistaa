package com.htbr.statistaa.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.htbr.statistaa.Adapters.ArchiveRecyclerAdapter;
import com.htbr.statistaa.Classes.Exercise;
import com.htbr.statistaa.Classes.FileWriter;
import com.htbr.statistaa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycelerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;





    List<Exercise> exerciseList;

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

        exerciseList = new ArrayList<Exercise>();


        //make dataset

        if (mNum == 0) {


            try {
                JSONObject jsonObject = new JSONObject(FileWriter.readFile(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid() + getString(R.string.mySelectedExerciseJSON)));
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.archive_JSON_ARRAY_key));

                for (int i = 0; i < jsonArray.length(); i++) {
                    //TODO: if a string is in the json that is not a file, what to do?
                    Exercise exercise = (Exercise) FileWriter.readObjectFromFile(getContext(), jsonArray.getString(i));
                    if (exercise != null) {
                        exerciseList.add(exercise);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        recycelerViewAdapter = new ArchiveRecyclerAdapter(exerciseList);




        recyclerView.setAdapter(recycelerViewAdapter);



    }



}
