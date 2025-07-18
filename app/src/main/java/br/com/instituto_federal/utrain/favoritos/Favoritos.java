package br.com.instituto_federal.utrain.favoritos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.instituto_federal.utrain.Home;
import br.com.instituto_federal.utrain.Login;
import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.planilhas.DatabaseHelper;
import br.com.instituto_federal.utrain.planilhas.Exercicio;

public class Favoritos extends AppCompatActivity {
    private List<Exercicio> todosExercicios;
    private List<Exercicio> favoritosList;
    private FavoritosAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavoritos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Carrega todos os exercícios possíveis
        carregarTodosExercicios();

        // Filtra apenas os favoritos
        favoritosList = filtrarFavoritos();

        // Seta o adapter
        adapter = new FavoritosAdapter(this, favoritosList);
        recyclerView.setAdapter(adapter);

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
            }
            return true;
        });
    }

    // trocando o carregamento para dados do slq
    private void carregarTodosExercicios() {
        DatabaseHelper db = new DatabaseHelper(this);
        todosExercicios = db.listarExercicios(); // pega todos do banco
    }


    private List<Exercicio> filtrarFavoritos() {
        SharedPreferences prefs = getSharedPreferences("FAVORITOS", MODE_PRIVATE);
        Set<String> favoritosIds = prefs.getStringSet("exercicios", new HashSet<>());

        List<Exercicio> favoritos = new ArrayList<>();
        for (Exercicio ex : todosExercicios) {
            if (favoritosIds.contains(String.valueOf(ex.getId()))) {
                favoritos.add(ex);
            }
        }
        return favoritos;
    }

    public void atualizarLista() {
        favoritosList.clear();
        favoritosList.addAll(filtrarFavoritos());
        adapter.notifyDataSetChanged();
    }
}
