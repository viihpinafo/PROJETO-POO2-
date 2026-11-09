package br.com.paroquia;

import br.com.paroquia.controller.CadastroController;
import br.com.paroquia.controller.FielController;
import br.com.paroquia.controller.RelatorioController;
import br.com.paroquia.model.FielComum;
import br.com.paroquia.repository.RepositorioMemoria;

/** Teste de dois fiéis previamente cadastrados e vinculados a um casamento. */
public final class TesteVinculoCasamento {
    public static void main(String[] args) throws Exception {
        FielController fieis = new FielController();
        CadastroController cadastro = new CadastroController();
        FielComum ana = new FielComum();
        ana.setNome("Ana Oliveira"); ana.setCpf("00000000901"); ana.setIdade(29);
        ana.getParoquia().setNome("São José");
        FielComum pedro = new FielComum();
        pedro.setNome("Pedro Almeida"); pedro.setCpf("00000000902"); pedro.setIdade(30);
        pedro.getParoquia().setNome("São José");
        fieis.cadastrar(ana);
        fieis.cadastrar(pedro);
        conferir(fieis.listar().size() == 2, "fiéis da comunidade cadastrados");
        conferir(fieis.listarFieisComuns().size() == 2, "consulta de fiéis comuns");
        conferir("Fiel".equals(FielController.cargo(ana)), "perfil do fiel comum");
        erro(() -> cadastro.casamento("00000000901", "00000000903", "12/11/2026", "16:00", "Matriz"),
             "impedir noivo/noiva inexistente");
        erro(() -> cadastro.casamento("00000000901", "00000000901", "12/11/2026", "16:00", "Matriz"),
             "impedir casamento com o mesmo CPF");
        cadastro.casamento("00000000902", "00000000901", "12/11/2026", "16:00", "Matriz");
        var lista = RepositorioMemoria.getInstancia().listarCasamentos();
        conferir(lista.size() == 1, "casamento salvo uma vez");
        conferir(lista.get(0).getNoivoCpf().equals(pedro.getCpf()), "noivo vinculado ao registro original");
        conferir(lista.get(0).getNoivaCpf().equals(ana.getCpf()), "noiva vinculada ao registro original");
        var rel = new RelatorioController().consultar("Casamentos");
        conferir(rel.linhas().size() == 1, "relatório de casamentos");
        conferir(rel.linhas().get(0)[1].equals("Pedro Almeida"), "nome do noivo no relatório");
        conferir(rel.linhas().get(0)[3].equals("Ana Oliveira"), "nome da noiva no relatório");
        conferir(new RelatorioController().consultar("Fiéis comuns").linhas().size() == 2,
                 "relatório dos fiéis comuns");
        conferir(fieis.alterarNome("00000000902", "Pedro Souza"), "alteração do fiel");
        conferir(new RelatorioController().consultar("Casamentos").linhas().get(0)[1].equals("Pedro Souza"),
                 "relatório busca nome atual sem duplicar dados");
        erro(() -> fieis.excluir("00000000902"), "não excluir fiel vinculado a casamento");
        conferir(fieis.consultar("00000000902") != null, "fiel referenciado continua existente");
        System.out.println("OK: cadastro e vínculo de fiéis comuns em casamento e relatórios.");
    }
    private static void conferir(boolean condicao, String nome) {
        if (!condicao) throw new AssertionError(nome);
    }
    private static void erro(Runnable operacao, String nome) {
        try { operacao.run(); throw new AssertionError("Esperava erro: " + nome); }
        catch (IllegalArgumentException esperado) { }
    }
}
