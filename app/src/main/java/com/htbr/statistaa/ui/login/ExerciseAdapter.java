package com.htbr.statistaa.ui.login;



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
        //MaterialRatingBar ratingBar;
        TextView numRatingsView;
        TextView priceView;
        TextView categoryView;
        TextView cityView;

        public ViewHolder(View itemView) {


           super(itemView);

           nameView = itemView.findViewById(R.id.title);

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


            /*nameView.setText(exercise.getName());
            ratingBar.setRating((float) restaurant.getAvgRating());
            cityView.setText(restaurant.getCity());
            categoryView.setText(restaurant.getCategory());
            numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
                    restaurant.getNumRatings()));
            priceView.setText(RestaurantUtil.getPriceString(restaurant));*/

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