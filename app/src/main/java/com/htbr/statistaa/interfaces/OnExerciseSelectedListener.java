package com.htbr.statistaa.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;
import com.htbr.statistaa.classes.Exercise;

public interface OnExerciseSelectedListener {
    void onExerciseSelected(DocumentSnapshot exercise);

    void onExerciseSelected(Exercise exercise);
}
