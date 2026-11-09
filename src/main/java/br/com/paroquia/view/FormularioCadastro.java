package br.com.paroquia.view;
import java.awt.*;
import javax.swing.*;

/** Formulário Swing reutilizável com campos editáveis, botão e validação por controlador. */
public class FormularioCadastro extends JFrame {
    @FunctionalInterface public interface Gravar { void executar(String[] campos) throws Exception; }
    public FormularioCadastro(String titulo,String[] campos,Gravar salvar) {
        super("Cadastro de " + titulo);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20,24,12,24));
        GridBagConstraints g=new GridBagConstraints();
        g.insets=new Insets(5,5,5,5);g.fill=GridBagConstraints.HORIZONTAL;
        JTextField[] entradas=new JTextField[campos.length];
        for(int i=0;i<campos.length;i++) {
            g.gridy=i;g.gridx=0;g.weightx=0;g.anchor=GridBagConstraints.WEST;
            painel.add(new JLabel(campos[i]+":"),g);
            g.gridx=1;g.weightx=1;
            entradas[i]=new JTextField(24);
            painel.add(entradas[i],g);
        }
        JButton gravar=new JButton("Cadastrar");
        gravar.addActionListener(e->{
            try {
                String[] valores=new String[entradas.length];
                for(int i=0;i<entradas.length;i++) valores[i]=entradas[i].getText().trim();
                salvar.executar(valores);
                JOptionPane.showMessageDialog(this,"Cadastro realizado com sucesso!");
                for(JTextField campo:entradas)campo.setText("");
            } catch(Exception erro) { ErrosTela.mostrar(this,erro); }
        });
        g.gridx=0;g.gridy=campos.length;g.gridwidth=2;g.weightx=1;
        g.insets=new Insets(15,5,8,5);
        painel.add(gravar,g);
        JLabel rodape=new JLabel("Data: dd/MM/aaaa   |   Horário: HH:mm   |   CPF: 11 dígitos",SwingConstants.CENTER);
        rodape.setBorder(BorderFactory.createEmptyBorder(0,5,13,5));
        setLayout(new BorderLayout());
        add(new JScrollPane(painel),BorderLayout.CENTER);
        add(rodape,BorderLayout.SOUTH);
        setSize(540,Math.min(600,180+campos.length*46));
        setLocationRelativeTo(null);
    }
}
