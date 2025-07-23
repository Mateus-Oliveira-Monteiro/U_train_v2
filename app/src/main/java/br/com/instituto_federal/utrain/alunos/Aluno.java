package br.com.instituto_federal.utrain.alunos;

public class Aluno {
    private String id;
    private String nome;
    private String dataDeNascimento;
    private double peso;
    private String email;

    public Aluno() {
        // Construtor vazio necessário para o Firestore
    }

    public Aluno(String nome, String dataDeNascimento, double peso, String email) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.peso = peso;
        this.email = email;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public double getPeso() {
        return peso;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
