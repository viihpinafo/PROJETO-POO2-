package br.com.paroquia.repository;

import br.com.paroquia.infra.Conexao;
import br.com.paroquia.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Guarda os dados no MySQL (vai substituir o RepositorioMemoria aos poucos). */
public final class RepositorioMySQL {
    private static final RepositorioMySQL INSTANCIA = new RepositorioMySQL();

    private RepositorioMySQL() { }
    public static RepositorioMySQL getInstancia() { return INSTANCIA; }

    private Connection abrir() {
        Connection con = Conexao.conectar();
        if (con == null) throw new IllegalStateException("Não foi possível conectar ao banco de dados.");
        return con;
    }

    private String texto(ResultSet rs, String coluna) throws SQLException {
        String valor = rs.getString(coluna);
        return valor == null ? "" : valor;
    }

    // --FIEL --

    public void inserirFiel(Fiel fiel) {
        if (fiel == null) throw new IllegalArgumentException("Fiel não informado.");

        String tipo, padreParoquia = null, padreCidade = null, padreSetor = null;
        String escala = null, tempoPastoral = null, etapa = null;
        Integer qtd = null;

                if (fiel instanceof Padre) {
            Padre p = (Padre) fiel;
            tipo = "PADRE";
            padreParoquia = p.getParoquiaPadre();
            padreCidade = p.getCidade();
            padreSetor = p.getSetorParoquia();
        } else if (fiel instanceof Coroinha) {
            Coroinha c = (Coroinha) fiel;
            tipo = "COROINHA";
            escala = c.getEscala();
            tempoPastoral = c.getTempoPastoral();
        } else if (fiel instanceof Catequistas) {
            Catequistas c = (Catequistas) fiel;
            tipo = "CATEQUISTA";
            etapa = c.getEtapa();
            qtd = c.getQtdCatequisandos();
        } else if (fiel instanceof FielComum) {
            // Nova classe da Tânia sendo tratada aqui
            tipo = "FIEL_COMUM";
        } else {
            throw new IllegalArgumentException("Tipo de fiel não reconhecido.");
        }


        String sql = "INSERT INTO fiel (cpf, nome, idade, tipo, paroquia_nome, paroquia_cidade, "
                + "padre_paroquia, padre_cidade, padre_setor, escala, tempo_pastoral, etapa, qtd_catequisandos) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fiel.getCpf());
            ps.setString(2, fiel.getNome());
            ps.setInt(3, fiel.getIdade());
            ps.setString(4, tipo);
            ps.setString(5, fiel.getParoquia().getNome());
            ps.setString(6, fiel.getParoquia().getCidade());
            ps.setString(7, padreParoquia);
            ps.setString(8, padreCidade);
            ps.setString(9, padreSetor);
            ps.setString(10, escala);
            ps.setString(11, tempoPastoral);
            ps.setString(12, etapa);
            if (qtd == null) ps.setNull(13, java.sql.Types.INTEGER);
            else ps.setInt(13, qtd);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062)
                throw new IllegalArgumentException("Já existe um fiel cadastrado com esse CPF.");
            throw new IllegalStateException("Erro ao salvar o fiel: " + e.getMessage(), e);
        }
    }

    public Fiel buscarFielPorCpf(String cpf) {
        String sql = "SELECT * FROM fiel WHERE cpf = ?";
        try (Connection con = abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montarFiel(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao buscar o fiel: " + e.getMessage(), e);
        }
    }

    public List<Fiel> listarFieis() {
        List<Fiel> lista = new ArrayList<>();
        String sql = "SELECT * FROM fiel ORDER BY nome";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(montarFiel(rs));
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar os fiéis: " + e.getMessage(), e);
        }
    }

    public boolean atualizarNomeFiel(String cpf, String nome) {
        String sql = "UPDATE fiel SET nome = ? WHERE cpf = ?";
        try (Connection con = abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cpf);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao alterar o fiel: " + e.getMessage(), e);
        }
    }

    public boolean excluirFiel(String cpf) {
        String sql = "DELETE FROM fiel WHERE cpf = ?";
        try (Connection con = abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451)
                throw new IllegalArgumentException("Este fiel está associado a outros cadastros e não pode ser excluído.");
            throw new IllegalStateException("Erro ao excluir o fiel: " + e.getMessage(), e);
        }
    }
        // -- MISSA --

    public void inserirMissa(Missa missa) {
        if (missa == null || missa.getPadre() == null
                || !(buscarFielPorCpf(missa.getPadre().getCpf()) instanceof Padre))
            throw new IllegalArgumentException("Cadastre um padre válido para a missa.");

        String verifica = "SELECT COUNT(*) FROM missa WHERE data = ? AND horario = ?";
        String sql = "INSERT INTO missa (data, horario, padre_cpf) VALUES (?,?,?)";

        try (Connection con = abrir()) {
            try (PreparedStatement ps = con.prepareStatement(verifica)) {
                ps.setString(1, missa.getData());
                ps.setString(2, missa.getHorario());
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0)
                        throw new IllegalArgumentException("Já existe uma missa neste dia e horário.");
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, missa.getData());
                ps.setString(2, missa.getHorario());
                ps.setString(3, missa.getPadre().getCpf());
                ps.executeUpdate();
                try (ResultSet chaves = ps.getGeneratedKeys()) {
                    if (chaves.next()) missa.setId(chaves.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar a missa: " + e.getMessage(), e);
        }
    }

    public List<Missa> listarMissas() {
        List<Missa> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.data, m.horario, f.* FROM missa m "
                + "JOIN fiel f ON f.cpf = m.padre_cpf ORDER BY m.data, m.horario";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Missa m = new Missa();
                m.setId(rs.getLong("id"));
                m.setData(rs.getString("data"));
                m.setHorario(rs.getString("horario"));
                m.setPadre((Padre) montarFiel(rs));
                lista.add(m);
            }
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar as missas: " + e.getMessage(), e);
        }
    }
        // --ajudantes --

    // grava uma linha e devolve o id que o banco gerou
    private long inserir(String sql, Object... valores) {
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < valores.length; i++) ps.setObject(i + 1, valores[i]);
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                return chaves.next() ? chaves.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar no banco: " + e.getMessage(), e);
        }
    }

    private java.time.LocalDate data(ResultSet rs, String coluna) throws SQLException {
        java.sql.Date d = rs.getDate(coluna);
        return d == null ? null : d.toLocalDate();
    }

    // --SACRAMENTO --

    public void inserirSacramento(Sacramento s) {
        s.setId(inserir("INSERT INTO sacramento (fiel_cpf, tipo, data, observacoes) VALUES (?,?,?,?)",
                s.getFielCpf(), s.getTipo(), s.getData(), s.getObservacoes()));
    }

    public List<Sacramento> listarSacramentos() {
        List<Sacramento> lista = new ArrayList<>();
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM sacramento ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Sacramento s = new Sacramento(rs.getString("fiel_cpf"), rs.getString("tipo"),
                        data(rs, "data"), rs.getString("observacoes"));
                s.setId(rs.getLong("id"));
                lista.add(s);
            }
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar sacramentos: " + e.getMessage(), e);
        }
    }

    // --CASAMENTO --

    public void inserirCasamento(Casamento c) {
        c.setId(inserir("INSERT INTO casamento (noivo_cpf, noiva_cpf, data, horario, local) VALUES (?,?,?,?,?)",
                c.getNoivoCpf(), c.getNoivaCpf(), c.getData(), c.getHorario(), c.getLocal()));
    }

    public List<Casamento> listarCasamentos() {
        List<Casamento> lista = new ArrayList<>();
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM casamento ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Casamento c = new Casamento(rs.getString("noivo_cpf"), rs.getString("noiva_cpf"),
                        data(rs, "data"), rs.getTime("horario").toLocalTime(), rs.getString("local"));
                c.setId(rs.getLong("id"));
                lista.add(c);
            }
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar casamentos: " + e.getMessage(), e);
        }
    }

    // --CATEQUIZANDO --

    public void inserirCatequizando(Catequizando c) {
        c.setId(inserir("INSERT INTO catequizando (nome, idade, etapa, responsavel, telefone, catequista_cpf) VALUES (?,?,?,?,?,?)",
                c.getNome(), c.getIdade(), c.getEtapa(), c.getResponsavel(), c.getTelefone(), c.getCatequistaCpf()));
    }

    public List<Catequizando> listarCatequizandos() {
        List<Catequizando> lista = new ArrayList<>();
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM catequizando ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Catequizando c = new Catequizando(rs.getString("nome"), rs.getInt("idade"),
                        rs.getString("etapa"), rs.getString("responsavel"),
                        rs.getString("telefone"), rs.getString("catequista_cpf"));
                c.setId(rs.getLong("id"));
                lista.add(c);
            }
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar catequizandos: " + e.getMessage(), e);
        }
    }

    // --DOAÇÃO --

    public void inserirDoacao(Doacao d) {
        d.setId(inserir("INSERT INTO doacao (doador_cpf, descricao, valor, data, forma_pagamento) VALUES (?,?,?,?,?)",
                d.getDoadorCpf(), d.getDescricao(), d.getValor(), d.getData(), d.getFormaPagamento()));
    }

    public List<Doacao> listarDoacoes() {
        List<Doacao> lista = new ArrayList<>();
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM doacao ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Doacao d = new Doacao(rs.getString("doador_cpf"), rs.getString("descricao"),
                        rs.getBigDecimal("valor"), data(rs, "data"), rs.getString("forma_pagamento"));
                d.setId(rs.getLong("id"));
                lista.add(d);
            }
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar doações: " + e.getMessage(), e);
        }
    }

    // -- DESPESA --

    public void inserirDespesa(Despesa d) {
        d.setId(inserir("INSERT INTO despesa (descricao, categoria, valor, data) VALUES (?,?,?,?)",
                d.getDescricao(), d.getCategoria(), d.getValor(), d.getData()));
    }

    public List<Despesa> listarDespesas() {
        List<Despesa> lista = new ArrayList<>();
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM despesa ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Despesa d = new Despesa(rs.getString("descricao"), rs.getString("categoria"),
                        rs.getBigDecimal("valor"), data(rs, "data"));
                d.setId(rs.getLong("id"));
                lista.add(d);
            }
            return lista;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar despesas: " + e.getMessage(), e);
        }
    }

    // -- EVENTO --

    public void inserirEvento(Evento e) {
        e.setId(inserir("INSERT INTO evento (nome, descricao, local, data, responsavel_cpf) VALUES (?,?,?,?,?)",
                e.getNome(), e.getDescricao(), e.getLocal(), e.getData(), e.getResponsavelCpf()));
    }

    public List<Evento> listarEventos() {
        List<Evento> lista = new ArrayList<>();
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM evento ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evento e = new Evento(rs.getString("nome"), rs.getString("descricao"),
                        rs.getString("local"), data(rs, "data"), rs.getString("responsavel_cpf"));
                e.setId(rs.getLong("id"));
                lista.add(e);
            }
            return lista;
        } catch (SQLException ex) {
            throw new IllegalStateException("Erro ao listar eventos: " + ex.getMessage(), ex);
        }
    }
    // transforma uma linha do banco em Padre, Coroinha ou Catequistas
    private Fiel montarFiel(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Fiel f;

        if ("PADRE".equals(tipo)) {
            Padre p = new Padre();
            p.setParoquiaPadre(texto(rs, "padre_paroquia"));
            p.setCidade(texto(rs, "padre_cidade"));
            p.setSetorParoquia(texto(rs, "padre_setor"));
            f = p;
        } else if ("COROINHA".equals(tipo)) {
            Coroinha c = new Coroinha();
            c.setEscala(texto(rs, "escala"));
            c.setTempoPastoral(texto(rs, "tempo_pastoral"));
            f = c;
        } else if ("CATEQUISTA".equals(tipo)) {
            Catequistas c = new Catequistas();
            c.setEtapa(texto(rs, "etapa"));
            c.setQtdCatequisandos(rs.getInt("qtd_catequisandos"));
            f = c;
        } else if ("FIEL_COMUM".equals(tipo)) {
            f = new FielComum();
        } else {
            throw new IllegalArgumentException("Tipo de fiel não reconhecido no banco: " + tipo);
        }

        try {
            f.setNome(rs.getString("nome"));
            f.setIdade(rs.getInt("idade"));
        } catch (NomePequenoException | IdadeInvalidaException e) {
            throw new IllegalStateException("Dados inválidos no banco para o CPF " + rs.getString("cpf"), e);
        }
        f.setCpf(rs.getString("cpf"));
        f.getParoquia().setNome(texto(rs, "paroquia_nome"));
        f.getParoquia().setCidade(texto(rs, "paroquia_cidade"));
        return f;
    }

}