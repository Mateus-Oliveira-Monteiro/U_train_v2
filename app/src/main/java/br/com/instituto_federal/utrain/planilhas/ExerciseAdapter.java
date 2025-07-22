package br.com.instituto_federal.utrain.planilhas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.instituto_federal.utrain.R;
import br.com.instituto_federal.utrain.model.Exercise;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        // Como a API Wger não retorna o nome diretamente no endpoint de exercícios,
        // vamos exibir o ID e UUID por enquanto
        holder.exerciseName.setText("Exercício ID: " + exercise.getId());
        holder.exerciseDetails.setText("UUID: " + exercise.getUuid());

        // Exibir informações sobre músculos e equipamentos
        String muscleInfo = "Músculos: " + (exercise.getMuscles() != null ? exercise.getMuscles().toString() : "N/A");
        String equipmentInfo = "Equipamentos: " + (exercise.getEquipment() != null ? exercise.getEquipment().toString() : "N/A");
        holder.exerciseInfo.setText(muscleInfo + "\n" + equipmentInfo);
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseName;
        TextView exerciseDetails;
        TextView exerciseInfo;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.tv_exercise_name);
            exerciseDetails = itemView.findViewById(R.id.tv_exercise_details);
            exerciseInfo = itemView.findViewById(R.id.tv_exercise_info);
        }
    }
}

