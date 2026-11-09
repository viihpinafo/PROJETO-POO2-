package br.com.paroquia.model;


//Nome: Vitória Pinafo  RA: 2525933
public final class Catequistas extends Fiel{

	private String etapa;
	private int qtdCatequisandos;

	public Catequistas(){
		etapa = "";
		qtdCatequisandos = 0;
	}

	//getters
	public String getEtapa(){
		return etapa;
	}

	public int getQtdCatequisandos(){
		return qtdCatequisandos;
	}

	//setters
	public void setEtapa(String etapa){
		this.etapa = etapa;
	}

	public void setQtdCatequisandos(int qtdCatequisandos){
		this.qtdCatequisandos = qtdCatequisandos;
	}

	//Sobrescrita
	public void mostrarFuncao(){
		System.out.println("O catequista ensina na catequese");
	}
}