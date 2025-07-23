package br.com.instituto_federal.utrain.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import br.com.instituto_federal.utrain.data.dao.ExercicioDao;
import br.com.instituto_federal.utrain.data.dao.PlanilhaDao;
import br.com.instituto_federal.utrain.data.model.Exercicio;
import br.com.instituto_federal.utrain.data.model.Planilha;

@Database(entities = {Planilha.class, Exercicio.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlanilhaDao planilhaDao();
    public abstract ExercicioDao exercicioDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "utrain_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                PlanilhaDao planilhaDao = INSTANCE.planilhaDao();
                ExercicioDao exercicioDao = INSTANCE.exercicioDao();

                planilhaDao.insertAll(
                        new Planilha("Planilha A", "Treino de Pernas"),
                        new Planilha("Planilha B", "Treino de Peito"),
                        new Planilha("Planilha C", "Treino de Costas")
                );

                Exercicio ex1 = new Exercicio();
                ex1.setNome("Agachamento"); ex1.setDescricao("Fortalece pernas 1"); ex1.setMusculos("Quadríceps"); ex1.setYoutubeId("V5iNNV9KaVA"); ex1.setPlanilhaId(1);
                Exercicio ex2 = new Exercicio();
                ex2.setNome("Supino"); ex2.setDescricao("Fortalece peitoral"); ex2.setMusculos("Peitoral"); ex2.setYoutubeId("eG6b1k2a4g0"); ex2.setPlanilhaId(2);

                exercicioDao.insertAll(ex1, ex2);
            });
        }
    };
}
