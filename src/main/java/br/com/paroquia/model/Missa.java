package br.com.paroquia.model;
import java.util.ArrayList;
import java.util.List;

public class Missa {
    private long id;
    private String data = "";
    private String horario = "";
    private Padre padre = new Padre();
    private final List<Coroinha> coroinhas = new ArrayList<>();

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getData() { return data; }
    public String getHorario() { return horario; }
    public Padre getPadre() { return padre; }
    public List<Coroinha> getCoroinhas() { return coroinhas; }
    public void setData(String data) { this.data = data; }
    public void setHorario(String horario) { this.horario = horario; }
    public void setPadre(Padre padre) { this.padre = padre; }
    public void addCoroinha(Coroinha coroinha) { coroinhas.add(coroinha); }
}
