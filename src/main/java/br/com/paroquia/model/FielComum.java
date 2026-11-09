package br.com.paroquia.model;

/** Paroquiano cadastrado sem uma função pastoral específica. */
public final class FielComum extends Fiel {
    public FielComum() { super(); }

    @Override
    public void mostrarFuncao() {
        System.out.println("Fiel da comunidade paroquial");
    }
}
