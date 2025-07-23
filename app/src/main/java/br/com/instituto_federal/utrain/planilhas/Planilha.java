package br.com.instituto_federal.utrain.planilhas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import br.com.instituto_federal.utrain.Home;
import br.com.instituto_federal.utrain.Login;
import br.com.instituto_federal.utrain.MuscleGroupSelectionActivity;
import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.data.AppDatabase;
import br.com.instituto_federal.utrain.favoritos.Favoritos;

public class Planilha extends AppCompatActivity {

    private ExercicioAdapter adapter;
    private AppDatabase db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilha);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ 1. Inicializa o adapter com uma lista vazia.
        // Isso é crucial para evitar que o RecyclerView comece com um adapter nulo.
        adapter = new ExercicioAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // ✅ 2. Obtém a instância do banco de dados Room.
        db = AppDatabase.getDatabase(this);

        setupBottomNavigation();

        // ✅ 3. Carrega os exercícios da planilha passada pelo Intent.
        int planilhaId = getIntent().getIntExtra("planilhaId", -1);
        if (planilhaId != -1) {
            carregarExercicios(planilhaId);
        } else {
            Toast.makeText(this, "Erro ao carregar planilha.", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a activity se o ID for inválido.
        }
    }

    private void carregarExercicios(int planilhaId) {
        // ✅ 4. Usa LiveData para observar mudanças no banco de dados.
        // O Room executa a busca em uma thread separada automaticamente.
        // O método 'observe' entrega o resultado na thread principal, de forma segura.
        db.exercicioDao().getExerciciosPorPlanilha(planilhaId).observe(this, exercicios -> {
            // Este código será executado sempre que os dados da planilha mudarem.
            adapter.setExercicios(exercicios);
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        // Lógica da sua BottomNavigationView (sem alterações)...
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, Home.class));
                return true;
            } else if (item.getItemId() == R.id.nav_logout) {
                Toast.makeText(this, "Deslogando...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_favoritos) {
                startActivity(new Intent(this, Favoritos.class));
                return true;
            } else if (item.getItemId() == R.id.nav_api_exercises) {
                startActivity(new Intent(this, MuscleGroupSelectionActivity.class));
                return true;
            }
            return false;
        });
    }
}
