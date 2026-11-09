package br.com.paroquia;

import br.com.paroquia.controller.FielController;
import br.com.paroquia.controller.RelatorioController;
import br.com.paroquia.model.Catequistas;
import br.com.paroquia.model.Coroinha;
import br.com.paroquia.model.Padre;
import java.util.Arrays;

/** Testa os relatórios separados sem banco de dados ou interface gráfica. */
public final class TesteRelatoriosPorFuncao {
    public static void main(String[] args) throws Exception {
        FielController controller = new FielController();
        RelatorioController relatorios = new RelatorioController();

        // Antes do cadastro, cada relatório deve existir e estar vazio.
        for (String modulo : new String[]{"Fiéis", "Coroinhas", "Padres", "Catequistas"}) {
            conferir(relatorios.consultar(modulo).linhas().isEmpty(), "inicialmente vazio: " + modulo);
        }

        Coroinha coroinha = new Coroinha();
        coroinha.setNome("Ana Coroinha");
        coroinha.setCpf("00000000101");
        coroinha.setIdade(14);
        coroinha.setEscala("Domingo às 19h");
        coroinha.setTempoPastoral("2 anos");
        controller.cadastrar(coroinha);

        Padre padre = new Padre();
        padre.setNome("Bruno Padre");
        padre.setCpf("00000000102");
        padre.setIdade(48);
        padre.setParoquiaPadre("São José");
        padre.setCidade("Cornélio Procópio");
        padre.setSetorParoquia("Centro");
        controller.cadastrar(padre);

        Catequistas catequista = new Catequistas();
        catequista.setNome("Clara Catequista");
        catequista.setCpf("00000000103");
        catequista.setIdade(35);
        catequista.setEtapa("Crisma");
        catequista.setQtdCatequisandos(18);
        controller.cadastrar(catequista);

        conferir(relatorios.consultar("Fiéis").linhas().size() == 3, "lista completa de fiéis");
        conferir(controller.listarCoroinhas().size() == 1, "filtro do controller para coroinhas");
        conferir(controller.listarPadres().size() == 1, "filtro do controller para padres");
        conferir(controller.listarCatequistas().size() == 1, "filtro do controller para catequistas");

        var relCoroinhas = relatorios.consultar("Coroinhas");
        conferir(relCoroinhas.linhas().size() == 1, "somente um coroinha");
        conferir(Arrays.equals(relCoroinhas.colunas(),
            new String[]{"Nome", "CPF", "Idade", "Paróquia", "Escala", "Tempo de pastoral"}),
            "colunas do coroinha");
        conferir(relCoroinhas.linhas().get(0)[4].equals("Domingo às 19h"), "escala do coroinha");
        conferir(relCoroinhas.linhas().get(0)[5].equals("2 anos"), "tempo de pastoral");
        conferir(relCoroinhas.resumo().contains("1"), "contagem dos coroinhas");

        var relPadres = relatorios.consultar("Padres");
        conferir(relPadres.linhas().size() == 1, "somente um padre");
        conferir(relPadres.linhas().get(0)[3].equals("São José"), "paróquia do padre");
        conferir(relPadres.linhas().get(0)[4].equals("Cornélio Procópio"), "cidade do padre");
        conferir(relPadres.linhas().get(0)[5].equals("Centro"), "setor do padre");

        var relCatequistas = relatorios.consultar("Catequistas");
        conferir(relCatequistas.linhas().size() == 1, "somente uma catequista");
        conferir(relCatequistas.linhas().get(0)[4].equals("Crisma"), "etapa da catequista");
        conferir(relCatequistas.linhas().get(0)[5].equals(18), "quantidade de catequizandos");

        conferir(controller.excluir("00000000101"), "excluir coroinha sem vínculos");
        conferir(relatorios.consultar("Coroinhas").linhas().isEmpty(), "relatório atualiza após exclusão");
        conferir(relatorios.consultar("Fiéis").linhas().size() == 2, "lista completa após exclusão");
        System.out.println("OK: relatórios de fiéis, coroinhas, padres e catequistas.");
    }

    private static void conferir(boolean condicao, String descricao) {
        if (!condicao) throw new AssertionError("Falhou: " + descricao);
    }
}
