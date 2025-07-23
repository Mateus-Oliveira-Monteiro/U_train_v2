package br.com.instituto_federal.utrain.planilhas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.utils.ShareUtils;
// ✅ Import corrigido para usar a entidade Exercicio do pacote de dados
import br.com.instituto_federal.utrain.data.model.Exercicio;

public class ExercicioAdapter extends RecyclerView.Adapter<ExercicioAdapter.ExercicioViewHolder> {
    private List<Exercicio> exercicios;
    private Context context;

    public ExercicioAdapter(Context context, List<Exercicio> exercicios) {
        this.context = context;
        this.exercicios = exercicios;
    }

    /**
     * ✅ Método para atualizar a lista de exercícios dinamicamente.
     * Essencial para que o LiveData (na tela de Planilhas) e as chamadas de API
     * (na tela de Exercícios da API) possam atualizar a UI.
     */
    public void setExercicios(List<Exercicio> novosExercicios) {
        this.exercicios = novosExercicios;
        notifyDataSetChanged(); // Informa ao RecyclerView para redesenhar a lista
    }

    @NonNull
    @Override
    public ExercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_planilha, parent, false);
        return new ExercicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercicioViewHolder holder, int position) {
        Exercicio exercicio = exercicios.get(position);
        holder.nome.setText(exercicio.getNome());
        holder.descricao.setText(exercicio.getDescricao());
        holder.musculos.setText(exercicio.getMusculos());

        String idStr = String.valueOf(exercicio.getId());

        // Lógica para favoritar (sem alterações)
        SharedPreferences prefs = context.getSharedPreferences("FAVORITOS", Context.MODE_PRIVATE);
        Set<String> favoritosAtual = prefs.getStringSet("exercicios", new HashSet<>());

        if (favoritosAtual.contains(idStr)) {
            holder.btnFavoritar.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.btnFavoritar.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.btnFavoritar.setOnClickListener(v -> {
            SharedPreferences preferences = context.getSharedPreferences("FAVORITOS", Context.MODE_PRIVATE);
            Set<String> favoritos = new HashSet<>(preferences.getStringSet("exercicios", new HashSet<>()));
            SharedPreferences.Editor editor = preferences.edit();

            if (favoritos.contains(idStr)) {
                favoritos.remove(idStr);
                holder.btnFavoritar.setImageResource(R.drawable.ic_favorite_border);
            } else {
                favoritos.add(idStr);
                holder.btnFavoritar.setImageResource(R.drawable.ic_favorite);
            }

            editor.putStringSet("exercicios", favoritos);
            editor.apply();
        });

        // Lógica do botão de execução (sem alterações)
        holder.btnExecucao.setOnClickListener(v -> {
            Intent intent = new Intent(context, Execucao.class);
            intent.putExtra("nomeExercicio", exercicio.getNome());
            intent.putExtra("descricaoExercicio", exercicio.getDescricao());
            intent.putExtra("musculosRecrutados", exercicio.getMusculos());
            intent.putExtra("youtubeVideoId", exercicio.getYoutubeId());
            context.startActivity(intent);
        });

        // Lógica do botão de compartilhar (sem alterações)
        holder.btnCompartilhar.setOnClickListener(v -> {
            // A classe ShareUtils precisa receber um objeto do tipo Exercicio da entidade
            // Verifique se o método `compartilharExercicio` está compatível.
            ShareUtils.compartilharExercicio(context, exercicio);
        });
    }

    @Override
    public int getItemCount() {
        // Adicionada verificação para evitar NullPointerException
        return exercicios != null ? exercicios.size() : 0;
    }

    public static class ExercicioViewHolder extends RecyclerView.ViewHolder {
        public View btnCompartilhar;
        TextView nome, descricao, musculos;
        Button btnExecucao;
        ImageButton btnFavoritar;

        public ExercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvNomeExercicio);
            descricao = itemView.findViewById(R.id.tvDescricaoExercicio);
            musculos = itemView.findViewById(R.id.tvMusculosRecrutados);
            btnExecucao = itemView.findViewById(R.id.btnExecucao);
            btnFavoritar = itemView.findViewById(R.id.btnFavoritar);
            btnCompartilhar = itemView.findViewById(R.id.btnCompartilhar);
        }
    }
}
