package br.com.paroquia.controller;

import br.com.paroquia.infra.Conversores;
import br.com.paroquia.model.*;
import br.com.paroquia.repository.RepositorioMySQL;
import java.util.ArrayList;
import java.util.List;

/** Regras de negócio para gerenciar os fiéis integradas de forma estática e dinâmica ao MySQL. */
public class FielController {
    private final RepositorioMySQL repositorio = RepositorioMySQL.getInstancia();

    // -- Métodos de cadastro chamados pelas telas --
    public void cadastrar(Padre p) { cadastrarFiel(p); }
    public void cadastrar(Coroinha c) { cadastrarFiel(c); }
    public void cadastrar(Catequistas c) { cadastrarFiel(c); }
    public void cadastrar(FielComum f) { cadastrarFiel(f); }

    public void cadastrarFiel(Fiel fiel) {
        if (fiel == null) throw new IllegalArgumentException("Dados do fiel não informados.");
        repositorio.inserirFiel(fiel);
    }

    public Fiel consultar(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) return null;
        try {
            return repositorio.buscarFielPorCpf(Conversores.cpf(cpf));
        } catch (Exception e) {
            return null;
        }
    }

    public List<Fiel> listar() {
        return repositorio.listarFieis();
    }

    // -- Filtros específicos que o RelatorioController e telas pedem --
    public List<FielComum> listarFieisComuns() {
        List<FielComum> lista = new ArrayList<>();
        for (Fiel f : listar()) { if (f instanceof FielComum) lista.add((FielComum) f); }
        return lista;
    }

    public List<Coroinha> listarCoroinhas() {
        List<Coroinha> lista = new ArrayList<>();
        for (Fiel f : listar()) { if (f instanceof Coroinha) lista.add((Coroinha) f); }
        return lista;
    }

    public List<Padre> listarPadres() {
        List<Padre> lista = new ArrayList<>();
        for (Fiel f : listar()) { if (f instanceof Padre) lista.add((Padre) f); }
        return lista;
    }

    public List<Catequistas> listarCatequistas() {
        List<Catequistas> lista = new ArrayList<>();
        for (Fiel f : listar()) { if (f instanceof Catequistas) lista.add((Catequistas) f); }
        return lista;
    }

    // Adicionado 'static' para resolver o erro de contexto estático das telas
    public static String cargo(Fiel f) {
        if (f instanceof Padre) return "PADRE";
        if (f instanceof Coroinha) return "COROINHA";
        if (f instanceof Catequistas) return "CATEQUISTA";
        if (f instanceof FielComum) return "FIEL_COMUM";
        return "DESCONHECIDO";
    }

    // Ajustado para retornar boolean conforme a classe FormAlterarExcluirFiel exige
    public boolean excluir(String cpf) {
        remover(cpf);
        return true;
    }

    public void remover(String cpf) {
        cpf = Conversores.cpf(cpf);
        if (!repositorio.excluirFiel(cpf)) {
            throw new IllegalArgumentException("Fiel não encontrado para exclusão.");
        }
    }

    // Alterado de void para boolean para satisfazer a verificação da tela da Tânia
    public boolean alterarNome(String cpf, String novoNome) {
        cpf = Conversores.cpf(cpf);
        novoNome = Conversores.obrigatorio(novoNome, "nome");
        if (!repositorio.atualizarNomeFiel(cpf, novoNome)) {
            throw new IllegalArgumentException("Fiel não encontrado para alteração.");
        }
        return true;
    }
}
