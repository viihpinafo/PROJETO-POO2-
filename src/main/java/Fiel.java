//Nome: Vitória Pinafo  RA: 2525933

public abstract class Fiel{


	private String nome;
	private int idade;
	private int cpf;
	private Paroquia paroquia;

	public Fiel(){

		nome = "";
		idade = 0;
		cpf = 0;
		paroquia = new Paroquia();

	}
	
	//Sobrecarga
	public Fiel(String nome){

		this.nome = nome;

}

	//getters
	public String getNome(){
		return nome;
	}

	public int getIdade(){
		return idade;
	}

	public int getCpf(){
		return cpf;
	}

	public Paroquia getParoquia(){
		return paroquia;
	}

	//setters
	public void setNome(String nome) throws NomePequenoException{

		if(nome.length() < 3){
			throw new NomePequenoException();
		}

		this.nome = nome;
	}

	public void setIdade(int idade) throws IdadeInvalidaException{

		if(idade < 0){
			throw new IdadeInvalidaException();
		}

		this.idade = idade;
	}

	public void setCpf(int cpf){
		this.cpf = cpf;
	}

	//Sobrescrita
	public abstract void mostrarFuncao();

}