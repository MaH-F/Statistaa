package com.htbr.statistaa.Interfaces;

import com.google.firebase.firestore.DocumentSnapshot;
import com.htbr.statistaa.Classes.Exercise;

public interface OnExerciseSelectedListener {
    void onExerciseSelected(DocumentSnapshot exercise);

    void onExerciseSelected(Exercise exercise);
}
