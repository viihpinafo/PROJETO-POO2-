package br.com.paroquia;
import br.com.paroquia.infra.Conversores;
import br.com.paroquia.model.*;
import java.math.BigDecimal;

/** Executar a função main deste arquivo para testar modelos sem depender de banco de dados. */
public final class TesteValidacoes {
    public static void main(String[] args) throws Exception {
        conferir("01234567890".equals(Conversores.cpf("012.345.678-90")),"CPF");
        conferir("2026-10-24".equals(Conversores.data("24/10/2026").toString()),"Data");
        conferir("08:30".equals(Conversores.horario("08:30").toString()),"Horário");
        conferir(new BigDecimal("1234.50").compareTo(Conversores.valor("1.234,50"))==0,"Valor monetário");
        conferir(Conversores.cpfOpcional("")==null,"CPF opcional");
        boolean rejeitou=false;
        try{ Conversores.data("31/02/2026"); } catch(IllegalArgumentException esperado) { rejeitou=true; }
        conferir(rejeitou,"Rejeição de data inválida");
        Padre padre=new Padre();padre.setNome("Padre João");padre.setIdade(44);padre.setCpf("01234567890");
        conferir(padre.getCpf().startsWith("0"),"CPF com zero à esquerda");
        Missa missa=new Missa();missa.setPadre(padre);
        conferir(missa.getPadre() instanceof Padre,"Associação da missa ao padre");
        System.out.println("OK: validações e modelos passaram em todos os testes locais.");
    }
    private static void conferir(boolean condicao,String nome) {
        if(!condicao) throw new AssertionError("Falha em: "+nome);
    }
}
