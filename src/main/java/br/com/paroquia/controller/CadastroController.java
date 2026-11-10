package br.com.paroquia.controller;

import br.com.paroquia.infra.Conversores;
import br.com.paroquia.model.*;
import br.com.paroquia.repository.RepositorioMySQL; 

/** Regras de negócio dos novos cadastros, agora salvando no MySQL. */
public class CadastroController {
    private final RepositorioMySQL repositorio = RepositorioMySQL.getInstancia(); 


    private void exigirFiel(String cpf, String papel) {
        if (new FielController().consultar(cpf)==null)
            throw new IllegalArgumentException("Cadastre o(a) " + papel + " antes de prosseguir.");
    }
    public void sacramento(String cpf,String tipo,String data,String observacoes) {
        cpf=Conversores.cpf(cpf);
        exigirFiel(cpf,"fiel");
        repositorio.inserirSacramento(new Sacramento(cpf,Conversores.obrigatorio(tipo,"tipo"),Conversores.data(data),observacoes));
    }
    public void casamento(String noivoCpf,String noivaCpf,String data,String horario,String local) {
        noivoCpf=Conversores.cpf(noivoCpf);noivaCpf=Conversores.cpf(noivaCpf);
        if(noivoCpf.equals(noivaCpf)) throw new IllegalArgumentException("Informe dois CPFs diferentes.");
        exigirFiel(noivoCpf,"noivo");exigirFiel(noivaCpf,"noiva");
        repositorio.inserirCasamento(new Casamento(noivoCpf,noivaCpf,Conversores.data(data),Conversores.horario(horario),Conversores.obrigatorio(local,"local")));
    }
    public void catequizando(String nome,String idade,String etapa,String responsavel,String telefone,String catequistaCpf) {
        int anos=Conversores.inteiro(idade,"idade");
        if(anos<0 || anos>120) throw new IllegalArgumentException("Idade inválida.");
        String cpf=Conversores.cpfOpcional(catequistaCpf);
        if(cpf!=null && !(new FielController().consultar(cpf) instanceof Catequistas))
            throw new IllegalArgumentException("O CPF do catequista deve pertencer a um catequista cadastrado.");
        repositorio.inserirCatequizando(new Catequizando(Conversores.obrigatorio(nome,"nome"),anos,Conversores.obrigatorio(etapa,"etapa"),responsavel,telefone,cpf));
    }
    public void doacao(String doadorCpf,String descricao,String valor,String data,String forma) {
        String cpf=Conversores.cpfOpcional(doadorCpf);
        if(cpf!=null) exigirFiel(cpf,"doador");
        repositorio.inserirDoacao(new Doacao(cpf,Conversores.obrigatorio(descricao,"descrição"),Conversores.valor(valor),Conversores.data(data),Conversores.obrigatorio(forma,"forma de pagamento")));
    }
    public void despesa(String descricao,String categoria,String valor,String data) {
        repositorio.inserirDespesa(new Despesa(Conversores.obrigatorio(descricao,"descrição"),Conversores.obrigatorio(categoria,"categoria"),Conversores.valor(valor),Conversores.data(data)));
    }
    public void evento(String nome,String descricao,String local,String data,String responsavelCpf) {
        String cpf=Conversores.cpfOpcional(responsavelCpf);
        if(cpf!=null) exigirFiel(cpf,"responsável");
        repositorio.inserirEvento(new Evento(Conversores.obrigatorio(nome,"nome"),descricao,Conversores.obrigatorio(local,"local"),Conversores.data(data),cpf));
    }
}
