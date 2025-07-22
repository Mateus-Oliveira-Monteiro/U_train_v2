package br.com.instituto_federal.utrain.api;

import br.com.instituto_federal.utrain.model.ExerciseResponse;
import br.com.instituto_federal.utrain.model.MuscleResponse;
import br.com.instituto_federal.utrain.model.ExerciseDetail;
import br.com.instituto_federal.utrain.model.ExerciseDetailResponse;
import br.com.instituto_federal.utrain.model.ExerciseInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WgerApiService {
    @GET("muscle/")
    Call<MuscleResponse> getMuscles();

    @GET("exercise/")
    Call<ExerciseResponse> getExercises(@Query("language") int languageId);

    @GET("exerciseinfo/")
    Call<ExerciseDetailResponse> getExerciseDetail(@Query("exercise") int exerciseId, @Query("language") int languageId);

    @GET("exerciseinfo/{id}/")
    Call<ExerciseInfo> getExerciseById(@Path("id") int exerciseId);
}

