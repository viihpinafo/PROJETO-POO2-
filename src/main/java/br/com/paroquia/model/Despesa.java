package br.com.paroquia.model;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Despesa {
    private long id;
    private String descricao, categoria;
    private BigDecimal valor;
    private LocalDate data;
    public Despesa(String descricao, String categoria, BigDecimal valor, LocalDate data) {
        this.descricao=descricao; this.categoria=categoria; this.valor=valor; this.data=data;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getData() { return data; }
}
