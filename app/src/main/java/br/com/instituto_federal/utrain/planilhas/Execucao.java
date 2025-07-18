package br.com.instituto_federal.utrain.planilhas;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import br.com.instituto_federal.utrain.favoritos.Favoritos;
import br.com.instituto_federal.utrain.Home;
import br.com.instituto_federal.utrain.Login;
import br.com.instituto_federal.utrain.R;

public class Execucao extends AppCompatActivity {

    private TextView tvNomeExercicio;
    private TextView tvDescricaoExercicio;
    private TextView tvMusculosRecrutados;
    private WebView webViewVideo;
    private Button btnTTS;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execucao);


        tvNomeExercicio = findViewById(R.id.tvNomeExercicio);
        tvDescricaoExercicio = findViewById(R.id.tvDescricaoExercicio);
        tvMusculosRecrutados = findViewById(R.id.tvMusculosRecrutados);
        webViewVideo = findViewById(R.id.webViewVideo);
        btnTTS = findViewById(R.id.btnTTS);

        //  WebView  do YouTube
        WebSettings webSettings = webViewVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true); // Habilita o DOM Storage para vídeos do YouTube
        webViewVideo.setWebViewClient(new WebViewClient());

        // get da Intent
        Intent intent = getIntent();
        String nomeExercicio = intent.getStringExtra("nomeExercicio");
        String descricaoExercicio = intent.getStringExtra("descricaoExercicio");
        String musculosRecrutados = intent.getStringExtra("musculosRecrutados");
        String youtubeVideoId = intent.getStringExtra("youtubeVideoId");


        if (nomeExercicio != null) {
            tvNomeExercicio.setText(nomeExercicio);
        }
        if (descricaoExercicio != null) {
            tvDescricaoExercicio.setText(descricaoExercicio);
        }
        if (musculosRecrutados != null) {
            tvMusculosRecrutados.setText(musculosRecrutados);
        }
        if (youtubeVideoId != null) {
            String html = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + youtubeVideoId + "?autoplay=1&fs=0&iv_load_policy=3&showinfo=0&rel=0&cc_load_policy=0&start=0&end=0&playsinline=1\" frameborder=\"0\" allowfullscreen></iframe>";
            webViewVideo.loadData(html, "text/html", "utf-8");
        }

        // TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("pt", "BR"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(Execucao.this, "Idioma não suportado para TTS", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Execucao.this, "Falha na inicialização do TTS", Toast.LENGTH_SHORT).show();
            }
        });


        btnTTS.setOnClickListener(v -> {
            String text = tvDescricaoExercicio.getText().toString();
            if (!text.isEmpty()) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });


        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(0); // Nenhum item selecionado por padrão

        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, Home.class));
                return true;
            } else if (item.getItemId() == R.id.nav_favoritos) {
                startActivity(new Intent(this, Favoritos.class));
                return true;
            } else if (item.getItemId() == R.id.nav_logout) {
                Toast.makeText(this, "Deslogando...", Toast.LENGTH_SHORT).show();
                Intent intentLogout = new Intent(this, Login.class);
                intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogout);
                return true;
            }
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}


