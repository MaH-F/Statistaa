package com.htbr.statistaa.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.htbr.statistaa.Classes.FileWriter;
import com.htbr.statistaa.R;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {


    private String[] mDataset;
    private String questinnaireID;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public SeekBar seekBar;
        public MyViewHolder(View v) {
            super(v);
            textView = itemView.findViewById(R.id.item_question_content);
            seekBar = itemView.findViewById(R.id.seekBar);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public QuestionAdapter(String[] myDataset, String questionnaireID) {
        mDataset = myDataset;
        this.questinnaireID = questionnaireID;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(inflater.inflate(R.layout.item_question, parent, false));



    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Write code to perform some action when progress is changed.

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Write code to perform some action when touch is started.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Write code to perform some action when touch is stopped.





            }
        });




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
