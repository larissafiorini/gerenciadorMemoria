import java.util.Arrays;
/*
 * Nome: Larissa Fiorini Martins
 * Data: 22/10/2018
 * 
 * Gerencia de memoria por particoes variaveis. Esse programa realiza uma solucao de software para 
 * gerenciar solicitacoes de alocacao e liberacao de processos na memoria. Por utilizar particoes variaveis,
 * pode acontecer fragmentacao externa no sistema. Quando ocorre fragmentacao, o sistema realiza a compactacao
 * do bloco de memoria.
 * 
 * Classe Bloco: Define atributos individuais para cada bloco que sera alocado na memoria.
 * 
 * */

public class Bloco {
	private int id;

	// posicao inicial e final do bloco na memória
	private int i;
	private int f;

	private int tamanho;

	private int[] bloco;

	public Bloco(int i, int f, int t, int id) {
		this.i = i;
		this.f = f;
		this.tamanho = t;
		this.id = id;
		criaBloco(this.i, this.f, this.tamanho);
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setI(int i) {
		this.i = i;
	}

	public void setF(int f) {
		this.f = f;
	}

	public void setBloco(int[] bloco) {
		this.bloco = bloco;
	}

	public int getId() {
		return id;
	}

	public int getI() {
		return i;
	}

	public int getF() {
		return f;
	}

	public int getTamanho() {
		return tamanho;
	}

	private void criaBloco(int i, int f, int tam) {
		bloco = new int[tam];
	}

	public int[] getBloco() {
		return bloco;
	}

	@Override
	public String toString() {
		return "Bloco [" + Arrays.toString(bloco) + "]";
	}

}
