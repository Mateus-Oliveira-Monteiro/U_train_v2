package br.com.instituto_federal.utrain.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import br.com.instituto_federal.utrain.data.model.Planilha;

@Dao
public interface PlanilhaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Planilha... planilhas);

    @Query("SELECT * FROM planilhas ORDER BY id ASC")
    List<Planilha> getAllPlanilhas();
}