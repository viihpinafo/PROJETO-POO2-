package br.com.paroquia.model;


//Nome: Vitória Pinafo  RA: 2525933
public final class Coroinha extends Fiel{

	private String escala;
	private String tempoPastoral;

	public Coroinha(){
		escala = "";
		tempoPastoral = "";
	}

	//getters
	public String getEscala(){
		return escala;
	}

	public String getTempoPastoral(){
		return tempoPastoral;
	}

	//setters
	public void setEscala(String escala){
		this.escala = escala;
	}

	public void setTempoPastoral(String tempoPastoral){
		this.tempoPastoral = tempoPastoral;
	}

	//Sobrescrita
	public void mostrarFuncao(){
		System.out.println("O coroinha auxilia nas santas missas");
	}

}