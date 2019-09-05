package com.htbr.statistaa.Adapters;



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
import com.htbr.statistaa.R;
import com.htbr.statistaa.Classes.Exercise;


public class ExerciseAdapter extends MyAdapter<ExerciseAdapter.ViewHolder> {

    public interface OnExerciseSelectedListener {

        void onExerciseSelected(DocumentSnapshot exercise);

    }

    private OnExerciseSelectedListener mListener;

    public ExerciseAdapter(Query query, OnExerciseSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_exercise, parent, false));
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

        public ViewHolder(View itemView) {
           super(itemView);

           nameView = itemView.findViewById(R.id.title);
           subtitle = itemView.findViewById(R.id.subtitle);

/*
            imageView = itemView.findViewById(R.id.restaurant_item_image);
            nameView = itemView.findViewById(R.id.restaurant_item_name);
            //ratingBar = itemView.findViewById(R.id.restaurant_item_rating);
            numRatingsView = itemView.findViewById(R.id.restaurant_item_num_ratings);
            priceView = itemView.findViewById(R.id.restaurant_item_price);
            categoryView = itemView.findViewById(R.id.restaurant_item_category);
            cityView = itemView.findViewById(R.id.restaurant_item_city);*/
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnExerciseSelectedListener listener) {

            Exercise exercise = snapshot.toObject(Exercise.class);
            Resources resources = itemView.getResources();


            nameView.setText(exercise.getName());
            subtitle.setText(exercise.getSubtitle());


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