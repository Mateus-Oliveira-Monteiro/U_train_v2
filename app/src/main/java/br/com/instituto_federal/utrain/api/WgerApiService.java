package br.com.instituto_federal.utrain.api;

import br.com.instituto_federal.utrain.model.ExerciseInfo;
import br.com.instituto_federal.utrain.model.ExerciseResponse;
import br.com.instituto_federal.utrain.model.MuscleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WgerApiService {

    @GET("muscle")
    Call<MuscleResponse> getMuscles();

    @GET("exercise")
    Call<ExerciseResponse> getExercisesByMuscle(
            @Query("language") int languageId,
            @Query("muscles") int muscleId,
            @Query("limit") int limit
    );

    // ✅ MÉTODO ADICIONADO DE VOLTA: Essencial para buscar os detalhes
    @GET("exerciseinfo/{id}/")
    Call<ExerciseInfo> getExerciseInfoById(@Path("id") int exerciseId);
}