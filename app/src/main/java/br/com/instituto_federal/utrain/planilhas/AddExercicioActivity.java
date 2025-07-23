package br.com.instituto_federal.utrain.planilhas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.data.AppDatabase;
import br.com.instituto_federal.utrain.data.model.Exercicio;

public class AddExercicioActivity extends Activity {

    private EditText edtNome, edtMusculos, edtDescricao, edtYoutubeId;
    private Spinner spinnerPlanilha;
    private Button btnSalvar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercicio);

        edtNome = findViewById(R.id.edtNome);
        edtMusculos = findViewById(R.id.edtMusculos);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtYoutubeId = findViewById(R.id.edtYoutubeId);
        spinnerPlanilha = findViewById(R.id.spinnerPlanilha);
        btnSalvar = findViewById(R.id.btnSalvar);

        db = AppDatabase.getDatabase(this);

        setupSpinner();

        btnSalvar.setOnClickListener(view -> {
            String nome = edtNome.getText().toString().trim();
            String musculos = edtMusculos.getText().toString().trim();
            String descricao = edtDescricao.getText().toString().trim();
            String youtubeId = edtYoutubeId.getText().toString().trim();

            if (nome.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show();
                return;
            }

            int planilhaId = spinnerPlanilha.getSelectedItemPosition() + 1;

            // ✅ CORREÇÃO: Usando o construtor vazio e os métodos setters
            Exercicio novoExercicio = new Exercicio();
            novoExercicio.setNome(nome);
            novoExercicio.setDescricao(descricao);
            novoExercicio.setMusculos(musculos);
            novoExercicio.setYoutubeId(youtubeId);
            novoExercicio.setPlanilhaId(planilhaId);

            // Inserção na thread de banco
            AppDatabase.databaseWriteExecutor.execute(() -> {
                db.exercicioDao().insert(novoExercicio);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Exercício salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                    finish();
                });
            });
        });
    }

    private void setupSpinner() {
        String[] planilhas = {"Planilha A", "Planilha B", "Planilha C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, planilhas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlanilha.setAdapter(adapter);
    }
}
