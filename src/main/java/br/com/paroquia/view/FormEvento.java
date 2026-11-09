package br.com.paroquia.view;
import br.com.paroquia.controller.CadastroController;

public final class FormEvento extends FormularioCadastro {
    public FormEvento() {
        super("Evento",new String[]{"Nome do evento","Descrição","Local","Data (dd/MM/aaaa)","CPF do responsável (opcional)"},
              v->new CadastroController().evento(v[0],v[1],v[2],v[3],v[4]));
    }
}
