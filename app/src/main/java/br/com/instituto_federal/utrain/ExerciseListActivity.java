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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.instituto_federal.utrain.api.RetrofitClient;
import br.com.instituto_federal.utrain.api.WgerApiService;
import br.com.instituto_federal.utrain.favoritos.Favoritos;
import br.com.instituto_federal.utrain.model.Exercise;
import br.com.instituto_federal.utrain.model.ExerciseDetailResponse;
import br.com.instituto_federal.utrain.model.ExerciseInfo;
import br.com.instituto_federal.utrain.model.ExerciseResponse;
import br.com.instituto_federal.utrain.model.Translation;
import br.com.instituto_federal.utrain.planilhas.Exercicio;
import br.com.instituto_federal.utrain.planilhas.ExercicioAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseListActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseListActivity";

    private RecyclerView recyclerView;
    private ExercicioAdapter exercicioAdapter;
    private ProgressBar progressBar;
    private TextView titleTextView;
    private WgerApiService apiService;
    private List<Exercicio> exerciciosParaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        // Inicializar views
        recyclerView = findViewById(R.id.recycler_view_exercises);
        progressBar = findViewById(R.id.progress_bar);
        titleTextView = findViewById(R.id.tv_muscle_title);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obter dados do Intent
        int muscleId = getIntent().getIntExtra("muscleId", -1);
        String muscleName = getIntent().getStringExtra("muscleName");

        // Configurar título
        titleTextView.setText("Exercícios para " + muscleName);

        // Inicializar API service
        apiService = RetrofitClient.getClient();

        // Inicializar a lista do adapter
        exerciciosParaAdapter = new ArrayList<>();
        exercicioAdapter = new ExercicioAdapter(this, exerciciosParaAdapter);
        recyclerView.setAdapter(exercicioAdapter);

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

        // Carregar exercícios
        if (muscleId != -1) {
            loadExercises(muscleId);
        } else {
            Toast.makeText(this, "Erro: ID do músculo inválido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadExercises(int muscleId) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Log.d(TAG, "Iniciando loadExercises para muscleId: " + muscleId);

        // Buscar todos os exercícios em português e filtrar localmente por grupo muscular
        Call<ExerciseResponse> call = apiService.getExercises(7); // language=2 para português
        call.enqueue(new Callback<ExerciseResponse>() {
            @Override
            public void onResponse(Call<ExerciseResponse> call, Response<ExerciseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Exercise> allExercises = response.body().getResults();
                    Log.d(TAG, "Total de exercícios recebidos da API: " + (allExercises != null ? allExercises.size() : 0));

                    if (allExercises != null && !allExercises.isEmpty()) {
                        // Filtrar exercícios que trabalham o grupo muscular selecionado
                        List<Exercise> filteredExercises = new ArrayList<>();
                        Set<Integer> addedExerciseIds = new HashSet<>(); // Para evitar duplicatas

                        for (Exercise exercise : allExercises) {
                            // Verifica se o músculo principal ou secundário corresponde ao muscleId
                            // E se o exercício ainda não foi adicionado (para evitar duplicatas da API)
                            if (!addedExerciseIds.contains(exercise.getId()) &&
                                    ((exercise.getMuscles() != null && exercise.getMuscles().contains(muscleId)) ||
                                            (exercise.getMusclesSecondary() != null && exercise.getMusclesSecondary().contains(muscleId)))) {
                                filteredExercises.add(exercise);
                                addedExerciseIds.add(exercise.getId());
                                Log.d(TAG, "Exercício filtrado: " + exercise.getId() + " - Músculos: " + exercise.getMuscles() + " - Músculos Secundários: " + exercise.getMusclesSecondary());
                            }
                        }

                        Log.d(TAG, "Total de exercícios filtrados para muscleId " + muscleId + ": " + filteredExercises.size());

                        if (!filteredExercises.isEmpty()) {
                            // Embaralhar a lista para ter exercícios diferentes a cada vez
                            Collections.shuffle(filteredExercises);
                            // Limitar a 10 exercícios para evitar muitas requisições e processamento
                            int maxExercises = Math.min(filteredExercises.size(), 100);
                            Log.d(TAG, "Carregando detalhes para " + maxExercises + " exercícios.");

                            exerciciosParaAdapter.clear(); // Limpa a lista para evitar duplicação
                            final AtomicInteger loadedDetailsCount = new AtomicInteger(0);
                            final Map<Integer, Exercicio> tempExerciseMap = Collections.synchronizedMap(new HashMap<>()); // Usar um mapa para garantir unicidade e ordem

                            for (int i = 0; i < maxExercises; i++) {
                                Exercise exercise = filteredExercises.get(i);
                                Log.d(TAG, "Buscando detalhes para o exercício ID: " + exercise.getId());
                                // Para cada exercício, buscar os detalhes (nome e descrição) usando o endpoint específico
                                apiService.getExerciseById(exercise.getId()).enqueue(new Callback<ExerciseInfo>() {
                                    @Override
                                    public void onResponse(Call<ExerciseInfo> detailCall, Response<ExerciseInfo> detailResponse) {
                                        if (detailResponse.isSuccessful() && detailResponse.body() != null) {
                                            ExerciseInfo exerciseInfo = detailResponse.body();

                                            String exerciseName = "Exercício " + exercise.getId();
                                            String exerciseDescription = "Descrição não disponível";

                                            Log.d(TAG, "Resposta da API para exercício ID " + exercise.getId() + ": " + exerciseInfo.toString());

                                            // Procurar por tradução em português (language = 7)
                                            if (exerciseInfo.getTranslations() != null && !exerciseInfo.getTranslations().isEmpty()) {
                                                Log.d(TAG, "Traduções encontradas para ID " + exercise.getId() + ": " + exerciseInfo.getTranslations().size());

                                                boolean foundPortuguese = false;
                                                for (Translation translation : exerciseInfo.getTranslations()) {
                                                    Log.d(TAG, "Tradução ID " + exercise.getId() + " - Language: " + translation.getLanguage() + " - Nome: " + translation.getName());
                                                    if (translation.getLanguage() == 7) { // ID 2 para Português
                                                        exerciseName = translation.getName() != null ? translation.getName() : exerciseName;
                                                        if (translation.getDescription() != null) {
                                                            exerciseDescription = translation.getDescription().replaceAll("<[^>]*>", "");
                                                        }
                                                        Log.d(TAG, "Tradução PT-BR encontrada para ID " + exercise.getId() + ": " + exerciseName);
                                                        foundPortuguese = true;
                                                        break;
                                                    }
                                                }
                                                // Se não encontrou em português, pegar a primeira tradução disponível como fallback
                                                if (!foundPortuguese) {
                                                    Translation firstTranslation = exerciseInfo.getTranslations().get(0);
                                                    exerciseName = firstTranslation.getName() != null ? firstTranslation.getName() : exerciseName;

                                                    if (firstTranslation.getDescription() != null) {
                                                        exerciseDescription = firstTranslation.getDescription().replaceAll("<[^>]*>", "");
                                                    }

                                                    Log.d(TAG, "Usando fallback de tradução para ID " + exercise.getId() + ": " + exerciseName + " (Language: " + firstTranslation.getLanguage() + ")");
                                                }
                                            } else {
                                                Log.w(TAG, "Nenhuma tradução encontrada para o exercício ID: " + exercise.getId());
                                            }

                                            tempExerciseMap.put(exercise.getId(), new Exercicio(
                                                    exercise.getId(),
                                                    exerciseName,
                                                    exerciseDescription,
                                                    "", // Músculos não vêm diretamente aqui
                                                    "", // YouTube ID não vem da API
                                                    -1 // planilhaId não aplicável
                                            ));
                                            Log.d(TAG, "Detalhes do exercício ID " + exercise.getId() + " processados. Nome: " + exerciseName);
                                        } else {
                                            Log.e(TAG, "Erro na resposta do detalhe do exercício ID " + exercise.getId() + ": " + detailResponse.code());
                                        }
                                        // Incrementar o contador e verificar se todos os detalhes foram carregados
                                        if (loadedDetailsCount.incrementAndGet() == maxExercises) {
                                            Log.d(TAG, "Todos os detalhes carregados. Total: " + loadedDetailsCount.get());
                                            // Adicionar todos os exercícios carregados à lista principal e notificar o adapter
                                            // Ordenar a lista temporária antes de adicionar para garantir consistência
                                            List<Exercicio> finalExerciseList = new ArrayList<>(tempExerciseMap.values());
                                            Collections.sort(finalExerciseList, (e1, e2) -> Integer.compare(e1.getId(), e2.getId()));
                                            exerciciosParaAdapter.addAll(finalExerciseList);
                                            exercicioAdapter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            Log.d(TAG, "Adapter atualizado e RecyclerView visível.");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ExerciseInfo> detailCall, Throwable t) {
                                        Log.e(TAG, "Falha ao carregar detalhes do exercício ID " + exercise.getId() + ": " + t.getMessage());
                                        // Incrementar o contador mesmo em caso de falha
                                        if (loadedDetailsCount.incrementAndGet() == maxExercises) {
                                            // Se houver falha, ainda assim atualizar o adapter para mostrar o que foi carregado
                                            List<Exercicio> finalExerciseList = new ArrayList<>(tempExerciseMap.values());
                                            Collections.sort(finalExerciseList, (e1, e2) -> Integer.compare(e1.getId(), e2.getId()));
                                            exerciciosParaAdapter.addAll(finalExerciseList);
                                            exercicioAdapter.notifyDataSetChanged();
                                            progressBar.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            Log.d(TAG, "Adapter atualizado após falha de detalhes.");
                                        }
                                        Toast.makeText(ExerciseListActivity.this, "Erro ao carregar detalhes do exercício: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            Toast.makeText(ExerciseListActivity.this, "Nenhum exercício encontrado para este grupo muscular", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Nenhum exercício filtrado para muscleId: " + muscleId);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Toast.makeText(ExerciseListActivity.this, "Nenhum exercício encontrado", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Nenhum exercício recebido da API.");
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(ExerciseListActivity.this, "Erro ao carregar exercícios", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Erro na resposta da lista de exercícios: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ExerciseResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(ExerciseListActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Falha na conexão ao carregar exercícios: " + t.getMessage());
            }
        });
    }
}