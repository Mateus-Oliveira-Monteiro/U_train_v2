package br.com.instituto_federal.utrain;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.instituto_federal.utrain.alunos.AlunosActivity;
import br.com.instituto_federal.utrain.favoritos.Favoritos;
import br.com.instituto_federal.utrain.planilhas.AddExercicioActivity;
import br.com.instituto_federal.utrain.planilhas.Planilha;

public class Home extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializar notificações
        setupNotifications();

        // Solicitar permissão de notificação
        requestNotificationPermission();

        // Botão para testar notificações
        Button testNotificationButton = findViewById(R.id.testNotificationButton);
        testNotificationButton.setOnClickListener(v -> {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.showTestNotification(); // Mostra imediatamente
            Toast.makeText(this, "Notificação de teste enviada!", Toast.LENGTH_SHORT).show();
        });

        // Botões de planilhas
        Button p1 = findViewById(R.id.planilha1);
        Button p2 = findViewById(R.id.planilha2);
        Button p3 = findViewById(R.id.planilha3);
        FloatingActionButton add = findViewById(R.id.adicionarMaisButton);

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
            } else if (item.getItemId() == R.id.nav_api_exercises) {
                startActivity(new Intent(this, MuscleGroupSelectionActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_logout) {
                Toast.makeText(this, "Deslogando...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_alunos) {
                startActivity(new Intent(this, AlunosActivity.class));
                return true;
            }
            return true;
        });
    }

    private void setupNotifications() {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.scheduleWorkoutReminder();
        notificationHelper.scheduleWaterReminder();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão de notificação concedida!", Toast.LENGTH_SHORT).show();
                // Configurar notificações após permissão concedida
                setupNotifications();
            } else {
                Toast.makeText(this, "Permissão de notificação negada!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void abrirPlanilha(int planilhaId) {
        Intent intent = new Intent(this, Planilha.class);
        intent.putExtra("planilhaId", planilhaId);
        startActivity(intent);
    }
}

