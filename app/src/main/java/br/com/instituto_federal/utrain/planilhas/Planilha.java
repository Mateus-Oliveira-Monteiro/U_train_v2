package br.com.instituto_federal.utrain.planilhas;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import br.com.instituto_federal.utrain.Home;
import br.com.instituto_federal.utrain.Login;
import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.favoritos.Favoritos;

public class Planilha extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private ExercicioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilha);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);

        // Configurar navegação
        setupBottomNavigation();

        // Carregar exercícios da planilha
        int planilhaId = getIntent().getIntExtra("planilhaId", -1);
        if (planilhaId != -1) {
            carregarExercicios(planilhaId);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
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
            }
            return false;
        });
    }

    private void carregarExercicios(int planilhaId) {
        List<Exercicio> exercicios = db.listarExerciciosPorPlanilha(planilhaId);

        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        adapter = new ExercicioAdapter(this, exercicios);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar dados quando a activity volta ao foco
        int planilhaId = getIntent().getIntExtra("planilhaId", -1);
        if (planilhaId != -1) {
            carregarExercicios(planilhaId);
        }
    }
}
