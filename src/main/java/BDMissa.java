//Nome: Vitória Pinafo RA: 2525933

import java.util.List;
import java.util.ArrayList;

public class BDMissa{

	private List<Missa> bdMissa;
	private static BDMissa bdMissaUnic;

	private BDMissa(){
		bdMissa = new ArrayList<Missa>();
	}

	//MÉTODO SINGLETON
	public static BDMissa geraBDMissa(){
		if(bdMissaUnic == null){
			bdMissaUnic = new BDMissa();
		}
		return bdMissaUnic;
	}

	public List<Missa> getBdMissa(){
		return bdMissa;
	}

	public Missa insMissa(Missa m){
		bdMissa.add(m);
		return m;
	}

	public Missa consMissaData(String data){
		for(int i = 0; i < bdMissa.size(); i++){
			if(data.equals(bdMissa.get(i).getData())){
				return bdMissa.get(i);
			}
		}
		return null;
	}

	public boolean delMissaData(String data){
		Missa m = consMissaData(data);
		if(m != null){
			bdMissa.remove(m);
			return true;
		}
		return false;
	}

	public boolean altMissaHorario(String data, String novoHorario){
		Missa m = consMissaData(data);
		if(m != null){
			m.setHorario(novoHorario);
			return true;
		}
		return false;
	}

}