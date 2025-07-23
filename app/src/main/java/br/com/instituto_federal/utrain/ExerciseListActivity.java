package br.com.instituto_federal.utrain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.instituto_federal.utrain.api.RetrofitClient;
import br.com.instituto_federal.utrain.api.WgerApiService;
import br.com.instituto_federal.utrain.favoritos.Favoritos;
import br.com.instituto_federal.utrain.model.Exercise;
import br.com.instituto_federal.utrain.model.ExerciseInfo;
import br.com.instituto_federal.utrain.model.ExerciseResponse;
import br.com.instituto_federal.utrain.model.Translation;
import br.com.instituto_federal.utrain.planilhas.ExercicioAdapter; // ✅ Usando o adapter principal

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseListActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseListActivity";
    private static final int LANGUAGE_ID_PORTUGUESE = 7;
    private static final int EXERCISE_LIMIT = 50; // Limite para evitar sobrecarga

    private RecyclerView recyclerView;
    private ExercicioAdapter exercicioAdapter; // ✅ Usando o adapter principal
    private ProgressBar progressBar;
    private TextView titleTextView;
    private WgerApiService apiService;
    private List<br.com.instituto_federal.utrain.data.model.Exercicio> exerciciosFinais = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.recycler_view_exercises);
        progressBar = findViewById(R.id.progress_bar);
        titleTextView = findViewById(R.id.tv_muscle_title);

        apiService = RetrofitClient.getClient();

        setupRecyclerView();

        int muscleId = getIntent().getIntExtra("muscleId", -1);
        String muscleName = getIntent().getStringExtra("muscleName");

        if (muscleId != -1 && muscleName != null) {
            titleTextView.setText("Exercícios para " + muscleName);
            loadExerciseIds(muscleId, muscleName);
        } else {
            Toast.makeText(this, "Erro: Dados do músculo inválidos", Toast.LENGTH_SHORT).show();
            finish();
        }

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.nav_api_exercises);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, Home.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (id == R.id.nav_favoritos) {
                startActivity(new Intent(this, Favoritos.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (id == R.id.nav_logout) {
                Toast.makeText(this, "Deslogando...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });

    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercicioAdapter = new ExercicioAdapter(this, exerciciosFinais);
        recyclerView.setAdapter(exercicioAdapter);
    }

    // Etapa 1: Carregar a lista de IDs de exercícios
    private void loadExerciseIds(int muscleId, final String muscleName) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Call<ExerciseResponse> call = apiService.getExercisesByMuscle(LANGUAGE_ID_PORTUGUESE, muscleId, EXERCISE_LIMIT);
        call.enqueue(new Callback<ExerciseResponse>() {
            @Override
            public void onResponse(Call<ExerciseResponse> call, Response<ExerciseResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getResults().isEmpty()) {
                    // Etapa 2: Para cada ID, buscar os detalhes
                    fetchDetailsForAllExercises(response.body().getResults(), muscleName);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ExerciseListActivity.this, "Nenhum exercício encontrado.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ExerciseResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ExerciseListActivity.this, "Falha na comunicação com a API.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Erro na Etapa 1", t);
            }
        });
    }

    // Etapa 2: Buscar detalhes para todos os exercícios da lista
    private void fetchDetailsForAllExercises(List<Exercise> exercises, final String muscleName) {
        final AtomicInteger counter = new AtomicInteger(0);
        final int totalExercises = exercises.size();
        exerciciosFinais.clear();

        for (Exercise apiExercise : exercises) {
            Call<ExerciseInfo> detailCall = apiService.getExerciseInfoById(apiExercise.getId());
            detailCall.enqueue(new Callback<ExerciseInfo>() {
                @Override
                public void onResponse(Call<ExerciseInfo> call, Response<ExerciseInfo> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        br.com.instituto_federal.utrain.data.model.Exercicio exercicioCompleto =
                                convertApiInfoToAppExercicio(response.body(), muscleName);
                        exerciciosFinais.add(exercicioCompleto);
                    }

                    // Verifica se todas as chamadas foram concluídas
                    if (counter.incrementAndGet() == totalExercises) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        exercicioAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ExerciseInfo> call, Throwable t) {
                    Log.e(TAG, "Falha ao buscar detalhe do exercício ID: " + apiExercise.getId(), t);
                    // Mesmo em caso de falha, incrementa para não bloquear a UI
                    if (counter.incrementAndGet() == totalExercises) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        exercicioAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private br.com.instituto_federal.utrain.data.model.Exercicio convertApiInfoToAppExercicio(ExerciseInfo exerciseInfo, String muscleName) {
        br.com.instituto_federal.utrain.data.model.Exercicio appExercicio = new br.com.instituto_federal.utrain.data.model.Exercicio();
        appExercicio.setId(exerciseInfo.getId());
        appExercicio.setMusculos(muscleName);
        appExercicio.setPlanilhaId(null);

        String name = "Exercício " + exerciseInfo.getId();
        String description = "Descrição não disponível.";
        String youtubeUrl = "";

        if (exerciseInfo.getTranslations() != null && !exerciseInfo.getTranslations().isEmpty()) {
            for (Translation t : exerciseInfo.getTranslations()) {
                if (t.getLanguage() == LANGUAGE_ID_PORTUGUESE) {
                    name = t.getName();
                    description = t.getDescription().replaceAll("<[^>]*>", "");
                    break;
                }
            }
        }

        appExercicio.setNome(name);
        appExercicio.setDescricao(description);

        // A API não fornece uma URL de vídeo de forma consistente, então deixamos em branco.
        appExercicio.setYoutubeId("");

        return appExercicio;
    }
}
