package br.com.instituto_federal.utrain.alunos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.instituto_federal.utrain.R;

public class AlunosActivity extends AppCompatActivity implements AlunosAdapter.OnAlunoActionListener {

    private RecyclerView recyclerViewAlunos;
    private FloatingActionButton fabAdicionarAluno;
    private AlunosAdapter alunosAdapter;
    private List<Aluno> listaAlunos;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alunos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarViews();
        inicializarFirebase();
        verificarAutenticacao();
        configurarRecyclerView();
        configurarEventos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            carregarAlunos(); // Recarrega a lista quando volta para a tela
        }
    }

    private void inicializarViews() {
        recyclerViewAlunos = findViewById(R.id.recyclerViewAlunos);
        fabAdicionarAluno = findViewById(R.id.fabAdicionarAluno);
    }

    private void inicializarFirebase() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void verificarAutenticacao() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado. Faça login primeiro.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Se chegou até aqui, usuário está autenticado
        carregarAlunos();
    }

    private void configurarRecyclerView() {
        listaAlunos = new ArrayList<>();
        alunosAdapter = new AlunosAdapter(this, listaAlunos, this);
        recyclerViewAlunos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAlunos.setAdapter(alunosAdapter);
    }

    private void configurarEventos() {
        fabAdicionarAluno.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(this, FormularioAlunoActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarAlunos() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("alunos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaAlunos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Aluno aluno = document.toObject(Aluno.class);
                            aluno.setId(document.getId());
                            listaAlunos.add(aluno);
                        }
                        alunosAdapter.updateAlunos(listaAlunos);
                    } else {
                        String errorMessage = task.getException() != null ?
                            task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(this, getString(R.string.erro_carregar_alunos, errorMessage),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onEditarAluno(Aluno aluno) {
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, FormularioAlunoActivity.class);
            intent.putExtra("aluno_id", aluno.getId());
            intent.putExtra("aluno_nome", aluno.getNome());
            intent.putExtra("aluno_data_nascimento", aluno.getDataDeNascimento());
            intent.putExtra("aluno_peso", aluno.getPeso());
            intent.putExtra("aluno_email", aluno.getEmail());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onExcluirAluno(Aluno aluno) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmar_exclusao))
                .setMessage(getString(R.string.tem_certeza_excluir, aluno.getNome()))
                .setPositiveButton(getString(R.string.sim), (dialog, which) -> excluirAluno(aluno))
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    private void excluirAluno(Aluno aluno) {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("alunos")
                .document(aluno.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, getString(R.string.aluno_excluido_sucesso), Toast.LENGTH_SHORT).show();
                    carregarAlunos(); // Recarrega a lista
                })
                .addOnFailureListener(e -> {
                    String errorMessage = e.getMessage() != null ? e.getMessage() : "Erro desconhecido";
                    Toast.makeText(this, getString(R.string.erro_excluir_aluno, errorMessage),
                            Toast.LENGTH_LONG).show();
                });
    }
}