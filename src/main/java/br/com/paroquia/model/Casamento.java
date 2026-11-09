package br.com.paroquia.model;
import java.time.LocalDate;
import java.time.LocalTime;

public class Casamento {
    private long id;
    private String noivoCpf, noivaCpf, local;
    private LocalDate data;
    private LocalTime horario;
    public Casamento(String noivoCpf, String noivaCpf, LocalDate data, LocalTime horario, String local) {
        this.noivoCpf=noivoCpf; this.noivaCpf=noivaCpf; this.data=data; this.horario=horario; this.local=local;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNoivoCpf() { return noivoCpf; }
    public String getNoivaCpf() { return noivaCpf; }
    public LocalDate getData() { return data; }
    public LocalTime getHorario() { return horario; }
    public String getLocal() { return local; }
}
