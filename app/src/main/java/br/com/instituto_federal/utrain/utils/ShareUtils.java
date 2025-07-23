package br.com.instituto_federal.utrain.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// ✅ Import corrigido para usar a nova entidade Exercicio do Room
import br.com.instituto_federal.utrain.data.model.Exercicio;

public class ShareUtils {
    /**
     * Compartilha os detalhes de um exercício via WhatsApp.
     * @param context O contexto da aplicação.
     * @param exercicio O objeto Exercicio (da entidade Room) a ser compartilhado.
     */
    public static void compartilharExercicio(Context context, Exercicio exercicio) {
        // A lógica interna do método não precisa de alterações,
        // pois os getters (getNome(), getDescricao(), etc.) continuam os mesmos.
        String texto = "Confira este exercício no uTrain!\n\n" +
                "💪 *Exercício:* " + exercicio.getNome() + "\n" +
                "📝 *Descrição:* " + exercicio.getDescricao() + "\n" +
                "🏋️ *Músculos:* " + exercicio.getMusculos() + "\n\n" +
                "▶️ *Veja a execução:* " + "\nhttps://www.youtube.com/watch?v=" + exercicio.getYoutubeId();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
        sendIntent.setType("text/plain");

        // Tenta abrir diretamente no WhatsApp, se instalado
        try {
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            // Se o WhatsApp não estiver instalado, abre o seletor padrão de compartilhamento
            try {
                context.startActivity(Intent.createChooser(sendIntent, "Compartilhar exercício via..."));
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(context, "Nenhum aplicativo de compartilhamento encontrado.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
