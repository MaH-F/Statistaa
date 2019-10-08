package com.htbr.statistaa.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htbr.statistaa.Classes.Exercise;
import com.htbr.statistaa.Interfaces.OnExerciseSelectedListener;
import com.htbr.statistaa.R;

import java.util.List;

public class ArchiveRecyclerAdapter extends RecyclerView.Adapter<ArchiveRecyclerAdapter.MyViewHolder> {




    private OnExerciseSelectedListener onExerciseSelectedListener;

    private List<Exercise> exerciseList;





    // Provide a suitable constructor (depends on the kind of dataset)
    public ArchiveRecyclerAdapter(List<Exercise> exerciseList, OnExerciseSelectedListener listener) {
        this.exerciseList = exerciseList;
        onExerciseSelectedListener = listener;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ArchiveRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        SharedPreferences sharedPreferences = parent.getContext().getSharedPreferences(parent.getContext().getString(R.string.text_size_prefs), Context.MODE_PRIVATE);
        int subTitleTextSize = sharedPreferences.getInt(parent.getContext().getString(R.string.exercise_subtitle_textSize), 36);




        return new ArchiveRecyclerAdapter.MyViewHolder(inflater.inflate(R.layout.item_exercise, parent, false), subTitleTextSize);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.bind(exerciseList.get(position), onExerciseSelectedListener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return exerciseList.size();
    }




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView subtitle;

        int subtitleSize;


        public MyViewHolder(View itemView, int subtitleSize) {
            super(itemView);

            nameView = itemView.findViewById(R.id.exercise_title);
            subtitle = itemView.findViewById(R.id.exercise_subtitle);

            this.subtitleSize = subtitleSize;


        }

        public void bind(final Exercise exercise, final OnExerciseSelectedListener listener) {





            nameView.setText(exercise.getName());
            subtitle.setText(exercise.getSubtitle());

            subtitle.setTextSize(subtitleSize);
            nameView.setTextSize(subtitleSize - 10);

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onExerciseSelected(exercise);
                    }
                }
            });
        }
    }
}


