package br.com.instituto_federal.utrain.alunos;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.instituto_federal.utrain.R;

public class FormularioAlunoActivity extends AppCompatActivity {

    private TextInputEditText editTextNome;
    private TextInputEditText editTextDataNascimento;
    private TextInputEditText editTextPeso;
    private TextInputEditText editTextEmail;
    private Button buttonSalvar;
    private Button buttonCancelar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Aluno alunoParaEditar;
    private boolean isEdicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_aluno);

        inicializarViews();
        inicializarFirebase();
        verificarAutenticacao();
        configurarEventos();
        verificarModoEdicao();
    }

    private void inicializarViews() {
        editTextNome = findViewById(R.id.editTextNome);
        editTextDataNascimento = findViewById(R.id.editTextDataNascimento);
        editTextPeso = findViewById(R.id.editTextPeso);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonCancelar = findViewById(R.id.buttonCancelar);
    }

    private void inicializarFirebase() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void verificarAutenticacao() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não autenticado. Faça login primeiro.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void configurarEventos() {
        buttonSalvar.setOnClickListener(v -> salvarAluno());
        buttonCancelar.setOnClickListener(v -> finish());
    }

    private void verificarModoEdicao() {
        if (getIntent().hasExtra("aluno_id")) {
            isEdicao = true;
            String alunoId = getIntent().getStringExtra("aluno_id");
            String nome = getIntent().getStringExtra("aluno_nome");
            String dataNascimento = getIntent().getStringExtra("aluno_data_nascimento");
            double peso = getIntent().getDoubleExtra("aluno_peso", 0.0);
            String email = getIntent().getStringExtra("aluno_email");

            alunoParaEditar = new Aluno(nome, dataNascimento, peso, email);
            alunoParaEditar.setId(alunoId);

            preencherCampos();
            setTitle(getString(R.string.editar_aluno));
        } else {
            setTitle(getString(R.string.cadastrar_aluno));
        }
    }

    private void preencherCampos() {
        if (alunoParaEditar != null) {
            editTextNome.setText(alunoParaEditar.getNome());
            editTextDataNascimento.setText(alunoParaEditar.getDataDeNascimento());
            editTextPeso.setText(String.valueOf(alunoParaEditar.getPeso()));
            editTextEmail.setText(alunoParaEditar.getEmail());
        }
    }

    private void salvarAluno() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validarCampos()) {
            return;
        }

        String nome = editTextNome.getText() != null ? editTextNome.getText().toString().trim() : "";
        String dataNascimento = editTextDataNascimento.getText() != null ? editTextDataNascimento.getText().toString().trim() : "";
        String pesoStr = editTextPeso.getText() != null ? editTextPeso.getText().toString().trim() : "0";
        String email = editTextEmail.getText() != null ? editTextEmail.getText().toString().trim() : "";

        double peso;
        try {
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            editTextPeso.setError("Peso deve ser um número válido");
            return;
        }

        Aluno aluno = new Aluno(nome, dataNascimento, peso, email);

        if (isEdicao) {
            aluno.setId(alunoParaEditar.getId());
            atualizarAluno(aluno);
        } else {
            adicionarAluno(aluno);
        }
    }

    private boolean validarCampos() {
        boolean valido = true;

        if (TextUtils.isEmpty(editTextNome.getText())) {
            editTextNome.setError("Nome é obrigatório");
            valido = false;
        }

        if (TextUtils.isEmpty(editTextDataNascimento.getText())) {
            editTextDataNascimento.setError("Data de nascimento é obrigatória");
            valido = false;
        }

        if (TextUtils.isEmpty(editTextPeso.getText())) {
            editTextPeso.setError("Peso é obrigatório");
            valido = false;
        } else {
            try {
                Double.parseDouble(editTextPeso.getText().toString());
            } catch (NumberFormatException e) {
                editTextPeso.setError("Peso deve ser um número válido");
                valido = false;
            }
        }

        if (TextUtils.isEmpty(editTextEmail.getText())) {
            editTextEmail.setError("E-mail é obrigatório");
            valido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText()).matches()) {
            editTextEmail.setError("E-mail inválido");
            valido = false;
        }

        return valido;
    }

    private void adicionarAluno(Aluno aluno) {
        db.collection("alunos")
                .add(aluno)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, getString(R.string.aluno_cadastrado_sucesso), Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    String errorMessage = e.getMessage() != null ? e.getMessage() : "Erro desconhecido";
                    Toast.makeText(this, getString(R.string.erro_cadastrar_aluno, errorMessage), Toast.LENGTH_LONG).show();
                });
    }

    private void atualizarAluno(Aluno aluno) {
        db.collection("alunos")
                .document(aluno.getId())
                .set(aluno)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, getString(R.string.aluno_atualizado_sucesso), Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    String errorMessage = e.getMessage() != null ? e.getMessage() : "Erro desconhecido";
                    Toast.makeText(this, getString(R.string.erro_atualizar_aluno, errorMessage), Toast.LENGTH_LONG).show();
                });
    }
}
