package br.com.paroquia.view;
import java.awt.Component;
import javax.swing.JOptionPane;

public final class ErrosTela {
    private ErrosTela() { }
    public static void mostrar(Component componente,Exception e) {
        String mensagem = e.getMessage()==null?e.toString():e.getMessage();
        if(e instanceof br.com.paroquia.model.NomePequenoException) mensagem="O nome deve ter pelo menos 3 caracteres.";
        else if(e instanceof br.com.paroquia.model.IdadeInvalidaException) mensagem="A idade deve estar entre 0 e 120.";
        JOptionPane.showMessageDialog(componente,mensagem,"Verifique os dados",JOptionPane.ERROR_MESSAGE);
    }
}
