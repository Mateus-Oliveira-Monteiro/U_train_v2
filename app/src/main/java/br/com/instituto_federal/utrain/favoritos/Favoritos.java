package br.com.instituto_federal.utrain.favoritos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.instituto_federal.utrain.Home;
import br.com.instituto_federal.utrain.Login;
import br.com.instituto_federal.utrain.MuscleGroupSelectionActivity;
import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.data.AppDatabase;
import br.com.instituto_federal.utrain.data.model.Exercicio;

public class Favoritos extends AppCompatActivity {

    private FavoritosAdapter adapter;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private TextView emptyView; // TextView para mostrar quando a lista está vazia

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        recyclerView = findViewById(R.id.recyclerViewFavoritos);
        emptyView = findViewById(R.id.tv_empty_favoritos); // Adicione este ID ao seu XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa o adapter com uma lista vazia
        adapter = new FavoritosAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Inicializa o banco de dados Room
        db = AppDatabase.getDatabase(this);

        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Carrega os favoritos sempre que a tela se torna visível,
        // garantindo que a lista esteja sempre atualizada.
        carregarFavoritos();
    }

    private void carregarFavoritos() {
        SharedPreferences prefs = getSharedPreferences("FAVORITOS", MODE_PRIVATE);
        Set<String> favoritosIdsStr = prefs.getStringSet("exercicios", new HashSet<>());

        if (favoritosIdsStr.isEmpty()) {
            // Se não houver favoritos, limpa a lista e mostra a mensagem de lista vazia
            adapter.setExercicios(new ArrayList<>());
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            return;
        }

        // Converte o Set de Strings para uma Lista de Integers
        List<Integer> favoritosIds = favoritosIdsStr.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // Usa o novo método do DAO para buscar apenas os exercícios favoritados
        // O LiveData cuidará de atualizar a UI automaticamente
        LiveData<List<Exercicio>> favoritosLiveData = db.exercicioDao().getExerciciosByIds(favoritosIds);

        favoritosLiveData.observe(this, exerciciosFavoritados -> {
            if (exerciciosFavoritados == null || exerciciosFavoritados.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                adapter.setExercicios(exerciciosFavoritados);
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.nav_favoritos);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, Home.class));
                finish();
                return true;
            } else if (id == R.id.nav_api_exercises) {
                startActivity(new Intent(this, MuscleGroupSelectionActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_logout) {
                Toast.makeText(this, "Deslogando...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
