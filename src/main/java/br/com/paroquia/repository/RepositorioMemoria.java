package br.com.paroquia.repository;

import br.com.paroquia.model.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Armazenamento temporário para a versão sem banco de dados.
 * Uma única instância é compartilhada por todas as janelas e controladores.
 * Os dados existem somente enquanto o programa estiver aberto.
 */
public final class RepositorioMemoria {
    private static final RepositorioMemoria INSTANCIA = new RepositorioMemoria();
    private final Map<String, Fiel> fieis = new LinkedHashMap<>();
    private final List<Missa> missas = new ArrayList<>();
    private final List<Sacramento> sacramentos = new ArrayList<>();
    private final List<Casamento> casamentos = new ArrayList<>();
    private final List<Catequizando> catequizandos = new ArrayList<>();
    private final List<Doacao> doacoes = new ArrayList<>();
    private final List<Despesa> despesas = new ArrayList<>();
    private final List<Evento> eventos = new ArrayList<>();

    private long sequenciaMissa = 1L;
    private long sequenciaSacramento = 1L;
    private long sequenciaCasamento = 1L;
    private long sequenciaCatequizando = 1L;
    private long sequenciaDoacao = 1L;
    private long sequenciaDespesa = 1L;
    private long sequenciaEvento = 1L;

    private RepositorioMemoria() { }
    public static RepositorioMemoria getInstancia() { return INSTANCIA; }

    public synchronized void inserirFiel(Fiel fiel) {
        if (fiel == null) throw new IllegalArgumentException("Fiel não informado.");
        if (fieis.containsKey(fiel.getCpf())) throw new IllegalArgumentException("Já existe um fiel cadastrado com esse CPF.");
        fieis.put(fiel.getCpf(), fiel);
    }
    public synchronized Fiel buscarFielPorCpf(String cpf) { return fieis.get(cpf); }
    public synchronized List<Fiel> listarFieis() {
        List<Fiel> lista = new ArrayList<>(fieis.values());
        lista.sort(Comparator.comparing(Fiel::getNome, String.CASE_INSENSITIVE_ORDER));
        return lista;
    }
    /**
     * Lista somente os fiéis do tipo informado, preservando a ordem alfabética.
     * Reaproveita a lista da classe base Fiel, sem duplicar cadastros por função.
     */
    public synchronized <T extends Fiel> List<T> listarFieisPorTipo(Class<T> tipo) {
        if (tipo == null) throw new IllegalArgumentException("Informe o tipo de fiel.");
        List<T> selecionados = new ArrayList<>();
        for (Fiel fiel : listarFieis()) {
            if (tipo.isInstance(fiel)) selecionados.add(tipo.cast(fiel));
        }
        return selecionados;
    }

    public synchronized boolean excluirFiel(String cpf) {
        if (!fieis.containsKey(cpf)) return false;
        // Mantém a integridade das associações do modelo sem precisar de chaves estrangeiras.
        if (missas.stream().anyMatch(m -> m.getPadre().getCpf().equals(cpf) ||
                m.getCoroinhas().stream().anyMatch(c -> c.getCpf().equals(cpf))) ||
            sacramentos.stream().anyMatch(s -> s.getFielCpf().equals(cpf)) ||
            casamentos.stream().anyMatch(c -> c.getNoivoCpf().equals(cpf) || c.getNoivaCpf().equals(cpf)) ||
            catequizandos.stream().anyMatch(c -> cpf.equals(c.getCatequistaCpf())) ||
            doacoes.stream().anyMatch(d -> cpf.equals(d.getDoadorCpf())) ||
            eventos.stream().anyMatch(e -> cpf.equals(e.getResponsavelCpf()))) {
            throw new IllegalArgumentException("Este fiel está associado a outros cadastros e não pode ser excluído.");
        }
        fieis.remove(cpf);
        return true;
    }

    public synchronized void inserirMissa(Missa missa) {
        if (missa == null || missa.getPadre() == null ||
                !(buscarFielPorCpf(missa.getPadre().getCpf()) instanceof Padre))
            throw new IllegalArgumentException("Cadastre um padre válido para a missa.");
        if (missas.stream().anyMatch(m -> m.getData().equals(missa.getData()) && m.getHorario().equals(missa.getHorario())))
            throw new IllegalArgumentException("Já existe uma missa neste dia e horário.");
        missa.setId(sequenciaMissa++);
        missas.add(missa);
    }
    public synchronized List<Missa> listarMissas() {
        List<Missa> lista=new ArrayList<>(missas);
        lista.sort(Comparator.comparing(Missa::getData).thenComparing(Missa::getHorario));
        return lista;
    }

    public synchronized void inserirSacramento(Sacramento s) { s.setId(sequenciaSacramento++); sacramentos.add(s); }
    public synchronized List<Sacramento> listarSacramentos() { return new ArrayList<>(sacramentos); }

    public synchronized void inserirCasamento(Casamento c) {
        if (c == null || c.getNoivoCpf() == null || c.getNoivaCpf() == null)
            throw new IllegalArgumentException("Informe os dois fiéis do casamento.");
        if (c.getNoivoCpf().equals(c.getNoivaCpf()))
            throw new IllegalArgumentException("Os noivos devem ser pessoas diferentes.");
        if (!fieis.containsKey(c.getNoivoCpf()) || !fieis.containsKey(c.getNoivaCpf()))
            throw new IllegalArgumentException("Cadastre ambos os fiéis antes do casamento.");
        c.setId(sequenciaCasamento++);
        casamentos.add(c);
    }
    public synchronized List<Casamento> listarCasamentos() { return new ArrayList<>(casamentos); }

    public synchronized void inserirCatequizando(Catequizando c) { c.setId(sequenciaCatequizando++); catequizandos.add(c); }
    public synchronized List<Catequizando> listarCatequizandos() { return new ArrayList<>(catequizandos); }

    public synchronized void inserirDoacao(Doacao d) { d.setId(sequenciaDoacao++); doacoes.add(d); }
    public synchronized List<Doacao> listarDoacoes() { return new ArrayList<>(doacoes); }

    public synchronized void inserirDespesa(Despesa d) { d.setId(sequenciaDespesa++); despesas.add(d); }
    public synchronized List<Despesa> listarDespesas() { return new ArrayList<>(despesas); }

    public synchronized void inserirEvento(Evento e) { e.setId(sequenciaEvento++); eventos.add(e); }
    public synchronized List<Evento> listarEventos() { return new ArrayList<>(eventos); }
}
