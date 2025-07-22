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
import android.util.Log;

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

        String videoId = extractYoutubeId(youtubeVideoId);


        if (nomeExercicio != null) {
            tvNomeExercicio.setText(nomeExercicio);
        }
        if (descricaoExercicio != null) {
            tvDescricaoExercicio.setText(descricaoExercicio);
        }
        if (musculosRecrutados != null) {
            tvMusculosRecrutados.setText(musculosRecrutados);
        }

        if (videoId != null) {
            Log.d("Execucao", "Video ID: " + videoId);

            String html = "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" " +
                    "src=\"https://www.youtube.com/embed/" + videoId +
                    "?autoplay=0&modestbranding=1&playsinline=1\" " +
                    "frameborder=\"0\" allowfullscreen></iframe></body></html>";

            Log.d("Execucao", "HTML gerado: " + html);

            webViewVideo.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null);
        } else {
            Log.e("Execucao", "videoId está nulo. URL de entrada: " + youtubeVideoId);
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

    private String extractYoutubeId(String url) {
        if (url == null || url.trim().isEmpty()) return null;

        try {
            if (url.contains("youtu.be/")) {
                // Ex: https://youtu.be/PwXUHMKamP8?si=xxxxx
                String path = url.substring(url.indexOf("youtu.be/") + 9);
                int end = path.indexOf('?');
                return end != -1 ? path.substring(0, end) : path;
            } else if (url.contains("youtube.com") && url.contains("v=")) {
                // Ex: https://www.youtube.com/watch?v=PwXUHMKamP8&ab_channel=xyz
                String query = url.substring(url.indexOf("v=") + 2);
                int end = query.indexOf('&');
                return end != -1 ? query.substring(0, end) : query;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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


