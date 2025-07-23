package br.com.instituto_federal.utrain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;



public class Login extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });

        auth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.emailInput);
        EditText passwordEditText = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(Login.this, "Logado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String erroTraduzido = traduzirErroFirebase(task.getException());
                            Toast.makeText(Login.this, erroTraduzido, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private String traduzirErroFirebase(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
            switch (errorCode) {
                case "ERROR_USER_NOT_FOUND":
                    return "Usuário não encontrado.";
                case "ERROR_WRONG_PASSWORD":
                    return "Senha incorreta.";
                case "ERROR_INVALID_EMAIL":
                    return "E-mail inválido.";
                case "ERROR_USER_DISABLED":
                    return "Conta desativada.";
                case "ERROR_TOO_MANY_REQUESTS":
                    return "Muitas tentativas. Tente novamente mais tarde.";
                case "ERROR_INVALID_CREDENTIAL":
                    return "Credenciais inválidas. Verifique seu e-mail e senha.";
                default:
                    return "Erro: " + exception.getMessage();
            }
        } else if (exception != null && exception.getMessage() != null) {
            return "Erro: " + exception.getMessage();
        } else {
            return "Ocorreu um erro desconhecido.";
        }
    }

}
