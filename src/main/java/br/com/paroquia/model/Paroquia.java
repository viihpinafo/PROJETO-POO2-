package br.com.paroquia.model;


//Nome: Vitória Pinafo RA: 2525933

public class Paroquia{

	private String nome;
	private String cidade;

	public Paroquia(){
		nome = "";
		cidade = "";
	}

	//getters
	public String getNome(){
		return nome;
	}

	public String getCidade(){
		return cidade;
	}

	//setters
	public void setNome(String nome){
		this.nome = nome;
	}

	public void setCidade(String cidade){
		this.cidade = cidade;
	}

}