package br.com.paroquia.model;
import java.time.LocalDate;

public class Evento {
    private long id;
    private String nome, descricao, local, responsavelCpf;
    private LocalDate data;
    public Evento(String nome, String descricao, String local, LocalDate data, String responsavelCpf) {
        this.nome=nome; this.descricao=descricao; this.local=local; this.data=data;
        this.responsavelCpf=responsavelCpf;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getLocal() { return local; }
    public LocalDate getData() { return data; }
    public String getResponsavelCpf() { return responsavelCpf; }
}
