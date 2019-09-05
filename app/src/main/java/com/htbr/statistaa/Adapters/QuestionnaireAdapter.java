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
        return new ViewHolder(inflater.inflate(R.layout.item_questionnaire, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameView;
        TextView date;



        public ViewHolder(View itemView) {


            super(itemView);

            nameView = itemView.findViewById(R.id.questionnaire_title);
            date = itemView.findViewById(R.id.questionnaire_date);

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