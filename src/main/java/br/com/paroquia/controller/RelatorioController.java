package br.com.paroquia.controller;
import br.com.paroquia.repository.RepositorioMySQL;
import br.com.paroquia.model.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RelatorioController {
    public record Dados(String[] colunas,List<Object[]> linhas,String resumo) { }
    private static final DateTimeFormatter DIA=DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static String opcional(String texto) { return texto==null?"":texto; }
    public Dados consultar(String modulo) {
       RepositorioMySQL repositorio=RepositorioMySQL.getInstancia();
        List<Object[]> linhas=new ArrayList<>();
        String[] colunas;
        String resumo="";
        switch(modulo) {
            case "Fiéis" -> {
                colunas=new String[]{"Nome","CPF","Idade","Perfil","Paróquia"};
                for(Fiel f:new FielController().listar()) linhas.add(new Object[]{f.getNome(),f.getCpf(),f.getIdade(),FielController.cargo(f),f.getParoquia().getNome()});
            }
            case "Fiéis comuns" -> {
                colunas=new String[]{"Nome","CPF","Idade","Paróquia","Cidade"};
                for(FielComum fiel:new FielController().listarFieisComuns()) {
                    linhas.add(new Object[]{fiel.getNome(),fiel.getCpf(),fiel.getIdade(),
                        fiel.getParoquia().getNome(),fiel.getParoquia().getCidade()});
                }
            }
            case "Coroinhas" -> {
                colunas=new String[]{"Nome","CPF","Idade","Paróquia","Escala","Tempo de pastoral"};
                for (Coroinha c : new FielController().listarCoroinhas()) {
                    linhas.add(new Object[]{c.getNome(),c.getCpf(),c.getIdade(),
                        c.getParoquia().getNome(),opcional(c.getEscala()),opcional(c.getTempoPastoral())});
                }
            }
            case "Padres" -> {
                colunas=new String[]{"Nome","CPF","Idade","Paróquia","Cidade","Setor"};
                for (Padre p : new FielController().listarPadres()) {
                    linhas.add(new Object[]{p.getNome(),p.getCpf(),p.getIdade(),
                        opcional(p.getParoquiaPadre()),opcional(p.getCidade()),opcional(p.getSetorParoquia())});
                }
            }
            case "Catequistas" -> {
                colunas=new String[]{"Nome","CPF","Idade","Paróquia","Etapa","Qtd. catequizandos"};
                for (Catequistas c : new FielController().listarCatequistas()) {
                    linhas.add(new Object[]{c.getNome(),c.getCpf(),c.getIdade(),
                        c.getParoquia().getNome(),opcional(c.getEtapa()),c.getQtdCatequisandos()});
                }
            }
            case "Missas" -> {
                colunas=new String[]{"ID","Data","Horário","Padre","CPF do padre"};
                for(Missa m:repositorio.listarMissas()) linhas.add(new Object[]{m.getId(),m.getData(),m.getHorario(),m.getPadre().getNome(),m.getPadre().getCpf()});
            }
            case "Sacramentos" -> {
                colunas=new String[]{"ID","CPF do fiel","Tipo","Data","Observações"};
                for(Sacramento s:repositorio.listarSacramentos()) linhas.add(new Object[]{s.getId(),s.getFielCpf(),s.getTipo(),DIA.format(s.getData()),opcional(s.getObservacoes())});
            }
            case "Casamentos" -> {
                colunas=new String[]{"ID","Noivo","CPF noivo","Noiva","CPF noiva","Data","Horário","Local"};
                for(Casamento c:repositorio.listarCasamentos()) {
                    Fiel pessoaNoivo = repositorio.buscarFielPorCpf(c.getNoivoCpf());
                    Fiel pessoaNoiva = repositorio.buscarFielPorCpf(c.getNoivaCpf());
                    linhas.add(new Object[]{c.getId(),pessoaNoivo.getNome(),c.getNoivoCpf(),
                        pessoaNoiva.getNome(),c.getNoivaCpf(),DIA.format(c.getData()),c.getHorario(),c.getLocal()});
                }
            }
            case "Catequizandos" -> {
                colunas=new String[]{"ID","Nome","Idade","Etapa","Responsável","Telefone","CPF catequista"};
                for(Catequizando c:repositorio.listarCatequizandos()) linhas.add(new Object[]{c.getId(),c.getNome(),c.getIdade(),c.getEtapa(),opcional(c.getResponsavel()),opcional(c.getTelefone()),opcional(c.getCatequistaCpf())});
            }
            case "Doações" -> {
                colunas=new String[]{"ID","CPF doador","Descrição","Valor","Data","Pagamento"};
                BigDecimal total=BigDecimal.ZERO;
                for(Doacao d:repositorio.listarDoacoes()) {
                    total=total.add(d.getValor());
                    linhas.add(new Object[]{d.getId(),opcional(d.getDoadorCpf()),d.getDescricao(),d.getValor(),DIA.format(d.getData()),d.getFormaPagamento()});
                }
                resumo="Total de doações: R$ "+total;
            }
            case "Despesas" -> {
                colunas=new String[]{"ID","Descrição","Categoria","Valor","Data"};
                BigDecimal total=BigDecimal.ZERO;
                for(Despesa d:repositorio.listarDespesas()) {
                    total=total.add(d.getValor());
                    linhas.add(new Object[]{d.getId(),d.getDescricao(),d.getCategoria(),d.getValor(),DIA.format(d.getData())});
                }
                resumo="Total de despesas: R$ "+total;
            }
            case "Eventos" -> {
                colunas=new String[]{"ID","Nome","Descrição","Local","Data","CPF responsável"};
                for(Evento e:repositorio.listarEventos()) linhas.add(new Object[]{e.getId(),e.getNome(),opcional(e.getDescricao()),e.getLocal(),DIA.format(e.getData()),opcional(e.getResponsavelCpf())});
            }
            default -> throw new IllegalArgumentException("Selecione um relatório disponível.");
        }
        return new Dados(colunas,linhas,resumo.isEmpty()?"Registros: "+linhas.size():"Registros: "+linhas.size()+"  |  "+resumo);
    }
}
