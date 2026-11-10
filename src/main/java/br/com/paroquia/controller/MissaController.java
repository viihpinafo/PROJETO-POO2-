package br.com.paroquia.controller;

import br.com.paroquia.infra.Conversores;
import br.com.paroquia.model.*;
import br.com.paroquia.repository.RepositorioMySQL;
import java.util.List;

public class MissaController {
    private final RepositorioMySQL repositorio=RepositorioMySQL.getInstancia();

    public void cadastrar(String data,String horario,String cpfPadre) {
        Fiel fiel=new FielController().consultar(cpfPadre);
        if(!(fiel instanceof Padre padre))
            throw new IllegalArgumentException("O CPF deve pertencer a um padre cadastrado.");
        Missa missa=new Missa();
        missa.setData(Conversores.data(data).toString());
        missa.setHorario(Conversores.horario(horario).toString());
        missa.setPadre(padre);
        repositorio.inserirMissa(missa);
    }
    public List<Missa> listar() { return repositorio.listarMissas(); }
}
