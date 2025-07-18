package br.com.instituto_federal.utrain.planilhas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "utrain.db";
    private static final int DATABASE_VERSION = 2; // Incrementado para nova estrutura

    // Tabela de exercícios
    private static final String TABLE_EXERCICIOS = "exercicios";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_MUSCULOS = "musculos";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_VIDEO_URL = "video_url";
    private static final String COLUMN_PLANILHA_ID = "planilha_id";

    // Tabela de planilhas
    private static final String TABLE_PLANILHAS = "planilhas";
    private static final String COLUMN_PLANILHA_ID_PK = "id";
    private static final String COLUMN_PLANILHA_NOME = "nome";
    private static final String COLUMN_PLANILHA_DESCRICAO = "descricao";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criar tabela de planilhas
        String CREATE_PLANILHAS_TABLE = "CREATE TABLE " + TABLE_PLANILHAS + " ("
                + COLUMN_PLANILHA_ID_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLANILHA_NOME + " TEXT,"
                + COLUMN_PLANILHA_DESCRICAO + " TEXT)";
        db.execSQL(CREATE_PLANILHAS_TABLE);

        // Criar tabela de exercícios
        String CREATE_EXERCICIOS_TABLE = "CREATE TABLE " + TABLE_EXERCICIOS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOME + " TEXT,"
                + COLUMN_MUSCULOS + " TEXT,"
                + COLUMN_DESCRICAO + " TEXT,"
                + COLUMN_VIDEO_URL + " TEXT,"
                + COLUMN_PLANILHA_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_PLANILHA_ID + ") REFERENCES " + TABLE_PLANILHAS + "(" + COLUMN_PLANILHA_ID_PK + "))";
        db.execSQL(CREATE_EXERCICIOS_TABLE);

        // Inserir dados iniciais
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCICIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANILHAS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Inserir planilhas padrão
        ContentValues planilhaValues = new ContentValues();

        planilhaValues.put(COLUMN_PLANILHA_NOME, "Planilha A");
        planilhaValues.put(COLUMN_PLANILHA_DESCRICAO, "Treino de Pernas");
        db.insert(TABLE_PLANILHAS, null, planilhaValues);

        planilhaValues.put(COLUMN_PLANILHA_NOME, "Planilha B");
        planilhaValues.put(COLUMN_PLANILHA_DESCRICAO, "Treino de Peito");
        db.insert(TABLE_PLANILHAS, null, planilhaValues);

        planilhaValues.put(COLUMN_PLANILHA_NOME, "Planilha C");
        planilhaValues.put(COLUMN_PLANILHA_DESCRICAO, "Treino de Costas");
        db.insert(TABLE_PLANILHAS, null, planilhaValues);

        // Inserir exercícios padrão
        ContentValues exercicioValues = new ContentValues();

        // Exercícios da Planilha A
        exercicioValues.put(COLUMN_NOME, "Agachamento");
        exercicioValues.put(COLUMN_DESCRICAO, "Fortalece pernas");
        exercicioValues.put(COLUMN_MUSCULOS, "Quadríceps");
        exercicioValues.put(COLUMN_VIDEO_URL, "V5iNNV9KaVA");
        exercicioValues.put(COLUMN_PLANILHA_ID, 1);
        db.insert(TABLE_EXERCICIOS, null, exercicioValues);

        exercicioValues.put(COLUMN_NOME, "Leg Press");
        exercicioValues.put(COLUMN_DESCRICAO, "Trabalha pernas");
        exercicioValues.put(COLUMN_MUSCULOS, "Quadríceps");
        exercicioValues.put(COLUMN_VIDEO_URL, "abc123");
        exercicioValues.put(COLUMN_PLANILHA_ID, 1);
        db.insert(TABLE_EXERCICIOS, null, exercicioValues);

        exercicioValues.put(COLUMN_NOME, "Stiff");
        exercicioValues.put(COLUMN_DESCRICAO, "Posicione os pés na largura dos ombros, pegue a barra ou halteres, estufe o peito, feche as escapulas, leve APENAS o quadril para trás fazendo com que a barra desça rente ao corpo");
        exercicioValues.put(COLUMN_MUSCULOS, "Posterior e Glúteos");
        exercicioValues.put(COLUMN_VIDEO_URL, "VkLIhN1HSFw");
        exercicioValues.put(COLUMN_PLANILHA_ID, 1);
        db.insert(TABLE_EXERCICIOS, null, exercicioValues);

        // Exercícios da Planilha B
        exercicioValues.put(COLUMN_NOME, "Supino");
        exercicioValues.put(COLUMN_DESCRICAO, "Fortalece peitoral");
        exercicioValues.put(COLUMN_MUSCULOS, "Peitoral");
        exercicioValues.put(COLUMN_VIDEO_URL, "eG6b1k2a4g0");
        exercicioValues.put(COLUMN_PLANILHA_ID, 2);
        db.insert(TABLE_EXERCICIOS, null, exercicioValues);

        exercicioValues.put(COLUMN_NOME, "Crucifixo");
        exercicioValues.put(COLUMN_DESCRICAO, "Isola peitoral");
        exercicioValues.put(COLUMN_MUSCULOS, "Peitoral");
        exercicioValues.put(COLUMN_VIDEO_URL, "def456");
        exercicioValues.put(COLUMN_PLANILHA_ID, 2);
        db.insert(TABLE_EXERCICIOS, null, exercicioValues);

        // Exercícios da Planilha C
        exercicioValues.put(COLUMN_NOME, "Remada");
        exercicioValues.put(COLUMN_DESCRICAO, "Fortalece costas");
        exercicioValues.put(COLUMN_MUSCULOS, "Dorsal");
        exercicioValues.put(COLUMN_VIDEO_URL, "ghi789");
        exercicioValues.put(COLUMN_PLANILHA_ID, 3);
        db.insert(TABLE_EXERCICIOS, null, exercicioValues);

        exercicioValues.put(COLUMN_NOME, "Puxada");
        exercicioValues.put(COLUMN_DESCRICAO, "Trabalha costas");
        exercicioValues.put(COLUMN_MUSCULOS, "Dorsal");
        exercicioValues.put(COLUMN_VIDEO_URL, "jkl012");
        exercicioValues.put(COLUMN_PLANILHA_ID, 3);
        db.insert(TABLE_EXERCICIOS, null, exercicioValues);
    }

    // Métodos para exercícios
    public long adicionarExercicio(Exercicio exercicio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, exercicio.getNome());
        values.put(COLUMN_MUSCULOS, exercicio.getMusculos());
        values.put(COLUMN_DESCRICAO, exercicio.getDescricao());
        values.put(COLUMN_VIDEO_URL, exercicio.getYoutubeId());
        values.put(COLUMN_PLANILHA_ID, exercicio.getPlanilhaId());

        long id = db.insert(TABLE_EXERCICIOS, null, values);
        db.close();
        return id;
    }

    public List<Exercicio> listarExercicios() {
        List<Exercicio> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXERCICIOS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Exercicio exercicio = new Exercicio(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSCULOS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIDEO_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLANILHA_ID))
                );
                lista.add(exercicio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public List<Exercicio> listarExerciciosPorPlanilha(int planilhaId) {
        List<Exercicio> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_PLANILHA_ID + " = ?";
        String[] selectionArgs = { String.valueOf(planilhaId) };

        Cursor cursor = db.query(TABLE_EXERCICIOS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Exercicio exercicio = new Exercicio(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSCULOS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIDEO_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLANILHA_ID))
                );
                lista.add(exercicio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public Exercicio buscarExercicioPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(TABLE_EXERCICIOS, null, selection, selectionArgs, null, null, null);

        Exercicio exercicio = null;
        if (cursor.moveToFirst()) {
            exercicio = new Exercicio(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSCULOS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIDEO_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLANILHA_ID))
            );
        }

        cursor.close();
        db.close();
        return exercicio;
    }

    public void deletarTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCICIOS, null, null);
        db.close();
    }
}