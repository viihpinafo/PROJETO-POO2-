package br.com.paroquia.view;
import br.com.paroquia.controller.CadastroController;

public final class FormCatequizando extends FormularioCadastro {
    public FormCatequizando() {
        super("Catequizando",new String[]{"Nome","Idade","Etapa","Responsável","Telefone","CPF do catequista (opcional)"},
              v->new CadastroController().catequizando(v[0],v[1],v[2],v[3],v[4],v[5]));
    }
}
