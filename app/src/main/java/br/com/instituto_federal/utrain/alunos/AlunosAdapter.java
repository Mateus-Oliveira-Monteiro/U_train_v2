package br.com.instituto_federal.utrain.alunos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.instituto_federal.utrain.R;

public class AlunosAdapter extends RecyclerView.Adapter<AlunosAdapter.AlunoViewHolder> {

    private List<Aluno> alunos;
    private Context context;
    private OnAlunoActionListener listener;

    public interface OnAlunoActionListener {
        void onEditarAluno(Aluno aluno);
        void onExcluirAluno(Aluno aluno);
    }

    public AlunosAdapter(Context context, List<Aluno> alunos, OnAlunoActionListener listener) {
        this.context = context;
        this.alunos = alunos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_aluno, parent, false);
        return new AlunoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoViewHolder holder, int position) {
        Aluno aluno = alunos.get(position);
        holder.bind(aluno);
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public void updateAlunos(List<Aluno> novosAlunos) {
        this.alunos = novosAlunos;
        notifyDataSetChanged();
    }

    class AlunoViewHolder extends RecyclerView.ViewHolder {
        private TextView textNomeAluno;
        private TextView textEmailAluno;
        private TextView textDataNascimento;
        private TextView textPeso;
        private ImageView imageViewMenu;

        public AlunoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNomeAluno = itemView.findViewById(R.id.textNomeAluno);
            textEmailAluno = itemView.findViewById(R.id.textEmailAluno);
            textDataNascimento = itemView.findViewById(R.id.textDataNascimento);
            textPeso = itemView.findViewById(R.id.textPeso);
            imageViewMenu = itemView.findViewById(R.id.imageViewMenu);
        }

        public void bind(Aluno aluno) {
            textNomeAluno.setText(aluno.getNome());
            textEmailAluno.setText(aluno.getEmail());
            textDataNascimento.setText(aluno.getDataDeNascimento());
            textPeso.setText(String.format(java.util.Locale.getDefault(), "%.1f kg", aluno.getPeso()));

            // Menu de contexto com long click
            itemView.setOnLongClickListener(v -> {
                showContextMenu(aluno);
                return true;
            });

            // Menu de contexto com clique no ícone
            imageViewMenu.setOnClickListener(v -> showContextMenu(aluno));
        }

        private void showContextMenu(Aluno aluno) {
            PopupMenu popupMenu = new PopupMenu(context, imageViewMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_aluno_context, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_editar) {
                    listener.onEditarAluno(aluno);
                    return true;
                } else if (itemId == R.id.menu_excluir) {
                    listener.onExcluirAluno(aluno);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        }
    }
}
