package com.htbr.statistaa.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.htbr.statistaa.Classes.FileWriter;
import com.htbr.statistaa.R;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {



    private String localJSONFilename;
    private JSONObject jsonObject;


    private String[] mDataset;
    private String questionnaireID;
    private Context context;


    int mode;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public SeekBar seekBar;
        public RelativeLayout seekBarRelativeLayout;
        public MyViewHolder(View v) {
            super(v);
            textView = itemView.findViewById(R.id.item_question_content);
            seekBar = itemView.findViewById(R.id.seekBar);
            seekBarRelativeLayout= itemView.findViewById(R.id.seekbar_relative_layout);

        }



    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public QuestionAdapter(String[] myDataset, String questionnaireID, Context context, int mode) {
        mDataset = myDataset;
        this.questionnaireID = questionnaireID;
        // we need the context to write to a file
        this.context = context;
        this.mode = mode;

        if(mode == 1){
            this.localJSONFilename = FirebaseAuth.getInstance().getCurrentUser().getUid() + "_" + questionnaireID;
            try {
                jsonObject = new JSONObject(FileWriter.readFile(context,  localJSONFilename));
            } catch (JSONException e) {
                e.printStackTrace();
                jsonObject = new JSONObject();
            }
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(inflater.inflate(R.layout.item_question, parent, false));



    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

        //0 means edit-sign appears -> no edit mode  , 1 is edit mode (fab shows a save sign)
        if (mode == 1){
            holder.seekBarRelativeLayout.setVisibility(View.VISIBLE);
           // holder.seekBar.setVisibility(View.VISIBLE);
            //holder.seekBar.setEnabled(true);


            //TODO is it possible to read JSON file only once?
            //JSONObject jsonObject;
            try {
                //jsonObject =  new JSONObject(FileWriter.readFile(context,  localJSONFilename));
                if(jsonObject.has(mDataset[position])){
                    holder.seekBar.setProgress((int)jsonObject.get(mDataset[position]));
                }
                else {
                    // JSON has no entry for the current question, so set it to mean progress
                    jsonObject.put(mDataset[position], holder.seekBar.getProgress());
                    // and write it to file
                    //TODO is this necessary for every time? find a better place for this and write only once
                    FileWriter.writeNewToFile(context, localJSONFilename, jsonObject.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




        } else if (mode == 0){
            holder.seekBarRelativeLayout.setVisibility(View.GONE);
            //holder.seekBar.setVisibility(View.INVISIBLE);
            //holder.seekBar.setEnabled(false);

        }

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

                //JSONObject jsonObject;

//                try {
//                    //File file = new File(getString(R.string.mySelectedExerciseJSON));
//                    //if(file.exists()){
//                    jsonObject = new JSONObject(FileWriter.readFile(context,  localJSONFilename));
//
//
//                } catch (JSONException e) {
//                    //if file does not exist
//                    e.printStackTrace();
//                    jsonObject = new JSONObject();
//
//                }

                try {
                    jsonObject.put(mDataset[position], seekBar.getProgress());
                } catch (Exception e){
                    e.printStackTrace();
                }


                FileWriter.writeNewToFile(context, localJSONFilename, jsonObject.toString());


            }
        });




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
