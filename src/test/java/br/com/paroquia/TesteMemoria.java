package br.com.paroquia;

import br.com.paroquia.controller.*;
import br.com.paroquia.model.*;
import br.com.paroquia.repository.RepositorioMemoria;

/** Testes de ponta a ponta do modelo e repositório em memória. */
public final class TesteMemoria {
    public static void main(String[] args) throws Exception {
        FielController fieis = new FielController();
        MissaController missas = new MissaController();
        CadastroController cadastros = new CadastroController();
        RepositorioMemoria repo = RepositorioMemoria.getInstancia();
        Padre padre = new Padre();
        padre.setNome("João Padre");padre.setIdade(45);padre.setCpf("00123456789");
        fieis.cadastrar(padre);
        Catequistas catequista = new Catequistas();
        catequista.setNome("Maria Catequista");catequista.setIdade(33);catequista.setCpf("01234567891");
        fieis.cadastrar(catequista);
        Coroinha coroinha = new Coroinha();
        coroinha.setNome("Ana Coroinha");coroinha.setIdade(15);coroinha.setCpf("01234567892");
        fieis.cadastrar(coroinha);
        conferir(fieis.listar().size()==3,"lista de fiéis");
        conferir(fieis.consultar("001.234.567-89") instanceof Padre,"CPF normalizado");
        conferir(fieis.alterarNome("00123456789","Padre José"),"alteração de nome");
        conferir("Padre José".equals(fieis.consultar("00123456789").getNome()),"consulta atualizada");
        esperarErro(()->fieis.cadastrar(padre),"duplicidade do CPF");
        missas.cadastrar("24/10/2026","19:00","00123456789");
        conferir(missas.listar().size()==1 && missas.listar().get(0).getId()>0,"cadastro de missa");
        esperarErro(()->missas.cadastrar("24/10/2026","19:00","00123456789"),"duplicidade da missa");
        cadastros.sacramento("01234567891","Batismo","24/10/2026","Registro de teste");
        cadastros.casamento("00123456789","01234567891","24/10/2026","15:00","Matriz");
        cadastros.catequizando("Pedro","10","1ª etapa","Responsável","43999999999","01234567891");
        cadastros.doacao("00123456789","Oferta","150,25","24/10/2026","Dinheiro");
        cadastros.despesa("Luz","Contas","90,50","24/10/2026");
        cadastros.evento("Quermesse","Evento anual","Salão","24/10/2026","00123456789");
        conferir(repo.listarSacramentos().size()==1,"sacramento");
        conferir(repo.listarCasamentos().size()==1,"casamento");
        conferir(repo.listarCatequizandos().size()==1,"catequizando");
        conferir(repo.listarDoacoes().size()==1,"doação");
        conferir(repo.listarDespesas().size()==1,"despesa");
        conferir(repo.listarEventos().size()==1,"evento");
        conferir(new RelatorioController().consultar("Fiéis").linhas().size()==3,"relatório de fiéis");
        conferir(new RelatorioController().consultar("Missas").linhas().size()==1,"relatório de missas");
        conferir(new RelatorioController().consultar("Sacramentos").linhas().size()==1,"relatório de sacramentos");
        conferir(new RelatorioController().consultar("Casamentos").linhas().size()==1,"relatório de casamentos");
        conferir(new RelatorioController().consultar("Catequizandos").linhas().size()==1,"relatório de catequizandos");
        conferir(new RelatorioController().consultar("Doações").resumo().contains("150.25"),"soma de doações");
        conferir(new RelatorioController().consultar("Despesas").resumo().contains("90.50"),"soma de despesas");
        conferir(new RelatorioController().consultar("Eventos").linhas().size()==1,"relatório de eventos");
        esperarErro(()->fieis.excluir("00123456789"),"restrição para excluir fiel associado");
        conferir(fieis.excluir("01234567892"),"exclusão de fiel sem vínculos");
        conferir(fieis.consultar("01234567892")==null,"consulta após exclusão");
        esperarErro(()->cadastros.sacramento("01234567892","Crisma","24/10/2026",""),"CPF de fiel ausente");
        System.out.println("OK: armazenamento em memória, cadastros, relatórios e validações testados.");
    }
    private static void conferir(boolean teste,String descricao) {
        if(!teste) throw new AssertionError("Falhou: " + descricao);
    }
    private static void esperarErro(Runnable rotina,String descricao) {
        try { rotina.run(); throw new AssertionError("Esperava erro: " + descricao); }
        catch(IllegalArgumentException esperado) { /* validação correta */ }
    }
}
