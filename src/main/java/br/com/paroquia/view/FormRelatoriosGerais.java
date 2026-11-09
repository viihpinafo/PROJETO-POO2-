package br.com.paroquia.view;
import br.com.paroquia.controller.RelatorioController;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Relatório unificado: consulta o repositório em memória da aplicação. */
public class FormRelatoriosGerais extends JFrame {
    private final JComboBox<String> seletor = new JComboBox<>(new String[]{
        "Fiéis","Fiéis comuns","Coroinhas","Padres","Catequistas", "Missas","Sacramentos",
        "Casamentos","Catequizandos","Doações","Despesas","Eventos"});
    private final JTable tabela = new JTable();
    private final JLabel rodape = new JLabel(" ");
    public FormRelatoriosGerais() {
        this("Fiéis");
    }

    /** Abre o relatório diretamente na categoria escolhida pelo menu. */
    public FormRelatoriosGerais(String moduloInicial) {
        super("Relatórios - Cadastros da paróquia");
        seletor.setSelectedItem(moduloInicial);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel superior=new JPanel(new FlowLayout(FlowLayout.LEFT));
        superior.add(new JLabel("Mostrar:"));superior.add(seletor);
        JButton atualizar=new JButton("Atualizar dados");superior.add(atualizar);
        atualizar.addActionListener(e->atualizar());
        seletor.addActionListener(e->atualizar());
        tabela.setAutoCreateRowSorter(true);
        tabela.setFillsViewportHeight(true);
        rodape.setBorder(BorderFactory.createEmptyBorder(8,14,12,14));
        add(superior,BorderLayout.NORTH);
        add(new JScrollPane(tabela),BorderLayout.CENTER);
        add(rodape,BorderLayout.SOUTH);
        setSize(920,475);setLocationRelativeTo(null);
        atualizar();
    }
    private void atualizar() {
        try {
            var dados=new RelatorioController().consultar((String)seletor.getSelectedItem());
            DefaultTableModel modelo=new DefaultTableModel(dados.colunas(),0){
                @Override public boolean isCellEditable(int linha,int coluna){ return false; }
            };
            for(Object[] linha:dados.linhas())modelo.addRow(linha);
            tabela.setModel(modelo);
            rodape.setText(dados.resumo());
        } catch(Exception ex) { ErrosTela.mostrar(this,ex); }
    }
}
