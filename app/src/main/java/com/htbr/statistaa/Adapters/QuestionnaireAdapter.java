package com.htbr.statistaa.Adapters;


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
import com.htbr.statistaa.R;
import com.htbr.statistaa.Classes.Questionnaire;


public class QuestionnaireAdapter extends MyAdapter<QuestionnaireAdapter.ViewHolder> {

    public interface OnQuestionnaireSelectedListener {

        void onQuestionnaireSelected(DocumentSnapshot questionnaire);

    }

    private OnQuestionnaireSelectedListener mListener;

    public QuestionnaireAdapter(Query query, OnQuestionnaireSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        SharedPreferences sharedPreferences = parent.getContext().getSharedPreferences(parent.getContext().getString(R.string.text_size_prefs), Context.MODE_PRIVATE);
        int textSize = sharedPreferences.getInt(parent.getContext().getString(R.string.exercise_subtitle_textSize), 36);

        return new ViewHolder(inflater.inflate(R.layout.item_questionnaire, parent, false), textSize);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameView;
        TextView date;
        int textSize;


        public ViewHolder(View itemView, int textSize) {


            super(itemView);

            nameView = itemView.findViewById(R.id.questionnaire_title);
            date = itemView.findViewById(R.id.questionnaire_date);
            this.textSize = textSize;
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
                         final OnQuestionnaireSelectedListener listener) {


            Questionnaire questionnaire = snapshot.toObject(Questionnaire.class);
            Resources resources = itemView.getResources();

            nameView.setText(questionnaire.getName());
            nameView.setTextSize(textSize);

            date.setTextSize(textSize - 10);

            //date.setText((String)questionnaire.getNumber());
            //date.setText(questionnaire.getDate().toDate());

           // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onQuestionnaireSelected(snapshot);
                    }
                }

            });



        }

    }
}