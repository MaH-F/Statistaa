package com.htbr.statistaa.Classes;

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
