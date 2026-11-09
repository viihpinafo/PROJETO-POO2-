package br.com.paroquia.view;
import br.com.paroquia.controller.CadastroController;

public final class FormDespesa extends FormularioCadastro {
    public FormDespesa() {
        super("Despesa",new String[]{"Descrição","Categoria","Valor (R$)","Data (dd/MM/aaaa)"},
              v->new CadastroController().despesa(v[0],v[1],v[2],v[3]));
    }
}
