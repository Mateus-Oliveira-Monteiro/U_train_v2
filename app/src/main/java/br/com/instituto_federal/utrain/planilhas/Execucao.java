package br.com.instituto_federal.utrain.planilhas;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.instituto_federal.utrain.Home;
import br.com.instituto_federal.utrain.Login;
import br.com.instituto_federal.utrain.MuscleGroupSelectionActivity;
import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.favoritos.Favoritos;

public class Execucao extends AppCompatActivity {

    private WebView webViewVideo;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execucao);

        TextView tvNomeExercicio = findViewById(R.id.tvNomeExercicio);
        TextView tvDescricaoExercicio = findViewById(R.id.tvDescricaoExercicio);
        TextView tvMusculosRecrutados = findViewById(R.id.tvMusculosRecrutados);
        webViewVideo = findViewById(R.id.webViewVideo);
        Button btnTTS = findViewById(R.id.btnTTS);

        Intent intent = getIntent();
        String nomeExercicio = intent.getStringExtra("nomeExercicio");
        String descricaoExercicio = intent.getStringExtra("descricaoExercicio");
        String musculosRecrutados = intent.getStringExtra("musculosRecrutados");
        String youtubeUrl = intent.getStringExtra("youtubeVideoId");

        tvNomeExercicio.setText(nomeExercicio);
        tvDescricaoExercicio.setText(descricaoExercicio);
        tvMusculosRecrutados.setText(musculosRecrutados);

        String videoId = extractYoutubeId(youtubeUrl);

        if (videoId != null && !videoId.isEmpty()) {
            setupWebView(videoId);
        } else {
            Log.e("Execucao", "Não foi possível extrair o ID do vídeo da URL: " + youtubeUrl);
            Toast.makeText(this, "URL do vídeo inválida.", Toast.LENGTH_SHORT).show();
        }

        setupTextToSpeech();
        btnTTS.setOnClickListener(v -> speakDescription(descricaoExercicio));
        setupBottomNavigation();
    }

    private void setupWebView(String videoId) {
        WebSettings webSettings = webViewVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webViewVideo.setWebViewClient(new WebViewClient());

        String videoHtml = "<html><body style='margin:0;padding:0;background-color:black;'><iframe width='100%' height='100%' src='https://www.youtube.com/embed/"
                + videoId
                + "?autoplay=0&modestbranding=1&playsinline=1' frameborder='0' allowfullscreen></iframe></body></html>";

        webViewVideo.loadData(videoHtml, "text/html", "utf-8");
    }

    private String extractYoutubeId(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }
        String videoId = null;
        String regex = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2Fvideos%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group();
        }
        return videoId;
    }

    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("pt", "BR"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Idioma não suportado.");
                }
            } else {
                Log.e("TTS", "Falha na inicialização do TTS.");
            }
        });
    }

    private void speakDescription(String description) {
        if (description != null && !description.isEmpty()) {
            textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // ✅ CORREÇÃO: Lógica de navegação completa e funcional
    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, Home.class));
                finish(); // Fecha a tela atual
                return true;
            } else if (itemId == R.id.nav_favoritos) {
                startActivity(new Intent(this, Favoritos.class));
                finish(); // Fecha a tela atual
                return true;
            } else if (itemId == R.id.nav_api_exercises) {
                startActivity(new Intent(this, MuscleGroupSelectionActivity.class));
                finish(); // Fecha a tela atual
                return true;
            } else if (itemId == R.id.nav_logout) {
                Toast.makeText(this, "Deslogando...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Fecha a tela atual
                return true;
            }
            return false;
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
