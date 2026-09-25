//Nome:Vitória Pinafo RA: 2525933

import java.util.List;
import java.util.ArrayList;

public class BDFiel{

    private List<Fiel> bdFiel;
    private static BDFiel bdFielUnic;

    private BDFiel(){
        bdFiel = new ArrayList<Fiel>();
    }

    //MÉTODO SINGLETON
    public static BDFiel geraBDFiel(){
        if(bdFielUnic == null){
            bdFielUnic = new BDFiel();
        }
        return bdFielUnic;
    }

    public List<Fiel> getBdFiel(){
        return bdFiel;
    }

    public Fiel insFiel(Fiel f){
        bdFiel.add(f);
        return f;
    }

    public Fiel consFielCpf(int cpf){
        for(int i = 0; i < bdFiel.size(); i++){
            if(cpf == bdFiel.get(i).getCpf()){
                return bdFiel.get(i);
            }
        }
        return null;
    }

    public boolean delFielCpf(int cpf){
        Fiel f = consFielCpf(cpf);
        if(f != null){
            bdFiel.remove(f);
            return true;
        }
        return false;
    }

    public boolean altFielCpf(int cpf, String novoNome){
        Fiel f = consFielCpf(cpf);
        if(f != null){
            try{
                f.setNome(novoNome);
            }
            catch(NomePequenoException npe){
                return false;
            }
            return true;
        }
        return false;
    }

}