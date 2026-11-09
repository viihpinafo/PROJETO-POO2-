package br.com.paroquia.model;

/** Modelo base para os três perfis já usados pelo projeto. CPF é texto para preservar 11 dígitos. */
public abstract class Fiel {
    private String nome = "";
    private int idade;
    private String cpf = "";
    private Paroquia paroquia = new Paroquia();

    public Fiel() { }
    public Fiel(String nome) { this.nome = nome; }

    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public String getCpf() { return cpf; }
    public Paroquia getParoquia() { return paroquia; }

    public void setNome(String nome) throws NomePequenoException {
        if (nome == null || nome.trim().length() < 3) throw new NomePequenoException();
        this.nome = nome.trim();
    }
    public void setIdade(int idade) throws IdadeInvalidaException {
        if (idade < 0 || idade > 120) throw new IdadeInvalidaException();
        this.idade = idade;
    }
    public void setCpf(String cpf) {
        if (cpf == null || !cpf.replaceAll("[.\\-\\s]", "").matches("\\d{11}")) {
            throw new IllegalArgumentException("O CPF deve ter 11 dígitos numéricos.");
        }
        this.cpf = cpf.replaceAll("\\D", "");
    }
    public void setCpf(int cpf) { setCpf(String.format("%011d", cpf)); } // compatibilidade com códigos antigos
    public abstract void mostrarFuncao();
}
