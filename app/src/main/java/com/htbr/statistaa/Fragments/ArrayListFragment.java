package com.htbr.statistaa.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htbr.statistaa.Classes.FileWriter;
import com.htbr.statistaa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArrayListFragment extends ListFragment {
    int mNum;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static ArrayListFragment newInstance(int num) {
        ArrayListFragment f = new ArrayListFragment();

        // Supply num input as an argument.
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
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_list, container, false);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        String [] exercises = new String[0];

        if (mNum == 0){
            try {
                JSONObject jsonObject = new JSONObject(FileWriter.readFile(getContext(),  FirebaseAuth.getInstance().getCurrentUser().getUid() + getString(R.string.mySelectedExerciseJSON)));
                //TODO: better way to convert JSONARRRAY to stringarray


                JSONArray jsonArray = (JSONArray) jsonObject.get("archive");
                exercises = jsonArray.toString().split(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            exercises = new String[]{"Exercise" + mNum, "Exercise" + mNum, "Exercise" + mNum};

        }


        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, exercises ));



    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }
}