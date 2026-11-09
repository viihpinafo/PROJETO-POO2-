package br.com.paroquia.view;

import br.com.paroquia.controller.FielController;
import br.com.paroquia.infra.Conversores;
import br.com.paroquia.model.FielComum;

/** Cadastra o fiel comum que poderá ser selecionado em sacramentos e casamentos. */
public final class FormFiel extends FormularioCadastro {
    public FormFiel() {
        super("fiel", new String[]{"Nome completo", "CPF", "Idade", "Paróquia", "Cidade"},
              campos -> {
                  FielComum fiel = new FielComum();
                  fiel.setNome(campos[0]);
                  fiel.setCpf(Conversores.cpf(campos[1]));
                  fiel.setIdade(Conversores.inteiro(campos[2], "idade"));
                  fiel.getParoquia().setNome(campos[3].trim());
                  fiel.getParoquia().setCidade(campos[4].trim());
                  new FielController().cadastrar(fiel);
              });
    }
}
