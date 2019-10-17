package com.htbr.statistaa.classes;

public class ExerciseSheet {

    String exerciseId;
    Exercise exercise;
    String status;


    public ExerciseSheet(Exercise exercise){
        this.exerciseId = exercise.getId();
        this.exercise = exercise;
        this.status = "new";
    }

}
