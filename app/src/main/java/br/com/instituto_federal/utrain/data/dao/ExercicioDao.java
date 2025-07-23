package br.com.instituto_federal.utrain.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import br.com.instituto_federal.utrain.data.model.Exercicio;

@Dao
public interface ExercicioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Exercicio exercicio);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Exercicio... exercicios);

    @Query("SELECT * FROM exercicios WHERE planilhaId = :planilhaId ORDER BY nome ASC")
    LiveData<List<Exercicio>> getExerciciosPorPlanilha(int planilhaId);

    @Query("SELECT * FROM exercicios")
    LiveData<List<Exercicio>> getAllExercicios();

    @Query("SELECT * FROM exercicios WHERE id IN (:exercicioIds)")
    LiveData<List<Exercicio>> getExerciciosByIds(List<Integer> exercicioIds);
}
