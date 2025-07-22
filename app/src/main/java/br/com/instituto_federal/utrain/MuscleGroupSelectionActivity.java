package br.com.instituto_federal.utrain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.instituto_federal.utrain.favoritos.Favoritos;

public class MuscleGroupSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_group_selection);

        // Configurar botões para cada grupo muscular
        Button btnOmbros = findViewById(R.id.btn_ombros);
        Button btnBiceps = findViewById(R.id.btn_biceps);
        Button btnTriceps = findViewById(R.id.btn_triceps);
        Button btnPeito = findViewById(R.id.btn_peito);
        Button btnAbdomen = findViewById(R.id.btn_abdomen);
        Button btnQuadriceps = findViewById(R.id.btn_quadriceps);
        Button btnIsquiotibiais = findViewById(R.id.btn_isquiotibiais);
        Button btnGluteos = findViewById(R.id.btn_gluteos);
        Button btnPanturrilhas = findViewById(R.id.btn_panturrilhas);
        Button btnCostas = findViewById(R.id.btn_costas);

        // Configurar listeners para cada botão
        btnOmbros.setOnClickListener(v -> openExerciseList(2, "Ombros"));
        btnBiceps.setOnClickListener(v -> openExerciseList(1, "Bíceps"));
        btnTriceps.setOnClickListener(v -> openExerciseList(5, "Tríceps"));
        btnPeito.setOnClickListener(v -> openExerciseList(4, "Peito"));
        btnAbdomen.setOnClickListener(v -> openExerciseList(6, "Abdômen"));
        btnQuadriceps.setOnClickListener(v -> openExerciseList(10, "Quadríceps"));
        btnIsquiotibiais.setOnClickListener(v -> openExerciseList(11, "Isquiotibiais"));
        btnGluteos.setOnClickListener(v -> openExerciseList(8, "Glúteos"));
        btnPanturrilhas.setOnClickListener(v -> openExerciseList(7, "Panturrilhas"));
        btnCostas.setOnClickListener(v -> openExerciseList(12, "Costas"));

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

    private void openExerciseList(int muscleId, String muscleName) {
        Intent intent = new Intent(this, ExerciseListActivity.class);
        intent.putExtra("muscleId", muscleId);
        intent.putExtra("muscleName", muscleName);
        startActivity(intent);
    }

}

