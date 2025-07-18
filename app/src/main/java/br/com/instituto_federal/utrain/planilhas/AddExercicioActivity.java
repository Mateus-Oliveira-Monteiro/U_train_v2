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

public class AddExercicioActivity extends Activity {

    private EditText edtNome, edtMusculos, edtDescricao, edtYoutubeId;
    private Spinner spinnerPlanilha;
    private Button btnSalvar;
    private DatabaseHelper db;

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

        db = new DatabaseHelper(this);

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

            int planilhaId = spinnerPlanilha.getSelectedItemPosition() + 1; // +1 porque as planilhas começam em 1

            Exercicio exercicio = new Exercicio(null, nome, descricao, musculos, youtubeId, planilhaId);
            long resultado = db.adicionarExercicio(exercicio);

            if (resultado != -1) {
                Toast.makeText(this, "Exercício salvo com sucesso!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar exercício", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        String[] planilhas = {"Planilha A", "Planilha B", "Planilha C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, planilhas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlanilha.setAdapter(adapter);
    }
}