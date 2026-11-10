package br.com.paroquia.model;



public final class Padre extends Fiel implements Celebracao{

	private String nomeparoquia;
	private String cidade;
	private String setorparoquia;

	public Padre(){
		nomeparoquia = "";
		cidade = "";
		setorparoquia = "";
	}

	//getters
	public String getParoquiaPadre(){
		return nomeparoquia;
	}

	public String getCidade(){
		return cidade;
	}

	public String getSetorParoquia(){
		return setorparoquia;
	}

	//setters
	public void setParoquiaPadre(String nomeparoquia){
		this.nomeparoquia = nomeparoquia;
	}

	public void setCidade(String cidade){
		this.cidade = cidade;
	}

	public void setSetorParoquia(String setorparoquia){
		this.setorparoquia = setorparoquia;
	}

	//Sobrescrita
	public void mostrarFuncao(){
		System.out.println("O padre celebra missas");
	}

	public void celebrar(){
		System.out.println("Celebrando missa");
	}
}