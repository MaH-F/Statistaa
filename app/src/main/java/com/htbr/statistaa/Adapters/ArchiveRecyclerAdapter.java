package com.htbr.statistaa.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    @Override
    public ArchiveRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ArchiveRecyclerAdapter.MyViewHolder(inflater.inflate(R.layout.item_exercise, parent, false));

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


        public MyViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);

        }

        public void bind(final Exercise exercise, final OnExerciseSelectedListener listener) {





            nameView.setText(exercise.getName());
            subtitle.setText(exercise.getSubtitle());



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


