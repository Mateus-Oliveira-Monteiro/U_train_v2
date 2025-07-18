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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.planilhas.Execucao;
import br.com.instituto_federal.utrain.planilhas.Exercicio;
import br.com.instituto_federal.utrain.utils.ShareUtils;

import com.google.android.material.snackbar.Snackbar;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ExercicioViewHolder> {
    private List<Exercicio> exercicios;
    private Context context;

    public FavoritosAdapter(Context context, List<Exercicio> exercicios) {
        this.context = context;
        this.exercicios = exercicios;

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

        SharedPreferences prefs = context.getSharedPreferences("FAVORITOS", Context.MODE_PRIVATE);
        Set<String> favoritos = prefs.getStringSet("exercicios", new HashSet<>());
        if (favoritos.contains(idStr)) {
            holder.btnFavoritar.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.btnFavoritar.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.btnFavoritar.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            Set<String> atualizados = new HashSet<>(prefs.getStringSet("exercicios", new HashSet<>()));

            if (atualizados.contains(idStr)) {
                atualizados.remove(idStr);
                holder.btnFavoritar.setImageResource(R.drawable.ic_favorite_border);

                int pos = holder.getAdapterPosition();
                Exercicio removido = exercicios.get(pos);
                // Remove da lista atual e notifica
                exercicios.remove(pos);
                notifyItemRemoved(pos);
                editor.putStringSet("exercicios", atualizados);
                editor.apply();

                Snackbar.make(holder.itemView, "Exercício retirado dos favoritos. Deseja desfazer?", Snackbar.LENGTH_LONG)
                        .setAction("Desfazer", view -> {
                            atualizados.add(idStr);
                            editor.putStringSet("exercicios", atualizados);
                            editor.apply();

                            exercicios.add(pos, removido);
                            notifyItemInserted(pos);
                        })
                        .show();

            } else {
                atualizados.add(idStr);
                holder.btnFavoritar.setImageResource(R.drawable.ic_favorite);
                editor.putStringSet("exercicios", atualizados);
                editor.apply();
            }

            editor.putStringSet("exercicios", atualizados);
            editor.apply();
        });

        holder.btnExecucao.setOnClickListener(v -> {
            Intent intent = new Intent(context, Execucao.class);
            intent.putExtra("nomeExercicio", exercicio.getNome());
            intent.putExtra("descricaoExercicio", exercicio.getDescricao());
            intent.putExtra("musculosRecrutados", exercicio.getMusculos());
            intent.putExtra("youtubeVideoId", exercicio.getYoutubeId());
            context.startActivity(intent);
        });

        holder.btnCompartilhar.setOnClickListener(v -> {
            ShareUtils.compartilharExercicio(context, exercicio);
        });
    }

    @Override
    public int getItemCount() {
        return exercicios.size();
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
