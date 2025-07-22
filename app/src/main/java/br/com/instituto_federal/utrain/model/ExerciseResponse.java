package br.com.instituto_federal.utrain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ExerciseResponse {
    @SerializedName("count")
    private int count;
    @SerializedName("next")
    private String next;
    @SerializedName("previous")
    private String previous;
    @SerializedName("results")
    private List<Exercise> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Exercise> getResults() {
        return results;
    }

    public void setResults(List<Exercise> results) {
        this.results = results;
    }
}