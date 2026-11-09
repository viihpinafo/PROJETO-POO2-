package br.com.paroquia.view;

import br.com.paroquia.controller.CadastroController;
import br.com.paroquia.controller.FielController;
import br.com.paroquia.model.Fiel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Registra o casamento associando dois fiéis já existentes pelo CPF. */
public final class FormCasamento extends JFrame {
    private final JComboBox<OpcaoFiel> noivo = new JComboBox<>();
    private final JComboBox<OpcaoFiel> noiva = new JComboBox<>();
    private final JTextField data = new JTextField(18);
    private final JTextField horario = new JTextField(18);
    private final JTextField local = new JTextField(18);
    private final JLabel mensagem = new JLabel("Cadastre os fiéis antes de registrar o casamento.");

    public FormCasamento() {
        super("Cadastro de Casamento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createEmptyBorder(18, 18, 12, 18));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 5, 6, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        noivo.setPrototypeDisplayValue(new OpcaoFiel("00000000000", "Nome completo do fiel — CPF 00000000000"));
        noiva.setPrototypeDisplayValue(new OpcaoFiel("00000000000", "Nome completo do fiel — CPF 00000000000"));
        linha(formulario, g, 0, "Noivo cadastrado", noivo);
        linha(formulario, g, 1, "Noiva cadastrada", noiva);
        linha(formulario, g, 2, "Data (dd/MM/aaaa)", data);
        linha(formulario, g, 3, "Horário (HH:mm)", horario);
        linha(formulario, g, 4, "Local", local);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cadastrarFiel = new JButton("Cadastrar novo fiel");
        cadastrarFiel.addActionListener(e -> new FormFiel().setVisible(true));
        JButton atualizar = new JButton("Atualizar lista de fiéis");
        atualizar.addActionListener(e -> atualizarFieis());
        JButton cadastrar = new JButton("Cadastrar casamento");
        cadastrar.addActionListener(e -> salvar());
        botoes.add(cadastrarFiel);
        botoes.add(atualizar);
        botoes.add(cadastrar);

        JPanel inferior = new JPanel(new BorderLayout());
        mensagem.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        inferior.add(mensagem, BorderLayout.NORTH);
        inferior.add(botoes, BorderLayout.SOUTH);
        add(formulario, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);
        setSize(780, 380);
        setLocationRelativeTo(null);
        // Recarrega quando o usuário retorna do formulário de novo fiel.
        addWindowListener(new WindowAdapter() {
            @Override public void windowActivated(WindowEvent evento) { atualizarFieis(); }
        });
        atualizarFieis();
    }

    private void linha(JPanel painel, GridBagConstraints g, int indice, String titulo, java.awt.Component entrada) {
        g.gridy = indice;
        g.gridx = 0;
        g.weightx = 0;
        painel.add(new JLabel(titulo + ":"), g);
        g.gridx = 1;
        g.weightx = 1;
        painel.add(entrada, g);
    }

    /** Mantém as seleções anteriores ao atualizar (por CPF), se ainda existirem. */
    private void atualizarFieis() {
        String cpfNoivo = cpfSelecionado(noivo);
        String cpfNoiva = cpfSelecionado(noiva);
        List<Fiel> cadastrados = new FielController().listar();
        noivo.removeAllItems();
        noiva.removeAllItems();
        for (Fiel fiel : cadastrados) {
            OpcaoFiel opcao = new OpcaoFiel(fiel.getCpf(), fiel.getNome() + " — CPF " + fiel.getCpf());
            noivo.addItem(opcao);
            noiva.addItem(opcao);
        }
        selecionarPorCpf(noivo, cpfNoivo);
        selecionarPorCpf(noiva, cpfNoiva);
        mensagem.setText(cadastrados.isEmpty()
            ? "Nenhum fiel cadastrado. Use 'Cadastrar novo fiel' para começar."
            : "Selecione os noivos cadastrados pelo nome e CPF (" + cadastrados.size() + " fiéis disponíveis).");
    }

    private static String cpfSelecionado(JComboBox<OpcaoFiel> seletor) {
        OpcaoFiel opcao = (OpcaoFiel) seletor.getSelectedItem();
        return opcao == null ? null : opcao.cpf();
    }

    private static void selecionarPorCpf(JComboBox<OpcaoFiel> seletor, String cpf) {
        seletor.setSelectedIndex(-1);
        if (cpf == null) return;
        for (int i = 0; i < seletor.getItemCount(); i++) {
            if (Objects.equals(cpf, seletor.getItemAt(i).cpf())) {
                seletor.setSelectedIndex(i);
                return;
            }
        }
    }

    private void salvar() {
        try {
            String cpfNoivo = cpfSelecionado(noivo);
            String cpfNoiva = cpfSelecionado(noiva);
            if (cpfNoivo == null || cpfNoiva == null) {
                throw new IllegalArgumentException("Selecione os dois fiéis já cadastrados antes de continuar.");
            }
            new CadastroController().casamento(cpfNoivo, cpfNoiva, data.getText().trim(),
                                                horario.getText().trim(), local.getText().trim());
            JOptionPane.showMessageDialog(this, "Casamento cadastrado e vinculado aos fiéis com sucesso!");
            data.setText("");
            horario.setText("");
            local.setText("");
            noivo.setSelectedIndex(-1);
            noiva.setSelectedIndex(-1);
        } catch (Exception erro) {
            ErrosTela.mostrar(this, erro);
        }
    }

    private record OpcaoFiel(String cpf, String rotulo) {
        @Override public String toString() { return rotulo; }
    }
}
