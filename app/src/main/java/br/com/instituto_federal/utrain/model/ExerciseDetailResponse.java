package br.com.instituto_federal.utrain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ExerciseDetailResponse {
    @SerializedName("count")
    private int count;
    @SerializedName("results")
    private List<ExerciseInfo> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ExerciseInfo> getResults() {
        return results;
    }

    public void setResults(List<ExerciseInfo> results) {
        this.results = results;
    }
}