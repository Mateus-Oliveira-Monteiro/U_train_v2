package br.com.instituto_federal.utrain.favoritos;

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

import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.planilhas.Execucao;
// ✅ Import corrigido para a nova entidade Exercicio
import br.com.instituto_federal.utrain.data.model.Exercicio;
import br.com.instituto_federal.utrain.utils.ShareUtils;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ExercicioViewHolder> {
    private List<Exercicio> exercicios;
    private Context context;

    public FavoritosAdapter(Context context, List<Exercicio> exercicios) {
        this.context = context;
        this.exercicios = exercicios;
    }

    /**
     * ✅ Método para atualizar a lista de exercícios dinamicamente.
     * Será útil na Activity de Favoritos para atualizar a lista após buscar os dados.
     */
    public void setExercicios(List<Exercicio> novosExercicios) {
        this.exercicios = novosExercicios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_planilha, parent, false);
        return new ExercicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercicioViewHolder holder, int position) {
        // A lógica aqui permanece a mesma, pois a nova entidade Exercicio
        // tem os mesmos métodos (getNome, getId, etc.)
        Exercicio exercicio = exercicios.get(position);
        holder.nome.setText(exercicio.getNome());
        holder.descricao.setText(exercicio.getDescricao());
        holder.musculos.setText(exercicio.getMusculos());

        String idStr = String.valueOf(exercicio.getId());

        SharedPreferences prefs = context.getSharedPreferences("FAVORITOS", Context.MODE_PRIVATE);

        // Define o ícone inicial do coração
        Set<String> favoritos = prefs.getStringSet("exercicios", new HashSet<>());
        if (favoritos.contains(idStr)) {
            holder.btnFavoritar.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.btnFavoritar.setImageResource(R.drawable.ic_favorite_border);
        }

        // Lógica para favoritar/desfavoritar
        holder.btnFavoritar.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            Set<String> atualizados = new HashSet<>(prefs.getStringSet("exercicios", new HashSet<>()));

            if (atualizados.contains(idStr)) {
                atualizados.remove(idStr);
                holder.btnFavoritar.setImageResource(R.drawable.ic_favorite_border);

                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Exercicio removido = exercicios.get(pos);
                    exercicios.remove(pos);
                    notifyItemRemoved(pos);

                    editor.putStringSet("exercicios", atualizados);
                    editor.apply();

                    Snackbar.make(holder.itemView, "Exercício removido dos favoritos.", Snackbar.LENGTH_LONG)
                            .setAction("Desfazer", view -> {
                                Set<String> favoritosParaRestaurar = new HashSet<>(prefs.getStringSet("exercicios", new HashSet<>()));
                                favoritosParaRestaurar.add(idStr);
                                editor.putStringSet("exercicios", favoritosParaRestaurar);
                                editor.apply();

                                exercicios.add(pos, removido);
                                notifyItemInserted(pos);
                                holder.btnFavoritar.setImageResource(R.drawable.ic_favorite);
                            })
                            .show();
                }
            } else {
                // Este 'else' é para o caso de um exercício que não está na lista de favoritos
                // ser favoritado novamente (não deve acontecer nesta tela, mas é uma boa prática).
                atualizados.add(idStr);
                holder.btnFavoritar.setImageResource(R.drawable.ic_favorite);
                editor.putStringSet("exercicios", atualizados);
                editor.apply();
            }
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
            ShareUtils.compartilharExercicio(context, exercicio);
        });
    }

    @Override
    public int getItemCount() {
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
