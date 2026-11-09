package br.com.paroquia.model;
import java.time.LocalDate;

public class Sacramento {
    private long id;
    private String fielCpf;
    private String tipo;
    private LocalDate data;
    private String observacoes;
    public Sacramento(String fielCpf, String tipo, LocalDate data, String observacoes) {
        this.fielCpf = fielCpf; this.tipo = tipo; this.data = data; this.observacoes = observacoes;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getFielCpf() { return fielCpf; }
    public String getTipo() { return tipo; }
    public LocalDate getData() { return data; }
    public String getObservacoes() { return observacoes; }
}
