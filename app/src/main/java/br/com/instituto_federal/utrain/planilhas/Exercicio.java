package br.com.instituto_federal.utrain.planilhas;

public class Exercicio {

    private Integer id;
    private String nome;
    private String descricao;
    private String musculos;
    private String youtubeId;
    private Integer planilhaId;
    public Exercicio(Integer id, String nome, String descricao, String musculos, String youtubeId, Integer planilhaId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.musculos = musculos;
        this.youtubeId = youtubeId;
        this.planilhaId = planilhaId;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getMusculos() {
        return musculos;
    }
    public void setMusculos(String musculos) {
        this.musculos = musculos;
    }
    public String getYoutubeId() {
        return youtubeId;
    }
    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public Integer getPlanilhaId() {
        return planilhaId;
    }

    public void setPlanilhaId(Integer planilhaId) {
        this.planilhaId = planilhaId;
    }
}
