package br.com.instituto_federal.utrain.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import br.com.instituto_federal.utrain.planilhas.Exercicio;

public class ShareUtils {
    public static void compartilharExercicio(Context context, Exercicio exercicio) {
        String texto = "Exercício: " + exercicio.getNome() +
                "\nDescrição: " + exercicio.getDescricao() +
                "\nMúsculos: " + exercicio.getMusculos() +
                "\nExecução: https://www.youtube.com/embed/" + exercicio.getYoutubeId() + "?autoplay=1&fs=0&iv_load_policy=3&showinfo=0&rel=0&cc_load_policy=0&start=0&end=0&playsinline=1";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");

        try {
            context.startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "WhatsApp não instalado.", Toast.LENGTH_SHORT).show();
        }
    }
}