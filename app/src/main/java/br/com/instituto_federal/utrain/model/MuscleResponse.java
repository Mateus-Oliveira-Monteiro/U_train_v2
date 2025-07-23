package br.com.instituto_federal.utrain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MuscleResponse {
    @SerializedName("count")
    private int count;
    @SerializedName("results")
    private List<Muscle> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Muscle> getResults() {
        return results;
    }

    public void setResults(List<Muscle> results) {
        this.results = results;
    }
}