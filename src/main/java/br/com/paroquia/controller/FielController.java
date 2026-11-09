package br.com.paroquia.controller;

import br.com.paroquia.infra.Conversores;
import br.com.paroquia.model.*;
import br.com.paroquia.repository.RepositorioMemoria;
import java.util.List;

/** Validações e operações com os fiéis cadastrados na memória. */
public class FielController {
    private final RepositorioMemoria repositorio = RepositorioMemoria.getInstancia();

    public void cadastrar(Fiel fiel) {
        if(fiel.getNome()==null || fiel.getNome().trim().length()<3)
            throw new IllegalArgumentException("Nome precisa ter pelo menos 3 caracteres.");
        if(fiel.getIdade()<0 || fiel.getIdade()>120)
            throw new IllegalArgumentException("Idade inválida.");
        Conversores.cpf(fiel.getCpf());
        repositorio.inserirFiel(fiel);
    }
    public Fiel consultar(String cpf) { return repositorio.buscarFielPorCpf(Conversores.cpf(cpf)); }
    public List<Fiel> listar() { return repositorio.listarFieis(); }
    public boolean excluir(String cpf) { return repositorio.excluirFiel(Conversores.cpf(cpf)); }
    /** Consultas tipadas para os relatórios específicos dos perfis de fiel. */
    public List<FielComum> listarFieisComuns() { return repositorio.listarFieisPorTipo(FielComum.class); }
    public List<Coroinha> listarCoroinhas() { return repositorio.listarFieisPorTipo(Coroinha.class); }
    public List<Padre> listarPadres() { return repositorio.listarFieisPorTipo(Padre.class); }
    public List<Catequistas> listarCatequistas() { return repositorio.listarFieisPorTipo(Catequistas.class); }
    public boolean alterarNome(String cpf, String nome) {
        nome=Conversores.obrigatorio(nome,"nome");
        if(nome.length()<3) throw new IllegalArgumentException("Nome precisa ter pelo menos 3 caracteres.");
        Fiel fiel=consultar(cpf);
        if(fiel==null) return false;
        try { fiel.setNome(nome); }
        catch(NomePequenoException e) { throw new IllegalArgumentException("Nome precisa ter pelo menos 3 caracteres.",e); }
        return true;
    }
    public static String cargo(Fiel f) {
        if(f instanceof FielComum) return "Fiel";
        if(f instanceof Padre) return "Padre";
        if(f instanceof Coroinha) return "Coroinha";
        if(f instanceof Catequistas) return "Catequista";
        return "Desconhecido";
    }
}
