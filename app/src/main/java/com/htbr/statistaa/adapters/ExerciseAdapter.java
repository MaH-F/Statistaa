package com.htbr.statistaa.adapters;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.htbr.statistaa.interfaces.OnExerciseSelectedListener;
import com.htbr.statistaa.R;
import com.htbr.statistaa.classes.Exercise;


public class ExerciseAdapter extends MyAdapter<ExerciseAdapter.ViewHolder> {


    private OnExerciseSelectedListener mListener;

    public ExerciseAdapter(Query query, OnExerciseSelectedListener listener) {
        super(query);
        mListener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        SharedPreferences sharedPreferences = parent.getContext().getSharedPreferences(parent.getContext().getString(R.string.text_size_prefs), Context.MODE_PRIVATE);
        int subTitleTextSize = sharedPreferences.getInt(parent.getContext().getString(R.string.exercise_subtitle_textSize), 20);


        return new ViewHolder(inflater.inflate(R.layout.item_exercise, parent, false), subTitleTextSize);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameView;
        TextView subtitle;
        //MaterialRatingBar ratingBar;
        TextView numRatingsView;
        TextView priceView;
        TextView categoryView;
        TextView cityView;



        int subtitleSize;


        public ViewHolder(View itemView, int subtitleSize) {
           super(itemView);

           nameView = itemView.findViewById(R.id.exercise_title);
           subtitle = itemView.findViewById(R.id.exercise_subtitle);

           this.subtitleSize = subtitleSize;


        }

        public void bind(final DocumentSnapshot snapshot, final OnExerciseSelectedListener listener) {

            Exercise exercise = snapshot.toObject(Exercise.class);
            Resources resources = itemView.getResources();


            nameView.setText(exercise.getName());
            subtitle.setText(exercise.getSubtitle());

            subtitle.setTextSize(subtitleSize);
            nameView.setTextSize(subtitleSize - 10);



            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onExerciseSelected(snapshot);
                    }
                }
            });
        }

    }
}