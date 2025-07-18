package br.com.instituto_federal.utrain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.instituto_federal.utrain.favoritos.Favoritos;
import br.com.instituto_federal.utrain.planilhas.AddExercicioActivity;
import br.com.instituto_federal.utrain.planilhas.Planilha;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Botões de planilhas
        Button p1 = findViewById(R.id.planilha1);
        Button p2 = findViewById(R.id.planilha2);
        Button p3 = findViewById(R.id.planilha3);
        Button add = findViewById(R.id.adicionarMaisButton);

        p1.setOnClickListener(v -> abrirPlanilha(1));
        p2.setOnClickListener(v -> abrirPlanilha(2));
        p3.setOnClickListener(v -> abrirPlanilha(3));

        add.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExercicioActivity.class);
            startActivity(intent);
        });

        // Navegação inferior
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.nav_home); // seleciona o botão atual

        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_favoritos) {
                startActivity(new Intent(this, Favoritos.class));
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

    private void abrirPlanilha(int planilhaId) {
        Intent intent = new Intent(this, Planilha.class);
        intent.putExtra("planilhaId", planilhaId);
        startActivity(intent);
    }
}
