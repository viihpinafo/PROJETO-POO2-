package br.com.paroquia.view;
import br.com.paroquia.controller.CadastroController;

public final class FormDoacao extends FormularioCadastro {
    public FormDoacao() {
        super("Doação",new String[]{"CPF do doador (opcional)","Descrição","Valor (R$)","Data (dd/MM/aaaa)","Forma de pagamento"},
              v->new CadastroController().doacao(v[0],v[1],v[2],v[3],v[4]));
    }
}
