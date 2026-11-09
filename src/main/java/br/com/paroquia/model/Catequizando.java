package br.com.paroquia.model;

public class Catequizando {
    private long id;
    private String nome, etapa, responsavel, telefone, catequistaCpf;
    private int idade;
    public Catequizando(String nome, int idade, String etapa, String responsavel, String telefone, String catequistaCpf) {
        this.nome=nome; this.idade=idade; this.etapa=etapa; this.responsavel=responsavel;
        this.telefone=telefone; this.catequistaCpf=catequistaCpf;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public String getEtapa() { return etapa; }
    public String getResponsavel() { return responsavel; }
    public String getTelefone() { return telefone; }
    public String getCatequistaCpf() { return catequistaCpf; }
}
