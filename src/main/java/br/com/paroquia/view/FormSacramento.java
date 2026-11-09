package br.com.paroquia.view;
import br.com.paroquia.controller.CadastroController;

public final class FormSacramento extends FormularioCadastro {
    public FormSacramento() {
        super("Sacramento",new String[]{"CPF do fiel", "Tipo de sacramento", "Data (dd/MM/aaaa)","Observações"},
              v->new CadastroController().sacramento(v[0],v[1],v[2],v[3]));
    }
}
