package br.com.instituto_federal.utrain.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Este é o seu arquivo Exercicio.java, agora modificado para ser uma entidade do Room.
@Entity(tableName = "exercicios",
        foreignKeys = @ForeignKey(entity = Planilha.class,
                parentColumns = "id",
                childColumns = "planilhaId",
                onDelete = ForeignKey.CASCADE))
public class Exercicio {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String nome;
    private String descricao;
    private String musculos;

    @ColumnInfo(name = "video_url")
    private String youtubeId;

    @ColumnInfo(index = true)
    private Integer planilhaId;

    // Construtor vazio é necessário para o Room
    public Exercicio() {}

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getMusculos() { return musculos; }
    public void setMusculos(String musculos) { this.musculos = musculos; }
    public String getYoutubeId() { return youtubeId; }
    public void setYoutubeId(String youtubeId) { this.youtubeId = youtubeId; }
    public Integer getPlanilhaId() { return planilhaId; }
    public void setPlanilhaId(Integer planilhaId) { this.planilhaId = planilhaId; }
}
