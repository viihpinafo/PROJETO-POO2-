//Nome: Vitória Pinafo RA: 2525933

import java.util.List;
import java.util.ArrayList;

public class Missa{

	private String data;
	private String horario;
	private Padre padre;
	private List<Coroinha> coroinhas;

	public Missa(){
		data = "";
		horario = "";
		padre = new Padre();
		coroinhas = new ArrayList<Coroinha>();
	}

	//getters
	public String getData(){
		return data;
	}

	public String getHorario(){
		return horario;
	}

	public Padre getPadre(){
		return padre;
	}

	public List<Coroinha> getCoroinhas(){
		return coroinhas;
	}

	//setters
	public void setData(String data){
		this.data = data;
	}

	public void setHorario(String horario){
		this.horario = horario;
	}

	public void setPadre(Padre padre){
		this.padre = padre;
	}

	public void addCoroinha(Coroinha c){
		coroinhas.add(c);
	}

}