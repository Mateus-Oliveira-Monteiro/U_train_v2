package br.com.instituto_federal.utrain.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "planilhas")
public class Planilha {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String descricao;

    // Construtor
    public Planilha(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}