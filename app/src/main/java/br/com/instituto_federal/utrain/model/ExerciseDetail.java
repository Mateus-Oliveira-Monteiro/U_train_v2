package br.com.instituto_federal.utrain.model;

import com.google.gson.annotations.SerializedName;

public class ExerciseDetail {
    @SerializedName("id")
    private int id;
    @SerializedName("exercise")
    private int exerciseId;
    @SerializedName("language")
    private int languageId;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}