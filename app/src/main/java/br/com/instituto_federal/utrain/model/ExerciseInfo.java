package br.com.instituto_federal.utrain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ExerciseInfo {
    @SerializedName("id")
    private int id;
    @SerializedName("translations")
    private List<Translation> translations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}