package br.com.paroquia.model;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Doacao {
    private long id;
    private String doadorCpf, descricao, formaPagamento;
    private BigDecimal valor;
    private LocalDate data;
    public Doacao(String doadorCpf, String descricao, BigDecimal valor, LocalDate data, String formaPagamento) {
        this.doadorCpf=doadorCpf; this.descricao=descricao; this.valor=valor;
        this.data=data; this.formaPagamento=formaPagamento;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getDoadorCpf() { return doadorCpf; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getData() { return data; }
    public String getFormaPagamento() { return formaPagamento; }
}
